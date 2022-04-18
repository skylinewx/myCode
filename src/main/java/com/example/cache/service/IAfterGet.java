package com.example.cache.service;

import com.example.cache.domain.MyObj;

@FunctionalInterface
public interface IAfterGet {

    void afterGet(MyObj myObj);
}
