package com.osunkwo.taskmaster;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class S3Client {

    private AmazonS3 s3client;

    @Value("${amazon.s3.endpoint}")
    private String endpointUrl;

    @Value("${amazon.s3Resized.endpoint}")
    private String resizedEndpointUrl;

    @Value("${amazon.aws.s3.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.s3.secretkey}")
    private String secretKey;

    @Value("${amazon.aws.bucket}")
    private String bucket;


    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    public ArrayList<String> uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        String resizedFileUrl = "";
        ArrayList<String> urlList = new ArrayList<>();

        try {
            if (multipartFile.getSize() > 350000) {
                sendMessageToQueue();
            }
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            urlList.add(endpointUrl + "/" + fileName);
            urlList.add(resizedEndpointUrl + "/" + "resized-" + fileName);
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlList;
    }


    private void sendMessageToQueue(){
        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

        String imageResizerQueue = System.getenv("IMAGE_RESIZER_QUEUE");

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(imageResizerQueue)
                .withMessageBody("call lambda")
                .withDelaySeconds(5);
        sqs.sendMessage(sendMessageRequest);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        return "Successfully deleted";
    }

}
