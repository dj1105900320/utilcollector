package utils.collector.vehiclestatuscheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
/**
 * 没啥用 基本用不到
 *
 * @author deng jie
 * @date 2021/7/16 16:46
 * @description 发送邮件工具类
 */
@Service
public class EmailUtil {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String required;

    @Value("${spring.mail.theme}")
    private String theme;

    @Value("${spring.mail.user}")
    private String user;

    public void sendMail(List<EmailData> list) throws Exception {
        // 1. 创建一封邮件
        // 用于连接邮件服务器的参数配置（发送邮件时才需要用到）
        Properties props = new Properties();
        // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.transport.protocol", "smtp");
        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.host", host);
        // 需要请求认证
        props.setProperty("mail.smtp.auth", auth);
        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        props.setProperty("mail.smtp.port", port);
        // 根据参数配置，创建会话对象（为了发送邮件准备的）
        Session session = Session.getInstance(props);
        session.setDebug(true);
        StringBuilder content = new StringBuilder("<html><head></head><body><h2>结果</h2>");
        content.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=14px;;font-size:18px; width:100%\">");
        content.append("<tr style=\"background-color: #428BCA; color:#ffffff\">" +
                "<th>自编号</th><th>车牌号</th><th>线路</th><th>第几周</th><th>问题说明</th>" +
                "</tr>");
        for (EmailData emailData : list) {
            content.append("<tr>");
            //第一列
            content.append("<td>" + emailData.getOwnPlateNum() + "</td>");
            //第二列
            content.append("<td>" + emailData.getPlateNum() + "</td>");
            //第三列
            content.append("<td>" + emailData.getLineName() + "</td>");
            //第四列
            content.append("<td>" + emailData.getWeek() + "</td>");
            //第五列
            content.append("<td>" + emailData.getProblemRecord() + "</td>");
            content.append("</tr>");
        }
        content.append("</table>");
        content.append("</body></html>");

        String cont = content.toString();

        MimeMessage message = createMimeMessage(session, userName, from, user, cont);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        //
        //    PS_01: 如果连接服务器失败, 都会在控制台输出相应失败原因的log。
        //    仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接,
        //    根据给出的错误类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //
        transport.connect(userName, password);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session  和服务器交互的会话
     * @param userName 发件人邮箱
     * @param from     收件人邮箱
     * @return
     * @throws Exception
     */
    MimeMessage createMimeMessage(Session session, String userName, String from, String user,
                                  String join) throws Exception {
        String newUser = new String(user.getBytes("ISO-8859-1"), "utf-8");
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        String sssc = "车辆检测工具";
        message.setFrom(new InternetAddress(userName, sssc, "UTF-8"));
        //不能使用string类型的类型，这样只能发送一个收件人
        List list = new ArrayList();
        //对输入的多个邮件进行逗号分割
        String[] median = from.split(",");
        for (int i = 0; i < median.length; i++) {
            list.add(new InternetAddress(median[i]));
        }
        InternetAddress[] address = (InternetAddress[]) list.toArray(new InternetAddress[list.size()]);
        //当邮件有多个收件人时，用逗号隔开
        message.setRecipients(Message.RecipientType.TO, address);

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
//        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(from, user, "UTF-8"));

        //    To: 增加收件人（可选）
//        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(from1, "USER_DD", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject("车辆检测结果", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(join, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }
}
