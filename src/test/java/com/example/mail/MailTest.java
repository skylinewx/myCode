package com.example.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * 发邮件测试
 */
public class MailTest {

    /**
     * 测试发送一封邮件
     * @throws MessagingException
     * @throws GeneralSecurityException
     */
    @Test
    public void trySendMail() throws MessagingException, GeneralSecurityException {
        String userName="wangxing@jiuqi.com.cn";
        String passWord = "密码";
        String host = "smtp.jiuqi.com.cn";
        Properties properties = new Properties();
        properties.setProperty("mail.host",host);
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.auth","true");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //创建一个session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName,passWord);
            }
        });

        //开启debug模式
        session.setDebug(true);
        //获取连接对象
        Transport transport = session.getTransport();
        //连接服务器
        transport.connect(host,userName,passWord);
        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);
        //邮件发送人
        mimeMessage.setFrom(new InternetAddress(userName));
        //邮件接收人
        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(userName));
        //邮件标题
        mimeMessage.setSubject("Hello Mail");
        //邮件内容
        mimeMessage.setContent("我的想法是把代码放进一个循环里","text/html;charset=UTF-8");
        //发送邮件
        transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        //关闭连接
        transport.close();
    }
}
