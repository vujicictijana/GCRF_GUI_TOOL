package gcrf_tool.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gcrf_tool.exceptions.ConfigurationParameterseException;
import gcrf_tool.gui.frames.MainFrame;

public class Reader {

	public static File jarFile() {
		URL location = MainFrame.class.getProtectionDomain().getCodeSource()
				.getLocation();
		File jarPath = null;
		try {
			jarPath = new File(location.toURI()).getParentFile();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jarPath;
	}

	public static double[][] readGraph(String path, int noOfNodes) {
		double[][] matrix = new double[noOfNodes][noOfNodes];
		String[] text = Reader.read(path);
		String[] line;
		int i = 0;
		int j = 0;
		double weigth = 0;
		for (int k = 0; k < text.length; k++) {
			line = text[k].split(",");
			i = Integer.parseInt(line[0]);
			j = Integer.parseInt(line[1]);
			if (i <= 0 || i > noOfNodes || j <= 0 || j > noOfNodes) {
				return null;
			}
			weigth = Double.parseDouble(line[2]);
			matrix[i - 1][j - 1] = weigth;
		}
		return matrix;
	}

	public static double[] readArray(String path, int noOfNodes) {
		double[] r = new double[noOfNodes];

		String[] text = Reader.read(path);
		if (text.length != noOfNodes) {
			return null;
		}
		for (int k = 0; k < text.length; k++) {
			r[k] = Double.parseDouble(text[k]);
		}
		return r;
	}

	public static double[][] readMatrixTwoFiles(String path1, String path2,
			int noOfNodes, int t, int train) {
		double[][] r = new double[noOfNodes][t];

		String[] text = Reader.read(path1);

		if (text.length != noOfNodes * train) {
			return null;
		}
		String[] text2 = Reader.read(path2);

		if (text2.length != noOfNodes * (t - train)) {
			return null;
		}

		int indexText = 0;
		int indexText2 = 0;
		for (int i = 0; i < noOfNodes; i++) {
			for (int j = 0; j < t; j++) {
				if (j <= train - 1) {
					r[i][j] = Double.parseDouble(text[indexText]);
					indexText++;
				} else {
					r[i][j] = Double.parseDouble(text2[indexText2]);
					indexText2++;
				}
			}
		}
		// GraphGenerator.showMatrix(r);
		return r;
	}

	public static double[][] readMatrix(String path, int noOfRows, int noOfCols) {
		double[][] matrix = new double[noOfRows][noOfCols];

		String[] text = Reader.read(path);

		if (text.length != noOfRows) {
			return null;
		}

		String[] line = null;
		for (int i = 0; i < noOfRows; i++) {
			line = text[i].split(",");
			for (int j = 0; j < noOfCols; j++) {
				matrix[i][j] = Double.parseDouble(line[j]);
			}
		}
		// GraphGenerator.showMatrix(matrix);
		return matrix;
	}

	public static String[] read(String fileName) {
		File f = new File(fileName);
		try {
			BufferedReader b = new BufferedReader(new FileReader(f));
			ArrayList<String> text = new ArrayList<>();
			String s = b.readLine();
			while (s != null) {
				text.add(s);
				s = b.readLine();
			}
			b.close();
			return text.toArray(new String[text.size()]);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public static String[] getAllFiles(String folder) {
		try {
			ArrayList<String> files = new ArrayList<>();
			Files.walk(Paths.get(folder)).forEach(
					filePath -> {
						if (Files.isRegularFile(filePath)) {
							if (filePath.getFileName().toString()
									.contains("DirGCRF")) {
								files.add(filePath.getParent().getParent().getFileName()+ " - "
										+ filePath.getParent().getFileName());
							}
						}
					});
			return files.toArray(new String[files.size()]);
		} catch (IOException e) {
			return null;
		}
	}

	// public static String[] getAllFilesInFolder(String folder) {
	// try {
	// ArrayList<String> files = new ArrayList<>();
	// Files.walk(Paths.get(folder)).forEach(filePath -> {
	// if (Files.isRegularFile(filePath)) {
	// files.add(filePath.getFileName().toString());
	//
	// }
	// });
	// return files.toArray(new String[files.size()]);
	// } catch (IOException e) {
	// return null;
	// }
	// }

	public static String[] getAllFolders(String folder) {
		File file = new File(folder);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
				// if (new File(current, name).isDirectory());
				// String[] files = getAllFilesInFolder(new File(current,
				// name).getPath());
				// for (int i = 0; i < files.length; i++) {
				// System.out.println(files[i]);
				// if(files[i].contains("Train")){
				// return true;
				// }
				//
				// }
				// }
			}
		});
		return directories;
	}

	public static void deleteFiles(String path) {
		File index = new File(path);
		String[] entries = index.list();
		for (String s : entries) {
			File currentFile = new File(index.getPath(), s);
			currentFile.delete();
		}
	}

	public static boolean checkFile(String path) {
		File f = new File(path);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}

	public static Map<String, String> readCfg()
			throws ConfigurationParameterseException {
		Map<String, String> params = new HashMap<>();
		String[] text = read(Reader.jarFile() + "/cfg.txt");
		if (text != null) {
			if (text.length != 19) {
				throw new ConfigurationParameterseException(
						"Configuration file reading failed. File has wrong format.");
			}
			for (int i = 0; i < text.length; i++) {
				if (!text[i].contains("=")) {
					throw new ConfigurationParameterseException(
							"Configuration file reading failed. File has wrong format.");
				}
				String[] line = text[i].split("=");
				if (line.length == 2) {
					params.put(line[0], line[1]);
				} else {
					params.put(line[0], "");
				}
			}
			return params;
		}else{
			return null;
		}
	}

	public static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		file.delete();

	}

}
