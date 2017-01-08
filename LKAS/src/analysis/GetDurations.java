package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class GetDurations {
	public static void main(String args[]) throws IOException{
		// 전체 파일을 다 돌면서
		// 각 파일별로 high curve는 언제부터 언제까지 얼마나 주행했나
		// low curve, straight 도 마찬가지로 얼마나 주행했는지 계산함
		// high curve에서 다른 status 로 바뀌면 현재까지의 정보를 계산하여 저장
		// 그리고 새로 시작
		
		int measurementStartIdx = 3;
		
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String savePath = folderPath + "\\durationInfo";
		String findPath = folderPath + "\\normalize\\normalized_new2";
		//String findPath = folderPath + "\\curve\\new2";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		
		/*
		String[] measurements = {"LEFTVIEWRANGE","RIGHTVIEWRANGE","LU16LDLANEWIDTH","POSITIONLEFT","POSITIONRIGHT",
									"LS16SASCOMP","LS16YAWRATECOMP","SASSPEED","SASANGLE","HEADINGANGLELEFT","HEADINGANGLERIGHT",
									"LSS32LKADESTORQUE","LS16DRIVERREDUCEDTQ","CRLKASSTRTOQREQ","CRMDPSSTRCOLTQ",
									"LQUALITY","RQUALITY","LSU8HANDSOFFREGIONFLG","LU1HANDOFFDETFLG","LU1HANDOFFWARNINGFLG",
									"PHYSICALCURVATURELEFT","PHYSICALCURVATURERIGHT","PHYCURVATUREDERIVATIVELEFT","PHYCURVATUREDERIVATIVERIGHT","LU16HANDOFFDETECTCNT"};
		*/
		
		HashMap<String, Float> measurements = new HashMap<String, Float>();
		
		
		for(int j = 0; j < listOfFiles.length; j++){
			measurements.clear();
			File f = new File(listOfFiles[j].toString());
			String fileName = listOfFiles[j].toString();
			String[] fileNameSplit = fileName.split("\\.");
			fileNameSplit = fileNameSplit[0].split("\\\\");
			fileName = fileNameSplit[fileNameSplit.length-1];
		
			int timeIdx = 0;
			int statusIdx = 0;
			
			int row = 0;
			int cnt = 0;
			
			String currStatus = "";
			float currTime = 0;
			
			float startTime = 0;
			float endTime = 0;
			float duration = 0;

			if(f.isFile()){
				BufferedReader inputStream = null;
				String line;
				String[] parseLine = null;
				String[] column;
				
				// File Writer
				FileWriter writer = new FileWriter(savePath + "\\" + fileName + ".csv");
				
				try{
					inputStream = new BufferedReader(new FileReader(f));
										
					line = inputStream.readLine();
					column= line.split(",");
					writer.append("STATUS," + "FILENAME,"+"STARTTIME,"+"ENDTIME,"+"DURATION"+",");
					for(int i = 0; i < column.length; i++){
						writer.append(column[i]+",");
					}
					writer.append("\n");						
					
					for(int i = 0; i < column.length; i++){
						if(column[i].equals("TIMES")){
							timeIdx = i;
						}
						
						if(column[i].equals("CurveStraight")){
							statusIdx = i;
						}
					}
					
					while((line = inputStream.readLine()) != null){
						parseLine = line.split(",");
						
						
						if(row == 0){
							currStatus = parseLine[statusIdx];
							currTime = Float.parseFloat(parseLine[timeIdx]);	
							startTime = Float.parseFloat(parseLine[timeIdx]);
							endTime = Float.parseFloat(parseLine[timeIdx]);
							for(int i = 0; i < column.length-measurementStartIdx; i++){
								measurements.put(column[i+measurementStartIdx], Float.parseFloat(parseLine[i+measurementStartIdx]));
							}
						}else{
							
							
							if(!(parseLine[statusIdx].equals(currStatus))){
								
								//System.out.println(startTime + "," + endTime + "," + currStatus);
								duration = endTime - startTime;
								if(duration < 4){
									currStatus = parseLine[statusIdx];
									currTime = Float.parseFloat(parseLine[timeIdx]);
									startTime = Float.parseFloat(parseLine[timeIdx]);
									
									cnt = 1;
									
									for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
										String measurementKey = column[i+measurementStartIdx];
										float measurementValue = Float.parseFloat(parseLine[i+measurementStartIdx]);
										measurements.put(measurementKey, measurementValue);
									}
									continue;
								}else{
									writer.append(currStatus + "," + fileName + "," + startTime + "," + endTime + "," + duration + ",");
									
									for(int i = 0; i < measurementStartIdx; i++){
										writer.append(",");
									}
									
									for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
										
										float value = measurements.get(column[i+measurementStartIdx]);
										value = value/cnt;
										
										String wrt = Float.toString(value);
										writer.append( wrt + "," );
									}
									
									writer.append("\n");
									//row = 0;
									currStatus = parseLine[statusIdx];
									currTime = Float.parseFloat(parseLine[timeIdx]);
									startTime = Float.parseFloat(parseLine[timeIdx]);
									
									for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
										String measurementKey = column[i+measurementStartIdx];
										float measurementValue = Float.parseFloat(parseLine[i+measurementStartIdx]);
										measurements.put(measurementKey, measurementValue);
									}
									
									cnt = 1;
									continue;
								}
								
							}else{
								if(Math.abs(currTime - Float.parseFloat(parseLine[timeIdx])) > 0.03){

																		
									//System.out.println(startTime + "," + endTime + "," + currStatus);
									duration = endTime - startTime;
									
									if(duration < 4){
										currStatus = parseLine[statusIdx];
										currTime = Float.parseFloat(parseLine[timeIdx]);
										startTime = Float.parseFloat(parseLine[timeIdx]);
										
										cnt = 1;
										
										for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
											String measurementKey = column[i+measurementStartIdx];
											float measurementValue = Float.parseFloat(parseLine[i+measurementStartIdx]);
											measurements.put(measurementKey, measurementValue);
										}
										continue;
									}else{
										writer.append(currStatus + "," + fileName + "," + startTime + "," + endTime + "," + duration + ",");
										for(int i = 0; i < measurementStartIdx; i++){
											writer.append(",");
										}
										for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
											float value = measurements.get(column[i+measurementStartIdx]);
											value = value/cnt;
											
											String wrt = Float.toString(value);
											writer.append( wrt + "," );
										}
										
										writer.append("\n");
										//row = 0;
										currStatus = parseLine[statusIdx];
										currTime = Float.parseFloat(parseLine[timeIdx]);
										startTime = Float.parseFloat(parseLine[timeIdx]);
										
										cnt = 1;
										
										for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
											String measurementKey = column[i+measurementStartIdx];
											float measurementValue = Float.parseFloat(parseLine[i+measurementStartIdx]);
											measurements.put(measurementKey, measurementValue);
										}
										continue;	
									}
									
									
								}else{
									for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
										String measurementKey = column[i+measurementStartIdx];
										float measurementValue = Float.parseFloat(parseLine[i+measurementStartIdx]) + measurements.get(measurementKey);
										measurements.put(measurementKey, measurementValue);
									}
									
									
								}
								
							}
						}
						endTime = Float.parseFloat(parseLine[timeIdx]);
						currTime = Float.parseFloat(parseLine[timeIdx]);
						
						row++;
						cnt++;
					}
					duration = endTime - startTime;
					if(duration > 4){
						writer.append(currStatus + "," + fileName + "," + startTime + "," + endTime + "," + duration + ",");
						for(int i = 0; i < measurementStartIdx; i++){
							writer.append(",");
						}
						for(int i = 0; i < parseLine.length-measurementStartIdx; i++){
							float value = measurements.get(column[i+measurementStartIdx]);
							value = value/cnt;
							
							String wrt = Float.toString(value);
							writer.append( wrt + "," );
						}
						
						writer.append("\n");
					}
					
					

					writer.close();
				}finally{
					inputStream.close();
				}
			}
			
			System.out.println("End of file: " + fileName);
		}

		
		System.out.println("End of Program");
	}
}
