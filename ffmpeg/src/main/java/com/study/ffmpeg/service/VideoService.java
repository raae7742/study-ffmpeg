package com.study.ffmpeg.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Service
public class VideoService {
    private static final String SAVING_PATH = "C:\\Users\\hyeon\\Documents\\study-ffmpeg\\img\\";

    public void save(MultipartFile file) throws IOException {
        file.transferTo(new File(SAVING_PATH + file.getOriginalFilename()));
    }

    public HashMap<String, String> readInfo(MultipartFile file) throws IOException {
        save(file);

        FFmpeg ffmpeg = new FFmpeg("C:/ProgramData/chocolatey/bin/ffmpeg");
        FFprobe ffprobe = new FFprobe("C:/ProgramData/chocolatey/bin/ffprobe");

        FFmpegProbeResult probeResult = ffprobe.probe(SAVING_PATH + file.getOriginalFilename());

        HashMap<String, String> response = new HashMap<>();
        response.put("bit_rate", String.valueOf(probeResult.getStreams().get(0).bit_rate));
        response.put("width", String.valueOf(probeResult.getStreams().get(0).width));
        response.put("height", String.valueOf(probeResult.getStreams().get(0).height));
        response.put("codec_name", String.valueOf(probeResult.getStreams().get(0).codec_name));
        response.put("codec_type", String.valueOf(probeResult.getStreams().get(0).codec_type));
        return response;
    }

    public void merge(String inputPath, String outputPath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("C:/ProgramData/chocolatey/bin/ffmpeg");
        FFprobe ffprobe = new FFprobe("C:/ProgramData/chocolatey/bin/ffprobe");

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)
                .addInput(inputPath)
                .addExtraArgs("-f", "concat")
                .addExtraArgs("-safe", "0")
                .addOutput(outputPath + "mergeVideo.mp4")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

    }
}
