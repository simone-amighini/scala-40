package it.simoneamighini.scala40.view.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SVGLoader {
    private static final Map<String, Image> imagesCache = new HashMap<>();

    private static class BufferedImageTranscoder extends ImageTranscoder {
        private BufferedImage bufferedImage = null;

        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public void writeImage(BufferedImage image, TranscoderOutput transcoderOutput) {
            this.bufferedImage = image;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }
    }

    public static Image load(String path) {
        path = "images/" + path;

        // search in cache
        if (imagesCache.containsKey(path)) {
            return imagesCache.get(path);
        }

        // if the image is not in cache, load from disk and put in cache
        BufferedImageTranscoder bufferedImageTranscoder = new BufferedImageTranscoder();
        try (InputStream file = GuiMain.class.getResourceAsStream(path)) {
            TranscoderInput transcoderInput = new TranscoderInput(file);
            try {
                bufferedImageTranscoder.transcode(transcoderInput, null);
                Image image = SwingFXUtils.toFXImage(
                        bufferedImageTranscoder.getBufferedImage(),
                        null
                );
                imagesCache.put(path, image);
                return image;
            } catch (TranscoderException transcoderException) {
                return null;
            }
        }
        catch (IOException exception) {
            return null;
        }
    }
}
