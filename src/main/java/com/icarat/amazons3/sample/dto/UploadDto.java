package com.icarat.amazons3.sample.dto;

public class UploadDto {
	
private String bucketName;
private String path;
private String key;
public String getBucketName() {
	return bucketName;
}
public void setBucketName(String bucketName) {
	this.bucketName = bucketName;
}
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}


}
