package com.example.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author skyline
 */
public class MyCache<K extends Object, V extends Object> implements Cache<K,V> {

    private final Cache<K,V> delegte;

    public MyCache(Cache<K, V> delegte) {
        this.delegte = delegte;
    }


    @Override
    public @Nullable V getIfPresent(@NonNull Object key) {
        return delegte.getIfPresent(key);
    }

    @Override
    public @Nullable V get(@NonNull K key, @NonNull Function<? super K, ? extends V> mappingFunction) {
        return delegte.get(key, mappingFunction);
    }

    @Override
    public @NonNull Map<@NonNull K, @NonNull V> getAllPresent(@NonNull Iterable<@NonNull ?> keys) {
        return delegte.getAllPresent(keys);
    }

    @Override
    public void put(@NonNull K key, @NonNull V value) {
        delegte.put(key, value);
    }

    @Override
    public void putAll(@NonNull Map<? extends @NonNull K, ? extends @NonNull V> map) {
        delegte.putAll(map);
    }

    @Override
    public void invalidate(@NonNull Object key) {
        delegte.invalidate(key);
    }

    @Override
    public void invalidateAll(@NonNull Iterable<@NonNull ?> keys) {
        delegte.invalidateAll(keys);
    }

    @Override
    public void invalidateAll() {
        delegte.invalidateAll();
    }

    @Override
    public @NonNegative long estimatedSize() {
        return delegte.estimatedSize();
    }

    @Override
    public @NonNull CacheStats stats() {
        return delegte.stats();
    }

    @Override
    public @NonNull ConcurrentMap<@NonNull K, @NonNull V> asMap() {
        return delegte.asMap();
    }

    @Override
    public void cleanUp() {
        delegte.cleanUp();
    }

    @Override
    public @NonNull Policy<K, V> policy() {
        return delegte.policy();
    }
}
