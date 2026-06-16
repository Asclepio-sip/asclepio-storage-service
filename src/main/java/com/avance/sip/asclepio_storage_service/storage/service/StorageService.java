package com.avance.sip.asclepio_storage_service.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.UUID;

@Service
public class StorageService {

    private final S3Client s3Client;

    @Value("${r2.bucket}")
    private String bucket;

    @Value("${r2.endpoint}")
    private String endpoint;

    @Value("${r2.public-url}")
    private String publicUrl;

    public StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String upload(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String extensao = originalName.substring(originalName.lastIndexOf("."));

            String key = "produtos/" + UUID.randomUUID() + extensao;

            s3Client.putObject(
                    b -> b.bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType()),
                    RequestBody.fromBytes(file.getBytes())
            );

            return publicUrl + "/" + key;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload da imagem", e);
        }
    }
}