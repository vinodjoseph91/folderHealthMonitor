package serviceImplementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import service.FileHandlerService;

public class FileHandlerServiceImpl implements FileHandlerService {

	@Override
	public boolean checkIfSourceExists(String sourcePath) {
		Path source = Paths.get(sourcePath);
		return Files.exists(source);
	}

	/*Method to move files from source to destination */
	@Override
	public boolean moveAllFiles(String sourcePath, String destinationPath) {
		/* if source is a directory iterate each file and move it to destination */
		try {
			File source = new File(sourcePath);
			File destination = new File(destinationPath);
			if (source.isDirectory()) {

				for (File file : source.listFiles()) {
					if (file.isDirectory()) {
						moveAllFiles(source + File.separator + file.getName(), destinationPath);
					} else {
						move(new File(source + File.separator + file.getName()),
								new File(destination + File.separator + file.getName()));
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// Move files by replacing already existing files in the destination path
	private void move(File source, File destination) throws IOException {
		try {
			Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException();
		}
	}

	// Method to create destination and archive folder if it does not exist
	@Override
	public boolean createFolderIfNotExists(String path) {
		try {

			Path directory = Paths.get(path);
			if(!Files.exists(directory)) {
				Files.createDirectory(directory);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Method to get size of secured folder , also deletes restricted files if any
	 */
	@Override
	public long getSecuredFolderSize(String path, List<String> fileExtentionsToRemove) {
		long size = 0;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		List<String> deletedFiles = new ArrayList<>();
		if (listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					int index = file.getName().lastIndexOf(".");
					if (index > 0) {
						String extension = file.getName().substring(index);
						if (fileExtentionsToRemove.contains(extension)) {
							file.delete();
							deletedFiles.add(file.getName());
							continue;
						}
					}
					size += file.length();
				}
			}
		}
		if (deletedFiles.size() > 0) {
			System.out.println();
			System.out.println("List of deleted files ");
			for (String fileName : deletedFiles) {
				System.out.println(fileName);
			}
			System.out.println();
		}
		return size;
	}

	/*
	 * Method to move larger files to archive folder once the secured folder size
	 * exceeds the limit
	 */

	@Override
	public void archiveOlderFiles(String sourcePath, String archivePath, long maxSize) {
		long folderSize = 0;

		File folder = new File(sourcePath);

		File[] files = folder.listFiles();

		List<File> filesToArchive = new ArrayList<File>();
		sortByLastModified(files);
		if (files.length > 0) {
			for (File file : files) {
				folderSize += file.length();
				if (folderSize > maxSize) {

					filesToArchive.add(file);

				}
			}
			moveFilesToArchivedFolder(filesToArchive, sourcePath, archivePath);
		}
	}

	private void moveFilesToArchivedFolder(List<File> filesToArchive, String sourcePath, String archievePath) {

		List<String> archivedFiles = new ArrayList<>();
		try {
			for (File file : filesToArchive) {
				File fileItem = new File(file.toString());
				File source = new File(sourcePath + File.separator + fileItem.getName());
				File destination = new File(archievePath + File.separator + fileItem.getName());
				Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.ATOMIC_MOVE);
				archivedFiles.add(fileItem.getName());
			}
			System.out.println("Total number of archived files : " + archivedFiles.size());
			System.out.println("Archived File Details.");
			for (String fileName : archivedFiles) {
				System.out.println(fileName);
			}
			System.out.println();
			long securedFolderSize = getSecuredFolderSize(sourcePath, new ArrayList<String>());
			double sizeMb = (double) securedFolderSize / (1024 * 1024);
			System.out.println(
					"Secured Folder size post archive : " + Math.round(sizeMb * 100.0) / 100.0 + "  Mega bytes");
			System.out.println("=============================================================");
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Sorting array of files based on last modified date
	private void sortByLastModified(File[] files) {
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File file1, File file2) {

				return Long.valueOf(file2.lastModified()).compareTo(file1.lastModified());
			}
		});
	}

}
