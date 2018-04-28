package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {
	
	public static String readFromFile(String fileName){
		try {
			String str;
	        File file = new File(fileName);
	        FileReader reader;
			reader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(reader);
	        StringBuilder sb = new StringBuilder();
	        String s = "";
	        while ((s =bReader.readLine()) != null) {
	            sb.append(s + "\n");
	        }
	        bReader.close();
	        str = sb.toString();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			throw new NullPointerException();
		} 
	}
	
	public static ArrayList<String> readLine(String fileName) {
		try {
			File file = new File(Constant.ruleFile);
			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);
			ArrayList<String> ans = new ArrayList<String>();
			String s;
			while ((s = bReader.readLine()) != null) {
				ans.add(s);
			}
			bReader.close();
			return ans;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
}
