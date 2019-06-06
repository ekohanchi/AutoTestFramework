package com.project.services.aws;

import java.io.File;
import java.util.List;

import org.testng.Reporter;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3 {
	private Regions clientRegion = Regions.US_WEST_2;
	private AmazonS3 s3client = null;
	private String bucketFolderPath = "InputFiles/";
	private static String BUCKETNAME = "sandbox";
	
	public S3() {
		Reporter.log("Connecting to S3 Instance...");
		s3client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build(); 
	}
	
	private boolean doesBucketExist(String bucketName) {
		boolean isBucketFound = false;
		List<Bucket> bucketList = s3client.listBuckets();
		for (Bucket bucket : bucketList) {
			if (bucket.getName().equals(bucketName)) {
				isBucketFound = true;
				return isBucketFound;
			}
		}
		return isBucketFound;
	}
	
	public void pushFileToS3Bucket(String fileName) {
		
		String bucketName = BUCKETNAME;
		
		Reporter.log("Checking to see if bucket with bucket name: [" + bucketName + "] exists", true);
		if (doesBucketExist(bucketName)) {
			String folderName = bucketFolderPath;
			s3client.putObject(new PutObjectRequest(bucketName, folderName + fileName, new File(fileName)));
			Reporter.log("File with filename: " + fileName + " pushed to", true);
			Reporter.log("Bucket: " + bucketName + "/" + folderName, true);
		} else {
			Reporter.log("ERROR: Can't find bucket name specified - " + bucketName, true);
		}
	}

}
