package com.young.dev.youtubeclone.service;

import com.young.dev.youtubeclone.dto.VideoDto;
import com.young.dev.youtubeclone.model.Video;
import com.young.dev.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service @RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public void uploadVideo(MultipartFile multipartFile) {
        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);

        videoRepository.save(video);
    }

    public VideoDto editVideo(VideoDto videoDto) {
        Video savedVideo = videoRepository.findById(videoDto.getId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Cannot find video by id: " + videoDto.getId())
                );
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        videoRepository.save(savedVideo);
        return videoDto;
    }
}
