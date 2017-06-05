package com.icarat.amazons3.sample.service.impl;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.icarat.amazons3.sample.exception.Conflict;
import com.icarat.amazons3.sample.exception.DataNotFoundException;
import com.icarat.amazons3.sample.exception.HandleAmazonClientException;
import com.icarat.amazons3.sample.exception.HandleAmazonServiceException;
import com.icarat.amazons3.sample.service.S3service;

@Service
public class S3serviceImpl implements S3service {

	
	@Autowired
	private AmazonS3Client amazonS3Client;
	
	/*
	 * This method is get all Buckets details in our s3
	 */
	
	public List<Bucket> listAllBucketNames() throws DataNotFoundException {
		if (amazonS3Client.listBuckets().size() != 0) {
			return amazonS3Client.listBuckets();
		} else {
			throw new DataNotFoundException(
					DataNotFoundException.BUCKETS_NOT_FOUND);
		}

	}

	/**
	 * This method is used to add object into to a specific bucket with key
	 */
	public String uploadFileToSpecificBucket(String bucketName, String key,
			String path) throws DataNotFoundException {
		if (amazonS3Client.doesBucketExist(bucketName)) {
			File file = new File(path);
			if (file.exists()) {
				amazonS3Client.putObject(new PutObjectRequest(bucketName, key,file));
				return "Successfully file uploaded";
			} else {
				throw new DataNotFoundException(DataNotFoundException.INVALID_BUCKET_NAME);
			}
		} else {
			throw new DataNotFoundException(
					DataNotFoundException.INVALID_BUCKET_NAME);
		}
	}

	/**
	 * This method id used to create a new bucket in s3
	 */
	public String createNewBuket(String bucketName)
			throws HandleAmazonServiceException, HandleAmazonClientException,Conflict {
		try {
			if (!(amazonS3Client.doesBucketExist(bucketName))) {				
				amazonS3Client.createBucket(new CreateBucketRequest(bucketName));
				String bucketLocation = amazonS3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
				System.out.println("bucket location = " + bucketLocation);
				return "Successfully bucket created." + bucketLocation;
			} else {
				throw new Conflict("Duplicate bucket name");
			}
		} catch (AmazonServiceException ase) {
			throw new HandleAmazonServiceException(ase.getMessage(),
					ase.getStatusCode());
		} catch (AmazonClientException ace) {
			throw new HandleAmazonClientException(ace.getMessage());
		}
	}
	
	/**
	 * This method is used to get all objects summary in a specific bucket 
	 */
	public List<S3ObjectSummary> getBucketObjectSummaries(String bucketName)
			throws HandleAmazonClientException, HandleAmazonServiceException {
		List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<S3ObjectSummary>();
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
					.withBucketName(bucketName);
			ObjectListing objectListing;
			do {
				objectListing = amazonS3Client.listObjects(listObjectsRequest);
				for (S3ObjectSummary objectSummary : objectListing
						.getObjectSummaries()) {
					s3ObjectSummaries.add(objectSummary);
				}
				listObjectsRequest.setMarker(objectListing.getNextMarker());
			} while (objectListing.isTruncated());
		} catch (AmazonServiceException ase) {
			throw new HandleAmazonServiceException(ase.getMessage(),
					ase.getStatusCode());
		} catch (AmazonClientException ace) {
			throw new HandleAmazonClientException(ace.getMessage());
		}
		return s3ObjectSummaries;
	}

	/**
	 * This method is used to list out the all bucket names in a s3
	 */
	public List<String> getBucketObjectNames(String bucketName)
			throws HandleAmazonClientException, HandleAmazonServiceException {
		List<String> s3ObjectNames = new ArrayList<String>();
		List<S3ObjectSummary> s3ObjectSummaries = getBucketObjectSummaries(bucketName);
		for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {
			s3ObjectNames.add(s3ObjectSummary.getKey());
		}
		return s3ObjectNames;
	}
	
    /*
     * This method is used to delete  specific object in a bucket
     */
	public String deleteObjectInBucket(String bucketName, String key)
			throws HandleAmazonServiceException, HandleAmazonClientException,DataNotFoundException {
		try {
			S3Object object = amazonS3Client.getObject(bucketName, key);
			if (object != null) {
				amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName,key));
				return "Successfuly deleted";
			} else {
				throw new DataNotFoundException(DataNotFoundException.OJECT_DOES_NOT_EXIST);
			}			
		} catch (AmazonServiceException ase) {
			throw new HandleAmazonServiceException(ase.getMessage(),ase.getStatusCode());
		} catch (AmazonClientException ace) {
			throw new HandleAmazonClientException(ace.getMessage());
		}
	}

	/**
	 * This method is used to download the specific object from bucket.
	 * And store into specified path and returns byte[] of the object
	 */
	public byte[] downLoadObjectformBucket(String bucketName, String key,String path)
			throws IOException, DataNotFoundException {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName,key);
		S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
		if (s3Object != null) {
			S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
			byte[] byteRespose=IOUtils.toByteArray(objectInputStream);
			FileUtils.writeByteArrayToFile(new File("F://new.txt"), byteRespose);
			return byteRespose;
		} else {
			throw new DataNotFoundException(DataNotFoundException.OJECT_DOES_NOT_EXIST);
		}
	}
	
    /*
     *  This method is used to delete empty bucket in s3
     */	
	public String deleteBuket(String bucketName) throws DataNotFoundException {
		if (amazonS3Client.doesBucketExist(bucketName)) {
			amazonS3Client.deleteBucket(bucketName);
			return "Bucket deleted successfully";
		}else {
			throw new DataNotFoundException(DataNotFoundException.INVALID_BUCKET_NAME);
		}
	}

	/*
	 * This method is used to generate pre signed url for particular object in bucket 
	 */
	public String GeneratePresignedUrl(String bucketName, String key) {
		  GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
          generatePresignedUrlRequest.setMethod(HttpMethod.GET);
          generatePresignedUrlRequest.setExpiration(DateTime.now().plusYears(1).toDate());
          URL signedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest); 
          return  signedUrl.toString();
	}

}
