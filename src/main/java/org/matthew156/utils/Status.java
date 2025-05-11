package org.matthew156.utils;

public enum Status {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");
private String name;
    Status(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
