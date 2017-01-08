package highway.type;

/*
 * This java file creates csv files which have information on each section is high/low/straight from where to where
 * The method is based on 3rd method (physical curvature right)
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

public class ClassifyRoadV3 {
	public static void main(String[] args) throws IOException{

		// Read time, SAS_Angle, WHL_SPD_FL
		// File & load csv
		String folderPath = "E:\\LKAS_data_test2";
		String savePath = folderPath + "\\curve";
		File folder = new File(folderPath + "\\filtered");
		File[] listOfFiles = folder.listFiles();
		//String fileName = "Time_SASAngle_WHLSpeed.csv";
		
		// File Writer
		FileWriter writer = new FileWriter(savePath + "\\" + "data_curve_base" + ".csv");
		writer.append("FileName" + "," + "startTime" + "," + "endTime" + "," + "duration" + "," + "angleAvg" + "," + "angleAvg_Avg" + "," +  "laneWidth_Stdev" + ", " + "speedAvg" + "," + "type" + "," + "filter" + "\n");
				
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
			ArrayList<Float> laneWidthal = new ArrayList<Float>();
			ArrayList<Float> lkasStrToqReqal = new ArrayList<Float>();
			ArrayList<Float> mdpsStrColTqal = new ArrayList<Float>();
			
			// index variables
			int timeIdx = 0;
			int angleIdx = 0;
			int speedIdx = 0;
			
			
			int CF_LKAS_SYS_STATE_idx = 0;	
			int CF_GWAY_HAZARD_SW_idx = 0; 		
			int CF_LEFTLINE_QUALITY_idx = 0;	
			int CF_RIGHTLINE_QUALITY_idx = 0;	
			int CF_MDPS_FAIL_STAT_idx = 0;		
			int CF_MDPS_TOI_FLT_idx = 0;		
			int LQUALITY_idx = 0;				
			int RQUALITY_idx = 0;
			int LANEWIDTH_idx = 0;
			int CRLKASSTRTOQREQ_idx = 0;
			int CRMDPSSTRCOLTQ_idx = 0;
			
			// regular variables
			float time = 0;
			float angle = 0;
			float speed = 0;
			float laneWidth = 0;
			float lkasStrToqReq = 0;
			float mdpsStrColTq = 0;
			
			float CF_LKAS_SYS_STATE = 0; 		// 3, 4	
			float CF_GWAY_HAZARD_SW = 0; 		// 0
			float CF_LEFTLINE_QUALITY = 0;	// 2, 3
			float CF_RIGHTLINE_QUALITY = 0;	// 2, 3
			float CF_MDPS_FAIL_STAT = 0;		// 0
			float CF_MDPS_TOI_FLT = 0;		// 0
			float LQUALITY = 0;				// 2, 3
			float RQUALITY = 0;				// 2, 3
			
			boolean is_CF_LKAS_SYS_STATE = true;
			boolean is_CF_GWAY_HAZARD_SW = true;
			boolean is_CF_LEFTLINE_QUALITY = true;	
			boolean is_CF_RIGHTLINE_QUALITY = true;
			boolean is_CF_MDPS_FAIL_STAT = true;	
			boolean is_CF_MDPS_TOI_FLT = true;		
			boolean is_LQUALITY = true;			
			boolean is_RQUALITY = true;
			boolean is_WHLSPDFL = true;
			
			float CF_LKAS_SYS_STATE_startTime = 0;
			float CF_LKAS_SYS_STATE_endTime = 0;
			int CF_LKAS_SYS_STATE_cnt = 0;
			
			float CF_LEFTLINE_QUALITY_startTime = 0;
			float CF_LEFTLINE_QUALITY_endTime = 0;
			int CF_LEFTLINE_QUALITY_cnt = 0;
			
			float CF_RIGHTLINE_QUALITY_startTime = 0;
			float CF_RIGHTLINE_QUALITY_endTime = 0;
			int CF_RIGHTLINE_QUALITY_cnt = 0;
			
			float LQUALITY_startTime = 0;
			float LQUALITY_endTime = 0;
			int LQUALITY_cnt = 0;
			
			float RQUALITY_startTime = 0;
			float RQUALITY_endTime = 0;
			int RQUALITY_cnt = 0;
			
			float CF_MDPS_FAIL_STAT_startTime = 0;
			float CF_MDPS_FAIL_STAT_endTime = 0;
			int CF_MDPS_FAIL_STAT_cnt = 0;
			
			float CF_MDPS_TOI_FLT_startTime = 0;
			float CF_MDPS_TOI_FLT_endTime = 0;
			int CF_MDPS_TOI_FLT_cnt = 0;
			
						
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
			float laneWidthStdev = 0;
			float lkasStrToqReqAvg = 0;
			float mdpsStrColTqAvg = 0;
			
			String type = "";
			String filterReason = "";
			
			// threshold setting
			float lowAngle = (float)0.0002;
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
						
						if(column[i].equals("CRLKASSTRTOQREQ")){
							CRLKASSTRTOQREQ_idx = i;
						}
						
						if(column[i].equals("CRMDPSSTRCOLTQ")){
							CRMDPSSTRCOLTQ_idx = i;
						}
						/*
						if(column[i].equals("LU16LDLANEWIDTH")){
							LANEWIDTH_idx = i;
						}
						
						if(column[i].equals("CFLKASSYSSTATE")){
							CF_LKAS_SYS_STATE_idx = i;
						}
						
						if(column[i].equals("CFGWAYHAZARDSW")){
							CF_GWAY_HAZARD_SW_idx = i;
						}
						
						if(column[i].equals("CFLEFTLINEQUALITY")){
							CF_LEFTLINE_QUALITY_idx = i;
						}
						
						if(column[i].equals("CFRIGHTLINEQUALITY")){
							CF_RIGHTLINE_QUALITY_idx = i;
						}
						
						if(column[i].equals("CFMDPSFAILSTAT")){
							CF_MDPS_FAIL_STAT_idx = i;
						}
						
						if(column[i].equals("CFMDPSTOIFLT")){
							CF_MDPS_TOI_FLT_idx = i;
						}
						
						if(column[i].equals("LQUALITY")){
							LQUALITY_idx = i;
						}
						
						if(column[i].equals("RQUALITY")){
							RQUALITY_idx = i;
						}
						*/
					}
					
					while((line = inputStream.readLine()) != null){					
						
						parseLine = line.split(",");
						
						time = Float.valueOf(parseLine[timeIdx]);
						angle = Float.valueOf(parseLine[angleIdx]);
						speed = Float.valueOf(parseLine[speedIdx]);
						laneWidth = Float.valueOf(parseLine[LANEWIDTH_idx]);
						lkasStrToqReq = Float.valueOf(parseLine[CRLKASSTRTOQREQ_idx]);
						mdpsStrColTq = Float.valueOf(parseLine[CRMDPSSTRCOLTQ_idx]);
						
						
						
						//writer.append(time + "\n");
						
						if(row == 0){
							// set flg (±âÁØ & curr)
							if(angle > lowAngle){
								strdCurveFlg = 1;
								currCurveFlg = 1;
							}else if(angle < -lowAngle){
								strdCurveFlg = 2;
								currCurveFlg = 2;
							}else{
								strdCurveFlg = 0;
								currCurveFlg = 0;
							}
							
							timeal.add(time);
							angleal.add(angle);
							speedal.add(speed);
							laneWidthal.add(laneWidth);
							lkasStrToqReqal.add(lkasStrToqReq);
							mdpsStrColTqal.add(mdpsStrColTq);
							
							maxAngle = Math.max(maxAngle, Math.abs(angle));
						}else{
							if(angle > lowAngle){
								currCurveFlg = 1;
							}else if(angle < -lowAngle){
								currCurveFlg = 2;
							}else{
								currCurveFlg = 0;
							}
							
							if(strdCurveFlg == currCurveFlg && Math.abs(time - timeal.get(timeal.size()-1)) < 0.02){
								timeal.add(time);
								angleal.add(angle);
								speedal.add(speed);
								laneWidthal.add(laneWidth);
								lkasStrToqReqal.add(lkasStrToqReq);
								mdpsStrColTqal.add(mdpsStrColTq);
								
								maxAngle = Math.max(maxAngle, Math.abs(angle));

							}else{
								//System.out.println(filterReason);
								startTime = timeal.get(0);
								endTime = timeal.get(timeal.size()-1);
								duration = endTime - startTime;
								
								
								bs = new BasicStats(angleal);
								angleAvg = bs.getAvg();
	
								bs = new BasicStats(speedal);
								speedAvg = bs.getAvg();

								bs = new BasicStats(laneWidthal);
								laneWidthStdev = bs.getStddev();
								
								bs = new BasicStats(lkasStrToqReqal);
								lkasStrToqReqAvg = bs.getAvg();
								
								bs = new BasicStats(mdpsStrColTqal);
								mdpsStrColTqAvg = bs.getAvg();
								
								timeal.clear();
								angleal.clear();
								speedal.clear();
								laneWidthal.clear();
								lkasStrToqReqal.clear();
								mdpsStrColTqal.clear();
																
								timeal.add(time);
								angleal.add(angle);
								speedal.add(speed);
								laneWidthal.add(laneWidth);
								lkasStrToqReqal.add(lkasStrToqReq);
								mdpsStrColTqal.add(mdpsStrColTq);
								
								strdCurveFlg = -1;
								currCurveFlg = -1;
								row = -1;
								
								
								type = "";
								
								if(Math.abs(maxAngle) >= highAngle){
									type = "highCurve";
								}else if(Math.abs(maxAngle) > lowAngle && Math.abs(maxAngle) < highAngle){
									type = "lowCurve";
								}else{
									type = "straight";
								}
								
								/*
								if(type.equals("lowCurve") && duration < 4 ){
									type = "straight";
								}
								*/
								
								
								writer.append(f.getName().split("\\.")[0] + "," + startTime + "," + endTime + "," + duration + "," + angleAvg + "," + Math.abs(angleAvg) + "," + laneWidthStdev + ","+ speedAvg + "," + type + "," + filterReason + "\n");
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
					bs = new BasicStats(laneWidthal);
					laneWidthStdev = bs.getStddev();
					
					writer.append(f.getName().split("\\.")[0] + "," + startTime + "," + endTime + "," + duration + "," + angleAvg + "," + Math.abs(angleAvg) + "," + laneWidthStdev + "," + speedAvg + "," + type + "\n");
	
					
					
				}finally{
					inputStream.close();
				}
			}
			
			System.out.println("End of file: " + j);
			
		}
		writer.close();
		
		System.out.println("End of System #1");
		
		// start program part #2: read the created file and merge multiple straight paths
		// File Writer
		FileWriter writer2 = new FileWriter(savePath + "\\" + "data_curve" + ".csv");
		
		writer2.append("FileName" + "," + "startTime" + "," + "endTime" + "," + "duration" + "," + "type" + "\n");
		
		File f2 = new File(savePath + "\\data_curve_base.csv"); 
		
		int type_idx = 0;
		int duration_idx = 0;
		int filename_idx = 0;
		int startTime_idx = 0;
		int endTime_idx = 0;
		
		if(f2.isFile()){
			BufferedReader inputStream2 = null;
			String line2;
			String[] parseLine2;
			
			try{
				
				inputStream2 = new BufferedReader(new FileReader(f2));
				// column
				line2 = inputStream2.readLine();
				parseLine2 = line2.split(",");
				
				for(int i = 0; i < parseLine2.length; i++){
					if(parseLine2[i].equals("FileName")){
						filename_idx = i;
					}
					
					if(parseLine2[i].equals("startTime")){
						startTime_idx = i;
					}
					
					if(parseLine2[i].equals("endTime")){
						endTime_idx = i;
					}
					
					if(parseLine2[i].equals("duration")){
						duration_idx = i;
					}
					
					if(parseLine2[i].equals("type")){
						type_idx = i;
					}
					
					

				}
				
				float startTime = 0;
				float endTime = 0;
				float duration = 0;
				int loopCnt = 0;
				String filename = "";
				String type = "";
				
				boolean combineBoolean = false;
				
				while((line2 = inputStream2.readLine()) != null){
					parseLine2 = line2.split(",");
						
					// if type is "high" and less than 4 seconds, then remove
					/*
					if(parseLine2[type_idx].equals("highCurve") && Float.parseFloat(parseLine2[duration_idx]) <= 4){
						continue; // skip
					}
					*/	
					
					// if type is "straight" and less than 4 seconds, then combine it together with the next section
					// if(parseLine2[type_idx].equals("straight")){
					
					if(loopCnt == 0){
						combineBoolean = true;
						if(loopCnt == 0){
							startTime = Float.parseFloat(parseLine2[startTime_idx]);
						}else{

						}
						
						endTime	= Float.parseFloat(parseLine2[endTime_idx]);
						duration = duration + Float.parseFloat(parseLine2[duration_idx]);
						filename = parseLine2[filename_idx];
						type = parseLine2[type_idx];
						
						loopCnt++;
						
						//continue; // next straight section
					}else if(loopCnt > 0 && type.equals(parseLine2[type_idx]) && filename.equals(parseLine2[filename_idx])){
						endTime = Float.parseFloat(parseLine2[endTime_idx]);
						duration = duration + Float.parseFloat(parseLine2[duration_idx]);
						filename = parseLine2[filename_idx];
						type = parseLine2[type_idx];
						loopCnt++;
					}else{
						if(duration <= 4){
							endTime = Float.parseFloat(parseLine2[endTime_idx]);
							duration = duration + Float.parseFloat(parseLine2[duration_idx]);
							filename = parseLine2[filename_idx];
							type = parseLine2[type_idx];
							loopCnt++;
							continue;
						}						
						//save until now
						writer2.append(filename + 
								"," + startTime + 
								"," + endTime + 
								"," + duration +
								"," + type);
						writer2.append("\n");
						
						//save current
						startTime = Float.parseFloat(parseLine2[startTime_idx]);
						endTime = Float.parseFloat(parseLine2[endTime_idx]);
						duration = Float.parseFloat(parseLine2[duration_idx]);
						filename = parseLine2[filename_idx];
						type = parseLine2[type_idx];
						loopCnt = 1;
					}
					
				}
				
					
				//save until now
				writer2.append(filename + 
						"," + startTime + 
						"," + endTime + 
						"," + duration +
						"," + type);
				writer2.append("\n");

				
				
			}finally{
				inputStream2.close();
			
			}
			
			System.out.println("End of System #2");
		}
		writer2.close();
		
		
		// start program part #3: read the created file and merge multiple straight & low curve paths
		// File Writer
		FileWriter writer3 = new FileWriter(savePath + "\\" + "data_curve_final" + ".csv");
		
		writer3.append("FileName" + "," + "startTime" + "," + "endTime" + "," + "duration" + "," + "type" + "\n");
		
		File f3 = new File(savePath + "\\data_curve.csv"); 
		
		type_idx = 0;
		duration_idx = 0;
		filename_idx = 0;
		startTime_idx = 0;
		endTime_idx = 0;
		
		if(f3.isFile()){
			BufferedReader inputStream3 = null;
			String line3;
			String[] parseLine3;
			
			try{
				
				inputStream3 = new BufferedReader(new FileReader(f3));
				// column
				line3 = inputStream3.readLine();
				parseLine3 = line3.split(",");
				
				for(int i = 0; i < parseLine3.length; i++){
					if(parseLine3[i].equals("FileName")){
						filename_idx = i;
					}
					
					if(parseLine3[i].equals("startTime")){
						startTime_idx = i;
					}
					
					if(parseLine3[i].equals("endTime")){
						endTime_idx = i;
					}
					
					if(parseLine3[i].equals("duration")){
						duration_idx = i;
					}
					
					if(parseLine3[i].equals("type")){
						type_idx = i;
					}
					
					

				}
				
				float startTime = 0;
				float endTime = 0;
				float duration = 0;
				int loopCnt = 0;
				String filename = "";
				String type = "";
				int idx = 0;
				
				boolean combineBoolean = false;
				
				while((line3 = inputStream3.readLine()) != null){
					parseLine3 = line3.split(",");
					if(idx == 0){
						filename = parseLine3[filename_idx];
						startTime = Float.parseFloat(parseLine3[startTime_idx]);
						endTime = Float.parseFloat(parseLine3[endTime_idx]);
						duration = Float.parseFloat(parseLine3[duration_idx]);
						type = parseLine3[type_idx];
					}else{
						if(!(parseLine3[type_idx].equals("highCurve")) && // highCurve should not be combined together and considered to be two separated section even if two highCurve sections are consecutive
								parseLine3[type_idx].equals(type) && parseLine3[filename_idx].equals(filename)){
							endTime = Float.parseFloat(parseLine3[endTime_idx]);
							duration = duration + Float.parseFloat(parseLine3[duration_idx]);
						}else{
							writer3.append(filename + "," + startTime + "," + endTime + "," + duration + "," + type + "\n");
							
							filename = parseLine3[filename_idx];
							startTime = Float.parseFloat(parseLine3[startTime_idx]);
							endTime = Float.parseFloat(parseLine3[endTime_idx]);
							duration = Float.parseFloat(parseLine3[duration_idx]);
							type = parseLine3[type_idx];
						}
					}
					idx++;
				}
				
				writer3.append(filename + "," + startTime + "," + endTime + "," + duration + "," + type + "\n");
				
			}finally{
				inputStream3.close();
			
			}
			
			System.out.println("End of System #3");
		}
		writer3.close();
		
		
		System.out.println("End of Program");
	}
}
