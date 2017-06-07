package com.icarat.amazons3.sample.exception;

/**
 *
 * @author Icarat1
 *
 */
public class DataNotFoundException extends Exception {

  public static final String BUCKETS_NOT_FOUND = "Buckets not found";

  public static final String INVALID_BUCKET_NAME = "Invalid Bucket name";

  public static final String INVALID_FILE_PATH = "Invalid file path";

  public static final String OBJECT_DOES_NOT_EXIST = "Object does not exist";

  private static final long serialVersionUID = 1L;

  public DataNotFoundException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

}
