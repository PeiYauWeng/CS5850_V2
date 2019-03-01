package CS5850_DropBox;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

public class AWSS3ServiceTest {

	private static final String BUCKET_1 = "bucket_1"; 
    private static final String KEY_1 = "key_1"; 
	
	private AmazonS3 s3; 
    private AWSS3Service service; 
    
	@Before
	public void setUp() throws Exception {
		s3 = mock(AmazonS3.class, RETURNS_DEEP_STUBS); 
        service = new AWSS3Service(s3);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Test comletes");
	}
	
	@Test
	public void whenInitializingAWSS3Service_thenNotNull() { 
        assertNotNull(new AWSS3Service(s3)); 
    } 
	
	@Test 
    public void whenVerifyingIfS3BucketExist_thenCorrect() { 
        service.doesBucketExist(BUCKET_1); 
        verify(s3).doesBucketExist(BUCKET_1); 
    } 
	
	@Test 
    public void whenVerifyingCreationOfS3Bucket_thenCorrect() { 
        service.createBucket(BUCKET_1); 
        verify(s3).createBucket(BUCKET_1); 
    }
	
	@Test 
    public void whenDeletingBucket_thenCorrect() {
		
        service.deleteBucket(BUCKET_1, s3); 
        verify(s3).listObjects(BUCKET_1); 
    }
	
	@Test
	public void whenListBucket_thenCorrect() {
		service.listBuckets(); 
        verify(s3).listBuckets(); 
	}
	
	@Test 
    public void whenVerifyingPutObject_thenCorrect() { 
        File file = mock(File.class); 
        PutObjectResult result = mock(PutObjectResult.class); 
        when(s3.putObject(anyString(), anyString(), (File) any())).thenReturn(result); 
 
        assertEquals(service.putObject(BUCKET_1, KEY_1, file),result); 
        verify(s3).putObject(BUCKET_1, KEY_1, file); 
    }
	
	@Test 
    public void whenVerifyingListObjects_thenCorrect() { 
        service.listObjects(BUCKET_1); 
        verify(s3).listObjects(BUCKET_1); 
    }
	
	@Test
	public void whenVerifyingDeleteObjects_thenCorrect() {
		service.deleteObject(BUCKET_1, KEY_1);
		verify(s3).deleteObject(BUCKET_1, KEY_1);
	}
	
	@Test
	public void whenVerifyingGetOject_thenCorrect() {
		service.getObject(BUCKET_1, KEY_1);
		verify(s3).getObject(BUCKET_1, KEY_1);
	}

}
