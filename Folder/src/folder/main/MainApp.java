package folder.main;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import folder.FolderScan;


public class MainApp {

	public static void main(String[] args) throws IOException, ParseException {
		

//		if (args.length == 0) {
//			System.out.println("Необходимо указать папку для которой будет выполнен анализ");
//			return;
//		}
//		FolderScan folderScan = new FolderScan(args[0]);
//		FolderScan folderScan = new FolderScan("/tmp/");
		FolderScan folderScan = new FolderScan("C:\\test");
		folderScan.JsonL();
		folderScan.startScan();
		folderScan.writeToFile();
		
		}

}
