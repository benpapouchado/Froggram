package backend.S3;

import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Service 
public class S3Client {

    @Value("${aws.key.id}")String accessKey;
    @Value("${aws.access.secret}") String secretKey;
    @Value("${aws.region}") String region;
    @Value("${s3.bucket.name}") String bucketName;

    public AmazonS3 getAmazonS3Client() {

        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(
                accessKey,
                secretKey);

        return AmazonS3ClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTPS))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(region)
                .build();
    }

}
