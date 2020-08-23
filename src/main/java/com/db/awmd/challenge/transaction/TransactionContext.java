package com.db.awmd.challenge.transaction;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TransactionContext<K, V> {

    @Getter
    private Map<K, V> savePoints = new HashMap<>();
}
