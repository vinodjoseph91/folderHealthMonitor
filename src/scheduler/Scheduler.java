package scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import service.FilesCopyingTask;
import service.FolderMonitoringTask;
import serviceImplementation.FilesCopyingTasksImpl;
import serviceImplementation.FolderMonitoringTasksImpl;

public class Scheduler {

	/*
	 * Invoke file copy service and file monitoring service from here
	 */
	private String sourcePath;
	private String destinationPath;
	private String archivePath;
	private long maxSize;
	private List<String> fileTypesToRemove;
	private long monitoringInterval;
	private long copyingInterval;

	public void setFileTypesToRemove(List<String> fileTypesToRemove) {
		this.fileTypesToRemove = fileTypesToRemove;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public void setMonitoringInterval(long monitoringInterval) {
		this.monitoringInterval = monitoringInterval;
	}

	public void setCopyingInterval(long copyingInterval) {
		this.copyingInterval = copyingInterval;
	}

	private FilesCopyingTask filesCopyingTask = new FilesCopyingTasksImpl();
	private FolderMonitoringTask folderMonitoringTask = new FolderMonitoringTasksImpl();

	private void copyFiles() {
		filesCopyingTask.copyFileFromTempToSecure(sourcePath, destinationPath);
	}

	/* Method to create destination and archieve folder on program start */
	private boolean createDestinationAndArchiveIfNotExists() {

		boolean isExists = filesCopyingTask.createFoldersIfNotExists(destinationPath);
		if (isExists) {
			return filesCopyingTask.createFoldersIfNotExists(archivePath);
		} else {
			return false;
		}

	}

	// Method to check if the source path is a valid folder
	public boolean sourceExists() {

		return filesCopyingTask.checkIfValidSource(sourcePath);

	}

	/* Method to invoke File copying and folder monitoring services */
	public void scheduleTasks() {

		if (createDestinationAndArchiveIfNotExists()) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
			new Timer().scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					Date startDate = new Date(System.currentTimeMillis());
					System.out.println("Copying over files from source to secured folder at Time :"
							+ timeFormat.format(startDate));
					copyFiles();
					System.out.println("Finished copying files to secured folder at Time :"
							+ timeFormat.format(new Date(System.currentTimeMillis())));
					System.out.println();
					new Timer().scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							System.out.println("Monitoring Secured Folder ");
							folderMonitoringTask.monitorSecuredFolder(destinationPath, archivePath, fileTypesToRemove,
									maxSize);
						}
					}, 0, monitoringInterval);
				}
			}, 0, copyingInterval);

		} else {
			System.out.println("Creating destination and archieve folders failed");
		}

	}

}
