package backend.S3;

import org.springframework.stereotype.Service;

import com.amazonaws.*;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Service 
public class S3Client {

    public AmazonS3 getAmazonS3Client() {

        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(
                System.getenv("AWS_KEY_ID"),
                System.getenv("AWS_ACCESS_SECRET"));

        return AmazonS3ClientBuilder.standard()
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTPS))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(System.getenv("AWS_REGION"))
                .build();
    }

}
