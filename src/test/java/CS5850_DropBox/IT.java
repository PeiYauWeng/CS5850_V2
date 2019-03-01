/**
 * 
 */
package CS5850_DropBox;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author wunggary
 *
 */
//@Category(IntegrationTest.class)
public class IT {
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
    
	
    static AWSS3Service awsService;

	static FolderWatch watcher;
	static S3Application s3app;
	final static String folderPath = "/Users/wunggary/dir";
    final static File folder = new File(folderPath);
    
    static String bucketname = "user-no1";
    
	@Before
	public void setUp() throws Exception {
		System.out.println("Start Integration Test");
		awsService = new AWSS3Service(s3client);
		watcher = new FolderWatch();
		s3app = new S3Application();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("End Integration Test");
	}

	@Test
	public void test() throws IOException {
		System.out.println("Start Integration Test");
		File folder = new File(folderPath);
    	FolderWatch watcher = new FolderWatch();
    	
    	awsService.createBucket(bucketname);
     	// Sync all files in local first
    	System.out.println("complete create Bucket");
    	s3app.firstSync(bucketname, watcher , folder, awsService);
        
        //watch folder and detect the change
		try {
			watcher.watchEvent(folderPath, s3client, bucketname);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //deleting bucket
     	awsService.deleteBucket(bucketname, s3client);
		System.out.println("Start Integration Test");
	}

}
