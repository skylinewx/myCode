package com.example.designpatterns.observer;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件中心
 *
 * @author skyline
 */
public enum EventCenter implements IEventCenter {
    /**
     * 实例
     */
    INSTANCE;

    private final Map<Class, List<IEventListener>> listenerMap = new HashMap<>();

    @Override
    public void publishEvent(BaseEvent event) {
        //根据event的类型获取对应的listener
        List<IEventListener> eventListeners = listenerMap.get(event.getClass());
        if (eventListeners != null) {
            eventListeners.forEach(eventListener -> eventListener.listener(event));
        }
        //如果发出的event不是BaseEvent，那就给所有对BaseEvent感兴趣的listener都发送一下消息
        if (!event.getClass().equals(BaseEvent.class)) {
            List<IEventListener> listeners = listenerMap.get(BaseEvent.class);
            if (listeners != null) {
                listeners.forEach(eventListener -> eventListener.listener(event));
            }
        }
    }

    @Override
    public void registerEvent(IEventListener<? extends BaseEvent> eventListener) {
        //建立起Listener感兴趣的Event类型与Listener的映射关系
        Class genericInterface = (Class) ((ParameterizedType) eventListener.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        List<IEventListener> eventListeners = listenerMap.computeIfAbsent(genericInterface, k -> new ArrayList<>());
        eventListeners.add(eventListener);
    }
}
