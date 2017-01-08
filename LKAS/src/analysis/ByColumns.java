package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ByColumns {
	public static void main(String args[]) throws IOException{
		
		int MAXROW = 2000000;
		int max = 0;
		String typeColumn = "STATUS";
		//String type = "highCurve";
		//String type = "lowCurve";
		String type = "straight";
		//String type = "total";
		
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String savePath = folderPath + "\\byColumns\\new2_avg\\" + type;
		String findPath = folderPath + "\\durationInfo\\new2\\avg";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();

		
		String[] columns = 				{"LS16SASCOMP", "SASSPEED", 
				"CRLKASSTRTOQREQ","CRMDPSSTRCOLTQ","LS16DRIVERREDUCEDTQ","LSS32LKADESTORQUE",
				"POSITIONRIGHT", "LU16LDLANEWIDTH",
				"PHYSICALCURVATURERIGHT","PHYCURVATUREDERIVATIVELEFT","PHYCURVATUREDERIVATIVERIGHT",
				"HEADINGANGLELEFT","HEADINGANGLERIGHT", "WHLSPDFL"};
		
		for(int i = 0; i < columns.length; i++){
			max = 0;
			
			FileWriter writer = new FileWriter(savePath + "\\" + columns[i] + "_" + type + ".csv");
			
			// append file names in first row
			for(int j = 0; j < listOfFiles.length; j++){
				String fileName = listOfFiles[j].toString();
				String[] fileNameSplit = fileName.split("\\.");
				fileNameSplit = fileNameSplit[0].split("\\\\");
				fileName = fileNameSplit[fileNameSplit.length-1];
				
				writer.append(fileName + ",");
			}
			writer.append("\n");
			
			// init
			 
			String[][] data = new String[listOfFiles.length][MAXROW];
			/*
			for(int k = 0; k < listOfFiles.length; k++){
				for(int j = 0; j < MAXROW; j++){
					data[k][j] = "";
				}
			}
			*/
			
			// save data in 'data' array
			for(int j = 0; j < listOfFiles.length; j++){
				File f2 = new File(listOfFiles[j].toString());
				String fileName = listOfFiles[j].toString();
				String[] fileNameSplit = fileName.split("\\.");
				fileNameSplit = fileNameSplit[0].split("\\\\");
				fileName = fileNameSplit[fileNameSplit.length-1];
				
				if(f2.isFile()){
					BufferedReader inputStream = null;
					String line;
					String[] parseLine;
					
					try{
						
						inputStream = new BufferedReader(new FileReader(f2));
						line = inputStream.readLine();
						parseLine = line.split(",");
						int idx = 0;
						int typeIdx = 0;
						for(int z = 0; z < parseLine.length; z++){
							if(parseLine[z].equals(columns[i])){
								idx = z;
							}
							
							if(parseLine[z].equals(typeColumn)){
								typeIdx = z;
							}
						}
						
						int row = 0;
						while((line = inputStream.readLine()) != null){
							parseLine = line.split(",");
							
							// comment this following if statement for 'total'
							if(parseLine[typeIdx].equals(type)){ // <--
								data[j][row] = parseLine[idx];
								
								row++;	
							}
							
							max = Math.max(max, row);
						}
						
					}finally{
						inputStream.close();
					}
				}
				
			}
			
			
			for(int j = 0; j < max; j++){
				for(int k = 0; k < listOfFiles.length; k++){
					if(data[k][j] == null){
						writer.append(",");
					}else{
						writer.append(data[k][j]+",");	
					}					
				}
				writer.append("\n");
			}
						
			writer.close();
			System.out.println("End of file: " + i);
		}
		System.out.println("End of Program");
	}
}
