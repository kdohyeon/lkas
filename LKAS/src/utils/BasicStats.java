package utils;

/*
 * Author: Dohyeon Kim
 * Date: 2015-12-28
 * Updated: 1
 * Description: basic stats using an arraylist
 * Input: an arraylist with Float data type elements
 * Output: basic stats, i.e. avg, stddev, max, min, so on...
 * */

import java.util.*;

public class BasicStats {
	ArrayList<Float> FloatAL = new ArrayList<Float>();
	
	public BasicStats(ArrayList<Float> arraylist)
	{
		FloatAL = arraylist;
	}
	
	public Float getMedian(){
		float result = 0;
		
		Collections.sort(FloatAL);
		
		if(FloatAL.size() % 2 == 0){
			int temp_1 = FloatAL.size()/2;
			int temp_2 = temp_1 - 1;
			result = (FloatAL.get(temp_1) + FloatAL.get(temp_2))/2;
		}else{
			result = FloatAL.get((FloatAL.size()-1)/2);
		}
		
		return result;
	}

	public Float getMaxNum()
	{
		float result = 0;
		
		result = FloatAL.get(0);
		for(int i = 1; i < FloatAL.size(); i++)
		{
			if(result < FloatAL.get(i))
			{
				result = FloatAL.get(i);
			}
		}
		
		return result;
	}
	
	public int getMaxNumIndex()
	{
		int result = 0;
		float value = 0;
		
		value = FloatAL.get(0);
				
		for(int i = 1; i < FloatAL.size(); i++)
		{
			if(value < FloatAL.get(i))
			{
				value = FloatAL.get(i);
				result = i;
			}
		}
		
		
		return result;
	}
	
	public Float getMinNum()
	{
		float result = 0;
		
		result = FloatAL.get(0);
		for(int i = 1; i < FloatAL.size(); i++)
		{
			if(result > FloatAL.get(i))
			{
				result = FloatAL.get(i);
			}
		}
		
		return result;
	}
	
	/* Get standard deviation of an arraylist */
	public Float getStddev(){
		float result = 0;
		float avg = getAvg();
		
		for(int i = 0; i < FloatAL.size(); i++){
			result = (float) (result + Math.pow((FloatAL.get(i) - avg), 2));
		}
		
		result = result / FloatAL.size();
		
		return (float) Math.sqrt(result);
	}
	
	/* Get average of an arraylist */
	public Float getAvg(){
		float result = 0;
		result = getSum();
		
		return result / FloatAL.size();
	}
	
	/* Get sum of an arraylist */
	public Float getSum(){
		float result = 0;
		for(int i = 0; i < FloatAL.size(); i++){
			result = result + FloatAL.get(i);
		}
		
		return result;
	}
	
}
