# CS5850_V2
Implemet  a prototype of DropBox and test it

## Set a Credentials

After you create a AWS S3 account, you will get an access key and secret key. The input in the S3Appication Class

### Prerequisites

type  your  ccess key and secret key in the variables, which call ccess key and secret key.

```
In class S3Application

static {
//put your access key and secret key here
    credentials = new BasicAWSCredentials(
    "access key", 
    "secret key"
);
}
```

### Usage

Open Ibox_cpp with any Java IDE: Intellij, Eclips, NetBeans etc. Run IboxApplication 

Do created deleted or updated files in folder demoDrive

It will sync up all the changes from demoDrive folder to Amazon S3 bucket: ibox-bucket


End with an example of getting some data out of the system or using it for a little demo

## Running code


```
mvn clean package
```

### All Testing

!https://raw.githubusercontent.com/PeiYauWeng/CS5850_V2/master/pic/螢幕快照%202019-02-28%20下午11.33.48.png

copy this link

* [copy this link](linkhttps://circleci.com/gh/PeiYauWeng/CS5850_V2/6)

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
