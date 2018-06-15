package service;

import java.util.List;

public interface FolderMonitoringTask {
	void monitorSecuredFolder(String securedFolderPath, String archivePath, List<String> fileExtentionsToRemove,long maxSize);
}
