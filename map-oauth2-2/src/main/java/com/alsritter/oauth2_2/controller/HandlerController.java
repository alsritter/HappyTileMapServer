package com.alsritter.oauth2_2.controller;

import com.alsritter.common.AuthConstant;
import com.alsritter.common.api.CommonResult;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.oauth2_2.OAuthApplication;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * 注意：这里不能删
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/handler")
public class HandlerController {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    public HandlerController(JavaMailSender javaMailSender,
                             MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    private Template template;

    @PostConstruct
    public void initData() throws IOException {
        //构建 Freemarker 的基本配置
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        // 配置模板位置
        ClassLoader loader = OAuthApplication.class.getClassLoader();
        configuration.setClassLoaderForTemplateLoading(loader, "templates");
        //加载模板
        this.template = configuration.getTemplate("mail.ftl");
    }


    /**
     * 设置手机验证码
     */
    @GetMapping("/getPhoneCode")
    public CommonResult<String> getPhoneCode(HttpServletRequest request,
                                             @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
                                             @NotBlank
                                             @RequestParam("phone") String phone,

                                             @NotBlank
                                             @RequestParam("type") String type
    ) {
        String code = randomCode(6);
        log.debug("手机验证码为： {}", code);
        //保存验证码到 session
        request.getSession().setAttribute("phoneCode", code);
        request.getSession().setAttribute("phoneCodeTimeout",
                System.currentTimeMillis() + AuthConstant.TIMEOUT_TIME);
        return CommonResult.success(null);
    }

    /**
     * 设置邮箱验证码
     */
    @GetMapping("/getEmailCode")
    public CommonResult<String> getEmailCode(HttpServletRequest request,
                                             @Email(message = "邮箱格式错误")
                                             @NotBlank
                                             @RequestParam("email") String email,

                                             @NotBlank
                                             @RequestParam("type") String type
    ) {
        String code = randomCode(6);
        String handlerType = "其它";
        switch (type) {
            case "login":
                handlerType = "登陆";
                break;
            case "register":
                handlerType = "注册";
                break;
            default:
                break;
        }
        try {
            sendFreemarkerMail(email, code, handlerType);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new BusinessException(e);
        }

        log.debug("邮件验证码为： {}", code);

        //保存验证码到 session
        request.getSession().setAttribute("emailCode", code);
        request.getSession().setAttribute("emailCodeTimeout",
                System.currentTimeMillis() + AuthConstant.TIMEOUT_TIME);
        return CommonResult.success(null);
    }

    /**
     * 发送验证码邮件
     */
    public void sendFreemarkerMail(String toEmail, String code, String handlerType) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("HappyMap 验证码");
        // 设置邮件发送者
        helper.setFrom(mailProperties.getUsername());
        // 设置邮件接收者，可以有多个接收者
        helper.setTo(toEmail);

        // // 设置邮件抄送人，可以有多个抄送人
        // helper.setCc("37xxxxx37@qq.com");
        // // 设置隐秘抄送人，可以有多个
        // helper.setBcc("14xxxxx098@qq.com");
        helper.setSentDate(new Date());
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("handlerType", handlerType);
        StringWriter out = new StringWriter();
        //模板渲染，渲染的结果将被保存到 out 中 ，将 out 中的 html 字符串发送即可
        template.process(map, out);
        helper.setText(out.toString(), true);
        javaMailSender.send(mimeMessage);
    }

    private static final Random random = new Random();

    /**
     * 生成随机 Code
     */
    public static String randomCode(int length) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
