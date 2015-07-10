package com.tw.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account implements Comparable<Account> {

    private int balance;
    public final Lock monitor = new ReentrantLock();

    @Override
    public int compareTo(Account other) {
        return new Integer(hashCode()).compareTo(other.hashCode());
    }

    public Account(final int initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(final int amount) {
        monitor.lock();
        try {
            if(amount > 0) {
                System.out.println(String.format("deposit %s to balance %s", amount, balance));
                balance += amount;
                System.out.println(String.format("now: %s", balance));


            }
        } finally {
            monitor.unlock();
        }
    }

    public boolean withdraw(final int amount) {
        try {
            monitor.lock();
            if(amount > 0 && balance >= amount) {
                System.out.println(String.format("withdraw %s from balance %s", amount, balance));
                balance -= amount;
                System.out.println(String.format("now: %s", balance));
                return true;
            }
            return false;
        } finally {
            monitor.unlock();
        }
    }
}
