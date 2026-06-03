package com.ftn.sbnz.service.config;

import com.ftn.sbnz.kjar.util.TemplateLoader;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.kie.api.io.ResourceType;

import java.io.InputStream;

@Configuration
public class DroolsConfiguration {

    private static final String[] DRL_FILES = {
        "/rules/level0.drl",
        "/rules/level1.drl",
        "/rules/level2.drl",
        "/rules/level3.drl",
        "/rules/backward.drl",
        "/rules/cep.drl"
    };

    @Bean
    public KieContainer kieContainer() throws Exception {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Učitaj kmodule.xml
        InputStream kmoduleStream = getClass().getResourceAsStream("/META-INF/kmodule.xml");
        if (kmoduleStream == null) {
            throw new RuntimeException("kmodule.xml not found in classpath.");
        }
        kieFileSystem.writeKModuleXML(new String(kmoduleStream.readAllBytes()));

        // Učitaj sve .drl fajlove
        for (String drlPath : DRL_FILES) {
            InputStream drlStream = getClass().getResourceAsStream(drlPath);
            if (drlStream == null) {
                throw new RuntimeException("DRL file not found: " + drlPath);
            }
            kieFileSystem.write(
                "src/main/resources" + drlPath,
                ResourceFactory.newByteArrayResource(drlStream.readAllBytes())
                    .setResourceType(ResourceType.DRL)
            );
        }

        // Generiši i učitaj template DRL
        String generatedDrl = generateCategoryDrl();
        System.out.println("=== GENERATED TEMPLATE DRL ===\n" + generatedDrl);
        kieFileSystem.write(
            "src/main/resources/rules/generated-category.drl",
            ResourceFactory.newByteArrayResource(generatedDrl.getBytes())
                .setResourceType(ResourceType.DRL)
        );

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("DRL build errors: " + results.getMessages());
        }

        return kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId());
    }

    private String generateCategoryDrl() throws Exception {
        InputStream templateStream = getClass().getResourceAsStream("/rules/flight-category.drt");
        InputStream excelStream = getClass().getResourceAsStream("/rules/flight-category.xlsx");

        if (templateStream == null || excelStream == null) {
            throw new RuntimeException("Template or Excel file not found in classpath.");
        }

        return TemplateLoader.generateDrl(templateStream, excelStream);
    }
}