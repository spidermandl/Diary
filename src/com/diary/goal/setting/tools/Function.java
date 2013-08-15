package com.diary.goal.setting.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

import com.diary.goal.setting.R;

public class Function {

	public static String fillStr(final String rough,final String pattern,final String[] args){
		String[] splits = rough.split(pattern, -1);
		StringBuffer buffer=new StringBuffer();
		int length=splits.length;
		for(int i=0;i<length;i++){
			buffer.append(splits[i]);
			if(i<length-1)
				buffer.append(args[i]);
		}
		
		return buffer.toString();
		
	}
	
	public static String ReadFile(File file) { 
        BufferedReader reader = null;  
        StringBuffer laststr = new StringBuffer();  
        try {  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            while ((tempString = reader.readLine()) != null) {  
                laststr.append(tempString);  
            }  
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {
                }  
            }  
        }  
        return laststr.toString();  
    }

	
	public static String ReadInputStream(InputStream inputStream) {
        BufferedReader reader = null;  
        StringBuffer laststr = new StringBuffer();  
        try {  
            reader = new BufferedReader(new InputStreamReader(inputStream));  
            String tempString = null;  
            while ((tempString = reader.readLine()) != null) {  
                laststr.append(tempString);  
            }  
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
        return laststr.toString();  
    }  
	
	public static void writeFile(String filePath, String sets)  
            throws IOException {  
        FileWriter fw = new FileWriter(filePath);  
        PrintWriter out = new PrintWriter(fw);  
        out.write(sets);  
        out.println();  
        fw.close();  
        out.close();  
    } 
	
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	public static String getAbbrMonth(Context context,int month){
		switch (month) {
		case 1:
			return context.getResources().getString(R.string.Jan);
		case 2:
			return context.getResources().getString(R.string.Feb);
		case 3:
			return context.getResources().getString(R.string.Mar);
		case 4:
			return context.getResources().getString(R.string.Apr);
		case 5:
			return context.getResources().getString(R.string.May);
		case 6:
			return context.getResources().getString(R.string.Jun);
		case 7:
			return context.getResources().getString(R.string.Jul);
		case 8:
			return context.getResources().getString(R.string.Aug);
		case 9:
			return context.getResources().getString(R.string.Sep);
		case 10:
			return context.getResources().getString(R.string.Oct);
		case 11:
			return context.getResources().getString(R.string.Nov);
		case 12:
			return context.getResources().getString(R.string.Dec);
	
		default:
			return context.getResources().getString(R.string.Jan);
		}
	}
}
