package converge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Converge different files of same column into one single big file
 * */

public class ConvergeIntoOne {
	public static void main(String[] args) throws IOException{
		
		
		
		String[] columns = {
				"TIMES", "CurveStraight",
				"CRLKASSTRTOQREQ","CRMDPSSTRCOLTQ","HEADINGANGLERIGHT",
				"LS16DRIVERREDUCEDTQ","LU16LDLANEWIDTH","PHYCURVATUREDERIVATIVERIGHT_M",
				"PHYSICALCURVATURERIGHT_M","POSITIONRIGHT",
				"SASANGLE","SASSPEED","VEHICLEPOSITION",
				"WHLSPDFL"
				
				};
		
		int[] columns_idx = new int[columns.length];
				
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		//String findPath = folderPath + "\\normalize\\normalized_new2";
		String findPath = folderPath + "\\curve\\new2";
		//String savePath = folderPath + "\\normalize";
		String savePath = folderPath + "\\curve";
		
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		//String fileName = "Time_SASAngle_WHLSpeed.csv";
		
		FileWriter writer;
		// File Writer
		
			
		
		writer = new FileWriter(savePath + "\\" + "overall" + ".csv"); // non absolute
		
		
		
					
		// Variable Declaration
				
		for(int j = 0; j < listOfFiles.length; j++){
			
			File f = new File(listOfFiles[j].toString());
			String fileName = listOfFiles[j].toString();
			String[] fileNameSplit = fileName.split("\\.");
			fileNameSplit = fileNameSplit[0].split("\\\\");
			fileName = fileNameSplit[fileNameSplit.length-1];
			
			if(f.isFile()){
				BufferedReader inputStream = null;
				String line;
				String[] parseLine = null;
				String[] column;
				
				try{
					inputStream = new BufferedReader(new FileReader(f));
										
					line = inputStream.readLine();
					column= line.split(",");
					
					if(j == 0){
						writer.append("FILENAME,");
						
						for(int i = 0; i < columns.length; i++){
							writer.append(columns[i] + ",");
						}
						
						
						writer.append("\n");
					}
					
					for(int i = 0; i < columns.length; i++){
						for(int k = 0; k < column.length; k++){
							if(columns[i].equals(column[k])){
								columns_idx[i] = k;
								//System.out.println(columns[i] + ", " + column[k] + ", " + columns_idx[i]);
								break;
							}
						}
					}
					
					/*
					for(int i = 0; i < columns.length; i++){
						System.out.println(columns[i] + ", " + columns_idx[i]);
					}
					*/
					
					while((line = inputStream.readLine()) != null){
						parseLine = line.split(",");
						writer.append(fileName + ",");
						
						for(int i = 0; i < columns_idx.length; i++){
							for(int k = 0; k < parseLine.length; k++){
								if(columns_idx[i] == k){
									if(columns_idx[i] == 0){
										writer.append(parseLine[k]+ ",");
									}else{
										//if(isAbsolute){
//											double temp = Math.abs(Double.parseDouble(parseLine[k]));	
											//writer.append(String.valueOf(temp) + ","); 			
										//}else{
											writer.append(parseLine[k]+ ","); 
										//}
										
									}
									
									break;
								}
							}
						}
						/*
						for(int i = 0; i < parseLine.length; i++){
							for(int k = 0; k < columns_idx.length; k++){
								if(i == columns_idx[k]){
									writer.append(parseLine[i] + ",");
									break;
								}
							}
						}
						 */
						
						writer.append("\n");
					}
				}finally{
					inputStream.close();
				} // end of try/finally
			}// end of f.isFile() if statement
			
			System.out.println("End of file: " + j);
		}// end of for loop
		
		writer.close();
		
		System.out.println("End of program" );
	}
}
