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

public class TailTag extends AbstractList<Tag> {

    private final List<Tag> tags;

    public TailTag() {
        tags = new ArrayList<Tag>();
    }

    public TailTag(List<Tag> tags) {
        this.tags = new ArrayList<Tag>(tags);
    }

    @Override
    public boolean add(Tag object) {
        return tags.add(object);
    }

    public void addAll(TailTag tailTag) {
        for (Tag tag : tailTag) {
            tags.add(tag);
        }
    }

    @Override
    public void clear() {
        tags.clear();
    }

    @Override
    protected TailTag clone() {
        return new TailTag(tags);
    }

    @Override
    public Tag get(int arg0) {
        return tags.get(arg0);
    }

    public Tag get(String key) {
        Tag t = obtain(key);
        return t == null ? new Tag(null, null) : t;
    }

    @Override
    public int indexOf(Object object) {
        return tags.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public TailTag put(String key, boolean val) {
        return put(key, "" + val);
    }

    public TailTag put(String key, double val) {
        return put(key, "" + val);
    }

    public TailTag put(String key, int val) {
        return put(key, "" + val);
    }

    public TailTag put(String key, String val) {
        Tag t = obtain(key);
        if (t != null)
            t.setValue(val);
        else
            add(new Tag(key, val));
        return this;
    }

    @Override
    public Tag remove(int location) {
        return tags.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        if (object instanceof String)
            return tags.remove(obtain("" + object));
        return tags.remove(object);
    }

    private Tag obtain(String key) {
        Tag t = null;
        for (Tag tag : tags) {
            if (tag.getKey().equals(key)) {
                t = tag;
                break;
            }
        }
        return t;
    }

    @Override
    public int size() {
        return tags.size();
    }

    @Override
    public List<Tag> subList(int start, int end) {
        return tags.subList(start, end);
    }
}
