package redlib.backend.controller;

// ... 省略其他import ...


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redlib.backend.annotation.NeedNoPrivilege;
import redlib.backend.service.utils.RandomValidateCodeUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.codec.digest.DigestUtils.md5;


@RestController
@RequestMapping("/Code")
public class ValidateCodeController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ValidateCodeController.class);


    @GetMapping("/getVerify")
    @NeedNoPrivilege
    public ResponseEntity<byte[]> getVerify(HttpServletRequest request, HttpServletResponse response) {

        RandomValidateCodeUtil randomValidateCodeUtil = new RandomValidateCodeUtil();
        RandomValidateCodeUtil.CaptchaResult captchaResult = randomValidateCodeUtil.getRandImage();
        BufferedImage image = captchaResult.getImage();

        String code = captchaResult.getCode() ;
        String salt = "gkdhhh";
        // 基于spring框架中的DigestUtils工具类进行密码加密
        String hashcode = DigestUtils.md5DigestAsHex((code + salt).getBytes());
        Cookie customInfoCookie = new Cookie("ValidateCode", hashcode);
        customInfoCookie.setPath("/");
        customInfoCookie.setHttpOnly(true);
        response.addCookie(customInfoCookie);




        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", baos);


            return ResponseEntity
                    .ok()
                    .header("Cache-Control", "no-store, no-cache")
                    .header("Content-Type", "image/jpeg")
                    .body(baos.toByteArray());
        } catch (Exception ex) {
            LOGGER.error("Error writing captcha to the output stream", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    /**
     * 校验验证码
     */
    @RequestMapping(value = "/checkVerify", method = RequestMethod.POST, headers = "Accept=application/json")
    @NeedNoPrivilege
    public boolean checkVerify(@RequestParam String verifyInput, HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            //从session中获取随机数
            String inputStr = verifyInput;
            String random = "";

//            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            for (Cookie cookie : cookies){
                if (Objects.equals(cookie.getName(), "ValidateCode")){
                    random = (String) cookie.getValue();
                }

            }
            System.out.println("random is :"+random);
            System.out.println("inputStr is : "+inputStr);
            String salt = "gkdhhh";
            // 基于spring框架中的DigestUtils工具类进行密码加密
            String hashcode = DigestUtils.md5DigestAsHex((inputStr + salt).getBytes());

            if (random == null || "".equals(random) || !random.equalsIgnoreCase(hashcode)) {
                System.out.println("验证码校验失败");
                Assert.isNull(null,"验证码校验失败");

                return false;
            } else {
                System.out.println("验证码校验成功！");
                return true;
            }

        } catch (Exception e) {
            LOGGER.error("验证码校验失败", e);
            Assert.isNull(null,"验证码校验失败");
            return false;
        }
    }

}

