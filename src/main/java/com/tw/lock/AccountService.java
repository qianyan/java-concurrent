package com.tw.lock;

public class AccountService {

    public static void main(String[] args) {
        final Account from = new Account(100);
        final Account to = new Account(0);
        Thread t1 = new Thread(() -> {
            transfer(from, to, 40);
        });

        Thread t2 = new Thread(() -> {
            transfer(from, to, 30);
        });

        Thread t3 = new Thread(() -> {
            transfer(from, to, 20);
        });

        t1.start();
        t2.start();
        t3.start();
    }

    public static boolean transfer(final Account from, final Account to, final int amount) {
        if (from.withdraw(amount)) {
            to.deposit(amount);
            return true;
        }

        return false;
    }
}
