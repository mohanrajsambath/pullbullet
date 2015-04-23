package com.micronic.pullbullet;

public class Tag {
    private final String key;
    private String val;

    public Tag(String key, boolean val) {
        this(key, "" + val);
    }

    public Tag(String key, double val) {
        this(key, "" + val);
    }

    public Tag(String key, int val) {
        this(key, "" + val);
    }

    public Tag(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public Tag(Tag tag) {
        this(tag.getKey(), tag.toString());
    }

    public void setValue(String val) {
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public boolean toBoolean() {
        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {
            return false;
        }
    }

    public double toDouble() {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public int toInteger() {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return val;
    }

}
