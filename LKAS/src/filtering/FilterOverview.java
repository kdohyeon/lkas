package filtering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import utils.BasicStats;

public class FilterOverview {
	public static void main(String[] args) throws IOException{
		String folderPath = "E:\\LKAS_data_test2";
		String savePath = folderPath + "\\curve";
		File folder = new File(folderPath + "\\integrated");
		File[] listOfFiles = folder.listFiles();
		
		// File Writer
		FileWriter writer = new FileWriter(savePath + "\\" + "filter_result_cumulated" + ".csv");
		FileWriter writer2 = new FileWriter(savePath + "\\" + "filter_resuilt_not_cumulated" + ".csv");
		FileWriter writer3 = new FileWriter(savePath + "\\" + "filter_result_lkas_state" + ".csv");
				
				
		writer.append("FileName" + "," + "startTime" + "," + "endTime" + "," + "duration" + "," + "filter" + "\n");
		writer2.append("FileName" + "," + "startTime" + "," + "endTime" + "," + "duration" + "\n");
		writer3.append("FileName" + "," + "startTime,endTime,duration,lkasState" + "\n");
		
		int CF_LKAS_SYS_STATE_idx = 0;	
		int CF_LEFTLINE_QUALITY_idx = 0;	
		int CF_RIGHTLINE_QUALITY_idx = 0;	
		int CF_MDPS_FAIL_STAT_idx = 0;		
		int CF_MDPS_TOI_FLT_idx = 0;		
		int LQUALITY_idx = 0;				
		int RQUALITY_idx = 0;
		int POSITION_RIGHT_idx = 0;

		
		// regular variables
		float time = 0;
		float speed = 0;
		float duration = 0;
		float startTime = 0;
		float endTime = 0;
		boolean isError = false;
		float duration2 = 0;
		
		
		float CF_LKAS_SYS_STATE = 0; 		// 3, 4	
		float CF_LEFTLINE_QUALITY = 0;	// 2, 3
		float CF_RIGHTLINE_QUALITY = 0;	// 2, 3
		float CF_MDPS_FAIL_STAT = 0;		// 0
		float CF_MDPS_TOI_FLT = 0;		// 0
		float LQUALITY = 0;				// 2, 3
		float RQUALITY = 0;				// 2, 3
		
		float CF_LKAS_SYS_STATE_startTime = 0;
		float CF_LKAS_SYS_STATE_endTime = 0;
		int CF_LKAS_SYS_STATE_cnt = 0;
		int CF_LKAS_SYS_STATE_cntChecker = 0;
				
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
		
		float speed_startTime = 0;
		float speed_endTime = 0;
		int speed_cnt = 0;
		
		for(int j = 0; j < listOfFiles.length; j++){
			File f = new File(listOfFiles[j].toString());
			String fileName = listOfFiles[j].toString();
			String[] fileNameSplit = fileName.split("\\.");
			fileNameSplit = fileNameSplit[0].split("\\\\");
			fileName = fileNameSplit[fileNameSplit.length-1];
			
			ArrayList<Float> POSITION_RIGHT_DIFF = new ArrayList<Float>();
			ArrayList<Float> POSITION_RIGHT = new ArrayList<Float>();
			ArrayList<Float> POSITION_RIGHT_SORT = new ArrayList<Float>();
			ArrayList<Float> TIME = new ArrayList<Float>();
			
			float[][] LkasSysState4 = new float[2][1000];
			int LkasSysStateCnt = 0;
			
			float position_right_avg = 0;
			
			
			
			System.out.println(j + ": " + fileName);
			
			// Handling the last section where not added in previous loop
			if(CF_LKAS_SYS_STATE_cnt > 0){
				duration = CF_LKAS_SYS_STATE_endTime - CF_LKAS_SYS_STATE_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + CF_LKAS_SYS_STATE_startTime + "," + CF_LKAS_SYS_STATE_endTime + "," + duration + "," + "CF_LKAS_SYS_STATE" + "\n");
			}
			
			if(CF_LEFTLINE_QUALITY_cnt > 0){
				duration = CF_LEFTLINE_QUALITY_endTime - CF_LEFTLINE_QUALITY_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + CF_LEFTLINE_QUALITY_startTime + "," + CF_LEFTLINE_QUALITY_endTime + "," + duration + "," + "CF_LEFTLINE_QUALITY" + "\n");
			}
			
			if(CF_RIGHTLINE_QUALITY_cnt > 0){
				duration = CF_RIGHTLINE_QUALITY_endTime - CF_RIGHTLINE_QUALITY_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + CF_RIGHTLINE_QUALITY_startTime + "," + CF_RIGHTLINE_QUALITY_endTime + "," + duration + "," + "CF_RIGHTLINE_QUALITY" + "\n");
			}
			
			if(LQUALITY_cnt > 0){
				duration = LQUALITY_endTime - LQUALITY_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + LQUALITY_startTime + "," + LQUALITY_endTime + "," + duration + "," + "LQUALITY" + "\n");
			}
			
			if(RQUALITY_cnt > 0){
				duration = RQUALITY_endTime - RQUALITY_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + RQUALITY_startTime + "," + RQUALITY_endTime + "," + duration + "," + "RQUALITY" + "\n");
			}
			
			if(CF_MDPS_FAIL_STAT_cnt > 0){
				duration = CF_MDPS_FAIL_STAT_endTime - CF_MDPS_FAIL_STAT_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + CF_MDPS_FAIL_STAT_startTime + "," + CF_MDPS_FAIL_STAT_endTime + "," + duration + "," + "CF_MDPS_FAIL_STAT" + "\n");
			}
			
			if(CF_MDPS_TOI_FLT_cnt > 0){
				duration = CF_MDPS_TOI_FLT_endTime - CF_MDPS_TOI_FLT_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + CF_MDPS_TOI_FLT_startTime + "," + CF_MDPS_TOI_FLT_endTime + "," + duration + "," + "CF_MDPS_TOI_FLT" + "\n");
			}
			
			if(speed_cnt > 0){
				duration = speed_endTime - speed_startTime;
				String temp = listOfFiles[j-1].toString();
				String[] tempSplit = temp.split("\\.");
				tempSplit = tempSplit[0].split("\\\\");
				temp = tempSplit[tempSplit.length-1];
				writer.append(temp + "," + speed_startTime + "," + speed_endTime + "," + duration + "," + "SPEED < 60km/h" + "\n");
			}
			
			
			
			CF_LKAS_SYS_STATE_startTime = 0;
			CF_LKAS_SYS_STATE_endTime = 0;
			CF_LKAS_SYS_STATE_cnt = 0;
			CF_LKAS_SYS_STATE_cntChecker = 0;
		
			CF_LEFTLINE_QUALITY_startTime = 0;
			CF_LEFTLINE_QUALITY_endTime = 0;
			CF_LEFTLINE_QUALITY_cnt = 0;
			
			CF_RIGHTLINE_QUALITY_startTime = 0;
			CF_RIGHTLINE_QUALITY_endTime = 0;
			CF_RIGHTLINE_QUALITY_cnt = 0;
			
			LQUALITY_startTime = 0;
			LQUALITY_endTime = 0;
			LQUALITY_cnt = 0;
			
			RQUALITY_startTime = 0;
			RQUALITY_endTime = 0;
			RQUALITY_cnt = 0;
			
			CF_MDPS_FAIL_STAT_startTime = 0;
			CF_MDPS_FAIL_STAT_endTime = 0;
			CF_MDPS_FAIL_STAT_cnt = 0;
			
			CF_MDPS_TOI_FLT_startTime = 0;
			CF_MDPS_TOI_FLT_endTime = 0;
			CF_MDPS_TOI_FLT_cnt = 0;
			
			speed_startTime = 0;
			speed_endTime = 0;
			speed_cnt = 0;
			
			//System.out.println(j + ": " + CF_GWAY_HAZARD_SW_cnt);
			
			String column[];
			int timeIdx = 0;

			int speedIdx = 0;
						
			
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
						
						if(column[i].equals("WHLSPDFL")){
							speedIdx = i;
						}
						
						if(column[i].equals("POSITIONRIGHT")){
							POSITION_RIGHT_idx = i;
						}
						
						if(column[i].equals("CFLKASSYSSTATE")){
							CF_LKAS_SYS_STATE_idx = i;
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
					}
					
					int row = 0;
					float POSITION_RIGHT_PREV = 0;
					
					float start = 0;
					float end = 0;
					float dur = 0;
					float state = 0;
					
					TIME.add((float)0.0);
					POSITION_RIGHT_DIFF.add((float)0.0);
					POSITION_RIGHT.add((float)0.0);
					
					while((line = inputStream.readLine()) != null){
						isError = false;
						row++;
						
						parseLine = line.split(",");
						
						time = Float.valueOf(parseLine[timeIdx]);
						speed = Float.valueOf(parseLine[speedIdx]);
						
						CF_LKAS_SYS_STATE = Float.valueOf(parseLine[CF_LKAS_SYS_STATE_idx]);
						CF_LEFTLINE_QUALITY = Float.valueOf(parseLine[CF_LEFTLINE_QUALITY_idx]);
						CF_RIGHTLINE_QUALITY = Float.valueOf(parseLine[CF_RIGHTLINE_QUALITY_idx]);
						CF_MDPS_FAIL_STAT = Float.valueOf(parseLine[CF_MDPS_FAIL_STAT_idx]);
						CF_MDPS_TOI_FLT = Float.valueOf(parseLine[CF_MDPS_TOI_FLT_idx]);
						LQUALITY = Float.valueOf(parseLine[LQUALITY_idx]);
						RQUALITY = Float.valueOf(parseLine[RQUALITY_idx]);
						
						if(row == 1){
							POSITION_RIGHT_PREV = Float.valueOf(parseLine[POSITION_RIGHT_idx]);
							POSITION_RIGHT_DIFF.add((float)0.0);
							
						}else{
							POSITION_RIGHT_DIFF.add(Math.abs(Float.valueOf(parseLine[POSITION_RIGHT_idx]) - POSITION_RIGHT_PREV));
							POSITION_RIGHT_PREV = Float.valueOf(parseLine[POSITION_RIGHT_idx]);
						}
						TIME.add(time);
						POSITION_RIGHT.add(Float.valueOf(parseLine[POSITION_RIGHT_idx]));
						
						
						/* Record the lkas state from & to
						 * startTime, endTime, Duration, whether 3 or else
						 * 
						 * */
						
												
						if(row == 1){
							start = time;
							end = time;
							state = CF_LKAS_SYS_STATE;
							dur = end-start;
						}else{
							
							if(state == CF_LKAS_SYS_STATE){
								end = time;
								dur = end-start;
							}else{
								writer3.append(fileName + "," + start + "," + end + "," + dur + "," + state + "\n");
								start = time;
								end = time;
								dur = end-start;
								state = CF_LKAS_SYS_STATE;
							}
						}
						
						
						
						
						
						/*
						 * ERROR #1: CF_LKAS_SYS_STATE
						 * 
						 * */
						//if(CF_LKAS_SYS_STATE != 3 && CF_LKAS_SYS_STATE != 4){
						//if(CF_LKAS_SYS_STATE == 0 || CF_LKAS_SYS_STATE == 2 || CF_LKAS_SYS_STATE == 14 || CF_LKAS_SYS_STATE == 15){
						//if(CF_LKAS_SYS_STATE == 5 || CF_LKAS_SYS_STATE == 6 || CF_LKAS_SYS_STATE == 7 || CF_LKAS_SYS_STATE == 8){
						if(CF_LKAS_SYS_STATE != 3){
							isError = true;
							if(CF_LKAS_SYS_STATE_cnt == 0){ // start of this error
								CF_LKAS_SYS_STATE_startTime = time;
							}else{
								
							}
							
							CF_LKAS_SYS_STATE_endTime = time;
							CF_LKAS_SYS_STATE_cnt++;
						}else if(CF_LKAS_SYS_STATE_cnt > 0){
							/*
							CF_LKAS_SYS_STATE_cntChecker++;
							if(CF_LKAS_SYS_STATE_cntChecker < 200){
								CF_LKAS_SYS_STATE_endTime = time;
								CF_LKAS_SYS_STATE_cnt++;
								continue;
							}
							*/
							
							duration = CF_LKAS_SYS_STATE_endTime - CF_LKAS_SYS_STATE_startTime;
							
							if(CF_LKAS_SYS_STATE == 4){
								LkasSysState4[0][LkasSysStateCnt] = CF_LKAS_SYS_STATE_startTime;
								LkasSysState4[1][LkasSysStateCnt] = CF_LKAS_SYS_STATE_endTime;
								LkasSysStateCnt++;
							}
							writer.append(fileName + "," + CF_LKAS_SYS_STATE_startTime + "," + CF_LKAS_SYS_STATE_endTime + "," + duration + "," + "1CF_LKAS_SYS_STATE" + "\n");
							
							CF_LKAS_SYS_STATE_startTime = 0;
							CF_LKAS_SYS_STATE_endTime = 0;
							CF_LKAS_SYS_STATE_cnt = 0;
							CF_LKAS_SYS_STATE_cntChecker = 0;
							//System.out.println(filterReason);
						}else{
							
						}
						
						/*
						 * ERROR #3: CF_LEFTLINE_QUALITY
						 * 
						 * */
						
						if(CF_LEFTLINE_QUALITY != 2 && CF_LEFTLINE_QUALITY != 3){
							isError = true;
							if(CF_LEFTLINE_QUALITY_cnt == 0){ // start of this error
								CF_LEFTLINE_QUALITY_startTime = time;
							}else{
								
							}
							
							CF_LEFTLINE_QUALITY_endTime = time;
							CF_LEFTLINE_QUALITY_cnt++;
						}else if(CF_LEFTLINE_QUALITY_cnt > 0){
							duration = CF_LEFTLINE_QUALITY_endTime - CF_LEFTLINE_QUALITY_startTime;
							writer.append(fileName + "," + CF_LEFTLINE_QUALITY_startTime + "," + CF_LEFTLINE_QUALITY_endTime + "," + duration + "," + "CF_LEFTLINE_QUALITY" + "\n");
							
							CF_LEFTLINE_QUALITY_startTime = 0;
							CF_LEFTLINE_QUALITY_endTime = 0;
							CF_LEFTLINE_QUALITY_cnt = 0;
						}else{
							
						}
						
						/*
						 * ERROR #4: CF_RIGHTLINE_QUALITY
						 * 
						 * */
						
						if(CF_RIGHTLINE_QUALITY != 2 && CF_RIGHTLINE_QUALITY != 3){
							isError = true;
							if(CF_RIGHTLINE_QUALITY_cnt == 0){ // start of this error
								CF_RIGHTLINE_QUALITY_startTime = time;
							}else{
								
							}
							
							CF_RIGHTLINE_QUALITY_endTime = time;
							CF_RIGHTLINE_QUALITY_cnt++;
						}else if(CF_RIGHTLINE_QUALITY_cnt > 0){
							duration = CF_RIGHTLINE_QUALITY_endTime - CF_RIGHTLINE_QUALITY_startTime;
							writer.append(fileName + "," + CF_RIGHTLINE_QUALITY_startTime + "," + CF_RIGHTLINE_QUALITY_endTime + "," + duration + "," + "CF_RIGHTLINE_QUALITY" + "\n");
							
							CF_RIGHTLINE_QUALITY_startTime = 0;
							CF_RIGHTLINE_QUALITY_endTime = 0;
							CF_RIGHTLINE_QUALITY_cnt = 0;
						}else{
							
						}
						
						/*
						 * ERROR #5: CF_MDPS_FAIL_STAT
						 * 
						 * */
						
						if(CF_MDPS_FAIL_STAT != 0){
							isError = true;
							if(CF_MDPS_FAIL_STAT_cnt == 0){ // start of this error
								CF_MDPS_FAIL_STAT_startTime = time;
							}else{
								
							}
							
							CF_MDPS_FAIL_STAT_endTime = time;
							CF_MDPS_FAIL_STAT_cnt++;
						}else if(CF_MDPS_FAIL_STAT_cnt > 0){
							duration = CF_MDPS_FAIL_STAT_endTime - CF_MDPS_FAIL_STAT_startTime;
							writer.append(fileName + "," + CF_MDPS_FAIL_STAT_startTime + "," + CF_MDPS_FAIL_STAT_endTime + "," + duration + "," + "CF_MDPS_FAIL_STAT" + "\n");
							
							CF_MDPS_FAIL_STAT_startTime = 0;
							CF_MDPS_FAIL_STAT_endTime = 0;
							CF_MDPS_FAIL_STAT_cnt = 0;
						}else{
							
						}
						
						/*
						 * ERROR #6: CF_MDPS_TOI_FLT
						 * 
						 * */
						
						if(CF_MDPS_TOI_FLT != 0){
							isError = true;
							if(CF_MDPS_TOI_FLT_cnt == 0){ // start of this error
								CF_MDPS_TOI_FLT_startTime = time;
							}else{
								
							}
							
							CF_MDPS_TOI_FLT_endTime = time;
							CF_MDPS_TOI_FLT_cnt++;
						}else if(CF_MDPS_TOI_FLT_cnt > 0){
							duration = CF_MDPS_TOI_FLT_endTime - CF_MDPS_TOI_FLT_startTime;
							writer.append(fileName + "," + CF_MDPS_TOI_FLT_startTime + "," + CF_MDPS_TOI_FLT_endTime + "," + duration + "," + "CF_MDPS_TOI_FLT" + "\n");
							
							CF_MDPS_TOI_FLT_startTime = 0;
							CF_MDPS_TOI_FLT_endTime = 0;
							CF_MDPS_TOI_FLT_cnt = 0;
						}else{
							
						}
						
						/*
						 * ERROR #7: LQUALITY
						 * 
						 * */
						
						if(LQUALITY != 2 && LQUALITY != 3){
							isError = true;
							if(LQUALITY_cnt == 0){ // start of this error
								LQUALITY_startTime = time;
							}else{
								
							}
							
							LQUALITY_endTime = time;
							LQUALITY_cnt++;
						}else if(LQUALITY_cnt > 0){
							duration = LQUALITY_endTime - LQUALITY_startTime;
							writer.append(fileName + "," + LQUALITY_startTime + "," + LQUALITY_endTime + "," + duration + "," + "LQUALITY" + "\n");
							
							LQUALITY_startTime = 0;
							LQUALITY_endTime = 0;
							LQUALITY_cnt = 0;
						}else{
							
						}
						
						/*
						 * ERROR #8: RQUALITY
						 * 
						 * */
						
						if(RQUALITY != 2 && RQUALITY != 3){
							isError = true;
							if(RQUALITY_cnt == 0){ // start of this error
								RQUALITY_startTime = time;
							}else{
								
							}
							
							RQUALITY_endTime = time;
							RQUALITY_cnt++;
						}else if(RQUALITY_cnt > 0){
							duration = RQUALITY_endTime - RQUALITY_startTime;
							writer.append(fileName + "," + RQUALITY_startTime + "," + RQUALITY_endTime + "," + duration + "," + "RQUALITY" + "\n");
							
							RQUALITY_startTime = 0;
							RQUALITY_endTime = 0;
							RQUALITY_cnt = 0;
						}else{
							
						}
						
						/*
						 * ERROR #9: SPEED less than 60km/h
						 * 
						 * */
						
						if(speed < 60){
							isError = true;
							if(speed_cnt == 0){ // start of this error
								speed_startTime = time;
							}else{
								
							}
							
							speed_endTime = time;
							speed_cnt++;
						}else if(speed_cnt > 0){
							duration = speed_endTime - speed_startTime;
							writer.append(fileName + "," + speed_startTime + "," + speed_endTime + "," + duration + "," + "SPEED < 60km/h" + "\n");
							
							speed_startTime = 0;
							speed_endTime = 0;
							speed_cnt = 0;
						}else{
							
						}
						
						if(isError){
							startTime = Float.valueOf(parseLine[timeIdx]);
							writer2.append(fileName + "," + startTime + "\n");	
						}
						
						
					}
				}finally{
					inputStream.close();
				}
				
				/*
				for(int i = 0; i < TIME.size(); i++){
					writer.append(TIME.get(i) + "," + POSITION_RIGHT.get(i) + "," + POSITION_RIGHT_DIFF.get(i) + "\n");
				}
				*/
				
				POSITION_RIGHT_SORT.addAll(POSITION_RIGHT);
				Collections.sort(POSITION_RIGHT_SORT);
				
				int bottomIdx, topIdx = 0;
				float bottomValue, topValue = 0;
				
				bottomIdx = POSITION_RIGHT_SORT.size()*10/100;
				topIdx = POSITION_RIGHT_SORT.size()*90/100;
				
				bottomValue = POSITION_RIGHT_SORT.get(bottomIdx);
				topValue = POSITION_RIGHT_SORT.get(topIdx);
				
				//System.out.println("Bottom: " + POSITION_RIGHT_SORT.get(bottomIdx) + ", Top: " + POSITION_RIGHT_SORT.get(topIdx));
				
				for(int i = 0; i < POSITION_RIGHT_DIFF.size(); i++){
					float startError = 0;
					float endError = 0;
					
					if(POSITION_RIGHT_DIFF.get(i) > 3){ // if changing lane is detected
						for(int k = 0; k < LkasSysStateCnt; k++){
							if(TIME.get(i) >= LkasSysState4[0][k] && TIME.get(i) <= LkasSysState4[1][k]){
								startError = LkasSysState4[0][k];
								endError = LkasSysState4[1][k];
								break;
							}
						}
						/*
						// if changing lane (-) --> (+)
						if(POSITION_RIGHT.get(i) > POSITION_RIGHT.get(i-1)){
							// find the start position of changing lane
							for(int k = i-1; k > 0; k--){
								//if(POSITION_RIGHT.get(k) > position_right_avg){
								//if(POSITION_RIGHT.get(k) > POSITION_RIGHT.get(k-1)){
								if(POSITION_RIGHT.get(k) > bottomValue){
									startError = TIME.get(k);
									break;
								}
							}
							
							// find the end position of changing lane
							for(int k = i; k < POSITION_RIGHT.size(); k++){
								//if(POSITION_RIGHT.get(k) < position_right_avg){
								//if(POSITION_RIGHT.get(k) < POSITION_RIGHT.get(k+1)){
								if(POSITION_RIGHT.get(k) < topValue){
									endError = TIME.get(k);
									break;
								}
							}
						}else{ // if changing lane (+) --> (-)
							// find the start position of changing lane
							for(int k = i-1; k > 0; k--){
								//if(POSITION_RIGHT.get(k) < position_right_avg){
								//if(POSITION_RIGHT.get(k) < POSITION_RIGHT.get(k-1)){
								if(POSITION_RIGHT.get(k) < topValue){
									startError = TIME.get(k);
									break;
								}
							}
							
							// find the end position of changing lane
							for(int k = i; k < POSITION_RIGHT.size(); k++){
								//if(POSITION_RIGHT.get(k) > position_right_avg){
								//if(POSITION_RIGHT.get(k) > POSITION_RIGHT.get(k+1)){
								if(POSITION_RIGHT.get(k) > bottomValue){
									endError = TIME.get(k);
									break;
								}
							}
						}	
						*/					
					}
					
					if(startError != 0 && endError != 0){
						float duration3 = endError - startError;
						//System.out.println("Start: " + startError + ", End:" + endError + ", Duration: " + duration3);
						writer.append(fileName + "," + startError + "," + endError + "," + duration3 + "," + "lane_width" + "\n");
						//System.out.println("" + startError + "," + endError + "," + duration3);
					}
				}
				
				
				//System.out.println("POSITION_RIGHT (avg): " + position_right_avg);
			}
		}
		
		String temp = listOfFiles[listOfFiles.length-1].toString();
		String[] tempSplit = temp.split("\\.");
		tempSplit = tempSplit[0].split("\\\\");
		temp = tempSplit[tempSplit.length-1];
		
		if(CF_LKAS_SYS_STATE_cnt > 0){
			duration = CF_LKAS_SYS_STATE_endTime - CF_LKAS_SYS_STATE_startTime;
			writer.append(temp + "," + CF_LKAS_SYS_STATE_startTime + "," + CF_LKAS_SYS_STATE_endTime + "," + duration + "," + "CF_LKAS_SYS_STATE" + "\n");
		}
		
		if(CF_LEFTLINE_QUALITY_cnt > 0){
			duration = CF_LEFTLINE_QUALITY_endTime - CF_LEFTLINE_QUALITY_startTime;
			writer.append(temp + "," + CF_LEFTLINE_QUALITY_startTime + "," + CF_LEFTLINE_QUALITY_endTime + "," + duration + "," + "CF_LEFTLINE_QUALITY" + "\n");
		}
		
		if(CF_RIGHTLINE_QUALITY_cnt > 0){
			duration = CF_RIGHTLINE_QUALITY_endTime - CF_RIGHTLINE_QUALITY_startTime;
			writer.append(temp + "," + CF_RIGHTLINE_QUALITY_startTime + "," + CF_RIGHTLINE_QUALITY_endTime + "," + duration + "," + "CF_RIGHTLINE_QUALITY" + "\n");
		}
		
		if(LQUALITY_cnt > 0){
			duration = LQUALITY_endTime - LQUALITY_startTime;
			writer.append(temp + "," + LQUALITY_startTime + "," + LQUALITY_endTime + "," + duration + "," + "LQUALITY" + "\n");
		}
		
		if(RQUALITY_cnt > 0){
			duration = RQUALITY_endTime - RQUALITY_startTime;
			writer.append(temp + "," + RQUALITY_startTime + "," + RQUALITY_endTime + "," + duration + "," + "RQUALITY" + "\n");
		}
		
		if(CF_MDPS_FAIL_STAT_cnt > 0){
			duration = CF_MDPS_FAIL_STAT_endTime - CF_MDPS_FAIL_STAT_startTime;
			writer.append(temp + "," + CF_MDPS_FAIL_STAT_startTime + "," + CF_MDPS_FAIL_STAT_endTime + "," + duration + "," + "CF_MDPS_FAIL_STAT" + "\n");
		}
		
		if(CF_MDPS_TOI_FLT_cnt > 0){
			duration = CF_MDPS_TOI_FLT_endTime - CF_MDPS_TOI_FLT_startTime;
			writer.append(temp + "," + CF_MDPS_TOI_FLT_startTime + "," + CF_MDPS_TOI_FLT_endTime + "," + duration + "," + "CF_MDPS_TOI_FLT" + "\n");
		}
		
		if(speed_cnt > 0){
			duration = speed_endTime - speed_startTime;
			writer.append(temp + "," + speed_startTime + "," + speed_endTime + "," + duration + "," + "SPEED < 60km/h" + "\n");
		}
		
		if(isError){
			writer2.append(temp + "," + startTime + "\n");	
		}
		
		writer.close();
		writer2.close();
		writer3.close();
		
		
		System.out.println("End of Program");
	}
	
	
}
