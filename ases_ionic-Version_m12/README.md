# spinops-ionic

Welcome to Spinops Front-end repository

Spinops is an app that controls a health insurance from Rio de Janeiro.

**Main Tools Version**

  ```
  Node: "16.20.2"
  TypeScript: "3.4.5"
  Ionic: "5.4.16"
  Angular: "8.3.29"
  Cordova: "10.0.0"
  Java: "1.8.0_412"
  ```  
  

To run the project, follow these steps: 

**1 - Clone the repo in your PC (You can use either SSH or HTTP)**

```
git clone https://github.com/desenvspintec/spinops-ionic.git
```

**2 - Access the project folder**

```
cd spinops-ionic
```

**3 - Run npm install**

```
npm install
```

  **3.1 - If an error shows up, run "rm -rf node_modules package-lock.json" and try the step 3 again**

  ```
  rm -rf node_modules package-lock.json
  ```

**4 - Running the APP**

  **4.1 - browser, run** 
  ```
  ionic serve -l
  ```

  **4.2 - android, run** 
  ```
  ionic cordova run android -ls
  ```
  
  **4.3 - ios (Only in MAC device), run**
  
  ```
  ionic cordova run ios -ls
  ```
  
  
**5 - Building the app (Android)**

  Obs - To perform this process will be necessary to install Android Studio and the following tools from SDK Manager
    
  *SDK Platform tab* 
    -Android API 31
    -Android 11.0
    -Android 10.0
    -Android 9.0
    -Android 8.1

  *SDK Tools tab* 
    -Android SDK Build-Tools 32-rc1
    -Android SDK Command-line Tools
    -Android Emulator
    -Android SDK Platform-Tools

  *It's important to remember that the above plugins could be unupdated in the moment you're reading this. Therefore, if you identify changes, please update this doc* 


   5.1 - This process requires that all the system environment variables are correctly defined. Follow the necessary variables:

  - ANDROID_SDK_ROOT - C:\Users\User\AppData\Local\Android\Sdk
  - GRADLE_HOME - C:\Program Files\Android\Android Studio\Gradle
  - JAVA_HOME - C:\Program Files\Java\jdk1.8.0_412

  And in the path, located on user variables, define the follow:
  - %JAVA_HOME%\bin

  This will allow you to access all the Java tools, such as jarsigner.

   5.2 - Follows the steps to generate the APK:   

   5.2.1 - The command that generates the APK

   ```
   ionic cordova build --release android
   ```

  > The apk will be generated on the follow path: spinops<nobr>-ionic\platforms\android\app\build\outputs\apk\release" 

   5.2.2 - Command to align the apk. Its good to remember that this command will generate a new apk named (in this example) outfile.apk

   ``` 
   zipalign -f -v 4 app-release-unsigned.apk outfile.apk
   ```

   5.2.3 - Command to sign the apk

   ```
   apksigner sign --ks-key-alias alias_name --ks my-release-key.keystore outfile.apk
   ```

   It will ask for the keystore password, which is: **spin.123**

  5.3 - In case of doubts about the build, check out the [official ionic page](https://ionicframework.com/docs/v1/guide/publishing.html)
  

  **6 - Building the app (iOS)**

  Before running the simulator, ensure that under "Signing & Capabilities," the team "Plano ASES LTDA" is selected.

  6.1 - Build the iOS project

  ```
  ionic cordova build --release ios
  ```

  This command will only execute the build. Now, within Xcode, follow these steps:

  - Click on the project in the left sidebar.
  - Go to Product > Clean Build Folder.
  - Go to Product > Build.

  If the build is successful, go to Product > Archive.
  
  Follow the on-screen instructions to complete the archiving process.