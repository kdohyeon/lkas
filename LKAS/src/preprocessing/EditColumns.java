package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/*
 * 1. Edits column names where it is not able to be uploaded in oracle database
 * 2. remove any column that is identical in names and contents
 *   2.1 if the names of columns are identical but the contents are different, then they are differed by numbers
 *     ex: column, column => column_1, column_2
 *   2.2 it is case sensitive
 *     ex: 'Column', 'column' are different columns
 * */

public class EditColumns {
	public static void main(String args[]) throws IOException{
		// File & load csv
		String folderPath = "E:\\LKAS_data_test3";
		String savePath = folderPath + "\\raw_columns_edited";
		String findPath = folderPath + "\\raw";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		
		
		Map<String, Integer> columns = new LinkedHashMap<String, Integer>(); 
		
				
		for(int j = 0; j < listOfFiles.length; j++){
			File f = new File(listOfFiles[j].toString());
			String fileName = listOfFiles[j].toString();
			String[] fileNameSplit = fileName.split("\\.");
			fileNameSplit = fileNameSplit[0].split("\\\\");
			fileName = fileNameSplit[fileNameSplit.length-1];
			
			
			// File Writer
			FileWriter writer = new FileWriter(savePath + "\\" + fileName + ".csv");
			
					
			if(f.isFile()){
				BufferedReader inputStream = null;
				String line;
				String[] parseLine;
				String[] column;

				
				try{
					inputStream = new BufferedReader(new FileReader(f));
										
					line = inputStream.readLine();
					//System.out.println(line);
					column= line.split(",");
					
					String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
					String match2 = "\\s{1,}";
					String str;
					
					for(int i = 0; i < column.length; i++){
						str = column[i].replaceAll(match, "");
						str = str.replaceAll(match2, "");
						str = str.toUpperCase();
						
						columns.put(str, i);
						column[i] = str;
					}
				
					Set<String> set = columns.keySet();
					Iterator<String> iter = set.iterator();
					
					
					while(iter.hasNext()){
						String key = (String)iter.next();
						//System.out.print(key+",");
						writer.append(key + ",");
					
					}
					writer.append("\n");
					
					

					while((line = inputStream.readLine()) != null){
						parseLine = line.split(",");
						
						//System.out.println(parseLine.length);
						

						for(int i = 0; i < parseLine.length; i++){
							
							if(columns.get(column[i]).equals(i)){
								writer.append(parseLine[i] + ",");	
							}
								
						}
						writer.append("\n");
						
						
						
					}


					
					writer.close();
					columns.clear();
					System.out.println("End of file: " + fileName);
				}finally{
					inputStream.close();
				}
			}
		}
		
		System.out.println("End of Program");
	}

}
