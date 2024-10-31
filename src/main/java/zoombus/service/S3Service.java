package zoombus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;

    public S3Service(
            @Value("${aws.accessKeyId}") String accessKeyId,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.region}") String region) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region))
                .build();
    }

    public String uploadFile(MultipartFile profilePic, String folder) throws IOException {
        InputStream inputStream = profilePic.getInputStream();

        String uniqueFileName = UUID.randomUUID() + "_" + profilePic.getOriginalFilename();
        String fileKey = folder + "/" + uniqueFileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(profilePic.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));

        return uniqueFileName;
    }

    // Method to construct and return file URL
    public String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    // Method to delete a file from S3
    public boolean deleteFile(String fileName,String folder) {
        String fileKey = folder + "/" + fileName;

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            s3Client.headObject(headObjectRequest); // Check if the file exists

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true; // Indicate success
        } catch (NoSuchKeyException e) {
            System.err.println("File not found: " + e.getMessage());
            return false; // Indicate failure
        } catch (SdkException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false; // Indicate failure
        }
    }

    public String updateFile(MultipartFile newProfilePic, String existingFileName, String folder) throws IOException {
        // Delete the existing file
        boolean isDeleted = deleteFile(existingFileName,folder);
        if (isDeleted) {
            // Upload the new file to the specified folder
            return uploadFile(newProfilePic, folder);
        } else {
            throw new IOException("Failed to delete the existing file before updating.");
        }
    }
}
