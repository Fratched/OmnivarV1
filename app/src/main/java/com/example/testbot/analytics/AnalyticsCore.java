package com.example.testbot.analytics;

import java.util.ArrayList;

public class AnalyticsCore {

    public static class Insight {
        public final String text;     // Message for the user
        public final double interest; // 0 (not interesting) .. 1 (very interesting)

        public Insight(String text, double interest) {
            this.text = text;
            this.interest = interest;
        }
    }

    public interface AnalyticsMetric {
        Insight analyze(double[] var1, double[] var2,
                        String var1Name, String var2Name);
    }
    private static double[] fillMissingWithMean(double[] v, boolean[] defined) {
        int n = Math.min(v.length, defined.length);
        double sum = 0.0;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (defined[i]) {
                sum += v[i];
                cnt++;
            }
        }
        double mean = (cnt > 0) ? sum / cnt : 0.0;
        double[] out = new double[n];
        for (int i = 0; i < n; i++) {
            out[i] = defined[i] ? v[i] : mean;
        }
        return out;
    }

    private static AnalyticsCore instance;

    public static AnalyticsCore getInstance() {
        if (instance == null) {
            instance = new AnalyticsCore();
        }
        return instance;
    }

    private ArrayList<AnalyticsMetric> metrics;

    public AnalyticsCore() {
        metrics = new ArrayList<>();
        metrics.add(new FindXForMaxY());
    }

    public ArrayList<String> getAnalytics(double[] var1, double[] var2, boolean[] defined1, boolean[] defined2,
                                          String var1Name, String var2Name) {
        fillMissingWithMean(var1, defined1);
        fillMissingWithMean(var2, defined2);

        ArrayList<String> results = new ArrayList<>();
        for (AnalyticsMetric analyticsMetric : metrics) {
            Insight result = analyticsMetric.analyze(var1, var2, var1Name, var2Name);
            if (result.interest > 0) {
                results.add(result.text);
            }
        }
        return results;
    }

}
