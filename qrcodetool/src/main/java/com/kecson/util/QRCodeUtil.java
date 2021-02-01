package com.kecson.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;


public class QRCodeUtil {
    private static final String CHARSET_UTF_8 = "utf-8";
    private static final String FORMAT_NAME = "jpg";
    private static final int QRCODE_SIZE = 240;
    private static final int LOGO_WIDTH = 48;
    private static final int LOGO_HEIGHT = 48;

    /**
     * 生成二维码
     *
     * @param content 二维码内容
     * @return 二维码BufferedImage
     * @throws Exception
     */
    public static BufferedImage encode(String content) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET_UTF_8);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, 1);

        for (int x = 0; x < QRCODE_SIZE; x++) {
            for (int y = 0; y < QRCODE_SIZE; y++) {
                image.setRGB(x, y, WHITE.getRGB());
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK.getRGB() : WHITE.getRGB());
            }
        }
        return image;
    }


    public static void encodeWithLogo(String content, String dir, String fileName, String logoPath, String labelIconPath, String label) throws Exception {
        BufferedImage image = encode(content);

        if (null != logoPath && new File(logoPath).exists()) {
            //二维码中间添加Logo
            image = addLogo(image, logoPath);
        }

        image = addBottomLabel(image, labelIconPath, label);

        File file = new File(dir, fileName);
        File imageFile;
        if (file.isDirectory()) {
            String now = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS").format(System.currentTimeMillis());
            imageFile = new File(file, now + ".jpg");
        } else {
            imageFile = file;
        }

        imageFile.mkdirs();
        ImageIO.write(image, FORMAT_NAME, imageFile);
        System.out.println("二维码图片路径:\n" +
                imageFile.getAbsolutePath());
    }

    /**
     * 生成二维码并保存到指定输出流
     *
     * @param content 内容
     * @param output  输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        BufferedImage image = encode(content);
        ImageIO.write(image, "jpg", output);
    }


    /**
     * 二维码中间加Logo
     *
     * @param qrCodeSource 二维码Image
     * @param logoPath     Logo文件名
     * @return
     * @throws Exception
     */
    public static BufferedImage addLogo(BufferedImage qrCodeSource, String logoPath) throws Exception {
        File file = new File(logoPath);
        if (!file.exists()) {
            System.err.println("[" + logoPath + "]   Logo文件不存在！");
            return qrCodeSource;
        }
        Image src = ImageIO.read(file);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (width > LOGO_WIDTH) {
            width = LOGO_WIDTH;
        }
        if (height > LOGO_HEIGHT) {
            height = LOGO_HEIGHT;
        }
        Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        src = image;
        // 插入LOGO
        Graphics2D graph = qrCodeSource.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        graph.dispose();
        return qrCodeSource;

    }


    public static BufferedImage addBottomLabel(BufferedImage sourceImage, String labelIconPath, String label) throws Exception {
        labelIconPath = labelIconPath == null ? "" : labelIconPath;
//        labelIconPath = "logo_apple.png";
//        labelIconPath = "logo_android.png";
        label = label == null ? "" : label.trim();
//        label = "Ver:1.0.0";
        File labelIcon = new File(labelIconPath);

        if (labelIcon.exists() || !label.isEmpty()) {
            int frontSize = 16;

            BufferedImage newImage = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE + frontSize, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < QRCODE_SIZE; x++) {
                for (int y = 0; y < QRCODE_SIZE + frontSize; y++) {
                    newImage.setRGB(x, y, WHITE.getRGB());
                }
            }
            Graphics2D graphics = newImage.createGraphics();

            // 设置“抗锯齿”的属性
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 设置字体类型和大小
            Font font = new Font("Default", Font.PLAIN, frontSize);
            graphics.setFont(font);
            // 设置颜色
            graphics.setColor(BLACK);
            int stringWidth = graphics.getFontMetrics(font).stringWidth(label);

            graphics.drawImage(sourceImage, 0, 0, QRCODE_SIZE, QRCODE_SIZE, null);

            int iconSize = 0;
            int labelWidth = 0;

            if (labelIcon.exists()) {
                iconSize = 32;
                labelWidth += iconSize;
            }
            labelWidth += stringWidth;

            int y = QRCODE_SIZE + 4;


            if (labelIcon.exists()) {

                Image icon = ImageIO.read(labelIcon);
                icon = icon.getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT);
                int iconX = (sourceImage.getWidth() - labelWidth) / 2;
                int iconY = y - 3 * iconSize / 4;
                graphics.drawImage(icon, iconX, iconY, iconSize, iconSize, null);
            }
            if (!label.isEmpty()) {
                // 添加文字
                int labelX = (sourceImage.getWidth() - labelWidth) / 2 + iconSize;
                int labelY = y;
                graphics.drawString(label, labelX, labelY);
            }

            graphics.dispose();
            sourceImage = newImage;
        }
        return sourceImage;
    }

    /* *****************************************
     *           解析二维码
     *******************************************/

    /**
     * 解析二维码
     *
     * @param file 二维码文件
     * @return 二维码内容
     * @throws Exception
     */
    public static String decode(File file)
            throws Exception {
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET_UTF_8);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 解析二维码
     *
     * @param path 二维码文件路径
     * @return 二维码内容
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }
}
