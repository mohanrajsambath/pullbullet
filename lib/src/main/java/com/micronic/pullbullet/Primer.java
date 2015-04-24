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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Primer extends AbstractList<Check> {
    private final List<Check> checks = new ArrayList<Check>();

    @Override
    public boolean add(Check check) {
        return checks.add(check);
    }

    @Override
    public Check get(int arg0) {
        return checks.get(arg0);
    }

    @Override
    public int indexOf(Object object) {
        return checks.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return checks.isEmpty();
    }

    @Override
    public Check remove(int location) {
        return checks.remove(location);
    }

    @Override
    public int size() {
        return checks.size();
    }
}
