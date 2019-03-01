package CS5850_DropBox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.amazonaws.services.s3.AmazonS3;

public class firstSyncTest {

	private AWSS3Service awss3service;
	private static final String BUCKET_1 = "bucket_1";
	private static final String KEY_1 = "Document/";
	private FolderWatch folderwatch;
	//private File folderInSync;
	
	@Rule
	public TemporaryFolder temporaryfolder = new TemporaryFolder();
	private File etcHost;
	
	@Before
	public void setUp() throws Exception {
		awss3service = mock(AWSS3Service.class, RETURNS_DEEP_STUBS);
		etcHost = temporaryfolder.newFolder();
		folderwatch = mock(FolderWatch.class, RETURNS_DEEP_STUBS);
		File f1 = new File("f1");
		File [] mockedFiles = { f1, new File("f2"), new File("f3")};
		when(folderwatch.listFiles(etcHost)).thenReturn(mockedFiles);
		awss3service.putObject(BUCKET_1, KEY_1, f1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testfirstSyncIsCorrect() {
		S3Application mockedApp = mock(S3Application.class, RETURNS_DEEP_STUBS);
		//mockedApp.firstSync(BUCKET_1, folderwatch, etcHost, awss3service);
		//awss3service.putObject(BUCKET_1, KEY_1, f1);
		assertEquals(mockedApp.firstSync(BUCKET_1, folderwatch, etcHost, awss3service), true);
		verify(folderwatch).listFiles(etcHost);
	}
	
}