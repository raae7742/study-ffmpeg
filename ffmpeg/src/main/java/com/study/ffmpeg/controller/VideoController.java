package com.study.ffmpeg.controller;

import com.study.ffmpeg.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/read")
    public HashMap<String, String> read(@RequestParam("file") MultipartFile file) throws IOException {
        return videoService.readInfo(file);
    }

    @PostMapping("/thumbnail")
    public void createThumbnail(@RequestParam("file") MultipartFile file, @RequestParam String thumbnail_path) throws IOException {
        videoService.createThumbnail(file, thumbnail_path);
    }
}
