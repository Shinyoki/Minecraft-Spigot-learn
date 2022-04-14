package com.senko.customeventplugin.enums;

public enum EnumBowMode {
    PUNCH(0, "击退"),
    LIGHTING(1, "雷击");
    Integer mode;
    String modeName;

    EnumBowMode(Integer mode,String modeName) {
        this.mode = mode;
        this.modeName = modeName;
    }

    public Integer getMode() {
        return mode;
    }

    public String getModeName() {
        return modeName;
    }
}