package CS5850_DropBox;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
//import com.amazonaws.services.s3.model.CopyObjectResult;
//import com.amazonaws.services.s3.model.DeleteObjectsRequest;
//import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSS3Service {
    private final AmazonS3 s3client;
    
    public AWSS3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }
    
    //is bucket exist?
    public boolean doesBucketExist(String bucketName) { 
        return s3client.doesBucketExist(bucketName); 
    } 
    
    //create a bucket
    public Bucket createBucket(String bucketName) { 
        return s3client.createBucket(bucketName); 
    } 

    //list all buckets
    public List<Bucket> listBuckets() { 
        return s3client.listBuckets(); 
    }

    //delete a bucket
    public void deleteBucket(String bucketName , AmazonS3 s3) { 
    	try {
    		System.out.println(" - removing all objects from bucket");
            ObjectListing objectlisting = s3.listObjects(bucketName);
            while(objectlisting != null) {
            	for (Iterator<?> iterator = objectlisting.getObjectSummaries().iterator();
            			iterator.hasNext();) {
            		S3ObjectSummary summary = (S3ObjectSummary)iterator.next();
                    s3.deleteObject(bucketName, summary.getKey());
            	}
            	if (objectlisting.isTruncated()) {
                    objectlisting = s3.listNextBatchOfObjects(objectlisting);
                } else {
                    break;
                }
            }
            System.out.println(" OK, bucket ready to delete!");
        	s3.deleteBucket(bucketName);
    	} catch(AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    	System.out.println("Done!");
        //s3client.deleteBucket(bucketName); 
    }  
    
    //uploading object
    public PutObjectResult putObject(String bucketName, String key, File file) {
        return s3client.putObject(bucketName, key, file);
    }
    
    
    //listing objects
    public ObjectListing listObjects(String bucketName) {
        return s3client.listObjects(bucketName);
    }
    
    //get an object
    public S3Object getObject(String bucketName, String objectKey) {
        return s3client.getObject(bucketName, objectKey);
    } 
    
    //deleting an object
    public void deleteObject(String bucketName, String objectKey) {
        s3client.deleteObject(bucketName, objectKey);
    }
    
    //deleting multiple Objects
    /*public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
    	return s3client.deleteObjects(delObjReq);
    }*/
}
