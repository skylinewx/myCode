package com.example.designpatterns.chainofresponsibility;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任链，链上每一个元素既可以是独立的Filter也可以是另一个责任链，有点拓扑的意思<br/>
 * 从某种程度上看，责任链也是套娃
 */
public class SimpleFilterChainMain {
    public static void main(String[] args) {
        Msg msg = new Msg();
        msg.setContent("hello，<script>alert(\"I am an alert box!!\")</script> world! ${jndi:rim:/xxx} ");
        FilterChain filterChain1 = new FilterChain();
        filterChain1.add(new HTMLFilter());
        FilterChain filterChain2 = new FilterChain();
        filterChain2.add(new SensitiveFilter());
        filterChain1.add(filterChain2);
        filterChain1.doFilter(msg);
        System.out.println(msg.getContent());
    }

    private interface Filter {
        /**
         * 过滤msg
         *
         * @param msg
         */
        void doFilter(Msg msg);
    }

    private static class Msg {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private static class HTMLFilter implements Filter {

        @Override
        public void doFilter(Msg msg) {
            String content = msg.getContent();
            content = content.replace('<', '[');
            content = content.replace('>', ']');
            msg.setContent(content);
        }
    }

    private static class SensitiveFilter implements Filter {

        @Override
        public void doFilter(Msg msg) {
            String content = msg.getContent();
            content = content.replaceAll("\\$\\{jndi:", "***");
            msg.setContent(content);
        }
    }

    private static class FilterChain implements Filter {
        List<Filter> filters = new ArrayList<>();

        public void add(Filter filter) {
            filters.add(filter);
        }

        @Override
        public void doFilter(Msg msg) {
            for (Filter filter : filters) {
                filter.doFilter(msg);
            }
        }
    }
}
