package com.passage.manipulation.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataOperationsUtil {
	
	public static Map<String,List<Map<String,Integer>>> searchWords(String[] words, String passageDate){
		Map<String,List<Map<String,Integer>>> countMap = new HashMap<String, List<Map<String,Integer>>>();
		
		List<Map<String,Integer>> wordCountList = new ArrayList<Map<String,Integer>>();
		
		for(String word:words){
				
				int count = 0;
				Pattern p = Pattern.compile(word.toLowerCase());
				Matcher m = p.matcher( passageDate.toLowerCase() );
				while (m.find()) {
					count++;
				}
			
				
				Map<String,Integer> wordWithCount = new HashMap<String, Integer>();				
				wordWithCount.put(word, count);				
				wordCountList.add(wordWithCount);
				
				
		}
		countMap.put("counts", wordCountList);
		
	return countMap;
	
	}

	public static Map<String, Integer> getHighestCountText(
			int size, String passageData) {
		Map<String,Integer> wordcountMap = new HashMap<String, Integer>();
		Map<String, Integer> topWordsWithCount = new LinkedHashMap<String, Integer>();
		
		String[] words = passageData.toLowerCase().split("[ \n\t\r.,;:!?(){}]+");
		
		List<String> wordListSubStrings = new ArrayList<String>();
		for(String word:words){

			for(int c = 0 ; c < word.length() ; c++ ){
				for(int i = 1; i <= word.length() - c ; i++ ){
					if((c+i - c) >1){
					String subString = word.substring(c, c+i);
					wordListSubStrings.add(subString);
					}
			
		}
	}

		}
		
		for(String word:wordListSubStrings){
			
			if(wordcountMap.get(word) == null){
			  wordcountMap.put(word, 1);
			}else{
				
				wordcountMap.put(word, wordcountMap.get(word)+1);
			}
			
			
		}
		
		Set<Entry<String, Integer>> set = wordcountMap.entrySet();
	       List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
	       Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
	       {
	           public int compare( Map.Entry<String, Integer> object1, Map.Entry<String, Integer> object2 )
	           {
	               return (object2.getValue()).compareTo( object1.getValue() );
	           }
	       } );
	      list = list.subList(0, size);
	       for(Map.Entry<String, Integer> entry:list){
	                 topWordsWithCount.put(entry.getKey(), entry.getValue());
	       }
	
		return topWordsWithCount;
	}		
	
	}

