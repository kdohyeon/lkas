package converge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ConvergeTImeData {
	
	/*
	 * This java file is for LKAS project.
	 * For sparsed data, it allows integrating data as one single row.
	 * */
	public static void main(String[] args) throws IOException{
		// variable declaration
		HashMap<String, Float> timeGroup = new HashMap<String, Float>(); 	// for each group, data is put into this hashmap
		String[] column;	// for names of columns

		// File & load csv
		String folderPath = "E:\\LKAS_data_test3";
		String savePath = folderPath + "\\integrated";
		//String findPath = folderPath + "\\raw_merged";
		String findPath = folderPath + "\\raw_columns_edited";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		
				
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
				
				try{
					inputStream = new BufferedReader(new FileReader(f));
										
					line = inputStream.readLine();
					column = line.split(",");
					
					// column names in hash map
					for(int i = 0; i < column.length; i++){
						timeGroup.put(column[i], null);
					}
					
					

					// writer: column
					for(int i = 0; i < column.length; i++){
						writer.append(column[i] + ",");
					}
					writer.append("\n");
					
					// find the index of 'AliveCounter'
					int counterIdx = 0;
					int timeIdx = 0;
					for(int i = 0; i < column.length; i++){
						if(column[i].equals("ALIVECOUNTER")){
							counterIdx = i;
						}
						
						if(column[i].equals("TIMES")){
							timeIdx = i;
						}
					}
					
					
					int row = 1;
					float timeSum = 0;
					while((line = inputStream.readLine()) != null){
						
						parseLine = line.split(",");
					
						//System.out.println(parseLine[0].toString() + ", " + parseLine[1].toString());
						// if AliveCounter contains some other value than current AliveCounter (ignore null), then group the current hashmap
						if( (row != 1) // if not first row AND
								&& (!parseLine[counterIdx].equals("")) // if input is not NULL
								&& (!((timeGroup.get(column[counterIdx]).toString()).equals(""))) ){ // if current AliveCounter is not NULL 
							if((Float.parseFloat(parseLine[counterIdx])) != (timeGroup.get(column[counterIdx])) ){ // if different

						
								/*
								System.out.println("=============");
								System.out.println("Sum: " + timeSum);
								System.out.println("Row: " + (row-1));
								System.out.println("Avg: " + timeSum/(row-1));
								*/
								
								// time average
								timeSum = timeSum/(row-1);
								
								for(int i = 0; i < column.length; i++){
									if(timeGroup.get(column[i]) == null){
										writer.append(",");
									}else{ 
										if(column[i].equals(column[timeIdx])){
											writer.append(timeSum + ",");
										}else{
											writer.append(timeGroup.get(column[i]).toString() + ",");	
										}
										
									}
								}
								writer.append("\n");


								
								row = 1;
								timeSum = 0;
								
								// clear
								for(int i = 0; i < column.length; i++){
									timeGroup.put(column[i], null);
								}								
								
							}
							
						}
					
						
						for(int i = 0; i < parseLine.length; i++){
							timeGroup.put(column[i], Float.parseFloat(parseLine[i]));	
						}
						
						timeSum = timeSum + Float.parseFloat(parseLine[timeIdx]);
						//System.out.println(timeSum + " " + row);
						//System.out.println(timeGroup.get("ALIVECOUNTER"));
						
						row++;
						
					}
					
					writer.close();
					System.out.println("End of File: " + fileName);
					
					
				}finally{
					inputStream.close();
				}
			}
			
		}
		
		
		System.out.println("End of program - Successful");
	}
}
