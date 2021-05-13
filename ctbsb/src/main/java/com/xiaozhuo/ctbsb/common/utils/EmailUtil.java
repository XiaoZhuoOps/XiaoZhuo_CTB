package com.xiaozhuo.ctbsb.common.utils;


import com.xiaozhuo.ctbsb.common.exception.Asserts;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmailUtil {

    /**
     * @param emailAddress
     * @param activateCode
     * @param javaMailSender
     */

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

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
            mMessageHelper.setSubject("CTB官方邮件");
            //设置为html格式发送
            mMessageHelper.setText(text, true);
            javaMailSender.send(mMessage);// 发送邮件
        } catch (MessagingException | IOException e) {
            Asserts.fail("邮件发送错误");
        }
    }

    public void sendActivateMail(String template, String emailAddress, String activateCode){
        try {
            // 获得模板
            Template template1 = freeMarkerConfigurer.getConfiguration().getTemplate(template);
            // 使用Map作为数据模型，定义属性和值
            Map<String,Object> model = new HashMap<>();
            model.put("emailAddress",emailAddress);
            model.put("activateCode",activateCode);
            // 传入数据模型到模板，替代模板中的占位符，并将模板转化为html字符串
            String templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template1,model);
            // 该方法本质上还是发送html邮件，调用之前发送html邮件的方法
            this.sendMail(emailAddress, templateHtml);
        } catch (TemplateException | IOException e) {
            Asserts.fail("邮件发送错误");
        }
    }
    public void sendCheckMail(String emailAddress, int checkCode){
        // 邮件的文本内容，true表示文本以html格式打开
        String text = "" + checkCode;
        sendMail(emailAddress, text);
    }
}

