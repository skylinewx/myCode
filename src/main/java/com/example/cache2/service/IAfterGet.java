package com.example.cache2.service;

@FunctionalInterface
public interface IAfterGet<T> {

    void afterGet(T myObj);
}
