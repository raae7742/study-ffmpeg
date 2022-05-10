package com.study.ffmpeg.controller;

import com.study.ffmpeg.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
