package ru.antonovmikhail.concurrency.synchronizations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ComplexTaskExecutor {
    private final int numberOfTasks;
    private final List<List<Integer>> partialResults = Collections.synchronizedList(new ArrayList<>());
    private CyclicBarrier cyclicBarrier;

    public ComplexTaskExecutor(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }

    public void executeTasks(int numberOfTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfTasks);
        AggregatorThread thread = new AggregatorThread();
        cyclicBarrier = new CyclicBarrier(numberOfTasks, thread);
        System.out.println("Spawning " + numberOfTasks
                + " threads to compute "
                + numberOfTasks + " partial results each");

        for (int i = 0; i < numberOfTasks; i++) {
            ComplexTask task = new ComplexTask(numberOfTasks, cyclicBarrier, partialResults);
            executorService.execute(task);
        }
        System.out.println(cyclicBarrier.getNumberWaiting());
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    class AggregatorThread implements Runnable {

        @Override
        public void run() {

            String thisThreadName = Thread.currentThread().getName();

            System.out.println(
                    thisThreadName + ": Computing sum of " + numberOfTasks
                            + " workers, having " + numberOfTasks + " results each.");
            int sum = 0;
            System.out.println(partialResults);
            for (List<Integer> threadResult : partialResults) {
                for (Integer partialResult : threadResult) {
                    sum += partialResult;
                }
            }
            System.out.println(thisThreadName + ": Final result = " + sum);
        }
    }
}

