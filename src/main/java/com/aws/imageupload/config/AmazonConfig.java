/**
 * 
 */
package com.aws.imageupload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author evaristosrodrigues
 *
 */

@Configuration
public class AmazonConfig {
	
	@Bean
	public AmazonS3 s3() {
		String accessKey="";
		String secretKey="";

		AWSCredentials awsCredentials = new BasicAWSCredentials(
				accessKey, secretKey);
				
		return AmazonS3ClientBuilder
				.standard().
				withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				//us-west-1
				.withRegion(Regions.US_WEST_1)
				.build();
	}

}
