package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class FileUtil {
	
	/** 将s写入文件  */
	public static void writeFile(String filePath, String s) {
        try {  
            File file = new File(filePath);  
            PrintStream ps = new PrintStream(new FileOutputStream(file));  
            ps.print(s);
            ps.close();
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
	}
	
	/** 读取文件内容  */
	public static String readFile(String filePath){
		try {
			String str;
	        File file = new File(filePath);
	        FileReader reader;
			reader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(reader);
	        StringBuilder sb = new StringBuilder();
	        String s = "";
	        while ((s =bReader.readLine()) != null) {
	            sb.append(s + "\n");
	        }
	        bReader.close();
	        reader.close();
	        str = sb.toString();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			throw new NullPointerException();
		} 
	}
	
	/** 读取文件内容,将文件内容以行形式返回  */
	public static ArrayList<String> readLine(String filePath) {
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
