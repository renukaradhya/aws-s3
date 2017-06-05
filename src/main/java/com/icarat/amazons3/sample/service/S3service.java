package com.icarat.amazons3.sample.service;

import java.io.IOException;
import java.util.List;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.icarat.amazons3.sample.exception.Conflict;
import com.icarat.amazons3.sample.exception.DataNotFoundException;
import com.icarat.amazons3.sample.exception.HandleAmazonClientException;
import com.icarat.amazons3.sample.exception.HandleAmazonServiceException;

public interface S3service {

	String createNewBuket(String bucketName) throws HandleAmazonServiceException, HandleAmazonClientException, Conflict;
	
	List<Bucket> listAllBucketNames() throws DataNotFoundException;
	
	String uploadFileToSpecificBucket(String bucketName,String key,String path) throws DataNotFoundException;	
	
	List<S3ObjectSummary> getBucketObjectSummaries(String bucketName) throws HandleAmazonClientException, HandleAmazonServiceException;
	
	List<String> getBucketObjectNames(String bucketName) throws HandleAmazonClientException, HandleAmazonServiceException;
	
	String deleteObjectInBucket(String bucketName,String key) throws HandleAmazonServiceException, HandleAmazonClientException, DataNotFoundException;
  
	byte[] downLoadObjectformBucket(String bucketName,String key,String path) throws IOException, DataNotFoundException;
	
	String deleteBuket(String bucketName) throws DataNotFoundException;
	
	String  GeneratePresignedUrl(String bucketName,String key);
}
