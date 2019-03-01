package CS5850_DropBox;

import java.io.File;
import java.io.IOException;
//import java.nio.file.WatchKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
//import com.amazonaws.services.s3.model.DeleteObjectsRequest;
//import com.amazonaws.services.s3.model.ObjectListing;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Application {
	/////////////// start initialize////////////////
    private final static AWSCredentials credentials; 

    static {
        //put your access key and secret key here
        credentials = new BasicAWSCredentials(
          "access key", 
          "secret key"
        );
    }
    
    //set up the client
    final static AmazonS3 s3client = AmazonS3ClientBuilder
    		.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();
    
    static AWSS3Service awsService = new AWSS3Service(s3client);
    
    //static FolderWatch watcher = new FolderWatch();
    
    final static String folderPath = "/Users/wunggary/dir";
    ///////////// end initialize////////////
    
    // first sync
    public static boolean firstSync(String bucket_name, FolderWatch folderwatch, File folderInSync, AWSS3Service awsservice) {
    	HashMap<String, Date> hashmap = new HashMap<String, Date>();
        File[] listfile = folderwatch.listFiles(folderInSync);
        
	    for(S3ObjectSummary os : awsservice.listObjects(bucket_name).getObjectSummaries()) {
	        String[] temp = os.getKey().split("/");
	        String filenameInCloud = temp[temp.length-1];
	        hashmap.put(filenameInCloud, os.getLastModified());
	    }
        
      //compare local and cloud file
        for (File file : listfile) {
        	Date filetime = new Date(file.lastModified());
            System.out.println(file.getName() + filetime);
            if(hashmap.containsKey(file.getName())) {
            	//if(hashmap.get(file.getName()).equals(filetime))
        			//System.out.println(file.getName()+" no change");
        		//else {
        			awsservice.putObject(bucket_name, "Document/"+file.getName(), new File(file.getPath()));
        			System.out.println(file.getPath());
        			System.out.println("change");
        		//}
            }
            else {
            	awsservice.putObject(bucket_name, "Document/"+file.getName(), new File(file.getPath()));
            	System.out.println(file.getPath());
            	System.out.println("upload " + file.getName());
            }
        }
        return true;
    }
}
