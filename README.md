# CS5850_V2
Implemet  a prototype of DropBox and test it

## Set a Credentials

After you create a AWS S3 account, you will get an access key and a secret key and input in the S3Appication Class

## Prerequisites

type  your  ccess key and secret key in this position, which call ccess key and secret key.

```
///////////In class S3Application///////////

static {
//put your access key and secret key here
    credentials = new BasicAWSCredentials(
    "put your access key", 
    "put your secret key"
);
}
```

## Usage

Open CS5850_prototype of dropbox with any Java IDE: Intellij, Eclips, NetBeans etc.

put this code in S3Application class
```
public void run(String bucketName, String folderPath ) throws IOException{
    	
    	File folder = new File(folderPath);
    	FolderWatch watcher = new FolderWatch();
        String bucketName = "put your bucket name here"
    	
    	awsService.createBucket(bucketName);
     	// Sync all files in local first
    	System.out.println("complete create Bucket");
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
```
and than run S3Application 

Do created deleted or updated files in folder demoDrive

It will sync up all the changes from demoDrive folder to Amazon S3 bucket: ibox-bucket


End with an example of getting some data out of the system or using it for a little demo

## Running code

```
mvn clean verify
```

## All Testing
#### The result of Integration Test
![image](https://raw.githubusercontent.com/PeiYauWeng/CS5850_V2/master/pic/螢幕快照%202019-02-28%20下午11.33.48.png)
#### The result of CIRCLECI
![image](https://github.com/PeiYauWeng/CS5850_V2/blob/master/pic/螢幕快照%202019-03-01%20上午12.24.14.png)

copy this link

linkhttps://circleci.com/gh/PeiYauWeng/CS5850_V2/6

You can see the test result in this website

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

Pei Yau Weng(pweng.cpp.edu)

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
