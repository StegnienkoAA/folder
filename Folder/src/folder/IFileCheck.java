package folder;

import java.io.File;

import folder.FolderScan.FileTypes;

public interface IFileCheck {
	public FileTypes check(File file);
}
