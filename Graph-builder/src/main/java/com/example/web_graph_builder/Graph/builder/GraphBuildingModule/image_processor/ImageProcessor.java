package com.example.web_graph_builder.Graph.builder.GraphBuildingModule.image_processor;

import jakarta.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ImageProcessor {
    public String processImage(String base64Input) throws Exception {
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Input);
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage inputImage = ImageIO.read(bais);
        int borderSize = 10;
        int newWidth = inputImage.getWidth() - 2 * borderSize;
        int newHeight = inputImage.getHeight() - 2 * borderSize;
        BufferedImage croppedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = croppedImage.createGraphics();
        g.drawImage(inputImage, 0, 0, newWidth, newHeight, borderSize, borderSize, inputImage.getWidth() - borderSize, inputImage.getHeight() - borderSize, null);
        g.dispose();

        Color backgroundColor = Color.WHITE;
        BufferedImage outputImage = new BufferedImage(croppedImage.getWidth(), croppedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < croppedImage.getHeight(); y++) {
            for (int x = 0; x < croppedImage.getWidth(); x++) {
                int pixel = croppedImage.getRGB(x, y);
                if (new Color(pixel, true).equals(backgroundColor)) {
                    outputImage.setRGB(x, y, 0x00FFFFFF); // Set alpha to 0 (transparent)
                } else {
                    outputImage.setRGB(x, y, pixel);
                }
            }
        }

        // Convert the processed image to base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", baos);
        byte[] processedImageBytes = baos.toByteArray();

        return DatatypeConverter.printBase64Binary(processedImageBytes);
    }
}
