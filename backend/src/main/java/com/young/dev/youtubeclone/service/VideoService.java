package com.young.dev.youtubeclone.service;

import com.young.dev.youtubeclone.dto.UploadVideoResponse;
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
    private final UserService userService;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);

        Video savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        Video savedVideo = getVideoById(videoDto.getId());
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        Video savedVideo = getVideoById(videoId);
        String thumbnailUrl = s3Service.uploadFile(file);
        savedVideo.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    private Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Cannot find video by id: " + videoId)
                );
    }

    public VideoDto getVideoDetails(String videoId) {
        Video savedVideo = getVideoById(videoId);
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setId(savedVideo.getId());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setTags(savedVideo.getTags());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());

        return videoDto;
    }

    public VideoDto likeVideo(String videoId) {
        Video videoById = getVideoById(videoId);
        if (userService.ifLikedVideo(videoId)) {
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if (userService.ifDisLikedVideo(videoId)) {
            videoById.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        } else {
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        }

        videoRepository.save(videoById);

        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(videoById.getVideoUrl());
        videoDto.setThumbnailUrl(videoById.getThumbnailUrl());
        videoDto.setId(videoById.getId());
        videoDto.setTitle(videoById.getTitle());
        videoDto.setDescription(videoById.getDescription());
        videoDto.setTags(videoById.getTags());
        videoDto.setVideoStatus(videoById.getVideoStatus());
        videoDto.setLikeCount(videoById.getLikes().get());
        videoDto.setDislikeCount(videoById.getDisLikes().get());

        return videoDto;
    }
}
