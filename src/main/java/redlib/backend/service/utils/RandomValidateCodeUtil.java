package redlib.backend.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomValidateCodeUtil {
    public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";//放到session中的key
    private static final String RAND_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int WIDTH = 95; // 图片宽
    private static final int HEIGHT = 25; // 图片高
    private static final int LINE_SIZE = 40; // 干扰线数量
    private static final int STRING_NUM = 4; // 随机产生字符数量

    private final Random random = new Random();

    // Logger实例
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomValidateCodeUtil.class);

    // 获取字体
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
    }

    // 获取随机颜色
    private Color getRandColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    // 生成随机图片
    public CaptchaResult getRandImage() {
        // 创建BufferedImage对象
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);

        // 获取Graphics对象
        Graphics g = image.getGraphics();

        // 填充背景色
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        g.setFont(getFont());

        // 绘制干扰线
        for (int i = 0; i <= LINE_SIZE; i++) {
            drawLine(g);
        }

        // 绘制随机字符
        String randomString = "";
        for (int i = 1; i <= STRING_NUM; i++) {
            randomString = drawString(g, randomString, i);
        }

        g.dispose(); // 释放资源

        // 将验证码文本打印到控制台，用于调试验证
        LOGGER.debug("Generated captcha: {}", randomString);

        return new CaptchaResult(image, randomString);
    }

    // 绘制字符串
    private String drawString(Graphics g, String randomString, int i) {
        g.setFont(getFont());
        g.setColor(getRandColor(50, 100));
        String randChar = String.valueOf(RAND_STRING.charAt(random.nextInt(RAND_STRING.length())));
        randomString += randChar;
        g.drawString(randChar, 13 * i, 16);
        return randomString;
    }

    // 绘制干扰线
    private void drawLine(Graphics g) {
        int x = random.nextInt(WIDTH);
        int y = random.nextInt(HEIGHT);
        int xl = random.nextInt(12) + 1; // 添加干扰线长度
        int yl = random.nextInt(6) + 1; // 添加干扰线高度

        g.setColor(getRandColor(170, 200)); // 设置干扰线颜色
        g.drawLine(x, y, x + xl, y + yl);
    }

    // 获取随机的字符
    public String getRandomString(int num) {
        return String.valueOf(RAND_STRING.charAt(num));
    }

    public static class CaptchaResult {
        private final BufferedImage image;
        private final String code;

        public CaptchaResult(BufferedImage image, String code) {
            this.image = image;
            this.code = code;
        }

        public BufferedImage getImage() {
            return image;
        }

        public String getCode() {
            return code;
        }
    }
}