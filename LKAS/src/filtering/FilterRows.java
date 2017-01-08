package filtering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FilterRows {
	public static void main(String[] args) throws IOException{
		// read "filter_result_cumulated
		// read each integrated files
		// for each integrated file, if the time is included in those "should be filtered timestamp", then exclude that timestamp from the result.
		
		// input: filter_result_cumulated (filter master file), integrated files
		// output: integrated files with filtered rows
		
		// curve 구간 파일 읽기
		// 읽어서 array에 저장
		String folderPath = "E:\\LKAS_data_test2";
		String curveDataPath = folderPath + "\\curve";
		
		File f = new File(curveDataPath + "\\" + "filter_result_cumulated.csv");
		File f3 = new File(curveDataPath + "\\" + "filter_result_lkas_state.csv");
		
		// variable declaration for curve구간
		ArrayList<String> fileAL = new ArrayList<String>();
		ArrayList<Float> startTimeAL = new ArrayList<Float>();
		ArrayList<Float> endTimeAL = new ArrayList<Float>();
		ArrayList<String> filterAL = new ArrayList<String>();
		
		
		if(f.isFile()){
			BufferedReader inputStream = null;
			String line;
			String[] parseLine;
			try{
				
				inputStream = new BufferedReader(new FileReader(f));
				// column
				String[] column;
				line = inputStream.readLine();
				column = line.split(",");
				
				int startTimeIdx = 0;
				int endTimeIdx = 0;
				int fileIdx = 0;
				int filterIdx = 0;
				// find index
				for(int i = 0; i < column.length; i++){
					if(column[i].equals("FileName")){
						fileIdx = i;
					}
					
					if(column[i].equals("startTime")){
						startTimeIdx = i;
					}
					
					if(column[i].equals("endTime")){
						endTimeIdx = i;
					}
					
					if(column[i].equals("filter")){
						filterIdx = i;
					}
				}
				
				
				while((line = inputStream.readLine()) != null){				
					parseLine = line.split(",");
					fileAL.add(parseLine[fileIdx]);
					startTimeAL.add(Float.parseFloat(parseLine[startTimeIdx])); 
					endTimeAL.add(Float.parseFloat(parseLine[endTimeIdx]));
					filterAL.add(parseLine[filterIdx]);
				}
				
				for(int i = 0; i < fileAL.size(); i++){
					System.out.println(fileAL.get(i) + ", " + startTimeAL.get(i) + ", " + endTimeAL.get(i) + ", " + filterAL.get(i));
				}
			}finally{
				inputStream.close();
			}
		}
		
		System.out.println("End of reading filter file.csv");
		
		ArrayList<String> fileAL2 = new ArrayList<String>();
		ArrayList<Float> startTimeAL2 = new ArrayList<Float>();
		ArrayList<Float> endTimeAL2 = new ArrayList<Float>();
		ArrayList<Float> durationAL2 = new ArrayList<Float>();
		ArrayList<String> filterAL2 = new ArrayList<String>();
		ArrayList<Boolean> shouldBeFilteredAL2 = new ArrayList<Boolean>();
		
		if(f3.isFile()){
			BufferedReader inputStream = null;
			String line;
			String[] parseLine;
			try{
				
				inputStream = new BufferedReader(new FileReader(f3));
				// column
				String[] column;
				line = inputStream.readLine();
				column = line.split(",");
				
				int startTimeIdx = 0;
				int endTimeIdx = 0;
				int durationIdx = 0;
				int fileIdx = 0;
				int filterIdx = 0;
				// find index
				for(int i = 0; i < column.length; i++){
					if(column[i].equals("FileName")){
						fileIdx = i;
					}
					
					if(column[i].equals("startTime")){
						startTimeIdx = i;
					}
					
					if(column[i].equals("endTime")){
						endTimeIdx = i;
					}
					
					if(column[i].equals("duration")){
						durationIdx = i;
					}
					
					if(column[i].equals("lkasState")){
						filterIdx = i;
					}
				}
				
				
				while((line = inputStream.readLine()) != null){				
					parseLine = line.split(",");
					fileAL2.add(parseLine[fileIdx]);
					startTimeAL2.add(Float.parseFloat(parseLine[startTimeIdx])); 
					endTimeAL2.add(Float.parseFloat(parseLine[endTimeIdx]));
					durationAL2.add(Float.parseFloat(parseLine[durationIdx]));
					filterAL2.add(parseLine[filterIdx]);
					shouldBeFilteredAL2.add(false);
				}
				
				for(int i = 0; i < fileAL2.size(); i++){
					System.out.println(fileAL2.get(i) + ", " + startTimeAL2.get(i) + ", " + endTimeAL2.get(i) + ", " + durationAL2.get(i) + "," + filterAL2.get(i));
				}
				
				System.out.println("======");
				
				for(int i = 1; i < fileAL2.size(); i++){
					if(filterAL2.get(i).equals("3.0") 
							&& ( (durationAL2.get(i-1) > durationAL2.get(i) && fileAL2.get(i-1).equals(fileAL2.get(i))) 
									|| (durationAL2.get(i+1) > durationAL2.get(i) && fileAL2.get(i+1).equals(fileAL2.get(i)))) ){
						//System.out.println(fileAL2.get(i) + "," + startTimeAL2.get(i) + "," + endTimeAL2.get(i));
						shouldBeFilteredAL2.set(i,true);
					}
				}
				
				/*
				for(int i = 0; i < shouldBeFilteredAL2.size(); i++){
					System.out.println(fileAL2.get(i) + "," + startTimeAL2.get(i) + "," + endTimeAL2.get(i) + "," + shouldBeFilteredAL2.get(i));
				}
				*/
			}finally{
				inputStream.close();
			}
		}
		
		System.out.println("End of reading filter lkasState file.csv");
		

		// 각각의 데이터 파일 하나씩 읽기
		// Time 데이터가 startTime 과 endTime에 한번이라도 들어가면 curve 구간
		// 아니라면 직선 구간임
		// 판단하여 제일 첫 column에 넣고 나머지 row는 그냥 붙이기
		
		// File & load csv
		
		String savePath = folderPath + "\\filtered";
		File folder = new File(folderPath + "\\integrated");
		File[] listOfFiles = folder.listFiles();
		//String fileName = "Time_SASAngle_WHLSpeed.csv";
		

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
					// File Writer
					FileWriter writer = new FileWriter(savePath + "\\" + fileName +  ".csv");
					
					inputStream = new BufferedReader(new FileReader(f2));
					String[] column;
					line = inputStream.readLine();
					
					writer.append(line);
					writer.append("\n");
					//System.out.println(line);
					column = line.split(",");
					
					int timeIdx = 0;
					
					for(int i = 0; i < column.length; i++){
						if(column[i].equals("TIMES")){
							timeIdx = i;
						}
					}
					
					//System.out.println(timeIdx);
					
					while((line = inputStream.readLine()) != null){
						parseLine = line.split(",");
												
						float time = Float.parseFloat(parseLine[timeIdx]);
						
						// f
						// 비교하여 포함되면 curve else straight
						boolean shouldBeFiltered = false;
						for(int i = 0; i < fileAL.size(); i++){
							if(fileAL.get(i).equals(fileName)){
								if(time >= startTimeAL.get(i) && time <= endTimeAL.get(i)){
									shouldBeFiltered = true;
									break;
								}else{
									
								}
							}
						}
						
						// f3
						boolean lkasStateFilter = false;
						for(int i = 1; i < fileAL2.size(); i++){
							if(fileAL2.get(i).equals(fileName)){
								if(time >= startTimeAL2.get(i) && time <= endTimeAL2.get(i) && shouldBeFilteredAL2.get(i)){

									//lkasStateFilter = true;
									shouldBeFiltered = true;
									//System.out.println(fileName + "," + startTimeAL2.get(i) + "," + endTimeAL2.get(i));
									break;
									
								}
							}
						}
						
						if( (!shouldBeFiltered) ){
							for(int k = 0; k < parseLine.length; k++){
								writer.append(parseLine[k] + ",");
							}
							writer.append("\n");	
						}
						
						
						
					}
					
					writer.close();
					System.out.println("end of file: " + j);
				}finally{
					inputStream.close();
				}
			}
		}
		 
	}
}
