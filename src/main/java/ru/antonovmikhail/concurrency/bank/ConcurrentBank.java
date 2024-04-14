package ru.antonovmikhail.concurrency.bank;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentBank {

    private final ConcurrentMap<UUID, BankAccount> accounts = new ConcurrentHashMap<>();

    public synchronized BankAccount createAccount(int balance) {
        UUID newId = UUID.randomUUID();
        BankAccount newAccount = new BankAccount(newId, balance);
        accounts.putIfAbsent(newId, newAccount);
        return accounts.get(newId);
    }

    public synchronized void transfer(BankAccount account1, BankAccount account2, int amount) {
        var a1 = accounts.get(account1.getId());
        var a2 = accounts.get(account2.getId());
        if (a1 != null && a2 != null) {
            try {
                a1.withdraw(amount);
                a2.deposit(amount);
            } catch (RuntimeException e) {
                throw new RuntimeException("Операция неудалась, недостаточно средств");
            }
        }
    }

    public int getTotalBalance() {
        return accounts.values().stream().mapToInt(BankAccount::getBalance).sum();
    }
}
