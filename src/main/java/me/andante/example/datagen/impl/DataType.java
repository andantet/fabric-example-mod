package me.andante.example.datagen.impl;

public enum DataType {
    ASSET("assets"),
    DATA("data");

    private final String folder;

    DataType(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return this.folder;
    }
}
