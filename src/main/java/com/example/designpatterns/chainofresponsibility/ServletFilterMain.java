package com.example.designpatterns.chainofresponsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 尝试模拟一下servlet的filter调用过程
 *
 * @author wangxing
 */
public class ServletFilterMain {

    public static void main(String[] args) {
        FilterChain filterChain = new MyFilterChain();
        filterChain.addFilter(new Filter1());
        filterChain.addFilter(new Filter2());
        filterChain.addFilter(new Filter3());
        filterChain.doFilter(new Request(), new Response());
    }

    /**
     * 自定义过滤器
     */
    private interface Filter {
        /**
         * 执行过滤的逻辑
         *
         * @param request
         * @param response
         */
        void doFilter(Request request, Response response, FilterChain filterChain);
    }

    /**
     * 过滤器链
     */
    private interface FilterChain {

        void doFilter(Request request, Response response);

        /**
         * 添加过滤器
         *
         * @param filter
         */
        void addFilter(Filter filter);
    }

    private static class Filter1 implements Filter {
        private static final Logger logger = LoggerFactory.getLogger(Filter1.class);

        @Override
        public void doFilter(Request request, Response response, FilterChain filterChain) {
            logger.info("before filterChain.doFilter");
            filterChain.doFilter(request, response);
            logger.info("after filterChain.doFilter");
        }
    }

    private static class Filter2 implements Filter {
        private static final Logger logger = LoggerFactory.getLogger(Filter2.class);

        @Override
        public void doFilter(Request request, Response response, FilterChain filterChain) {
            logger.info("before filterChain.doFilter");
            filterChain.doFilter(request, response);
            logger.info("after filterChain.doFilter");
        }
    }

    private static class Filter3 implements Filter {
        private static final Logger logger = LoggerFactory.getLogger(Filter3.class);

        @Override
        public void doFilter(Request request, Response response, FilterChain filterChain) {
            logger.info("before filterChain.doFilter");
            filterChain.doFilter(request, response);
            logger.info("after filterChain.doFilter");
        }
    }

    /**
     * 模拟请求
     */
    private static class Request {
    }

    /**
     * 模拟响应
     */
    private static class Response {
    }

    private static class MyFilterChain implements FilterChain {

        private final List<Filter> filters;
        private int index;

        private MyFilterChain() {
            filters = new ArrayList<>();
            index = -1;
        }

        @Override
        public void doFilter(Request request, Response response) {
            int size = filters.size();
            //索引向后移动
            index++;
            if (index < size) {
                filters.get(index).doFilter(request, response, this);
            }
        }

        @Override
        public void addFilter(Filter filter) {
            filters.add(filter);
        }
    }
}
