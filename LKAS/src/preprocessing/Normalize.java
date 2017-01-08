package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.BasicStats;

public class Normalize {
	public static void main(String[] args) throws IOException{
		int MAXROW = 10000000;
		
	
		String fileName = "";
		
		fileName = "overall";
		
		
		//String fileName = "overall";
		
		
		// File & load csv
		String folderPath = "E:\\LKAS_data_test";
		String findPath = folderPath + "\\curve\\new2";
		String savePath = folderPath + "\\normalize\\";
		File folder = new File(findPath);
		File[] listOfFiles = folder.listFiles();
		

//		for(int j = 0; j < listOfFiles.length; j++){
			
			//File f2 = new File(listOfFiles[j].toString());
			//String fileName = listOfFiles[j].toString();
			//String[] fileNameSplit = fileName.split("\\.");
			//fileNameSplit = fileNameSplit[0].split("\\\\");
			//fileName = fileNameSplit[fileNameSplit.length-1];
		
		
		
		
		File f2 = new File(folderPath + "\\curve\\" + fileName + ".csv");
			
			FileWriter writer = new FileWriter(savePath + "\\" + fileName + "_nrml.csv");
			
			if(f2.isFile()){
				BufferedReader inputStream = null;
				String line;
				String[] parseLine;
				String[] column;
				
				try{					
					inputStream = new BufferedReader(new FileReader(f2));
					line = inputStream.readLine();
					
					column = line.split(",");
					for(int i = 0; i < column.length; i++){
						writer.append(column[i] + ",");
					}
					writer.append("\n");
					
					int filename_idx = 0;
					int type_idx = 0;
					int time_idx = 0;
					for(int i = 0; i < column.length; i++){
						if(column[i].equals("FILENAME")){
							filename_idx = i;
						}
						
						if(column[i].equals("CurveStraight")){
							type_idx = i;
						}
						
						if(column[i].equals("TIMES")){
							time_idx = i;
						}
					}
					
					int collen = 0;
					int row = 0;
					float[][] data;
					float[][] result;
					String[] type;
					String[] times;
					String[] filenames;

					
					collen = column.length;
					data = new float[collen][MAXROW];
					result = new float[collen][MAXROW];
					type = new String[MAXROW];
					times = new String[MAXROW];
					filenames = new String[MAXROW];

					row = 0;
					
					while((line = inputStream.readLine()) != null){

						parseLine = line.split(",");
						type[row] = parseLine[type_idx];
						times[row] = parseLine[time_idx];
						filenames[row] = parseLine[filename_idx];
						
						if(type[row].equals("Outlier")){
							continue;
						}
						
						for(int i = 0; i < parseLine.length; i++){
							if(i == filename_idx || i == type_idx || i == time_idx){
								
							}else{
								data[i][row] = Float.parseFloat(parseLine[i]);	
							}
							
							//System.out.print(data[i][row] + ", ");
						} 
						//System.out.println("");
						
						row++;
					}
					
					for(int i = 0; i < collen; i++){
						
						float[] temp = new float[row];
						for(int z = 0; z < row; z++){
							temp[z] = data[i][z];
						}
						
						temp = normalize(temp);
						
						for(int z = 0; z < row; z++){
							result[i][z] = temp[z];
						}

					}
					
					
					for(int z = 0; z < row; z++){
												
						for(int i = 0; i < collen; i++){
							if(i == type_idx){
								writer.append(type[z] + ",");
							}else if(i == time_idx){
								writer.append(times[z] + ",");
							}else if(i == filename_idx){
								writer.append(filenames[z] + ",");
							}else{
								writer.append(result[i][z] + ",");
							}
						}
						writer.append("\n");
					}
					
					writer.close();
					
				}finally{
					inputStream.close();
				}
			}
		
			System.out.println("end of file");
		//}
		
	}
	
	public static float[] normalize(float[] array){
		
		float avg = getAvg(array);
		float stdev = getStddev(array);

		float[] result = new float[array.length];

		for(int i = 0; i < array.length; i++){
			result[i] = ((array[i] - avg)/stdev);
		}
		
		return result; 
	}
	
	private static float getAvg(float[] array){
		float result = 0;
		for(int i = 0; i < array.length; i++){
			result = result + array[i];
		}
		result = result/array.length;
		
		return result;
	}
	
	private static float getStddev(float[] array){
		float result = 0;
		float avg = getAvg(array);
		
		for(int i = 0; i < array.length; i++){
			result = (float) (result + Math.pow((array[i] - avg), 2));
		}
		
		result = result / array.length;
		return (float) Math.sqrt(result);
		
		
	}
}
