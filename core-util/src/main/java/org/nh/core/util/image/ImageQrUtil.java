package org.nh.core.util.image;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @Classname ImageQrUtil
 * @Description TODO 二维码功能
 * @Date 2020/1/15 2:46 PM
 * @Created by nihui
 */
public class ImageQrUtil {

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;


    /**
     * 生成二维码图片
     * @param contents
     * @return
     */
    public static BufferedImage getQRcodeBufferedImage(String contents) {
        if (StringUtils.isBlank(contents)) {
            return null;
        }
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(contents,
                    BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        BufferedImage image = toBufferedImage(bitMatrix);
        return image;
    }


    /**
     * 将Bit点阵转换为图片
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }


    /**
     * 二维码文件解码
     * @param file
     * @return
     */
    public String decode(File file) {
        try {
//			Reader reader = new MultiFormatReader();
            BufferedImage image;
            try {
                image = ImageIO.read(file);
                if (image == null) {
                    System.out.println("Could not decode image");
                    return null;
                }
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                        source));
                Result result;
                Hashtable<DecodeHintType,String> hints = new Hashtable<DecodeHintType,String>();
                hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
                // 解码设置编码方式为：utf-8，
                result = new MultiFormatReader().decode(bitmap, hints);
                String resultStr = result.getText();
                return resultStr;

            } catch (IOException ioe) {
                System.out.println(ioe.toString());
                return null;
            } catch (ReaderException re) {
                System.out.println(re.toString());
                return null;
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
