package com.hd.batch.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Util {

    /**
     * Returns a predicate that maintains state containing information about what was seen previously.
     * Predicate returns whether the given element was seen for the first time.
     *
     * @param keyExtractor
     * @param <T> any object
     * @return Predicate
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
