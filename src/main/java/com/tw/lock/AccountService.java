package com.tw.lock;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AccountService {

    public static void main(String[] args) {
        final Account from = new Account(100);
        final Account to = new Account(0);
        Thread t1 = new Thread(() -> {
            try {
                transfer(from, to, 40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                transfer(from, to, 40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                transfer(from, to, 40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }

    public static boolean transfer(final Account from, final Account to, final int amount) throws InterruptedException, LockException {
        final Account[] accounts = new Account[]{from, to};
        Arrays.sort(accounts);
        if (accounts[0].monitor.tryLock(1, TimeUnit.SECONDS)) {
            try {
                if (accounts[1].monitor.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        if (from.withdraw(amount)) {
                            to.deposit(amount);
                            return true;
                        }
                    } finally {
                        accounts[1].monitor.unlock();
                    }
                }
            } finally {
                accounts[0].monitor.unlock();
            }
        }

        throw new LockException("Unable to acquire locks on the accounts");
    }
}
