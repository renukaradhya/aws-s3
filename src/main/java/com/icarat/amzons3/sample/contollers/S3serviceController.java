package com.icarat.amzons3.sample.contollers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.icarat.amzons3.sample.dto.UploadDto;
import com.icarat.amzons3.sample.exception.Conflict;
import com.icarat.amzons3.sample.exception.DataNotFoundException;
import com.icarat.amzons3.sample.exception.HandleAmazonClientException;
import com.icarat.amzons3.sample.exception.HandleAmazonServiceException;
import com.icarat.amzons3.sample.service.S3service;

@RestController
@RequestMapping(value="/s3")
@Api(value = "s3 service", description = "API to support First level tonic Operation", position = 1)
public class S3serviceController {
	
	@Autowired
	 private S3service s3service;
	
	@ApiOperation(value = "create bucket", notes = "This api is used create bucket", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@RequestMapping(value = "bucketname/{bucketname}", method = RequestMethod.GET)
	public ResponseEntity<String> createBucket(@PathVariable("bucketname") String bucketName) throws IOException, HandleAmazonClientException, HandleAmazonServiceException, Conflict {
       return new ResponseEntity<String>(s3service.createNewBuket(bucketName), HttpStatus.OK);
	}
	

	@ApiOperation(value = "upload file", notes = "This api is used upload file in bucket", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@RequestMapping(value = "/upload", method = RequestMethod.POST,consumes="application/json")
	public String upload(@RequestBody UploadDto dto) throws DataNotFoundException {
		return s3service.uploadFileToSpecificBucket(dto.getBucketName(), dto.getKey(), dto.getPath());
	}

	@ApiOperation(value = "download bucket", notes = "This api is used download bucket", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@RequestMapping(value = "bucketname/{bucketname}/path/{path}/key/{key}/download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable("bucketname")String bucketName,@PathVariable("key")String key,@PathVariable("path")String path) throws IOException, DataNotFoundException {
		return new ResponseEntity<byte[]>(s3service.downLoadObjectformBucket(bucketName, key, path), HttpStatus.OK);
	}

	@ApiOperation(value = "bucket summary", notes = "This api is used list bucket summaries", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@RequestMapping(value = "bucketname/{bucketname}/objects", method = RequestMethod.GET)
	public List<S3ObjectSummary> list(@PathVariable("bucketname") String bucketName) throws IOException, HandleAmazonClientException, HandleAmazonServiceException {
		return s3service.getBucketObjectSummaries(bucketName);
	}
	
	@ApiOperation(value = "list buckets", notes = "This api is used list bucket details", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@RequestMapping(value = "buckets", method = RequestMethod.GET)
	public ResponseEntity<List<Bucket>> listBuckets() throws IOException, HandleAmazonClientException, HandleAmazonServiceException, DataNotFoundException {
         return new ResponseEntity<List<Bucket>>(s3service.listAllBucketNames(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get object names", notes = "This api is used get object names in bucket", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })	
	@RequestMapping(value = "bucket/{bucketname}/objects", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getBucketObjectNames(@PathVariable("bucketname") String bucketName) throws IOException, HandleAmazonClientException, HandleAmazonServiceException, DataNotFoundException {
         return new ResponseEntity<List<String>>(s3service.getBucketObjectNames(bucketName), HttpStatus.OK);
	}
	
	@ApiOperation(value = "delete Objects In Bucket", notes = "This api is used to delete object in bucket", httpMethod = "PUT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@PutMapping( path="bucketname/{bucketname}/key/{key}")
	public ResponseEntity<?> deleteObjectInBucket(@PathVariable("bucketname") String bucketName,@PathVariable("key")String key) throws HandleAmazonServiceException, HandleAmazonClientException, DataNotFoundException   {		
		return new ResponseEntity<String>(s3service.deleteObjectInBucket(bucketName, key),HttpStatus.CREATED);
	}
	
	
	@ApiOperation(value = "delete  Bucket", notes = "This api is used to delete  bucket", httpMethod = "PUT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@PutMapping( path="bucketname/{bucketname}")
	public ResponseEntity<?> deleteBuket(@PathVariable("bucketname") String bucketName) throws  DataNotFoundException   {		
		return new ResponseEntity<String>(s3service.deleteBuket(bucketName),HttpStatus.CREATED);
	}
	
	
	@ApiOperation(value = "get presigned url", notes = "This api is used get presignedurl of object", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server error"),
			@ApiResponse(code = 404, message = "NOT_FOUND") })
	@GetMapping( path="bucketname/{bucketname}/key/{key}/presignedurl")	
	public ResponseEntity<String> getPresignedUrl(@PathVariable("bucketname") String bucketName,@PathVariable("key") String key){
         return new ResponseEntity<String>(s3service.GeneratePresignedUrl(bucketName, key), HttpStatus.OK);
	}
}
