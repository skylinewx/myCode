package com.example.juc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author wangxing
 */
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private ConcurrentHashMapCacheContainer<StudentDO> concurrentHashMapCache;

    @Autowired
    private HashMapCacheContainer<StudentDO> hashMapCache;

    @Autowired
    private ReadWriteLockCacheContainer<StudentDO> readWriteLockCache;

    @Autowired
    private ReadWriteLockCacheContainer2<StudentDO> readWriteLockCache2;

    @Autowired
    private StudentDAO studentDAO;


    public StudentDO getByIdFromConcurrentHashMapCache(String id) {
        Assert.notNull(id, "id is null");
        return concurrentHashMapCache.get(id, studentDAO::getById);
    }

    public StudentDO getByIdFromHashMapCache(String id) {
        Assert.notNull(id, "id is null");
        return hashMapCache.get(id, studentDAO::getById);
    }

    public StudentDO getByIdFromReadWriteLockCache(String id) {
        Assert.notNull(id, "id is null");
        return readWriteLockCache.get(id, studentDAO::getById);
    }

    public StudentDO getByIdFromReadWriteLockCache2(String id) {
        Assert.notNull(id, "id is null");
        return readWriteLockCache2.get(id, studentDAO::getById);
    }
}
