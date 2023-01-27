package com.example.recipe2022.service;

import com.example.recipe2022.config.redis.RedisUtils;
import com.example.recipe2022.data.dto.UserRequestDto;
import com.example.recipe2022.service.interfacee.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final RedisUtils redisUtil;
    private final JavaMailSender emailSender;
    public static String ePW = createKey();

    private MimeMessage createMessage(String to)throws Exception {
        log.info("보내는 대상 : "+ to);
        log.info("인증 번호 : "+ ePW);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msg="";
        msg+= "<div style='margin:20px;'>";
        msg+= "<h1> 인증 코드 </h1>";
        msg+= "<br>";
        msg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msg+= "<br>";
        msg+= "<p>감사합니다.<p>";
        msg+= "<p>인증이 안된다면, dyw1014@gachon.ac.kr로 연락주세요<p>";
        msg+= "<br>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "CODE : <strong>";
        msg+=  ePW+"</strong><div><br/> ";
        msg+= "</div>";
        message.setText(msg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("dyw1014@gachon.ac.kr","10000's recipe"));//보내는 사람

        return message;
    }
    public static void updateKey() {
        ePW = createKey();
    }
    public static String createKey() {
        StringBuilder key = new StringBuilder();
        SecureRandom rnd = new SecureRandom();
        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ( (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ( (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
                default : log.info("error! 나올 수 없는 값이 나왔어요"); break;
            }
        }
        return key.toString();
    }
    public void sendSimpleMessage(UserRequestDto.mailSend mailSend)throws Exception {
        updateKey();
        MimeMessage message = createMessage(mailSend.getEmail());
        try{//예외처리
            emailSender.send(message);
        } catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        if (redisUtil.isExists(ePW)) { redisUtil.deleteData(ePW); }
        redisUtil.setDataExpire(ePW, mailSend.getEmail(), 60 * 5L);
    }
}