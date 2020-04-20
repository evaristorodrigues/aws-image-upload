package com.aws.imageupload.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

@Service
public class FileStore {

	private final AmazonS3 s3;

	@Autowired
	public FileStore(AmazonS3 s3) {
		super();
		this.s3 = s3;
	}
	
    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {
    	ObjectMetadata objectMetadata = new ObjectMetadata();
    	
    	optionalMetaData.ifPresent(map ->{
    		if(!map.isEmpty()) {
    			map.forEach(objectMetadata::addUserMetadata);
    		}
    	});
    	try {
    		s3.putObject(path,fileName, inputStream, objectMetadata);
    	}catch(AmazonServiceException e) {
    		throw new IllegalStateException("Failed to Store file to S3");
    	}
    }

	public byte[] donwload(String path, String key) {
		S3Object s3Object=  s3.getObject(path, key);
		try {
			return IOUtils.toByteArray(s3Object.getObjectContent());
    	}catch(AmazonServiceException | IOException e) {
    		throw new IllegalStateException("Failed to download file from S3");
    	}
	}
}
