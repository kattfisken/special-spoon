# powershell script to produce a redovisnings-mapp

#Set-ExecutionPolicy -Scope Process -ExecutionPolicy Undefined

# setup - clean all old files 

cd C:\Users\Sara\Desktop\Skola\ipwebprog\special-spoon\redovisning
ls -Directory | remove-item



## collect files for uppgift 1
mkdir 1
Copy-Item ..\Uppgift1\app\build.gradle 1
