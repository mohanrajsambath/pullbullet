package com.micronic.pullbullet;

public class Precheck extends Check {
    public final static int AND = 1;
    public final static int NOT = 3;
    public final static int OR = 2;
    private final Check c1;
    private final Check c2;
    private final int op;

    public Precheck(Check c1, Check c2, int op) {
        super("", "", 0);
        this.c1 = c1;
        this.c2 = c2;
        this.op = op;
    }

    @Override
    public boolean matches(String val) {
        try {
            switch (op) {
                case 1:
                    return c1.matches(val) && c2.matches(val);
                case 2:
                    return c1.matches(val) || c2.matches(val);
                case 3:
                    return !c1.matches(val);
            }
        } catch (Exception e) {
        }
        return false;
    }
}
