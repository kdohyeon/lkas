package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class PartitionAnalysis {
	public static void main(String[] args) throws IOException{
		String type = "straight";
		
		
		
		String[] section = {"highCurve", "lowCurve", "straight"};
		String[] columns = {
				"CRLKASSTRTOQREQ","CRMDPSSTRCOLTQ","HEADINGANGLERIGHT",
				"LS16DRIVERREDUCEDTQ","LU16LDLANEWIDTH","PHYCURVATUREDERIVATIVERIGHT_M",
				"PHYSICALCURVATURERIGHT_M","POSITIONRIGHT",
				"SASANGLE","SASSPEED","VEHICLEPOSITION",
				"WHLSPDFL"
		};
		
		String[] findCols = {"h","b","w","g","m"};
		
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String savePath = folderPath + "\\partitionAnalysis";
		String findPath = folderPath + "\\partitions\\new2_avg";
		 
		
		
						
		
		int typeCnt = 3; //straight, low, high
		int settingCnt = 4; // FT, OVT_1, OVT_2, WKT
		
		
		HashMap<String, Integer> typeSectionMap = new HashMap<String, Integer>();
		
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		String[] filelist = new String[1000];
		for(int i = 0; i < listOfFiles.length; i++){
			
			String[] parseLine = listOfFiles[i].getName().split("_");
			boolean flg = false;
			
			if(parseLine.length > 1){
				String curveType = parseLine[0];
				String variable = parseLine[1];
				
				String setting = "";
				for(int j = 2; j < parseLine.length; j++){
					if(j == parseLine.length-1){
						setting = setting + parseLine[j];	
					}else{
						if(parseLine[j].equals("M")){
							variable = variable + "M";
							flg = true;
						}else{
							setting = setting + parseLine[j] + "_";	
						}
					}
				}
				
				if(flg){
					filelist[i] = curveType + "_" + variable + "_" + setting;
				}else{
					filelist[i] = listOfFiles[i].getName();
				}
				
				File f = new File(listOfFiles[i].getPath());
				int sectionCnt = 0; // # of sections
				if(f.isFile()){
					BufferedReader inputStream = null;
					String line;
					String[] parseLine2;
					try{
						
						inputStream = new BufferedReader(new FileReader(f));
						inputStream.readLine();
						line = inputStream.readLine();
						
						
						if(line != null){
							sectionCnt = line.split(",").length;	
						}
						
						
					}finally{
						inputStream.close();
					}
				}
			
				typeSectionMap.put(curveType+"_"+setting, sectionCnt-1);
				
				
				
				//System.out.println(curveType + ", " + variable + ", " + setting + ", " + sectionCnt);
			}
			
		}
		
		
		for(String key : typeSectionMap.keySet()){
			System.out.println("key : " + key + ", value : " + typeSectionMap.get(key));
			String[] parseLine = key.split("_");
			String curveType = parseLine[0];
			String setting = parseLine[1] + "_" + parseLine[2] + "_" + parseLine[3];
			
			for(int i = 0; i < typeSectionMap.get(key); i++){
				String[][] data = new String[100000][100];
				int sectionCnt = i+1;
				int columnIdx = 0;
				int rowIdx = 0;
				FileWriter writer = new FileWriter(savePath + "\\" + curveType + "_" + setting + "_" + sectionCnt + ".csv");
				
				for(int j = 0; j < filelist.length; j++){
					if(filelist[j] != null){
						String[] parseLine2 = filelist[j].split("_");
						
						String compCurve = parseLine2[0];
						String compVariable = parseLine2[1];
						String compSetting = parseLine2[2] + "_" + parseLine2[3] + "_" + parseLine2[4];
						/*
						for(int z = 0; z < parseLine2.length; z++){
							if(z == parseLine2.length-1){
								compSetting = compSetting + parseLine2[z];
							}else{
								compSetting = compSetting + parseLine2[z] + "_";
							}
						}
						*/
						
						if(curveType.equals(compCurve) && setting.equals(compSetting)){
							
							// read listOfFiles[j]
							File f = new File(listOfFiles[j].getPath());
							
							//System.out.println(sectionCnt + ": " + listOfFiles[j].getName());
							
							if(f.isFile()){
								BufferedReader inputStream = null;
								String line;
								String parseLine3[];
								try{
									inputStream = new BufferedReader(new FileReader(f));
									inputStream.readLine();
									rowIdx = 0;
									while((line = inputStream.readLine()) != null){	
										
										parseLine3 = line.split(",");
										//System.out.println(parseLine3[0] + ", " + parseLine3.length + ", " + sectionCnt);
										if(parseLine3.length <= sectionCnt){
											break;
										}
										
										if(parseLine3[sectionCnt].equals("")){
											
											break;
										}
										
										data[rowIdx][columnIdx] = parseLine3[sectionCnt];
										
										
										rowIdx++;
										
									}									
									
									columnIdx++;
								}finally{
									inputStream.close();
								}
							}
							
							// get appropriate data of specific section
							
							// write
							writer.append(compVariable + ",");
						}
						
					}
					
				}
				writer.append("\n");
				
				for(int j = 0; j < rowIdx; j++){
					for(int z = 0; z < columnIdx; z++){
						writer.append(data[j][z] + ",");
					}
					writer.append("\n");
				}
				
				rowIdx = 0;
				columnIdx = 0;
				
				writer.close();
			}
		}
		
		
		for(int i = 0; i < filelist.length; i++){
			if(filelist[i] != null){
				System.out.println(filelist[i]);	
			}
			
		}
		
		
	}
}
