package com.gary.config;

import com.gary.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Employee> itemReader,
                   ItemProcessor<Employee, Employee> itemProcessor,
                   ItemWriter<Employee> itemWriter) {
        Step step = stepBuilderFactory.get("ETL-STEP")
                .<Employee, Employee>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        Job job = jobBuilderFactory.get("ETL-JOB")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

        return job;
    }

    @Bean
    public FlatFileItemReader<Employee> itemReader(@Value("${input}") Resource resource) {
        FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("CSV-READER");
        flatFileItemReader.setLinesToSkip(1); // skip the header line
        flatFileItemReader.setLineMapper(lineMapper());

        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Employee> lineMapper() {
        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employee.class);

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[]{"id", "name", "dept", "salary"});

        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

//    @Bean
//    public StaxEventItemReader<Inventory> xmlItemReader() throws URISyntaxException {
//        File file = new File(getClass().getClassLoader().getResource("A6.xml").toURI());
//        return new StaxEventItemReaderBuilder<Inventory>().name("xmlItemReader")
//                .resource(new FileSystemResource(file))
//                .addFragmentRootElements("v")
//                .unmarshaller(vehicleMarshaller())
//                .build();
//    }
//
//    @Bean()
//    public XStreamMarshaller vehicleMarshaller() {
//        Map<String, Class> aliases = new HashMap<>();
//        aliases.put("v", Vehicle.class);
//        aliases.put("DealerID", String.class);
//        aliases.put("VIN", String.class);
//        aliases.put("StockID", String.class);
//        aliases.put("Year", String.class);
//        aliases.put("Make", String.class);
//        aliases.put("Model", String.class);
//        aliases.put("Body", String.class);
//        aliases.put("Trim", String.class);
//        aliases.put("Engine", String.class);
//        aliases.put("Transmission", String.class);
//        aliases.put("DriveTrain", String.class);
//        aliases.put("ExternalColor", String.class);
//        aliases.put("InteriorColor", String.class);
//        aliases.put("Mileage", String.class);
//        aliases.put("AskingPrice", String.class);
//        aliases.put("WebPrice", String.class);
//        aliases.put("Trim", String.class);
//        aliases.put("Trim", String.class);
//        aliases.put("Trim", String.class);
//        aliases.put("Trim", String.class);
//        aliases.put("Trim", String.class);
//        aliases.put("Trim", String.class);
//        aliases.put("Trim", String.class);
//
//
//
//
//    }
}
