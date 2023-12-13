package com.adventofcode.adventofcode2023;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Memoizer<T, U> {

  private final Map<T, U> cache = new HashMap<>();

  private Memoizer() {
  }

  private Function<T, U> doMemoize(final Function<T, U> function) {
    return input -> computeIfAbsent(cache, input, function);
  }

  public static <T, U> Function<T, U> memoize(final Function<T, U> function) {
    return new Memoizer<T, U>().doMemoize(function);
  }

  /**
   * non blocking computeIfAbsent to prevent java.util.ConcurrentModificationException only use this if  multiple computes may occur
   *
   * @param cache
   * @param key
   * @param function
   * @param <K>
   * @param <V>
   * @return
   */
  public static <K, V> V computeIfAbsent(
      Map<K, V> cache,
      K key,
      Function<? super K, ? extends V> function
  ) {
    V result = cache.get(key);

    if (result == null) {
      result = function.apply(key);
      cache.put(key, result);
    }

    return result;
  }
}
