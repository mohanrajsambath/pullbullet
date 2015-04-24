/*
 * Copyright (C) 2015 Vallabh Shevate
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
