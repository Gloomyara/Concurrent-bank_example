package ru.antonovmikhail.streams;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolExample {
    public static void main(String[] args) {
        int n = 10; // Вычисление факториала для числа 10

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FactorialTask factorialTask = new FactorialTask(n);

        long result = forkJoinPool.invoke(factorialTask);

        System.out.println("Факториал " + n + "! = " + result);
    }
}

class FactorialTask extends RecursiveTask<Integer> {
    private int n;
    boolean isSubtask = false;

    FactorialTask(int n) {
        this.n = n;
    }

    FactorialTask(int n, boolean isSubtask) {
        this.n = n;
        this.isSubtask = isSubtask;
    }

    @Override
    protected Integer compute() {
        if (n < 2) {
            return 1;
        }
        int result = n * (n - 1);
        FactorialTask subTask = new FactorialTask(n - 2);
        subTask.fork();
        return result * subTask.join();
    }
}