package highway.type;
/*
 * This java file creates csv files which have information on each section is high/low/straight from where to where
 * The method is based on 1st method (physical curvature right)
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

public class ClassifyRoadV1 {
	public static void main(String[] args) throws IOException{

		// Read time, SAS_Angle, WHL_SPD_FL
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String savePath = folderPath + "\\curve";
		String findPath = folderPath + "\\integrated";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		//String fileName = "Time_SASAngle_WHLSpeed.csv";
		
		// File Writer
		//FileWriter writer = new FileWriter(savePath + "\\" + "data" + "_curve" + ".txt");
		//writer.append("FileName" + "," + "startTime" + "," + "endTime" + "," + "duration" + "," + "angleAvg" + "," + "angleAvg_Abs" + "," + "speedAvg" + "," + "type" + "\n");
		
		
		for(int j = 0; j < listOfFiles.length; j++){
			File f = new File(listOfFiles[j].toString());
			String fileName = listOfFiles[j].toString();
			String[] fileNameSplit = fileName.split("\\.");
			fileNameSplit = fileNameSplit[0].split("\\\\");
			fileName = fileNameSplit[fileNameSplit.length-1];
			
			FileWriter writer = new FileWriter(savePath + "\\" + fileName + ".csv");
			
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
			
			float highCurvature = (float) 0.001; // Threshold
			float lowCurvature = (float) 0.0005; // Threshold
			
			//System.out.println(highCurvature + ", " + lowCurvature);
	
			BasicStats bs;;
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
					writer.append("TYPE"+",");
					for(int i = 0; i < column.length; i++){
						writer.append(column[i] + ",");
					}
					writer.append("\n");
					
					
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
						
											
						time = Float.valueOf(parseLine[timeIdx]);
						angle = Float.valueOf(parseLine[angleIdx]);
						speed = Float.valueOf(parseLine[speedIdx]);
						

						
						if(Math.abs(angle) <= lowCurvature){
							writer.append("straight" + ",");
							
						}else if( (lowCurvature < Math.abs(angle) && Math.abs(angle) <= highCurvature) ){
							writer.append("lowCurve" + ",");
						}else{
							writer.append("highCurve" + ",");
						}
						
						for(int i = 0; i < parseLine.length; i++){
							writer.append(parseLine[i] + ",");
						}
						writer.append("\n");
						
					}
	
					writer.close();
					
				}finally{
					inputStream.close();
				}
			}
			
			System.out.println("End of file: " + j);
			
		}
		
		
		System.out.println("End of System");
	}
}
