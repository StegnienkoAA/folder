package folder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FolderScan {
	public final HashMap<String, FileTypes> resultHashMap;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	private File dump;
	private String sourceFolder;
	private static final Logger log = Logger.getLogger(FolderScan.class);
	private String sourceFileJson = "FolderScan.json";
	
	public String getSourceFileJson() {
		return sourceFileJson;
	}

	public void setSourceFileJson(String sourceFileJson) {
		this.sourceFileJson = sourceFileJson;
	}

	private List<IFileCheck> listType = new ArrayList<>();

	public FolderScan(String sourceFolder) throws ParseException, IOException {
		this.resultHashMap = new HashMap<>();
		this.sourceFolder = sourceFolder;
		// this.dump = new File("C:\\test\\" +
		// sdf.format(System.currentTimeMillis()) + ".dump");
		// this.dump = new File("/data/fileregdump/" +
		// sdf.format(System.currentTimeMillis()) + ".dump");
		this.dump = new File(JsonP("filedump") + sdf.format(System.currentTimeMillis()) + JsonP("filedumptype"));
	}

	public void startScan() throws IOException, ParseException {
		scanFolder(sourceFolder);
	}

	public String JsonP(String Name) throws org.json.simple.parser.ParseException, IOException {
		JSONParser jsonParser = new JSONParser();
		FileReader fileReader = new FileReader(
				Paths.get("").toAbsolutePath().toString() + "/resources/" + sourceFileJson);
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
		String value = (String) jsonObject.get(Name);
		System.out.println(Name + " " + value);
		return value;
	}

	public String JsonP1(String Name) throws org.json.simple.parser.ParseException, IOException {
		JSONParser jsonParser = new JSONParser();
		FileReader fileReader = new FileReader(
				Paths.get("").toAbsolutePath().toString() + "/resources/" + sourceFileJson);
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
		String value = (String) jsonObject.get(Name);
		System.out.println(Name + " " + value);
		return value;
	}
	
	
	public List<IFileCheck> JsonL() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		FileReader fileReader = new FileReader(
				Paths.get("").toAbsolutePath().toString() + "/resources/" + sourceFileJson);
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
		JSONArray names = (JSONArray) jsonObject.get("list");
		@SuppressWarnings("rawtypes")
		Iterator i = names.iterator();
		IFileCheck fch = null;
		while (i.hasNext()) {
			switch (i.next().toString()) {
			case "CheckType":
				fch = new CheckType();
				break;
			}
			listType.add(fch);
		}
		return listType;

	}

//	public String JsonP(String Name) throws IOException {
//		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("FolderScan.json");
//		int data = inputStream.read();
//		char content;
//		while (data != -1) {
//			content = (char) data;
//			System.out.print(content);
//			data = inputStream.read();
//		}
//		Properties prop = new Properties();
//		prop.load(inputStream);
//		String value = (String) prop.getProperty(Name);
//		// System.out.println(Name + " " + value);
//		return value;
//	}

	private void scanFolder(String filePath) throws IOException, ParseException {
		TreeMap<FileTypes, Integer> counterMap = new TreeMap<FileTypes, Integer>();
		File fil = new File(filePath);
		for (File selFile : fil.listFiles()) {
			if (selFile.isFile()) {
				FileTypes fileType = fileProccesing(selFile);
				if (fileType != FileTypes.Null) {
					int count = counterMap.containsKey(fileType) ? counterMap.get(fileType) : 0;
					counterMap.put(fileType, count + 1);
				}
			} else if (selFile.isDirectory()) {
				scanFolder(selFile.getAbsolutePath());
			}
		}
		for (Map.Entry<FileTypes, Integer> map : counterMap.entrySet()) {
			log.info(" Dir " + filePath + " parsed. " + map.getValue() + " files detected with type - " + map.getKey()
					+ ".");
		}

	}

	public void writeToFile() {
		try {
			dump.createNewFile();
			FileWriter out = new FileWriter(dump.getAbsoluteFile(), true);

			for (Map.Entry<String, FileTypes> map : resultHashMap.entrySet()) {
				System.out.println(map.getKey() + "  ===  " + map.getValue());
				// log.info(map.getKey() + " === " + map.getValue());
				out.write(map.getKey() + "  ===  " + map.getValue() + "\n");
			}
			out.close();
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		}

	}

	public enum FileTypes {
		Binary, Null
	}

	public FileTypes fileProccesing(File file) throws IOException, ParseException {
		FileTypes result = FileTypes.Null;
		for (IFileCheck elem : listType) {
			if (elem.check(file) != FileTypes.Null) {
				result = FileTypes.Binary;
				resultHashMap.put(file.getAbsolutePath(), elem.check(file));
				break;
			}

		}
		return result;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public File getDump() {
		return dump;
	}

	public void setDump(File dump) {
		this.dump = dump;
	}

	public String getSourceFolder() {
		return sourceFolder;
	}

	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public HashMap<String, FileTypes> getResultHashMap() {
		return resultHashMap;
	}
}
