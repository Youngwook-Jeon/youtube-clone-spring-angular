package com.young.dev.youtubeclone.controller;

import com.young.dev.youtubeclone.dto.CommentDto;
import com.young.dev.youtubeclone.dto.UploadVideoResponse;
import com.young.dev.youtubeclone.dto.VideoDto;
import com.young.dev.youtubeclone.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) {
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(CREATED)
    public String uploadThumbnail(
            @RequestParam("file") MultipartFile file,
            @RequestParam("videoId") String videoId) {
        return videoService.uploadThumbnail(file, videoId);
    }

    @PutMapping
    @ResponseStatus(OK)
    public VideoDto editVideoMetaData(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(OK)
    public VideoDto getVideoDetails(@PathVariable String videoId) {
        return videoService.getVideoDetails(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(OK)
    public VideoDto likeVideo(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/dislike")
    @ResponseStatus(OK)
    public VideoDto disLikeVideo(@PathVariable String videoId) {
        return videoService.disLikeVideo(videoId);
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(OK)
    public void addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {
        videoService.addComment(videoId, commentDto);
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(OK)
    public List<CommentDto> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<VideoDto> getAllVideos() {
        return videoService.getAllVideos();
    }
}
