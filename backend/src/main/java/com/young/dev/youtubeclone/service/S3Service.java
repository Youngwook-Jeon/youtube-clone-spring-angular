package com.young.dev.youtubeclone.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service @RequiredArgsConstructor
public class S3Service implements FileService {

    public static final String BUCKET_NAME = "youtube-clone-dev-young";
    private final AmazonS3Client amazonS3Client;

    @Override
    public String uploadFile(MultipartFile file) {
        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = UUID.randomUUID().toString() + filenameExtension;
        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3Client.putObject(BUCKET_NAME, key, file.getInputStream(), metadata);
        } catch (IOException ioException) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "파일 업로드 중 익셉션이 발생했습니다.");
        }

        amazonS3Client.setObjectAcl(BUCKET_NAME, key, PublicRead);

        return amazonS3Client.getResourceUrl(BUCKET_NAME, key);
    }
}
