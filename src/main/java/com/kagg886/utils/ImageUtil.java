package com.kagg886.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageUtil {
    public static BufferedImage verticalImageGen(List<BufferedImage> images) {
        if (images == null) {
            return null;
        }
        int width = images.stream().mapToInt(BufferedImage::getWidth).max().getAsInt();
        int height = images.stream().mapToInt(BufferedImage::getHeight).sum();

        BufferedImage rtn = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = rtn.createGraphics();

        int newH = 0;
        for (BufferedImage image : images) {
            graphics2D.drawImage(image, 0, newH, image.getWidth(), image.getHeight(), null);
            newH += image.getHeight();
        }

        graphics2D.dispose();
        return rtn;
    }
}
