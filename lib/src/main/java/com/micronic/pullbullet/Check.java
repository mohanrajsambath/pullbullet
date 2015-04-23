package com.micronic.pullbullet;

import java.math.BigDecimal;
import java.util.Locale;

public class Check {
    public final static int EQUALS = 1;
    public final static int EQUALS_IGNORE_CASE = 2;
    public final static int EQUALS_NOT = 3;
    public final static int EQUALS_NOT_IGNORE_CASE = 4;
    public final static int GREATER_THAN = 5;
    public final static int LESS_THAN = 6;
    public final static int MATCHES = 7;
    public final static int MATCHES_IGNORE_CASE = 8;
    public final static int MATCHES_NOT = 9;
    public final static int MATCHES_NOT_IGNORE_CASE = 10;
    public final static int REGEX = 11;
    public final static int REGEX_IGNORE_CASE = 12;
    public final static int REGEX_NOT = 13;
    public final static int REGEX_NOT_IGNORE_CASE = 14;
    private final String key;
    private final int op;

    private final String val;

    public Check(String key, String val, int op) {
        this.key = key;
        this.val = val;
        this.op = op;
    }

    public String getArg() {
        return key;
    }

    public boolean matches(String val) {
        try {
            switch (op) {
                case 1:
                    return this.val.equals(val);
                case 2:
                    return this.val.equalsIgnoreCase(val);
                case 3:
                    return !this.val.equals(val);
                case 4:
                    return !this.val.equalsIgnoreCase(val);
                case 5:
                    return new BigDecimal(val).compareTo(new BigDecimal(this.val)) > 0;
                case 6:
                    return new BigDecimal(val).compareTo(new BigDecimal(this.val)) < 0;
                case 7:
                    String temp = val;
                    temp = temp.replaceAll("([^a-zA-Z0-9*.])", "\\\\$1");
                    temp = temp.replaceAll("\\*", "(.*?)");
                    temp = temp.replaceAll("\\.", ".");
                    return this.val.matches(temp);
                case 8:
                    temp = val.toLowerCase(Locale.getDefault());
                    temp = temp.replaceAll("([^a-zA-Z0-9*.])", "\\\\$1");
                    temp = temp.replaceAll("\\*", "(.*?)");
                    temp = temp.replaceAll("\\.", ".");
                    return this.val.toLowerCase(Locale.getDefault()).matches(temp);
                case 9:
                    temp = val;
                    temp = temp.replaceAll("([^a-zA-Z0-9*.])", "\\\\$1");
                    temp = temp.replaceAll("\\*", "(.*?)");
                    temp = temp.replaceAll("\\.", ".");
                    return !this.val.matches(temp);
                case 10:
                    temp = val.toLowerCase(Locale.getDefault());
                    temp = temp.replaceAll("([^a-zA-Z0-9*.])", "\\\\$1");
                    temp = temp.replaceAll("\\*", "(.*?)");
                    temp = temp.replaceAll("\\.", ".");
                    return !this.val.toLowerCase(Locale.getDefault()).matches(temp);
                case 11:
                    return this.val.matches(val);
                case 12:
                    return this.val.toLowerCase(Locale.getDefault()).matches(val.toLowerCase(Locale.getDefault()));
                case 13:
                    return !this.val.matches(val);
                case 14:
                    return !this.val.toLowerCase(Locale.getDefault()).matches(val.toLowerCase(Locale.getDefault()));
            }
        } catch (Exception e) {
        }
        return false;
    }

}
