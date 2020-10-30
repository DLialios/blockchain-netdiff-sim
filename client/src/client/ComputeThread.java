package client;

import report.TaskCompleteReport;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;

public class ComputeThread extends Thread {

    private final int difficulty;
    private TaskCompleteReport report;
    public static boolean isRunning = false;
    private final static ArrayList<Integer> SOLVED = new ArrayList<>();

    @Override
    public void run() {
        boolean needsToRun = true;
        for (Integer d : SOLVED) {
            if (difficulty == d) {
                needsToRun = false;
            }
        }
        if (needsToRun) {
            isRunning = true;
            System.out.println("ComputeThread started for difficulty  " + difficulty);
            reverseSHA();
            System.out.println("ComputeThread finished for difficulty " + difficulty + " (" + report.getIterationCount() + " iterations in " + report.getDuration() + "ms)");
            SOLVED.add(difficulty);
            isRunning = false;
        }
    }

    public ComputeThread(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public TaskCompleteReport getReport() {
        return report;
    }

    public static boolean hasFinishedThisJob(int d) {
        return SOLVED.contains(d);
    }

    private void reverseSHA() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.exit(0);
            return;
        }
        BigInteger iterationCount = new BigInteger("0");
        boolean isCorrect;
        byte[] MDInput;
        byte[] MDOutput;
        long start = System.nanoTime();
        do {
            MDInput = new byte[32];
            new Random().nextBytes(MDInput);
            MDOutput = md.digest(MDInput);
            iterationCount = iterationCount.add(new BigInteger("1"));
            String outputHex = DatatypeConverter.printHexBinary(MDOutput);
            String outputHexFormatted = outputHex.substring(0, difficulty);
            isCorrect = true;
            for (int i = 0; i < outputHexFormatted.length(); i++) {
                if (outputHexFormatted.charAt(i) != '0') {
                    isCorrect = false;
                }
            }
        } while (!isCorrect);
        String duration = new DecimalFormat("0.00").format((System.nanoTime() - start) * Math.pow(10, -6));
        report = new TaskCompleteReport(DatatypeConverter.printHexBinary(MDInput), difficulty, iterationCount, duration);
    }
}
