package com.springbatch.csvmodel;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.springbatch.csvmodel.ParsingModel;
public class InputFieldSetMapper implements FieldSetMapper<ParsingModel> {
	
	@Override
	public ParsingModel mapFieldSet(FieldSet fieldSet) throws BindException {
		//@// @formatter:off
		return ParsingModel.builder()
				.input(fieldSet.readString("input"))
				//.compName(fieldSet.readString("compName"))				
				.build(); 
		// @formatter:on
	}
}
