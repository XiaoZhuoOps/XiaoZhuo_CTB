package com.xiaozhuo.ctbsb.common.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class EmailUtil {

    /**
     * @param emailAddress
     * @param activateCode
     * @param javaMailSender
     */

    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(String emailAddress, String text) {
        // 创建邮件对象
        MimeMessage mMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mMessageHelper;
        Properties prop = new Properties();
        try {
            // 从配置文件中拿到发件人邮箱地址
            //classpath - classLoader - inputStream
            prop.load(EmailUtil.class.getResourceAsStream("/mail.properties"));
            String from = prop.get("mail.smtp.username") + "";
            mMessageHelper = new MimeMessageHelper(mMessage, false, "UTF-8");
            // 发件人邮箱
            mMessageHelper.setFrom(from);
            // 收件人邮箱
            mMessageHelper.setTo(emailAddress);
            // 邮件的主题也就是邮件的标题
            mMessageHelper.setSubject("CTB注册邮件");
            //设置为html格式发送
            mMessageHelper.setText(text, true);
//            // 通过文件路径获取文件名字
//            String filename = StringUtils.getFileName(location);
//            // 定义要发送的资源位置
//            File file = new File(location);
//            FileSystemResource resource = new FileSystemResource(file);
//            FileSystemResource resource2 = new FileSystemResource("D:/email.txt");
//            mMessageHelper.addAttachment(filename, resource);// 在邮件中添加一个附件
//            mMessageHelper.addAttachment("JavaApiRename.txt", resource2);//
            // 在邮件中添加一个附件
            javaMailSender.send(mMessage);// 发送邮件
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendActivateMail(String emailAddress, String activateCode){
        // 邮件的文本内容，true表示文本以html格式打开
        String text = "<h1>来自CTB的账号激活邮件，激活请点击下方链接：" +
                "    <h3>" +
                "        <a href='http://localhost:8080/CTB/user/active?emailAddress=" + emailAddress + "&activateCode=" + activateCode + "'>" +
                "请点击此处" +
                "        </a>" +
                "    </h3>" +
                "</h1>";
        sendMail(emailAddress, text);
    }
    public void sendCheckMail(String emailAddress, int checkCode){
        // 邮件的文本内容，true表示文本以html格式打开
        String text = "" + checkCode;
        sendMail(emailAddress, text);
    }
}

