/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

package com.luter.heimdall.starter.captcha.producer;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.luter.heimdall.starter.captcha.dto.CaptchaDTO;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@Slf4j
public class SimpleCaptchaProducer {
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final Integer SIZE;
    private final int LINES;
    private final int WIDTH;
    private final int HEIGHT;
    private final int FONT_SIZE;
    private final boolean TILT;

    private final Color BACKGROUND_COLOR;

    private SimpleCaptchaProducer(Builder builder) {
        SIZE = builder.size;
        LINES = builder.lines;
        WIDTH = builder.width;
        HEIGHT = builder.height;
        FONT_SIZE = builder.fontSize;
        TILT = builder.tilt;
        BACKGROUND_COLOR = builder.backgroundColor;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public CaptchaDTO createImage() throws IOException {
        StringBuilder sb = new StringBuilder();
        // 创建空白图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 获取图片画笔
        Graphics2D graphic = image.createGraphics();
        // 设置抗锯齿
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 设置画笔颜色
        graphic.setColor(BACKGROUND_COLOR);
        // 绘制矩形背景
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        // 画随机字符
        Random ran = new Random();

        //graphic.setBackground(Color.WHITE);

        int codeWidth = WIDTH / (SIZE + 1);
        // 字符所处的y轴的坐标
        int y = HEIGHT * 3 / 4;

        for (int i = 0; i < SIZE; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 初始化字体
            Font font = new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE);

            if (TILT) {
                // 随机一个倾斜的角度 -45到45度之间
                int theta = ran.nextInt(45);
                // 随机一个倾斜方向 左或者右
                theta = (ran.nextBoolean()) ? theta : -theta;
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.rotate(Math.toRadians(theta), 0, 0);
                font = font.deriveFont(affineTransform);
            }
            // 设置字体大小
            graphic.setFont(font);

            // 计算当前字符绘制的X轴坐标
            int x = (i * codeWidth) + (codeWidth / 2);

            // 取随机字符索引
            int n = ran.nextInt(CHARS.length);
            // 得到字符文本
            String code = String.valueOf(CHARS[n]);
            // 画字符
            graphic.drawString(code, x, y);

            // 记录字符
            sb.append(code);
        }
        // 画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }
        // 返回验证码和图片
        return new CaptchaDTO()
                .setCode(sb.toString())
                .setContent(bufferedImageToBase64(image))
                .setUuid(IdUtil.fastSimpleUUID());
    }

    private Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    }

    public static class Builder {
        private int size = 4;
        private int lines = 10;
        private int width = 80;
        private int height = 35;
        private int fontSize = 25;
        private boolean tilt = true;
        private Color backgroundColor = Color.LIGHT_GRAY;

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setLines(int lines) {
            this.lines = lines;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setFontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder setTilt(boolean tilt) {
            this.tilt = tilt;
            return this;
        }

        public Builder setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public SimpleCaptchaProducer build() {
            return new SimpleCaptchaProducer(this);
        }
    }

    public static String bufferedImageToBase64(BufferedImage bufferedImage) throws IOException {
        //io流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //写入流中
        ImageIO.write(bufferedImage, "png", baos);
        //转换成字节
        byte[] bytes = baos.toByteArray();
        //转换成base64串
        String pngBase64 = Base64.encode(bytes).trim();
        //删除 \r\n
        pngBase64 = pngBase64.replaceAll("\n", "").replaceAll("\r", "");
        return "data:image/jpg;base64," + pngBase64;
    }

    public static BufferedImage base64ToBufferedImage(String base64) throws IOException {
        byte[] bytes1 = Base64.decode(base64);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
        return ImageIO.read(bais);
    }
}
