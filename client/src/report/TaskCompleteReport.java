package report;

import java.io.Serializable;
import java.math.BigInteger;

public class TaskCompleteReport implements Serializable {

    private final String answer;
    private final int difficulty;
    private final BigInteger iterationCount;
    private final String duration;

    public TaskCompleteReport(String answer, int difficulty, BigInteger iterationCount, String duration) {
        this.answer = answer;
        this.difficulty = difficulty;
        this.iterationCount = iterationCount;
        this.duration = duration;
    }

    public String getAnswer() {
        return answer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public BigInteger getIterationCount() {
        return iterationCount;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "TaskCompleteReport{" + "answer=" + answer + ", difficulty=" + difficulty + ", iterationCount=" + iterationCount + ", duration=" + duration + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaskCompleteReport)) {
            return false;
        }
        return this.difficulty == ((TaskCompleteReport) o).getDifficulty();
    }
}
