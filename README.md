# folderHealthMonitor
Foldedr Health Monitor

To run this program there is a config.properties file inside resource folder where the below details can be configured
 1. Source path
 2. Destination path(Secured folder path)
 3. Archive path
 4. Max allowed size of secured folder (In Mega Bytes)
 5. Copy task interval time in milliseconds.
 6. Folder monitor task interval time in milliseconds.
 7. File extensions to be deleted (Comma seperated values).

To run the program there is a Runner.java file inside scheduler package. Running This will print the 
folder status details in console based on the time interval
