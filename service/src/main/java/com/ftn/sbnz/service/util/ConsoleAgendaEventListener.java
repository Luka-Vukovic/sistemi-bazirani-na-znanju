package com.ftn.sbnz.service.util;

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;

public class ConsoleAgendaEventListener extends DefaultAgendaEventListener {

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        String ruleName = event.getMatch().getRule().getName();
        System.out.println("   >>> [RULE FIRED]: " + ruleName);
    }
}