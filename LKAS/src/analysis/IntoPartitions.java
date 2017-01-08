package analysis;
/*
 * Whole data into partitions
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.BasicStats;

public class IntoPartitions {
	public static void main(String[] args) throws IOException{
		
		boolean isAbsolute = false;
		boolean isAverage = true; // true => average, false => stdev
		
		String[] section = {"highCurve", "lowCurve", "straight"};
		String[] columns = {
				
				"CRLKASSTRTOQREQ","CRMDPSSTRCOLTQ","HEADINGANGLERIGHT",
				"LS16DRIVERREDUCEDTQ","LU16LDLANEWIDTH","PHYCURVATUREDERIVATIVERIGHT_M",
				"PHYSICALCURVATURERIGHT_M","POSITIONRIGHT",
				"SASANGLE","SASSPEED","VEHICLEPOSITION",
				"WHLSPDFL"
				
		};
		String[] tempColNames = {"a","b","c","d","e","f","g","h","i","j","k", "l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
				"aa","ab","ac","ad","ae","af","ah","ai","aj", "ak", "al", "am", "an", "ao", "ap", "aq"};
		
		
		// read reference file
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String savePath = "";
		String findPath = "";
		FileWriter writer_stdev; 
		findPath = folderPath + "\\normalize\\normalized_new2\\";
		if(isAverage){
			if(isAbsolute){
				savePath = folderPath + "\\partitions\\new2_abs_avg\\";
				writer_stdev = new FileWriter(savePath + "\\" + "abs_avg.csv");
			}else{
				savePath = folderPath + "\\partitions\\new2_avg\\";
				writer_stdev = new FileWriter(savePath + "\\" + "avg.csv");
			}
			
		}else{
			if(isAbsolute){
				savePath = folderPath + "\\partitions\\new2_abs_std\\";
				writer_stdev = new FileWriter(savePath + "\\" + "abs_std.csv");
			}else{
				savePath = folderPath + "\\partitions\\new2_std\\";
				writer_stdev = new FileWriter(savePath + "\\" + "std.csv");
			}
		}
						
		//String findPath = folderPath + "\\curve\\new2\\";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		
		ArrayList<String> startTime = new ArrayList<String>();
		ArrayList<String> endTime = new ArrayList<String>();
		
		
		
		int stdevColCnt = 0;
		String[][] stdevArr = new String[1000][100];
		for(int i = 0; i < 1000; i++){
			for(int j = 0; j < 100; j++){
				stdevArr[i][j] = "";
			}
		}
		
		for(int i = 0; i < listOfFiles.length; i++){
			for(int j = 0; j < section.length; j++){
				for(int l = 0; l < columns.length; l++){
		//for(int i = 0; i < 1; i++){
			//for(int j = 2; j < 3; j++){
			//	for(int l = 4; l < 5; l++){
					
				System.out.println(listOfFiles[i].getName() + ", " + section[j] + ", " + columns[l]);
				startTime.clear();
				endTime.clear();
				
				String type = section[j];
				String currType = "";
				String prevType = "";
				String[][] data = new String[100][100000];
				for(int k = 0; k < 100; k++){
					for(int m = 0; m < 100000; m++){
						data[k][m] = "";
					}
				}
				
				File f = new File(listOfFiles[i].toString());
				String fileName = listOfFiles[i].toString();
				String[] fileNameSplit = fileName.split("\\.");
				fileNameSplit = fileNameSplit[0].split("\\\\");
				fileName = fileNameSplit[fileNameSplit.length-1];
				
				if(f.isFile()){
					BufferedReader inputStream = null;
					String line;
					String[] parseLine;
					
					try{
						inputStream = new BufferedReader(new FileReader(f));
						line = inputStream.readLine();
						
						parseLine = line.split(",");
						int typeIdx = 0;
						int timeIdx = 0;
						int columnIdx = 0;
						
						for(int z = 0; z < parseLine.length; z++){							
							if(parseLine[z].equals("CurveStraight")){
								typeIdx = z;
							}
							if(parseLine[z].equals("TIMES")){
								timeIdx = z;
							}
							if(parseLine[z].equals(columns[l])){
								columnIdx = z;
							}
						}
						
						//System.out.println(typeIdx + ", " + timeIdx + ", " + columnIdx);
						
						
						int row = 0;
						int col = 0;
						int currRow = 0;
						int maxRow = 0;
						
						float currTime = 0;
						
						boolean flg = false;
						while((line = inputStream.readLine()) != null){
							parseLine = line.split(",");
							
							// comment this following if statement for 'total'
							if(parseLine[typeIdx].equals(type)){ // <--
								if(!flg){
									flg = true;
									currRow = 0;
									row = 0;
									col = 0;
									currTime = Float.parseFloat(parseLine[timeIdx]);
								}else{
									//if( row != currRow || ( Math.abs(currTime - Float.parseFloat(parseLine[timeIdx])) > 0.02 )){
									if( row != currRow ){
										/*
										if(currRow > 400){
											col++;	
										}else{
											
										}
										*/
										
										col++;
										currRow = 0;
										row = 0;
									}	
								}
								
								data[col][currRow] = parseLine[columnIdx];
								
								
								currRow++;
								maxRow = Math.max(currRow, maxRow);
							}
							
							
							row++;
							currTime = Float.parseFloat(parseLine[timeIdx]);
							
							//System.out.println("row: " + row + ", currRow: " + currRow + ", col: " + col);
						}
						
						// if the last column contains less than 400 elements, then remove
						if(currRow < 400){
							col--;
						}
						
						//System.out.println("col: " + col);
						
						FileWriter writer = new FileWriter(savePath + "\\" + section[j] + "_" + columns[l] + "_" + fileName +   ".csv");
						writer.append(",");
						for(int z = 0; z < tempColNames.length; z++){
							writer.append(tempColNames[z] + ",");
						}
						writer.append("\n");
								
						
						
						for(int z = 0; z < maxRow; z++){
							writer.append(z + ",");
							for(int k = 0; k < col+1; k++){
								//System.out.print(data[k][z] + ",");
								
								//if(!data[k][z].equals("")){
									if(isAbsolute){
										if(data[k][z].isEmpty()){
											writer.append(",");
										}else{
											writer.append(Math.abs(Float.valueOf(data[k][z])) + ","); // if calculating absoluted avgerage	
										}
									}else{
										
										writer.append(data[k][z] + ",");	
									}
									

								//}
								
							}
							writer.append("\n");
							//System.out.println("");
						}
						
						writer.close();
						
						
						ArrayList<Float> tempAL = new ArrayList<Float>();
						int stdevRowCnt = 0;
						stdevArr[stdevColCnt][stdevRowCnt] = section[j] + "," + columns[l] + "," + fileName + ",";
						
						stdevRowCnt++;
						for(int k = 0; k < col+1; k++){
							tempAL.clear();
							for(int z = 0; z < maxRow; z++){
								if(data[k][z].equals("")){
									
								}else{
									if(isAbsolute){
										tempAL.add(Math.abs(Float.parseFloat(data[k][z])));
									}else{
										tempAL.add(Float.parseFloat(data[k][z]));	
									}
									
								}
								
							}
							BasicStats bs = new BasicStats(tempAL);
							
							if(isAverage){
								stdevArr[stdevColCnt][stdevRowCnt] = bs.getAvg().toString();
							}else{
								stdevArr[stdevColCnt][stdevRowCnt] = bs.getStddev().toString();
							}
							//stdevArr[stdevColCnt][stdevRowCnt] = bs.getStddev().toString();
							//stdevArr[stdevColCnt][stdevRowCnt] = bs.getAvg().toString();
							stdevRowCnt++;
						}
						
						
						stdevColCnt++;
						
						
						/*
						System.out.println(row);
						for(int k = 0; k < endTime.size(); k++){
							System.out.println(startTime.get(k) + ", " + endTime.get(k));
						}
						*/
						
						
						
						
						
						
						}finally{
							inputStream.close();
						}// end of try finally statement
					}// end of isFile() statement
				}// end of l loop
			}// end of j loop
		}// end of i loop
		
		writer_stdev.write("curve,column,type\n");
		for(int j = 0; j < 1000; j++){
			for(int i = 0; i < 100; i++){
				writer_stdev.write(stdevArr[j][i] + ",");
			}
			writer_stdev.write("\n");
		}
		
		writer_stdev.close();
		
		System.out.println("End of Program");
		
	}
}
