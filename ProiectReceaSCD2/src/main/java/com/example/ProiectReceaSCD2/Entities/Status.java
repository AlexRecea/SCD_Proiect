package com.example.ProiectReceaSCD2.Entities;

public enum Status {
    PENDING,
    PUBLISHED,
    REMOVED;

    public static boolean isValid(String value) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
