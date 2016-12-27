# powershell script to produce a redovisnings-mapp


# setup - clean all old files 

cd C:\Users\Sara\Desktop\Skola\ipwebprog\special-spoon\redovisning
ls -Directory | remove-item -Recurse



## collect files for uppgift 1
mkdir 1
Copy-Item ..\Uppgift1\app\build.gradle 1
Copy-Item ..\Uppgift1\app\src\main\AndroidManifest.xml 1
Copy-Item ..\Uppgift1\app\src\main\java\com\example\sara\uppg1\NameGenerator.java 1
Copy-Item ..\Uppgift1\app\src\main\res\layout\activity_name_generator.xml 1
copy-item ..\Uppgift1\app\src\main\res\drawable-hdpi\ic_add_white_48dp.png 1
Copy-Item ..\Uppgift1\app\src\main\res\values\strings.xml 1
Copy-Item ..\Uppgift1\app\app-release.apk 1
Copy-Item '..\Uppgift1\Uppgift 1.3.txt' 1


## collect files for uppgift 2
mkdir 2
Copy-Item ..\Uppgift2\app\build.gradle 2
Copy-Item ..\Uppgift2\app\src\main\AndroidManifest.xml 2
Copy-Item ..\Uppgift2\app\src\main\java\com\example\sara\uppgift2\WelcomeScreen.java 2
Copy-Item ..\Uppgift2\app\src\main\java\com\example\sara\uppgift2\DisplayNumber.java 2
Copy-Item ..\Uppgift2\app\src\main\res\menu\main.xml 2
Copy-Item ..\Uppgift2\app\src\main\res\values\strings.xml 2
Copy-Item ..\Uppgift2\app\src\main\res\values\styles.xml 2
Copy-Item ..\Uppgift2\app\src\main\res\layout\* 2\.