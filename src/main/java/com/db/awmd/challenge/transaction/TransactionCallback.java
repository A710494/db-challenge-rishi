package com.db.awmd.challenge.transaction;

@FunctionalInterface
public interface TransactionCallback {

    void process();
}
