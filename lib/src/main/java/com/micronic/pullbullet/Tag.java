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
