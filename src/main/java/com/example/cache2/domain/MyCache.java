package com.example.cache2.domain;

import com.example.cache2.sender.IMessageSender;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 本地缓存
 *
 * @param <V>
 */
public class MyCache<K,V> extends HashMap<K, V> implements CacheGroup, BeanNameAware, ApplicationContextAware {

    private String cacheGroup;
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    @Autowired
    private IMessageSender messageSender;
    private ApplicationContext applicationContext;

    @Override
    public String getCacheGroup() {
        return cacheGroup;
    }

    private CacheSyncMessage getCacheSyncMessage(String key) {
        CacheSyncMessage cacheSyncMessage = applicationContext.getBean(CacheSyncMessage.class);
        cacheSyncMessage.setCacheGroup(getCacheGroup());
        cacheSyncMessage.setKey(key);
        return cacheSyncMessage;
    }

    private void sendSyncMessage(String key) {
        CacheSyncMessage cacheSyncMessage = getCacheSyncMessage(key);
        messageSender.sendMessage(cacheSyncMessage);
    }

    @Override
    public V remove(Object key) {
        readWriteLock.writeLock().lock();
        V remove;
        try {
            remove = super.remove(key);
            sendSyncMessage(key.toString());
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return remove;
    }

    public void removeNoSync(String key) {
        readWriteLock.writeLock().lock();
        try {
            super.remove(key);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        readWriteLock.writeLock().lock();
        try {
            boolean remove = super.remove(key, value);
            if (remove) {
                sendSyncMessage(key.toString());
            }
            return remove;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void setBeanName(String name) {
        this.cacheGroup = name;
    }

    @Override
    public int size() {
        readWriteLock.readLock().lock();
        try {
            return super.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readWriteLock.readLock().lock();
        try {
            return super.isEmpty();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public V get(Object key) {
        readWriteLock.readLock().lock();
        try {
            return super.get(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        readWriteLock.readLock().lock();
        try {
            return super.containsKey(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        readWriteLock.writeLock().lock();
        try {
            return super.put(key, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        readWriteLock.writeLock().lock();
        try {
            super.putAll(m);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        readWriteLock.writeLock().lock();
        try {
            super.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        readWriteLock.readLock().lock();
        try {
            return super.containsValue(value);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        readWriteLock.readLock().lock();
        try {
            return super.keySet();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Collection<V> values() {
        readWriteLock.readLock().lock();
        try {
            return super.values();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        readWriteLock.readLock().lock();
        try {
            return super.entrySet();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        readWriteLock.readLock().lock();
        try {
            return super.getOrDefault(key, defaultValue);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        readWriteLock.writeLock().lock();
        try {
            return super.putIfAbsent(key, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        readWriteLock.writeLock().lock();
        try {
            return super.replace(key, oldValue, newValue);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        readWriteLock.writeLock().lock();
        try {
            return super.replace(key, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        readWriteLock.writeLock().lock();
        try {
            return super.computeIfAbsent(key, mappingFunction);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        readWriteLock.writeLock().lock();
        try {
            return super.computeIfPresent(key, remappingFunction);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        readWriteLock.writeLock().lock();
        try {
            return super.compute(key, remappingFunction);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        readWriteLock.writeLock().lock();
        try {
            return super.merge(key, value, remappingFunction);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        readWriteLock.readLock().lock();
        try {
            super.forEach(action);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        readWriteLock.writeLock().lock();
        try {
            super.replaceAll(function);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Object clone() {
        readWriteLock.writeLock().lock();
        try {
            return super.clone();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
