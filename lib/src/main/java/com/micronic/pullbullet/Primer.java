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
