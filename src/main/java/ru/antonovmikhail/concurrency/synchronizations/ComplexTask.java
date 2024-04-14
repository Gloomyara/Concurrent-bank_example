package ru.antonovmikhail.concurrency.synchronizations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ComplexTask implements Runnable {
    private final int numberOfTasks;
    private Random random = new Random();
    private final List<List<Integer>> partialResults;
    private final CyclicBarrier cyclicBarrier;

    public ComplexTask(int numberOfTasks, CyclicBarrier cyclicBarrier, List<List<Integer>> partialResults) {
        this.numberOfTasks = numberOfTasks;
        this.partialResults = partialResults;
        this.cyclicBarrier = cyclicBarrier;
    }

    public void execute() {
        List<Integer> partialResult = new ArrayList<>();

        // Crunch some numbers and store the partial result
        for (int i = 0; i < numberOfTasks; i++) {
            Integer num = random.nextInt(10);
            partialResult.add(num);
        }

        partialResults.add(partialResult);
    }

    @Override
    public void run() {
        execute();
        try {
            System.out.println(Thread.currentThread().getName()
                    + " waiting for others to reach barrier.");
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}