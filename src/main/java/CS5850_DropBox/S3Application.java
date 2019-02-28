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
          "AKIAJWJMSPWMX7ZDW27A", 
          "C8wPGk6aZAwBH4RDt7XNhmwSOGsj3k5wrb3iSfbn"
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
    
    //get a boolean
    public static boolean istrue(String answer) {
    	if (answer.equals("y"))
    		return true;
    	else
    		return false;
    }
    
    //USER INTERFACE
    public static String SimpleUI() {
    	//list all the buckets
    	System.out.println("Bucket List:");
    	List<Bucket> buckets = awsService.listBuckets();
     	for(Bucket b: buckets) {
     		System.out.println(b.getName());
     	}
    	//check if want to create new bucket
    	String bucket_name = null;
    	
     	System.out.println("Do you wan to create new bucket?(y/n)");
     	Scanner scannerA = new Scanner(System.in);
     	boolean answer = istrue(scannerA.nextLine());
     	
     	//scan bucket name users input
     	Scanner scanner = new Scanner(System.in);
     	if(answer) {
	     	System.out.println("Creating a bucket:");
			bucket_name = scanner.nextLine();
	     	while(awsService.doesBucketExist(bucket_name)) {
	     		System.out.println("This bucket has existed");
	     		try {
	    			System.out.println("Creating a bucket:");
	    			bucket_name = scanner.nextLine();
	    		}catch (Exception e){
	    			scanner.next();
	    		}
	     	}
	     	//creating a bucket
	    	try {
	    		awsService.createBucket(bucket_name);
	    	} catch (AmazonS3Exception e) {
	    		System.err.println(e.getErrorMessage());
	    	}
     	}
     	else {
     		System.out.println("Assign a bucket:");
     		bucket_name = scanner.nextLine();
	     	while(!awsService.doesBucketExist(bucket_name)) {
	     		System.out.println("Input a wrong bucket name");
	     		try {
	    			System.out.println("Input a correct name again:");
	    			bucket_name = scanner.nextLine();
	    		}catch (Exception e){
	    			scanner.next();
	    		}
	     	}
     	}
     	scannerA.close();
     	scanner.close();
     	return bucket_name;
    }
    
    // first sync
    public static void firstSync(String bucket_name, FolderWatch folderwatch, File folderInSync, AWSS3Service awsservice) {
    	HashMap<String, Date> hashmap = new HashMap<String, Date>();
        File[] listfile = folderwatch.listFiles(folderInSync);
        
        if(awsservice.listObjects(bucket_name).getObjectSummaries()==null)
        	System.out.println("you have problem");
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
            	if(hashmap.get(file.getName()).equals(filetime))
        			System.out.println(file.getName()+" no change");
        		else {
        			awsservice.putObject(bucket_name, "Document/"+file.getName(), new File(file.getPath()));
        			System.out.println(file.getPath());
        			System.out.println("change");
        		}
            }
            else {
            	awsservice.putObject(bucket_name, "Document/"+file.getName(), new File(file.getPath()));
            	System.out.println(file.getPath());
            	System.out.println("upload " + file.getName());
            }
        }
    }
    
    public void run(String bucketName, String folderPath ) throws IOException{
    	
    	File folder = new File(folderPath);
    	FolderWatch watcher = new FolderWatch();
    	
    	awsService.createBucket(bucketName);
     	// Sync all files in local first
     	firstSync(bucketName, watcher , folder, awsService);
        
        //watch folder and detect the change
		try {
			watcher.watchEvent(folderPath, s3client, bucketName);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //deleting bucket
     	awsService.deleteBucket(bucketName, s3client);
    }
}
