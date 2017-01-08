package converge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Split a single big file by specific column name
 * 
 * */

public class SplitByColumn {
	public static void main(String[] args) throws IOException{
		
//		boolean isAbsolute = true;
		
		// variable declaration
		String columnName = "FILENAME"; // split a single big file by this column name
		
		ArrayList<String> filename = new ArrayList<String>();
		Set<String> s = null;
		String[][] data = null;
		String[] columns = null;
				
		int filename_idx = 0;
		int filter_idx = 0;
		int collen = 0;
		int row = 0;
		
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String fileName = "";
		String savePath = "";
		
//		if(isAbsolute){
//			fileName = "overall_abs_nrml";
//			savePath = folderPath + "\\normalize\\normalized_new2_abs\\";
//		}else{
			fileName = "overall_nrml";
			savePath = folderPath + "\\normalize\\normalized_new2\\";
//		}
		
		String findFileName = folderPath + "\\normalize\\" + fileName + ".csv";
		
		
		
		File f = new File(findFileName);
		
		
		if(f.isFile()){
			BufferedReader inputStream = null;
			String line;
			String[] parseLine = null;
			String[] column;
			
			try{
				inputStream = new BufferedReader(new FileReader(f));
									
				line = inputStream.readLine();
				column= line.split(",");
				collen = column.length;
				data = new String[column.length][1000000];
				
				for(int i = 0; i < column.length; i++){
					if(column[i].equals(columnName)){
						filename_idx = i;
					}
				}
				
				columns = new String[column.length];
				for(int i = 0; i < column.length; i++){
					columns[i] = column[i];
				}
				
				row=0;
				while((line = inputStream.readLine()) != null){
					parseLine = line.split(",");
					filename.add(parseLine[filename_idx]);
					
					for(int i = 0; i < parseLine.length; i++){
						data[i][row] = parseLine[i];
					}
					row++;
				}
				
				s = new LinkedHashSet<>(filename);
				
			}finally{
				inputStream.close();
			}
		}
		
		Iterator<String> iterator = s.iterator();
		
		
		while(iterator.hasNext()){
			
			String setElement = iterator.next();
			FileWriter writer = new FileWriter(savePath + "\\" + setElement + "_nrml.csv");
			
			
			for(int i = 0; i < columns.length; i++){
				writer.append(columns[i] + ",");
			}
			writer.append("\n");
			
			for(int i = 0; i < row; i++){
				if(data[0][i].equals(setElement)){
					for(int j = 0; j < collen; j++){
						writer.append(data[j][i] + ",");
					}
					
					writer.append("\n");
				}
			}
			writer.close();
			System.out.println("End of file: " + setElement);
		}
		
		System.out.println("End of program");
		System.out.println("==============");
		
	}
}
