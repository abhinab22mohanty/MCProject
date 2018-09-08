# BrainNet- An Authentication system using Brain Signals for Android Devices

User is identified via Identification process first and later authenticated using 4 different machie learning models (SVM, KNN, Naive Bayes, Random Forest).

## Getting Started

Installation Required:
1. Java (jdk-1.8, jre8 and java ee)
2. Tomcat Servers (version 8.5 <to act as Fog Server> and version 9.0 <to act as Cloud Server with port forwarding> )
3. Android SDK
4. MATLAB Runtime (MCR version 96) 

### Prerequisites

What things you need to install the software and how to install them

Set Environment variables for Java
```
JAVA_HOME="C:\Program Files\Java\jdk1.8.0_65"
```

Set Environment variables for Android SDK
```
ANDROID_SDK_HOME="C:\Android"
```
It will mostly be set sutomatically if you install Android Studio and follow all required instructions.

Set Path for MATLAB Runtime and MATLAB Compiler Runtime
```
PATH="C:\Program Files\MATLAB\MATLAB Compiler Runtime\v83\runtime\win64;C:\Program Files\MATLAB\MATLAB Runtime\v92\runtime\win64"
```
### Installing

1. Clone all files into a local repository
2. Run the code in Project Folder in Android Studio or run the apk on any android device.
3. Run the code in MCCloudServer and CSEMCServer on eclipse jee on corresponding Tomcat Servers. (Read README.txt inside code Folder for further clarity)
4. Forward the port of any one server to act as a cloud server (You can use tools like ngrok.exe)
5. Change String REGISTRY_DIRECTORY to point to the path of the Registered Users Folder onn your system.(Add edf brain signal files for registered users in the folder)



## Authors

* **Abhinab Mohanty** - *Initial work* 



## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details


