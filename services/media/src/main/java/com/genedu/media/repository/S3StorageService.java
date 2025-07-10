package com.genedu.media.repository;

import com.genedu.media.buckets.BucketName;
import com.genedu.media.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3StorageService {
    private final S3Client s3Client;
    private static final Logger LOGGER = LoggerFactory.getLogger(S3StorageService.class);
    private final BucketName bucketName;

    public String upload(String path, String fileName, MultipartFile multipartFile) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName.getGeneduBucket())
                .key(path + "/" + fileName)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(FileUtil.convertMultipartFileToFile(multipartFile)));
            LOGGER.info("File {} saved to S3 at {}", fileName, path);
        } catch (SdkException e) {
            LOGGER.error("Failed to save file to S3: {}", e.getMessage());
            throw new IOException("Failed to save file to S3", e);
        }
        return path + "/" + fileName; // Return the full path of the uploaded file
    }

    public byte[] download(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName.getGeneduBucket())
                .key(key)
                .build();
        try {
            return s3Client.getObject(getObjectRequest).readAllBytes();
        } catch (NoSuchKeyException e) {
            LOGGER.warn("Could not find object: {}", key);
            return new byte[0]; // Return empty byte array when object is not found
        } catch (SdkException e) {
            LOGGER.error("Failed to download file from S3: {}", e.getMessage());
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
            LOGGER.error("Failed to delete objects from S3: {}", e.getMessage());
        }
    }

    private void handleDeleteErrors(List<S3Error> errors) {
        if (!errors.isEmpty()) {
            LOGGER.error("Errors occurred while deleting objects:");
            errors.forEach(error -> LOGGER.error("Object: {}, Error Code: {}, Error Message: {}",
                    error.key(), error.code(), error.message()));
        } else {
            LOGGER.info("Objects deleted successfully.");
        }
    }
}
