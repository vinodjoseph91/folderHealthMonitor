package service;

import java.io.File;
import java.util.List;

public interface FileHandlerService {
	boolean checkIfSourceExists(String path);

	boolean createFolderIfNotExists(String path);

	boolean moveAllFiles(String source, String destination);

	// boolean copyFiles(String source, String dest,String fileExt);
	void archiveOlderFiles(String sourcePath, String archivePath, long maxSize);

	long getSecuredFolderSize(String path, List<String> fileExtentionsToRemove);
}
