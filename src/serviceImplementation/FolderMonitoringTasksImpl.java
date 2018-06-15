package serviceImplementation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import service.FileHandlerService;
import service.FolderMonitoringTask;

public class FolderMonitoringTasksImpl implements FolderMonitoringTask {
	private FileHandlerService FileHandlerService = new FileHandlerServiceImpl();

	/*
	 * Method which monitors secured folder on a given interval by removing
	 * restricted files and archiving bigger files
	 */
	@Override
	public void monitorSecuredFolder(String securedFolderPath, String archivePath, List<String> fileExtentionsToRemove,
			long maxSize) {

		long securedFolderSize = FileHandlerService.getSecuredFolderSize(securedFolderPath, fileExtentionsToRemove);
		double sizeInMb = (double) securedFolderSize / (1024 * 1024);
		sizeInMb = Math.round(sizeInMb * 100.0) / 100.0;
		SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
		System.out.println("Current Secured Folder Size : " + sizeInMb + " Mega bytes at : "
				+ timeFormat.format(new Date(System.currentTimeMillis())));
		if (securedFolderSize > maxSize) {

			System.out.println("Secured folder size exceeded moving files to archive folder.");
			FileHandlerService.archiveOlderFiles(securedFolderPath, archivePath, maxSize);

		} else {
			System.out.println("=============================================================");
		}
	}

}
