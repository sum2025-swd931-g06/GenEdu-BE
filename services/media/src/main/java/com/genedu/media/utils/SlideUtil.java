package com.genedu.media.utils;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SlideUtil {

    public static List<Path> convertToImages(Path pptxPath, Path outputDir) {
        List<Path> slideImagePaths = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(pptxPath.toFile());
             XMLSlideShow ppt = new XMLSlideShow(fis)) {

            Dimension pageSize = ppt.getPageSize();
            List<XSLFSlide> slides = ppt.getSlides();

            // Ensure output directory exists
            Files.createDirectories(outputDir);

            for (int i = 0; i < slides.size(); i++) {
                XSLFSlide slide = slides.get(i);

                // Create a new image
                BufferedImage img = new BufferedImage(
                        pageSize.width, pageSize.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = img.createGraphics();

                // Improve quality
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setPaint(Color.white); // background
                graphics.fill(new Rectangle(0, 0, pageSize.width, pageSize.height));

                // Draw slide
                slide.draw(graphics);
                graphics.dispose();

                // Save image
                Path outputImagePath = outputDir.resolve("slide-" + (i + 1) + ".png");
                ImageIO.write(img, "png", outputImagePath.toFile());
                slideImagePaths.add(outputImagePath);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert PPTX to images", e);
        }

        return slideImagePaths;
    }
}
