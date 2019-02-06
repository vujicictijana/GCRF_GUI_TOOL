/** This file is part of GCRF GUI TOOL.

    GCRF GUI TOOL is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GCRF GUI TOOL is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GCRF GUI TOOL.  If not, see <https://www.gnu.org/licenses/>.*/

package gcrf_tool.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;

public class Writer {

	public static void writeGraph(double[][] matrix, String fileName) {
		String[] edges = new String[edges(matrix)];
		int edge = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] != 0) {
					edges[edge] = (i + 1) + "," + (j + 1) + "," + matrix[i][j];
					edge++;
				}
			}
		}
		write(edges, fileName);
	}

	public static void writeDoubleArray(double[] r, String fileName) {
		String[] rText = new String[r.length];
		for (int i = 0; i < r.length; i++) {
			rText[i] = r[i] + "";
		}
		write(rText, fileName);
	}

	public static int edges(double[][] matrix) {
		int edges = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] != 0) {
					edges++;
				}
			}
		}
		return edges;
	}

	public static void write(String[] text, String fileName) {
		File file = new File(fileName);
		try {
			PrintStream print = new PrintStream(file);

			for (int i = 0; i < text.length; i++) {
				print.println(text[i]);
			}
			print.flush();
			print.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void createFolder(String name) {
		File dir = new File(name);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public static String folderName(String name) {
		String[] words = name.split(" ");
		String result = "";
		for (int i = 0; i < words.length; i++) {
			result += (words[i].charAt(0) + "").toUpperCase()
					+ words[i].substring(1).toLowerCase();
		}
		return result;
	}

	public static boolean checkFolder(String name) {
		File dir = new File(name);
		return dir.exists();
	}

	public static void copyFile(File sourceFile, String newFileName) {
		File destFile = new File(newFileName);
		try {
			FileChannel source = null;
			FileChannel destination = null;
			FileInputStream input = null;
			FileOutputStream output = null;
			
			try {
				input = new FileInputStream(sourceFile);
				source = input.getChannel();
				output =  new FileOutputStream(destFile);
				destination =output.getChannel();
				destination.transferFrom(source, 0, source.size());
			} finally {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
				if (source != null) {
					source.close();
				}
				if (destination != null) {
					destination.close();
				}
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void renameDir(String dirPath,String newName){
		File dir = new File(dirPath);		
		File newDir = new File(dir.getParent() + "/" + newName);
		dir.renameTo(newDir);
	}

}
