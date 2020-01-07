package org.nh.core.util.email;

import lombok.extern.slf4j.Slf4j;
import org.nh.core.util.email.bean.EmailMessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @ClassName: Sendmail
 * @Description: 发送一封只包含文本的简单邮件
 */
@Slf4j
@Component
public class EmailUtil {

    @Value("${mail.from}")
    private String mailFrom;// 指明邮件的发件人
    @Value("${mail.from_password}")
    private String password_mailFrom;// 指明邮件的发件人登陆密码
//    private static String mailTittle = null;// 邮件的标题
//    private static String mailText =null;	// 邮件的文本内容
    @Value("${mail.host}")
    private String mail_host;	// 邮件的服务器域名

//    public static void main(String[] args) throws Exception {
//        mailFrom = "sunwenjie-ds@gome.com.cn";
//        password_mailFrom="123.gome";
//        mailTo = "sunwenjie-ds@gome.com.cn";
////        mailTittle="节日快乐2！";
////        mailText = "这是一个简单的邮件";
//        mail_host="owa.corp.gome.com.cn";
//
//        Properties prop = new Properties();
//        prop.setProperty("mail.host", mail_host);
//        prop.setProperty("mail.transport.protocol", "smtp");
//        prop.setProperty("mail.smtp.auth", "true");
//
//        // 使用JavaMail发送邮件的5个步骤
//
//        // 1、创建session
//        Session session = Session.getInstance(prop);
//        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
//        session.setDebug(true);
//        // 2、通过session得到transport对象
//        Transport ts = session.getTransport();
//        // 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
//        ts.connect(mail_host,mailFrom, password_mailFrom);
//        // 4、创建邮件
//        Message message = createSimpleMail(session,mailFrom,mailTo,mailTittle,mailText);
//        // 5、发送邮件
//        ts.sendMessage(message, message.getAllRecipients());
//        ts.close();
//    }

    public void sendMail(EmailMessageDTO dto, String mailTo) {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.host", mail_host);
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");

            // 使用JavaMail发送邮件的5个步骤

            // 1、创建session
            Session session = Session.getInstance(prop);
            // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(true);
            // 2、通过session得到transport对象
            Transport ts = session.getTransport();
            // 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
            ts.connect(mail_host, mailFrom, password_mailFrom);
            // 4、创建邮件
            Message message = createSimpleMail(session, mailFrom, mailTo, dto.getSubject(), dto.getContent());
            // 5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
        }catch (Exception e){
            log.error("邮件发送异常{}",e);
        }
    }


    /**
     * @Method: createSimpleMail
     * @Description: 创建一封只包含文本的邮件
     */
    public static MimeMessage createSimpleMail(Session session, String mailFrom, String mailTo, String mailTittle,
                                               String mailText) throws Exception {
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明邮件的发件人
        message.setFrom(new InternetAddress(mailFrom));
        // 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        // To: 收件人（可以增加多个收件人、抄送、密送）
        InternetAddress[] sendTo = InternetAddress.parse(mailTo);
        System.out.println(sendTo);
        message.setRecipients(MimeMessage.RecipientType.TO, sendTo);

//        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        // 邮件的标题
        message.setSubject(mailTittle);
        // 邮件的文本内容
        message.setContent(mailText, "text/html;charset=UTF-8");
        // 返回创建好的邮件对象
        return message;
    }
}

