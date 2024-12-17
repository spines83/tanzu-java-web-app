package com.example.springboot;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.core.ipc.http.HttpSender.Response;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class S3Controller {

    private final S3Client s3Client;

    public S3Controller () {
        s3Client = S3Client.builder()
                        .endpointOverride(URI.create(System.getenv("MINIO_ENDPOINT")))
                        .region(Region.US_EAST_1)
                        .credentialsProvider(
                            StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(System.getenv("MINIO_ACCESS_KEY"),
                                                           System.getenv("MINIO_ACCESS_SECRET"))
                            )               
                        )
                        .build();
        
    }

	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
		// Assume the bucket is already created
        String fileName = file.getOriginalFilename();

        try {

            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(System.getenv("MINIO_BUCKET_NAME"))
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build(),
                RequestBody.fromBytes(file.getBytes())
            );

            return ResponseEntity.ok("File uploaded successfully: " + fileName);

        } catch (S3Exception e) {
            return ResponseEntity.status(500).body("S3 error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File error: " + e.getMessage());
        }
	}

}