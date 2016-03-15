package com.passage.manipulation.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.passage.manipulation.model.Search;
import com.passage.manipulation.util.DataOperationsUtil;

@Controller
public class PassageProcessingController {
	
	
    public static final String GET_DATA = "/data/sample";
    public static final String SEARCH_DATA = "/search";
    public static final String TOP_DATA = "/top/{size}";
    
    
    @RequestMapping(value = GET_DATA, method = RequestMethod.GET )
    public @ResponseBody String getSampleData() {
    	 
    		InputStream stream =  this.getClass().getResourceAsStream("/sampleData.txt");
       
       HttpHeaders  responseHeaders = new HttpHeaders();
       responseHeaders.add("Content-Type", "text/html; charset=utf-8");
     
		return getDataFromInputStream(stream);
	
      
    }
    
    private static String getDataFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
   /* {
    	 "searchText":["Duis", "Sed"]
    	}*/
    
    @RequestMapping(value = SEARCH_DATA, method = RequestMethod.POST)
    public @ResponseBody Map<String,List<Map<String,Integer>>> searchText(@RequestBody Search searchModel) {
    	 HttpHeaders  responseHeaders = new HttpHeaders();
          responseHeaders.add("Content-Type", "application/json");
    	String[] searchwords = searchModel.getSearchText();
    	String passageDate =getSampleData();
    	return DataOperationsUtil.searchWords(searchwords,passageDate);
         
     
    }
    
    @RequestMapping(value = TOP_DATA, method = RequestMethod.POST,produces = "text/csv")
    public @ResponseBody void getHighestCountText(@PathVariable("size") int size,HttpServletResponse response) throws IOException {
    
    	 response.setContentType("text/csv; charset=utf-8");
      	String passageDate =getSampleData();
      	Map<String,Integer> topWordCountMap = DataOperationsUtil.getHighestCountText(size,passageDate);
      	
      
		String reportName = "Top_Results.csv";
		response.setHeader("Content-disposition", "attachment;filename="+reportName);
		try {
		ArrayList<String> rows = new ArrayList<String>();
		rows.add("Text,Count");
		rows.add("\n");
 
	
		for(Map.Entry<String,Integer> topWordCountEntry :topWordCountMap.entrySet()){
			 rows.add(topWordCountEntry.getKey()+","+(topWordCountEntry.getValue()));
			 rows.add("\n");
		}
		
 
		Iterator<String> iter = rows.iterator();
		while (iter.hasNext()) {
			String outputString = (String) iter.next();
			response.getOutputStream().print(outputString);
			
		}
 
		response.getOutputStream().flush();
      
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}