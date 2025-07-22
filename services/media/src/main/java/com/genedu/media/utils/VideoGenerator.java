package com.genedu.media.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.util.List;

@Slf4j
public class VideoGenerator {
    private static final long SLIDE_PADDING_MICROSECONDS = 750_000;

    public static void generate(List<Path> slideImages, List<Path> audios, Path outputVideo) {
        if (slideImages.size() != audios.size()) {
            throw new IllegalArgumentException("The number of slide images and audio files must match.");
        }

        FFmpegFrameRecorder recorder = null;
        Java2DFrameConverter converter = new Java2DFrameConverter();

        try {
            // BEST PRACTICE: Use standard 1080p resolution for better quality.
            int width = 1920;
            int height = 1080;

            File outputDir = outputVideo.getParent().toFile();
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                throw new IOException("Failed to create output directory: " + outputDir);
            }

            recorder = new FFmpegFrameRecorder(outputVideo.toFile(), width, height, 2); // Set audio channels to 2 (stereo)
            recorder.setVideoOption("preset", "ultrafast");
            recorder.setVideoOption("crf", "28"); // A good balance of quality and file size
            recorder.setFormat("mp4");
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setVideoBitrate(2000000); // Increased for better 1080p quality
            recorder.setFrameRate(30);
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // Essential for compatibility

            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setAudioBitrate(192000); // Good quality stereo audio
            recorder.setSampleRate(44100);
            recorder.setAudioChannels(2); // Use stereo for broader compatibility

            recorder.start();

            // This will keep track of the total video time in microseconds.
            long totalTimestamp = 0;

            for (int i = 0; i < slideImages.size(); i++) {
                log.info("Processing slide {} with audio {}", i, audios.get(i));

                // --- 1. Prepare the static image frame for this segment ---
                BufferedImage originalImage = ImageIO.read(slideImages.get(i).toFile());
                if (originalImage == null) {
                    throw new IOException("Failed to load image: " + slideImages.get(i));
                }
                // Convert to a type that JavaCV understands well to prevent color issues.
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                image.createGraphics().drawImage(originalImage, 0, 0, width, height, null);
                Frame imageFrame = converter.convert(image);

                // --- 2. Interleave audio and video frames for this segment ---
                FFmpegFrameGrabber audioGrabber = null;
                try {
                    audioGrabber = new FFmpegFrameGrabber(audios.get(i).toFile());
                    audioGrabber.start();

                    long videoTimestamp = totalTimestamp;

                    Frame audioFrame;
                    while ((audioFrame = audioGrabber.grab()) != null) {
                        long absoluteAudioTimestamp = totalTimestamp + audioFrame.timestamp;

                        // Record video frames until our video timestamp "catches up" to the audio.
                        while (videoTimestamp < absoluteAudioTimestamp) {
                            recorder.setTimestamp(videoTimestamp);
                            recorder.record(imageFrame);
                            // Manually increment our video timestamp counter.
                            videoTimestamp += (1000000L / recorder.getFrameRate());
                        }

                        // Now that the timelines are aligned, record the audio frame with its absolute timestamp.
                        recorder.setTimestamp(absoluteAudioTimestamp);
                        recorder.record(audioFrame);
                    }

                    long audioDuration = audioGrabber.getLengthInTime();
                    long endOfAudioTimestamp = totalTimestamp + audioDuration;

                    // Fill any remaining video frames to match the full audio duration.
                    while (videoTimestamp < endOfAudioTimestamp) {
                        recorder.setTimestamp(videoTimestamp);
                        recorder.record(imageFrame);
                        videoTimestamp += (1000000L / recorder.getFrameRate());
                    }

                    // CRITICAL FIX: Update the master timestamp to the precise end of the last recorded video frame.
                    // This ensures the next slide starts at the correct, monotonically increasing time.
                    totalTimestamp = videoTimestamp;

                } finally {
                    if (audioGrabber != null) {
                        audioGrabber.stop();
                        audioGrabber.release();
                    }
                }
            }

            recorder.stop();
            recorder.release();
            converter.close();
            log.info("Video generation successful. Output at: {}", outputVideo);

        } catch (Exception e) {
            log.error("Video generation failed", e);
            throw new RuntimeException("Video generation failed: " + e.getMessage(), e);
        } finally {
            // Ensure resources are always released, even on error.
            try {
                if (recorder != null) {
                    recorder.close();
                }
            } catch (Exception e) {
                log.warn("Failed to release recorder resources", e);
            }
        }
    }

}
