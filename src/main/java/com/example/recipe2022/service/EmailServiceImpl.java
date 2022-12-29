package com.example.recipe2022.service;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.recipe2022.config.redis.RedisUtils;
import com.example.recipe2022.service.interfacee.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final RedisUtils redisUtil;
    private final JavaMailSender emailSender;
    public static String ePW = createKey();

    private MimeMessage createMessage(String to)throws Exception {
        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ ePW);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 만개의 레시피 인증 코드 </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<p>인증이 안된다면, dyw1014@gachon.ac.kr로 연락주세요<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+=  ePW+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("dyw1014@gachon.ac.kr","10000's recipe"));//보내는 사람

        return message;
    }
    public static void updateKey() {
        ePW = createKey();
    }
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        SecureRandom rnd = new SecureRandom();
        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
    @Override
    public String sendSimpleMessage(String to)throws Exception {
        updateKey();
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        } catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        redisUtil.setDataExpire(ePW, to, 60 * 5L);
        return ePW;
    }
}