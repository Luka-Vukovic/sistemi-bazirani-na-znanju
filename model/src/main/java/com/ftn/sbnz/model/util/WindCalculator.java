package com.ftn.sbnz.model.util;

public class WindCalculator {

    /**
     * @param windSpeed    ukupna brzina vetra u km/h
     * @param windDirection smer vetra u stepenima (0-360)
     * @param runwayHeading orijentacija piste u stepenima (0-360)
     * @return int[] gde je [0] crosswind i [1] tailwind u km/h
     *         Negativan tailwind znači headwind (povoljan)
     */
    public static int[] calculate(int windSpeed, int windDirection, int runwayHeading) {
        double angle = Math.toRadians(Math.abs(windDirection - runwayHeading));
        int crosswind = (int) Math.round(windSpeed * Math.sin(angle));
        int tailwind = (int) Math.round(windSpeed * Math.cos(angle));
        return new int[]{crosswind, tailwind};
    }
}
