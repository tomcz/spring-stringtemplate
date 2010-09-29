package com.watchitlater.spring;

import java.util.HashMap;
import java.util.Map;

public abstract class CachingDataMap extends DataMap {

    private final Map<Object, Object> cache = new HashMap<Object, Object>();

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public Object get(Object key) {
        Object value = cache.get(key);
        if (value == null) {
            value = getValue(key);
            cache.put(key, value);
        }
        return value;
    }

    protected abstract Object getValue(Object key);
}
