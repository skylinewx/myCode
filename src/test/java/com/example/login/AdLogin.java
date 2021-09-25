package com.example.login;

import org.junit.Test;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;

public class AdLogin {

    /**
     * ad域登陆
     */
    @Test
    public void tryLogin() {
        //AD域认证，用户的登录UserName
        String userName = "wangxing";
        //AD域认证，用户的登录PassWord
        String password = "";
        //AD域IP，必须填写正确
        String host = "xx.xxx.xxx.x";
        //域名后缀，例.@noker.com.cn
        String domain = "@noker.cn.com";
        //端口，一般默认389
        String port = "389";
        String user = userName.endsWith(domain) ? userName : userName + domain;
        //实例化一个Env
        Hashtable<String, String> env = new Hashtable<>();
        DirContext ctx = null;
        //LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        //用户名
        env.put(Context.SECURITY_PRINCIPAL, user);
        //密码
        env.put(Context.SECURITY_CREDENTIALS, password);
        // LDAP工厂类
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        //ad域地址：windos server上输入ipconfig查看，369是固定端口，dc=noker,dc=com,dc=cn是域的范围
        String url = "ldap://" + host + ":" + port + "/dc=noker,dc=com,dc=cn";
        env.put(Context.PROVIDER_URL, url);
        try {
            // 初始化上下文
            ctx = new InitialDirContext(env);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //根据用户名查看ad域中是否存在当前用户
            NamingEnumeration<SearchResult> results = ctx.search("", "(&(objectclass=person)(userprincipalname=" + user + "))", controls);
            SearchResult searchResult = results.next();
            Attributes attributes = searchResult.getAttributes();
            System.out.println("身份验证成功!" + attributes);
        } catch (AuthenticationException e) {
            System.out.println("身份验证失败!");
            e.printStackTrace();
        } catch (javax.naming.CommunicationException e) {
            System.out.println("AD域连接失败!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("身份验证未知异常!");
            e.printStackTrace();
        } finally {
            if (null != ctx) {
                try {
                    ctx.close();
                    ctx = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
