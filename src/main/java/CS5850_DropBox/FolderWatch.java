package CS5850_DropBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.s3.AmazonS3;


public class FolderWatch {
	
	public void watchEvent (String folder , AmazonS3 s3, String bucket_name) throws IOException, InterruptedException {
		WatchService watchservice = FileSystems.getDefault().newWatchService();
		AWSS3Service awsService = new AWSS3Service(s3);
		Path folderPath = Paths.get(folder);
		folderPath.register(
				watchservice, 
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
		
		long startTime = System.currentTimeMillis();
		long endTime;
		System.out.println("test");
		WatchKey watchkey = watchservice.poll(10, TimeUnit.SECONDS);
		
		while(watchkey != null) {
			Thread.sleep(300);
			//System.out.println("test1");
			List<WatchEvent<?>> watchEvents = watchkey.pollEvents();
            for (WatchEvent<?> event : watchEvents) {
                if(StandardWatchEventKinds.ENTRY_CREATE == event.kind()){
                    System.out.println("CREATE:[" + folderPath + "/" + event.context() + "]");
                    awsService.putObject(bucket_name,"Document/"+event.context().toString(), 
                    		new File(folderPath + "/" +event.context().toString()));
                }
                if(StandardWatchEventKinds.ENTRY_DELETE == event.kind()){
                    System.out.println("DELETE:[" + folderPath + "/" + event.context() + "]");
                    awsService.deleteObject(bucket_name,"Document/"+event.context().toString());
                }
                if(StandardWatchEventKinds.ENTRY_MODIFY == event.kind()){
                    System.out.println("MODIFY:[" + folderPath + "/" + event.context() + "]");
                    awsService.putObject(bucket_name,"Document/"+event.context().toString(), 
                    		new File(folderPath + "/" +event.context().toString()));
                }
                
            }
            endTime = System.currentTimeMillis();
            watchkey.reset();
            if((endTime-startTime) > 100000 )
            	break;
		}
		//watchservice.close();
	
	}

	public File[] listFiles(File folder) {
		if (folder.isDirectory())
			return folder.listFiles();
		else
			return null;
	}
}
