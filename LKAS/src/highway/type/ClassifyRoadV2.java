package highway.type;

/*
 * This java file creates csv files which have information on each section is high/low/straight from where to where
 * The method is based on 2nd method (physical curvature right)
 * 	Straight: 0.0000 ~ +/- 0.0005
 * 	Low Curve: +/- 0.0005 ~ +/- 0.0010
 *  High Curve: +/- 0.0010 ~
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.BasicStats;

public class ClassifyRoadV2 {
	public static void main(String[] args) throws IOException{

		// Read time, SAS_Angle, WHL_SPD_FL
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String savePath = folderPath + "\\curve";
		File folder = new File(folderPath + "\\integrated");
		File[] listOfFiles = folder.listFiles();
		//String fileName = "Time_SASAngle_WHLSpeed.csv";
		
		// File Writer
		FileWriter writer = new FileWriter(savePath + "\\" + "data_curve" + ".csv");
		writer.append("FileName" + "," + "startTime" + "," + "endTime" + "," + "duration" + "," + "angleAvg" + "," + "angleAvg_Avg" + "," + "speedAvg" + "," + "type" + "\n");
		
		
		for(int j = 0; j < listOfFiles.length; j++){
			File f = new File(listOfFiles[j].toString());
			String fileName = listOfFiles[j].toString();
			String[] fileNameSplit = fileName.split("\\.");
			fileNameSplit = fileNameSplit[0].split("\\\\");
			fileName = fileNameSplit[fileNameSplit.length-1];
						
			// Variable Declaration
			String[] column;
			
			ArrayList<Float> timeal = new ArrayList<Float>();
			ArrayList<Float> angleal = new ArrayList<Float>();
			ArrayList<Float> speedal = new ArrayList<Float>();
			
			int timeIdx = 0;
			int angleIdx = 0;
			int speedIdx = 0;
			
			float time = 0;
			float angle = 0;
			float speed = 0;
			
			float maxAngle = 0;
			
			float startTime = 0;
			float endTime = 0;
			float duration = 0;
			float angleAvg = 0;
			//float angleMedian = 0;
			//float angleMax = 0;
			//float angleMin = 0;
			float speedAvg = 0;
			//float speedMedian = 0;
			//float speedMax = 0;
			//float speedMin = 0;
			
			String type = "";
			
			//float thresholdAngle = 2; // SAS_Angle threshold setting
			float lowAngle = (float)0.0005;
			float highAngle = (float)0.001;
	
			BasicStats bs;
			// curveFlg => 
			//   0: 0, 
			//   1: +, 
			//   2: -
			int currCurveFlg = -1;
			int strdCurveFlg = -1;
			
			// row count
			int row = 0;
			
			if(f.isFile()){
				BufferedReader inputStream = null;
				String line;
				String[] parseLine;
				
				try{
					
					inputStream = new BufferedReader(new FileReader(f));
					// column
					line = inputStream.readLine();
					column = line.split(",");
					
					
					for(int i = 0; i < column.length; i++){
						if(column[i].equals("TIMES")){
							timeIdx = i;
						}
						
						if(column[i].equals("PHYSICALCURVATURERIGHT")){
							angleIdx = i;
						}
						
						if(column[i].equals("WHLSPDFL")){
							speedIdx = i;
						}
					}
					
					
					while((line = inputStream.readLine()) != null){					
						
						parseLine = line.split(",");
						//System.out.println(line);
						
						/*
						if(parseLine.length < Math.max(speedIdx, angleIdx)+1){
							continue;
						}

						// validate na values		
						if(parseLine[angleIdx].equals("") || parseLine[speedIdx].equals("")){
							continue;
						}
						*/
																	
						time = Float.valueOf(parseLine[timeIdx]);
						angle = Float.valueOf(parseLine[angleIdx]);
						speed = Float.valueOf(parseLine[speedIdx]);
						
						//writer.append(time + "\n");
						
						if(row == 0){
							// set flg (±âÁØ & curr)
							if(angle > 0){
								strdCurveFlg = 1;
								currCurveFlg = 1;
							}else if(angle < 0){
								strdCurveFlg = 2;
								currCurveFlg = 2;
							}else{
								strdCurveFlg = 0;
								currCurveFlg = 0;
							}
							
							timeal.add(time);
							angleal.add(angle);
							speedal.add(speed);
							maxAngle = Math.max(maxAngle, Math.abs(angle));
						}else{
							if(angle > 0){
								currCurveFlg = 1;

							}else if(angle < 0){
								currCurveFlg = 2;

							}else{
								currCurveFlg = 0;

							}
							
							if(strdCurveFlg == currCurveFlg){
								timeal.add(time);
								angleal.add(angle);
								speedal.add(speed);
								maxAngle = Math.max(maxAngle, Math.abs(angle));
								/*
								for(int i = 0; i < timeal.size(); i++){
									writer.append(timeal.get(i) + ",");
								}
								writer.append("\n");
								*/
							}else{
								
								startTime = timeal.get(0);
								endTime = timeal.get(timeal.size()-1);
								duration = endTime - startTime;
								
								bs = new BasicStats(angleal);
								angleAvg = bs.getAvg();
								//angleMin = bs.getMinNum();
								//angleMax = bs.getMaxNum();
								//angleMedian = bs.getMedian();
								
								bs = new BasicStats(speedal);
								speedAvg = bs.getAvg();
								//speedMin = bs.getMinNum();
								//speedMax = bs.getMaxNum();
								//speedMedian = bs.getMedian();
								
								
								timeal.clear();
								angleal.clear();
								speedal.clear();
								
								
								timeal.add(time);
								angleal.add(angle);
								speedal.add(speed);
								
								/*
								for(int i = 0; i < timeal.size(); i++){
									writer.append(timeal.get(i) + ",");
								}
								writer.append("\n");
								*/
								
								strdCurveFlg = -1;
								currCurveFlg = -1;
								row = -1;
								
								type = "";
								
								/*
								if(thresholdAngle < Math.abs(angleAvg)){
									type = "Curve";
								}else{
									type = "Straight";
								}
								*/
								
								if(Math.abs(maxAngle) >= 0 && Math.abs(maxAngle) < lowAngle){
									type = "straight";
								}else if(Math.abs(maxAngle) >= lowAngle && Math.abs(maxAngle) < highAngle){
									type = "lowCurve";
								}else{
									type = "highCurve";
								}
								
								
								writer.append(f.getName().split("\\.")[0] + "," + startTime + "," + endTime + "," + duration + "," + angleAvg + "," + Math.abs(angleAvg) + "," + speedAvg + "," + type + "," + maxAngle + "\n");
//								System.out.println(startTime + "," + endTime + "," + duration + "," + angleAvg + "," + Math.abs(angleAvg) + "," + speedAvg + "," + type);
								
								maxAngle = 0;
								
								
							}
							
						}
						
						
						row++;
					}
					startTime = timeal.get(0);
					endTime = timeal.get(timeal.size()-1);
					duration = endTime - startTime;
					bs = new BasicStats(angleal);
					angleAvg = bs.getAvg();
					bs = new BasicStats(speedal);
					speedAvg = bs.getAvg();
					writer.append(f.getName().split("\\.")[0] + "," + startTime + "," + endTime + "," + duration + "," + angleAvg + "," + Math.abs(angleAvg) + "," + speedAvg + "," + type + "\n");
	
					
					
				}finally{
					inputStream.close();
				}
			}
			
			System.out.println("End of file: " + j);
			
		}
		writer.close();
		
		System.out.println("End of System");
	}
}
