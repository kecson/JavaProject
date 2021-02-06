package com.kecson;

import com.kecson.util.QRCodeUtil;

import java.io.File;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

/**
 * <pre>
 * *********************************************************
 * QRtool是一个生成二维码和解析二维码工具，github:[www.github.com/kecson/JavaProject]
 *
 * -h       显示此帮助信息
 * -e       生成二维码
 * -d       解析二维码
 *
 * ***********************************************
 * -e    生成二维码相关参数:
 *          -text=          二维码内容
 *          -file=          保存的文件名，如：qrcode.jpg
 *          -dir=          保存的文件目录，如：D:/
 *          -logo=          二维码中间Logo的图片路径(此项可不填)
 * ---------------------------------------------------------
 * 生成二维码命令格式为:
 * 不带Logo命令:   java -jar QRtool-1.0.jar -e -text=hello1 -file=qr.jpg -dir=C:/
 * 带有Logo命令:   java -jar QRtool-1.0.jar -e -text=hello1 -file=qr.jpg -dir=C:/ -logo=C:/logo.jpg
 * *********************************************************\
 * -d   解析二维码相关参数
 *          -f 二维码文件路径
 * ---------------------------------------------------------
 * 解析二维码命令格式为:
 * java -jar QRtool-1.0.jar -d  D:/qr-logo.jpg
 * *********************************************************\
 *  </pre>
 */
public class QRCodeTool {

    private static final String HELP_TIPS = " \r\n*********************************************************\n" +
            " QRtool是一个生成二维码和解析二维码工具，github:[www.github.com/kecson/JavaProject]\r\n" +
            " -h       显示此帮助信息\n" +
            " -e       生成二维码\n" +
            " -d       解析二维码\n" +
            " *********************************************************\n" +
            " -e 生成二维码相关参数:\n" +
            "     -text=      二维码内容如：-text=www.github.com/kecson/JavaProject\n" +
            "     -file=      保存的文件名，如：-file=qrcode.jpg \n" +
            "     -dir=       保存的文件目录，如：-dir=D:/ \n" +
            "     -logo=      二维码中间Logo的图片路径(此项可不填)，如: -logo=C:/logo.jpg\n" +
            " ---------------------------------------------------------\n" +
            " 生成二维码命令格式为:\n" +
            " 不带Logo命令:   java -jar  QRtool-1.0.jar -e -text=hello1 -file=qr.jpg -dir=C:/\n" +
            " 带有Logo命令:   java -jar  QRtool-1.0.jar -e -text=hello1 -file=qr.jpg -dir=C:/ -logo=C:/logo.jpg\n" +
            " *********************************************************\n" +
            " -d   解析二维码文件路径\n" +
            " ---------------------------------------------------------\n" +
            " 解析二维码命令格式为:\n" +
            " java -jar QRtool-1.0.jar -d  D:/qr-logo.jpg\n" +
            " *********************************************************";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println(HELP_TIPS);
        } else if (args.length == 1) {
            String arg = args[0].trim();
            if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-help")) {
                System.out.println(HELP_TIPS);
            }
        } else {
            String cmd = args[0].trim();
            if ("-e".equals(cmd)) {
                encode(Arrays.copyOfRange(args, 1, args.length));
            } else {
                if ("-d".equals(cmd)) {
                    decode(args[1]);
                } else {
                    System.err.println("命令参数错误：" + Arrays.toString(args));
                    System.out.println(HELP_TIPS);
                }
            }
        }
    }

    public static void decode(String filePath) throws Exception {
        String decode = QRCodeUtil.decode(filePath);
        System.out.println("" + decode);
    }

    private static void encode(String[] args) throws Exception {


        String text = "";


        String fileName = "";
        String dirPath = "";

        String logoPath = "";

        String labelIconPath = "", label = "";

        if (args.length != 0) {
            String split[];
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-text=")) {
                    split = arg.trim().split("=");
                    if (split.length != 2) {
                        System.err.println("must be like： -text=hello1");
                    }
                    text = split[1];
                } else if (arg.startsWith("-file=")) {
                    split = arg.trim().split("=");
                    if (split.length != 2) {
                        System.err.println("must be like： -file=qr.jpg");
                    }
                    fileName = split[1];
                } else if (arg.startsWith("-dir=")) {
                    split = arg.trim().split("=");
                    if (split.length != 2) {
                        System.err.println("must be like： -dir=C:/");
                    }
                    dirPath = split[1];
                } else if (arg.startsWith("-logo=")) {
                    split = arg.trim().split("=");
                    if (split.length != 2) {
                        System.err.println("must be like： -logo=C:/logo.jpg");
                    }
                    logoPath = split[1];
                } else if (arg.startsWith("-labelIcon=")) {
                    split = arg.trim().split("=");
                    if (split.length != 2) {
                        System.err.println("must be like： -labelIcon=C:/icon.jpg");
                    }
                    labelIconPath = split[1];
                } else if (arg.startsWith("-label=")) {
                    split = arg.trim().split("=");
                    if (split.length != 2) {
                        System.err.println("must be like： -label=1.0.0");
                    }
                    label = split[1];
                }
            }
        } else {
            throw new RuntimeErrorException(null, "url or image save path are mandatory!");
        }


        if (text.equals("")) {
            text = "Hello";
        }


        if (".".equalsIgnoreCase(dirPath.trim())
                || "/".equalsIgnoreCase(dirPath.trim())
                || "\\".equalsIgnoreCase(dirPath.trim())
                || "./".equalsIgnoreCase(dirPath.trim())
                || ".\\".equalsIgnoreCase(dirPath.trim())) {
            File currentDir = new File("");//设定为当前文件夹
            dirPath = currentDir.getAbsolutePath();
        }

        QRCodeUtil.encodeWithLogo(text, dirPath, fileName, logoPath, labelIconPath, label);
    }
}

