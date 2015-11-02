package folder;

import java.io.File;

import folder.FolderScan.FileTypes;

public class CheckType implements IFileCheck {

	@Override
	public FileTypes check(File file) {
		FileTypes result = FileTypes.Null;
		//if (file.getName().contains(".P00")){
		if (file.getName().contains("1")){	
			return FileTypes.Binary;
		 }
		return result;
	}
		
}
