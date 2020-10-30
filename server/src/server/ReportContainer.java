package server;

import report.TaskCompleteReport;
import java.util.ArrayList;

public class ReportContainer {

    private static final ArrayList<TaskCompleteReport> REPORTS = new ArrayList<>();

    public static synchronized void add(TaskCompleteReport report, String remoteAddress) {
        if (!REPORTS.contains(report)) {
            REPORTS.add(report);
            System.out.println("Received report for difficulty " + report.getDifficulty() + " from " + remoteAddress);
        }
    }

    public static synchronized TaskCompleteReport get(int i) {
        return REPORTS.get(i);
    }

    public static synchronized int size() {
        return REPORTS.size();
    }
}
