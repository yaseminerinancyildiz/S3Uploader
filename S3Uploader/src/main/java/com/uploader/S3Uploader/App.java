package com.uploader.S3Uploader;

/**
 * 
 *
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.amazonaws.util.IOUtils;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


public class App {
    public static void main(String[] args) {
        String zipPath = "path zip file.zip";
        String bucket = "s3 bucket name";
        String objectKey = "path destination file.zip";
        
        try {
            // Execute bash command and generate zip file
            Process process = Runtime.getRuntime().exec("bash command for generate zip file.sh");
            process.waitFor();
            
            // Upload file to S3
           S3Client client = S3Client.builder()
                 .credentialsProvider(DefaultCredentialsProvider.create())
                  .build();
            
            
            try (InputStream input = new FileInputStream(zipPath)) {
                client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .build(),
                        request(input));
                
                System.out.println("File uploaded.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | InterruptedException | S3Exception e) {
            e.printStackTrace();
        }
    }
    
    private static RequestBody request(InputStream inputSt) throws IOException {
    	 byte[] content = IOUtils.toByteArray(inputSt);
    	    return software.amazon.awssdk.core.sync.RequestBody.fromBytes(content);
    }
}
