
package backend.S3;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

@Service
public class S3Service {

    private final S3Client s3client;
    @Value("${s3.bucket.name}") String bucketName;

    public S3Service(S3Client s3client) {
        this.s3client = s3client;
    }

    public String generateUrl(String username, HttpMethod httpMethod) {

        Date expiration = new Date(
            System.currentTimeMillis() + 5 * 60 * 1000
        );

        AmazonS3 amazonS3 = s3client.getAmazonS3Client();

        try {
            GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucketName, username)
                    .withMethod(httpMethod)
                    .withExpiration(expiration);

            return amazonS3.generatePresignedUrl(request).toString();

        } catch (Exception e) {
            throw new RuntimeException(
                "Unable to generate S3 presigned URL", e
            );
        }
    }
}
