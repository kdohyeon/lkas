package converge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConvergeRoadType {
	public static void main(String[] args) throws IOException{
		// curve 구간 파일 읽기
		// 읽어서 array에 저장
		String folderPath = "E:\\LKAS_data_test2";
		String curveDataPath = folderPath + "\\curve";
		
		//File f = new File(curveDataPath + "\\" + "data_curve.csv");
		File f = new File(curveDataPath + "\\" + "data_curve_final.csv"); 		// 2016. 11. 16 edit
		
		// variable declaration for curve구간
		ArrayList<String> dataTypeAL = new ArrayList<String>();
		ArrayList<String> fileAL = new ArrayList<String>();
		ArrayList<Float> startTimeAL = new ArrayList<Float>();
		ArrayList<Float> endTimeAL = new ArrayList<Float>();
		ArrayList<String> filterAL = new ArrayList<String>();
		ArrayList<Float> durationAL = new ArrayList<Float>();
		
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
				int dataTypeIdx = 0;
				int fileIdx = 0;
				int filterIdx = 0;
				int durationIdx = 0;
				// find index
				for(int i = 0; i < column.length; i++){
					if(column[i].equals("FileName")){
						fileIdx = i;
					}
					
					if(column[i].equals("type")){
						dataTypeIdx = i;
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
					
					if(column[i].equals("duration")){
						durationIdx = i;
					}
				}
				
				System.out.println(durationIdx);
				
				while((line = inputStream.readLine()) != null){				
					parseLine = line.split(",");
					if(Float.parseFloat(parseLine[durationIdx]) < 4 && parseLine[dataTypeIdx].equals("highCurve")){
						
					}else{
						fileAL.add(parseLine[fileIdx]);
						dataTypeAL.add(parseLine[dataTypeIdx]);
						startTimeAL.add(Float.parseFloat(parseLine[startTimeIdx])); 
						endTimeAL.add(Float.parseFloat(parseLine[endTimeIdx]));
						//filterAL.add(parseLine[filterIdx]);	
					}
					
				}
				
				for(int i = 0; i < dataTypeAL.size(); i++){
					System.out.println(fileAL.get(i) + ", " + dataTypeAL.get(i) + ", " + startTimeAL.get(i) + ", " + endTimeAL.get(i));
				}
			}finally{
				inputStream.close();
			}
		}
		
		System.out.println("End of reading data_curve.csv");

		// 각각의 데이터 파일 하나씩 읽기
		// Time 데이터가 startTime 과 endTime에 한번이라도 들어가면 curve 구간
		// 아니라면 직선 구간임
		// 판단하여 제일 첫 column에 넣고 나머지 row는 그냥 붙이기
		
		// File & load csv
		
		String savePath = folderPath + "\\curve\\new2";
		File folder = new File(folderPath + "\\filtered");
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
					FileWriter writer = new FileWriter(savePath + "\\" + fileName + "_curve" + ".csv");
					
					inputStream = new BufferedReader(new FileReader(f2));
					String[] column;
					line = inputStream.readLine();
					
					writer.append("CurveStraight," + line);
					writer.append("\n");
					//System.out.println(line);
					column = line.split(",");
					
					int timeIdx = 0;
					int whl_spd_flIdx = 0;
					for(int i = 0; i < column.length; i++){
						if(column[i].equals("TIMES")){
							timeIdx = i;
						}
						
						if(column[i].equals("WHL_SPD_FL")){
							whl_spd_flIdx = i;
						}
					}
					
					//System.out.println(timeIdx);
					
					while((line = inputStream.readLine()) != null){
						parseLine = line.split(",");
												
						float time = Float.parseFloat(parseLine[timeIdx]);
						
						String type = "";
						for(int i = 0; i < fileAL.size(); i++){
							if(  fileAL.get(i).equals(fileName)  ){
								if(time >= startTimeAL.get(i) && time <= endTimeAL.get(i)){
									type = dataTypeAL.get(i);	
									break;
								}
							}
						}
						
						
						if(type.isEmpty()){
							
						}else{
							writer.append(type + ",");
							
							for(int i = 0; i < parseLine.length; i++){
								writer.append(parseLine[i] + ",");
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

		
		System.out.println("End of System");
	}
}
