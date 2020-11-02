package com.springbatch.xmlparsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.springbatch.csvmodel.ParsingModel;

public class ValidationProcessor implements ItemProcessor<ParsingModel, ParsingModel> {
	
	 

		    @Autowired
		    MultiResourceItemReader<ParsingModel> multiResourceItemReader;

		    private String fileName;
			String delim = ";";


            private String extension = "";
            ArrayList<String> companies =new ArrayList<String>();
            ArrayList<String> news =new ArrayList<String>();



	@Override
	public ParsingModel process(ParsingModel item) throws Exception {

        if(multiResourceItemReader.getCurrentResource()!=null){
            fileName =  multiResourceItemReader.getCurrentResource().getFilename();
            int i = fileName.lastIndexOf('.');
            if (i >= 0) { 
            	extension = fileName.substring(i+1); 
            	if(extension.contains("csv")) {
            		
            		
            		String splitData[]=item.getInput().split(";",2);
            		//System.out.println(splitData[1]);
            		companies.add(splitData[1]);
            		
            		
            	}
            	else if(extension.contains("xml")) {
            		news.add(item.getInput());           		
            	}
            }
           
        }
		// TODO Auto-generated method stub
			
			//System.out.println(item.getInput());

		


		return item;	

	}
}
