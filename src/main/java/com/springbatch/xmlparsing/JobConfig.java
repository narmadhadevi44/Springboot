package com.springbatch.xmlparsing;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import com.opencsv.exceptions.CsvValidationException;
import com.springbatch.csvmodel.InputFieldSetMapper;
import com.springbatch.csvmodel.ParsingModel;
@Configuration
public class JobConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;


	@Value("file:C:/Users/muhil/Desktop/RepRisk/160502_data/data/*.*")
	private Resource[] inputFiles;
	@Bean
	public MultiResourceItemReader<ParsingModel> multiResourceItemreader() {
		MultiResourceItemReader<ParsingModel> reader = new MultiResourceItemReader<>();
		reader.setDelegate(inputItemReader());
		reader.setResources(inputFiles);
		return reader;
	}

	@Bean
	public ItemProcessor<ParsingModel, ParsingModel> itemProcessor() {
		return new ValidationProcessor();       
	}

	@Bean
	public FlatFileItemReader<ParsingModel> inputItemReader() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "input"});
		DefaultLineMapper<ParsingModel> inputLineMapper = new DefaultLineMapper<>();
		inputLineMapper.setLineTokenizer(tokenizer);
		inputLineMapper.setFieldSetMapper(new InputFieldSetMapper());
		inputLineMapper.afterPropertiesSet();
		FlatFileItemReader<ParsingModel> reader = new FlatFileItemReader<>();
		reader.setLineMapper(inputLineMapper);
		return reader;
	}
	@Bean
	public ItemWriter<ParsingModel> outItemWriter() throws FileNotFoundException, IOException, CsvValidationException{

		return outitems -> {
			ArrayList<String> csvList=csvReader();
			Iterator i = csvList.iterator();
			while (i.hasNext()) {
				if(org.apache.commons.lang3.StringUtils.contains(outitems.toString(), i.next().toString()))
				{
					System.out.println(i.next());

				}
			}

		};
	}


	public ArrayList<String>csvReader() throws IOException {
		String csvFile = "C:\\Users\\muhil\\Desktop\\RepRisk - Copy\\160502_data\\160408_company_list.csv";
		String line = "";
		String cvsSplitBy = ";";
		ArrayList<String> csvList=new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {

			// use comma as separator
			String[] company = line.split(cvsSplitBy);
			String companyName=company[1];
			csvList.add(companyName);               
		}
		return csvList;

	}


	@Bean
	public Step step1() throws FileNotFoundException, CsvValidationException, IOException {
		return stepBuilderFactory.get("step1")
				.<ParsingModel, ParsingModel>chunk(10)
				.reader(multiResourceItemreader())
				.writer(outItemWriter())
				.faultTolerant()
				.skipLimit(10000000)
				.skip(RuntimeException.class)
				.build();
	}







	@Bean
	public Job job() throws FileNotFoundException, CsvValidationException, IOException {
		return jobBuilderFactory.get("job")
				.start(step1())
				.build();
	}
}
