package service;

public interface FilesCopyingTask {
	boolean checkIfValidSource(String source);
	boolean createFoldersIfNotExists(String path);
	void copyFileFromTempToSecure(String source, String destination);
}
