package ru.antonovmikhail.concurrency.bank;

import java.util.UUID;

public class BankAccount {

    private final UUID id;

    private int balance;
    public BankAccount(UUID id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public synchronized void deposit(int amount) {
        balance += amount;
    }

    public synchronized void withdraw(int amount) {
        if (balance < amount) throw new RuntimeException("Недостаточно средств на счету");
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }

    public UUID getId() {
        return id;
    }
}
