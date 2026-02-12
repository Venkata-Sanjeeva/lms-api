package com.example.cat_api.utils;

import java.util.UUID;

public class IdentifierGenerator {
    public static String generate(String prefix) {
        return prefix.toUpperCase() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}