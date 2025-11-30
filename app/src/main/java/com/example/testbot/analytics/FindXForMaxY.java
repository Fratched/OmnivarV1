package com.example.testbot.analytics;

import android.util.Pair;

import java.util.ArrayList;

public class FindXForMaxY implements AnalyticsCore.AnalyticsMetric {


    public FindXForMaxY() {

    }
    @Override
    public AnalyticsCore.Insight analyze(double[] var1, double[] var2,
                                         String var1Name, String var2Name) {
        int window = 3;
        int n = Math.min(var1.length, var2.length);
        if (n < window) {
            return new AnalyticsCore.Insight(
                    "There are too few days to analyze recent averages of " + var1Name + " and " + var2Name + ".",
                    0.0
            );
        }

        ArrayList<Pair<Double, Double>> pairs = new ArrayList<>();

        // 3 day window averages
        for (int i = window - 1; i < n; i++) {
            double sumY = 0.0;
            double sumX = 0.0;
            for (int j = i - window + 1; j <= i; j++) {
                sumY += var1[j];
                sumX += var2[j];
            }
            double avgY = sumY / window;
            double avgX = sumX / window;
            pairs.add(new Pair<>(avgX, avgY)); // (x, y)
        }

        if (pairs.size() == 0) {
            return new AnalyticsCore.Insight(
                    "No valid windows could be formed for " + var1Name + " and " + var2Name + ".",
                    0.0
            );
        }

        // sort by y
        pairs.sort((p1, p2) -> Double.compare(p2.second, p1.second));

        // take top 20% by y
        int m = pairs.size();
        int topCount = (int) Math.floor(m * 0.2);
        if (topCount < 1) topCount = 1;
        if (topCount > m) topCount = m;

        // for those windows, compute mean and standard deviation of x
        double sumX = 0.0;
        for (int i = 0; i < topCount; i++) {
            sumX += pairs.get(i).first;
        }
        double meanX = sumX / topCount;

        double varX = 0.0;
        for (int i = 0; i < topCount; i++) {
            double dx = pairs.get(i).first - meanX;
            varX += dx * dx;
        }
        double sdX = Math.sqrt(varX / topCount);

        String msg = String.format("To maximize %s: %s = %.2f ± %.2f", var1Name, var2Name, meanX, sdX);

        return new AnalyticsCore.Insight(msg, 1);
    }

}
