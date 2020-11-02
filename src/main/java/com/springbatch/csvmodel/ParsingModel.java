package com.springbatch.csvmodel;

import javax.annotation.Resource;

import org.springframework.batch.item.ResourceAware;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ParsingModel implements ResourceAware {
	
		private String input;
		//private String compName;
		private Resource resource;
		

	
	@Override
    public String toString() {
        return "input" + input + "";
        		//+ " companyName=" + compName + "";
    }



	@Override
	public void setResource(org.springframework.core.io.Resource resource) {
		// TODO Auto-generated method stub
		
	}
   

   

}
