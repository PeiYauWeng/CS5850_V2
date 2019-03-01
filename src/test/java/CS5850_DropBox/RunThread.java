package CS5850_DropBox;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RunThread extends Thread {
	
	private static String DIR = "/Users/wunggary/dir/"; 
	
	@Override
    public void run() {
        System.out.println("MyThread - START "+Thread.currentThread().getName());
        try {
            //Thread.sleep(20);
            java.io.File file1 = new java.io.File(DIR + "testFile1.txt");
            file1.createNewFile();
            Thread.sleep(20);
            java.io.File file2 = new java.io.File(DIR +"testFile2.txt");
            file2.delete();
            Thread.sleep(20);
            java.io.File file4 = new java.io.File(DIR +"testFile4.txt");
            file4.createNewFile();
            Thread.sleep(20);
            java.io.File file5 = new java.io.File(DIR +"testFile5.txt");
            file5.createNewFile();
            Thread.sleep(20);
            File file3 = new File(DIR + "testFile3.txt");
            file3.setLastModified(new Date().getTime());
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
        System.out.println("MyThread - END "+Thread.currentThread().getName());
    }

}
