package br.gov.mt.seplag.music_library_api.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Service
public class MinioStorageService {

    private final io.minio.MinioClient minioClient;
    private final String bucket;

    public MinioStorageService(io.minio.MinioClient minioClient,
                               @Value("${minio.bucket:album-covers}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    public String uploadAlbumImage(Integer albumId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo é obrigatório");
        }
        String ct = file.getContentType();
        if (ct == null || (!MediaType.IMAGE_JPEG_VALUE.equals(ct) && !MediaType.IMAGE_PNG_VALUE.equals(ct))) {
            throw new IllegalArgumentException("Apenas PNG ou JPEG são aceitos");
        }
        ensureBucketExists();
        String ext = file.getOriginalFilename() != null && file.getOriginalFilename().toLowerCase().endsWith(".png") ? ".png" : ".jpg";
        String objectKey = "albums/" + albumId + "/images/" + UUID.randomUUID() + ext;
        try (InputStream in = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(in, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return objectKey;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar arquivo para o MinIO", e);
        }
    }

    public String presignedGetUrl(String objectKey, Duration expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry((int) expiry.getSeconds())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar URL pré-assinada", e);
        }
    }

    private void ensureBucketExists() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao verificar/criar bucket no MinIO", e);
        }
    }
}
