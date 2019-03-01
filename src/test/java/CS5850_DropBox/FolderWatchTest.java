package CS5850_DropBox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.amazonaws.services.s3.AmazonS3;

public class FolderWatchTest {
	
	private AWSS3Service awss3service;
	private static final String BUCKET_1 = "user-no2"; 
	private static final String FolderPath = "/Users/wunggary/dir/";
	
	private AmazonS3 s3; 
    private FolderWatch folderwatch;
    
	@Rule
	public TemporaryFolder temporaryfolder = new TemporaryFolder();
	private File etcHost;
	private File etcFile;

	@Before
	public void setUp() throws Exception {
		etcHost = temporaryfolder.newFolder();
		etcFile = new File(etcHost.toString()+"/temp.txt");
		s3 = mock(AmazonS3.class, RETURNS_DEEP_STUBS);
		awss3service = mock(AWSS3Service.class, RETURNS_DEEP_STUBS);
		
		folderwatch = new FolderWatch();
		//RunThread t = new RunThread();
		//System.out.println(etcHost.toString());
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Test complete");
	}

	@Test
	public void watchEventTest() throws IOException, InterruptedException{
		// given
	    try(BufferedWriter bufferedWriter = Files.newBufferedWriter(etcFile.toPath())) {
	        bufferedWriter.write("127.0.0.1 xxxxx.local");
	    }

	    try(BufferedWriter bufferedWriter = Files.newBufferedWriter(etcFile.toPath())) {
	    	bufferedWriter.write("127.1.1.1 zzzzz.local");
	        //System.out.println("write");
	        bufferedWriter.newLine();
	        bufferedWriter.write("127.0.0.1 xxxxx.local");
	        folderwatch.watchEvent(etcHost.toString(), s3, BUCKET_1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		folderwatch.watchEvent(FolderPath, s3, BUCKET_1);
		
	}
	
	@Test
	public void listFilesTest() {
		boolean result = etcHost.isDirectory();
		File[] listOfFile = folderwatch.listFiles(etcHost);
		assertNotNull(listOfFile);
		System.out.println(result);
	}

}
