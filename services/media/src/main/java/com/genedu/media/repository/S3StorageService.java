package com.genedu.media.repository;

import com.genedu.media.buckets.BucketName;
import com.genedu.media.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService {
    private final S3Client s3Client;
    private final BucketName bucketName;

    public String upload(String path, String fileName, MultipartFile multipartFile) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName.getGeneduBucket())
                .key(path + "/" + fileName)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .build();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));
            log.info("File {} saved to S3 at {}", fileName, path);
        } catch (SdkException e) {
            log.error("Failed to save file to S3: {}", e.getMessage());
            throw new IOException("Failed to save file to S3", e);
        }
        return path + "/" + fileName; // Return the full path of the uploaded file
    }

    public String upload(String path, String fileName, String contentType, byte[] content) throws IOException {
        String fullS3Key = path + "/" + fileName;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName.getGeneduBucket())
                .key(fullS3Key)
                .contentType(contentType)
                .contentLength((long) content.length)
                .build();

        try {
            // BEST PRACTICE: Use fromBytes for direct, efficient byte array uploads.
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
            log.info("Successfully uploaded byte array as file '{}' to S3 key '{}'", fileName, fullS3Key);
        } catch (SdkException e) {
            log.error("Failed to upload byte array to S3. Key: '{}', Error: {}", fullS3Key, e.getMessage(), e);
            throw new IOException("Failed to save byte array to S3", e);
        }
        return fullS3Key;
    }

    public byte[] download(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName.getGeneduBucket())
                .key(key)
                .build();
        try {
            return s3Client.getObject(getObjectRequest).readAllBytes();
        } catch (NoSuchKeyException e) {
            log.warn("Could not find object: {}", key);
            return new byte[0]; // Return empty byte array when object is not found
        } catch (SdkException e) {
            log.error("Failed to download file from S3: {}", e.getMessage());
            throw new IOException("Failed to download file from S3", e);
        }
    }

    public Path downloadToFile(String key, Path targetPath) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName.getGeneduBucket())
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            Files.createDirectories(targetPath.getParent());
            Files.copy(s3Object, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath;
        } catch (NoSuchKeyException e) {
            log.warn("Could not find object: {}", key);
            throw new FileNotFoundException("S3 object not found: " + key);
        } catch (SdkException e) {
            log.error("Failed to download file from S3: {}", e.getMessage());
            throw new IOException("Failed to download file from S3", e);
        }
    }

    public void delete(String folderPrefix) {
        List<ObjectIdentifier> keysToDelete = new ArrayList<>();

        s3Client.listObjectsV2Paginator(builder -> builder.bucket(bucketName.getGeneduBucket())
                .prefix(folderPrefix))
                .contents()
                .forEach(s3Object -> keysToDelete.add(ObjectIdentifier.builder().key(s3Object.key()).build()));

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName.getGeneduBucket())
                .delete(Delete.builder().objects(keysToDelete).build())
                .build();

        try {
            DeleteObjectsResponse deleteResponse = s3Client.deleteObjects(deleteObjectsRequest);
            handleDeleteErrors(deleteResponse.errors());
        } catch (SdkException e) {
            log.error("Failed to delete objects from S3: {}", e.getMessage());
        }
    }

    private void handleDeleteErrors(List<S3Error> errors) {
        if (!errors.isEmpty()) {
            log.error("Errors occurred while deleting objects:");
            errors.forEach(error -> log.error("Object: {}, Error Code: {}, Error Message: {}",
                    error.key(), error.code(), error.message()));
        } else {
            log.info("Objects deleted successfully.");
        }
    }
}
