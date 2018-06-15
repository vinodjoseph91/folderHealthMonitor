package serviceImplementation;

import service.FileHandlerService;
import service.FilesCopyingTask;

public class FilesCopyingTasksImpl implements FilesCopyingTask {
	private FileHandlerService FileHandlerService = new FileHandlerServiceImpl();

	/*Method to copy files from source to destination*/ 
	@Override
	public void copyFileFromTempToSecure(String sourcePath, String destinationPath) {

		FileHandlerService.moveAllFiles(sourcePath, destinationPath);

	}

	@Override
	public boolean checkIfValidSource(String source) {

		return FileHandlerService.checkIfSourceExists(source);
	}

	@Override
	public boolean createFoldersIfNotExists(String path) {
		
		return FileHandlerService.createFolderIfNotExists(path);
	}

}
