package com.study.ffmpeg.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Service
public class VideoService {
    private static final String SAVING_PATH = "C:\\Users\\hyeon\\Documents\\study-ffmpeg\\img\\";
    @Value("${ffmpeg.ffmpeg_path}")
    private String ffmpegPath;
    @Value("${ffmpeg.ffprobe_path}")
    private String ffprobePath;

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    @PostConstruct
    public void init() {
        try {
            ffmpeg = new FFmpeg(ffmpegPath);
            ffprobe = new FFprobe(ffprobePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String save(MultipartFile file) throws IOException {
        file.transferTo(new File(SAVING_PATH + file.getOriginalFilename()));

        return SAVING_PATH + file.getOriginalFilename();
    }

    public HashMap<String, String> readInfo(MultipartFile file) throws IOException {
        String filePath = save(file);

        FFmpegProbeResult probeResult = ffprobe.probe(filePath);

        HashMap<String, String> response = new HashMap<>();
        response.put("bit_rate", String.valueOf(probeResult.getStreams().get(0).bit_rate));
        response.put("width", String.valueOf(probeResult.getStreams().get(0).width));
        response.put("height", String.valueOf(probeResult.getStreams().get(0).height));
        response.put("codec_name", String.valueOf(probeResult.getStreams().get(0).codec_name));
        response.put("codec_type", String.valueOf(probeResult.getStreams().get(0).codec_type));
        return response;
    }

    public void createThumbnail(MultipartFile file, String thumbnailPath) throws IOException {
        String filePath = save(file);

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)
                .setInput(filePath)
                .addExtraArgs("-ss", "00:00:05")
                .addOutput(thumbnailPath)
                .setFrames(1)
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    public void cut(MultipartFile file, String newPath) throws IOException {
        String filePath = save(file);

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)
                .setInput(filePath)
                .addExtraArgs("-ss", "00:00:05")
                .addExtraArgs("-t", "00:00:06")
                .addOutput(newPath)
                .setVideoCodec("copy")
                .setAudioCodec("copy")
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    public void merge(MultipartFile file1, MultipartFile file2, String newPath) throws IOException {
        String filePath1 = save(file1);
        String filePath2 = save(file2);

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)
                .setInput(filePath1)
                .setInput(filePath2)
                .addExtraArgs("-f", "concat")
                .addExtraArgs("-safe", "0")
                .addOutput(newPath)
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

}
