package scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Runner {

	/*Read config from properties file*/
	public Properties getProperties() {
		Properties prop = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("./resources/config.properties");
		try {
			prop.load(input);
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		Runner runner = new Runner();
		Properties prop = runner.getProperties();
		if (prop != null) {
			Scheduler scheduler = new Scheduler();
			scheduler.setSourcePath(prop.getProperty("SECURE.FOLDER.SOURCE.PATH"));
			scheduler.setDestinationPath(prop.getProperty("SECURE.FOLDER.DESTINATION.PATH"));
			scheduler.setArchivePath(prop.getProperty("SECURE.FOLDER.ARCHIVE.PATH"));
			scheduler.setMonitoringInterval(Long.parseLong(prop.getProperty("SECURE.FOLDER.COPY_INTERVAL")));
			scheduler.setCopyingInterval(Long.parseLong(prop.getProperty("SECURE.FOLDER.MONITOR_INTERVAL")));
			long maxSize = Long.parseLong(prop.getProperty("SECURE.FOLDER.MAX_SIZE"));
			maxSize = maxSize * 1024 * 1024;
			scheduler.setMaxSize(maxSize);
			
			List<String> fileExtentionsToRemove = Arrays.asList(prop.getProperty("SECURE.FOLDER.AUTO_DELETE_FILE_EXTNS").split("\\s*,\\s*"));
			scheduler.setFileTypesToRemove(fileExtentionsToRemove);
			
			if(scheduler.sourceExists()) {

				scheduler.scheduleTasks();
			}
			else {
				
				System.out.println("Source Doesn't exist . Please provide a valid source.");
				
			}
		} else {
			System.out.println("Could not load the properties file.");
		}
	}

}
