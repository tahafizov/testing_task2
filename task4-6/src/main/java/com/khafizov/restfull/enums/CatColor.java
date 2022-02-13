package com.khafizov.restfull.enums;

public enum CatColor {

    BLACK("black"),
    WHITE("white"),
    BLACK_AND_WHITE("black & white"),
    RED("red"),
    RED_AND_WHITE("red & white"),
    RED_AND_BLACK_AND_WHITE("red & black & white");

    private final String name;

    CatColor(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
