package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.BasicStats;

public class BasicPerformanceAnalysis {
	public static void main(String[] args) throws IOException{

		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String savePath = folderPath + "\\analysis\\basicperformance_normalize\\new2";
		String findPath = folderPath + "\\durationInfo\\new2\\";
		//String findPath = folderPath + "\\normalize\\normalized_new2";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		
		
		// search for...
		String[] measurements = {	
				"LS16SASCOMP", "SASSPEED", 
				"CRLKASSTRTOQREQ","CRMDPSSTRCOLTQ","LS16DRIVERREDUCEDTQ","LSS32LKADESTORQUE",
				"POSITIONRIGHT", "LU16LDLANEWIDTH",
				"PHYSICALCURVATURERIGHT","PHYCURVATUREDERIVATIVELEFT","PHYCURVATUREDERIVATIVERIGHT",
				"HEADINGANGLELEFT","HEADINGANGLERIGHT", "WHLSPDFL"};
		
		
		//String searchColumn = "RIGHTVIEWRANGE";
		
		//String searchType = "straight";
		//String searchType = "highCurve";
		//String searchType = "lowCurve";
		String searchType = "total";
		
		
		boolean isAbs = false;
		ArrayList<Float> elements = new ArrayList<Float>();
		
		FileWriter writer = new FileWriter(savePath + "\\" + "basicperformance" + "_" + searchType + ".csv");
				
		for(int z = 0; z < measurements.length; z++){
			
			//System.out.println(searchColumn + " - FileName,Average");
			System.out.println(measurements[z] + " - FileName," + measurements[z] );
			writer.append(measurements[z] + " - FileName," + measurements[z]);
			writer.append("\n");
		
			for(int j = 0; j < listOfFiles.length; j++){
				
				
				File f2 = new File(listOfFiles[j].toString());
				String fileName = listOfFiles[j].toString();
				String[] fileNameSplit = fileName.split("\\.");
				fileNameSplit = fileNameSplit[0].split("\\\\");
				fileName = fileNameSplit[fileNameSplit.length-1];
				
				// init
				elements.clear();
				float average = 0;
				float median = 0;
				float maximum = 0;
				float minimum = 0;
				float stdev = 0;
				float total = 0;
				
				if(f2.isFile()){
					// System.out.println(f2.getName());
					BufferedReader inputStream = null;
					String line;
					String[] parseLine;
					
					try{
						// File Writer
						//FileWriter writer = new FileWriter(savePath + "\\" + fileName + "_curveStraight" + ".txt");
						
						inputStream = new BufferedReader(new FileReader(f2));
						line = inputStream.readLine();
						
						parseLine = line.split(",");
						int typeIdx = 0;
						int searchColumnIdx = 0;
						for(int i = 0; i < parseLine.length; i++){
							//if(parseLine[i].equals("CurveStraight")){
							if(parseLine[i].equals("STATUS")){
								typeIdx = i;
							}
							
							//if(parseLine[i].equals(searchColumn)){
							if(parseLine[i].equals(measurements[z])){
								searchColumnIdx = i;
							}
						}
						
						while((line = inputStream.readLine()) != null){
							parseLine = line.split(",");
	
							//if(parseLine[typeIdx].equals(searchType)){ // comment this if statement for "total" section
								if(parseLine.length <= searchColumnIdx){
									continue;
								}
								
								if(parseLine[searchColumnIdx].equals("")){
									continue;
								}
								
								if(isAbs){
									elements.add(Math.abs(Float.parseFloat(parseLine[searchColumnIdx])));
								}else{
									elements.add(Float.parseFloat(parseLine[searchColumnIdx]));								
								}
							//} //<---
						}
	
						BasicStats bs = new BasicStats(elements);
						average = bs.getAvg();
						stdev = bs.getStddev();
						/*
						median = bs.getMedian();
						minimum = bs.getMinNum();
						maximum = bs.getMaxNum();
						
						total = bs.getSum();
						*/
						
						//System.out.println(fileName + "," + average);
						writer.append(fileName + "," + String.format("%.2f", average) + " (" + String.format("%.2f", stdev) + ")");
						writer.append("\n");
						/*
						System.out.println("Average: " + average);
						System.out.println("Stdev: " + stdev);
						System.out.println("Median: " + median);
						System.out.println("Minimum: " + minimum);
						System.out.println("Maximum: " + maximum);
						*/
						
					}finally{
						inputStream.readLine();
					}
				}
			}
		}
		writer.close();
		System.out.println("End of Program");
		
	}
}
