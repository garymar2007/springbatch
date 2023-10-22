package com.gary.config;

import com.gary.model.Vehicle;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.io.File;
import java.net.URISyntaxException;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    public static final String JOB_NAME = "Feed-File-Processing-Job";
    public static final String STEP_NAME = "Feed-File-Processing-Step";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(ItemReader<Vehicle> itemReader,
                   ItemProcessor<Vehicle, Vehicle> itemProcessor,
                   ItemWriter<Vehicle> itemWriter) {
        Step step = stepBuilderFactory.get(STEP_NAME)
                .<Vehicle, Vehicle>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        Job job = jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

        return job;
    }

//    @Bean
//    public FlatFileItemReader<Employee> itemReader(@Value("${input}") Resource resource) {
//        FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
//        flatFileItemReader.setResource(resource);
//        flatFileItemReader.setName("CSV-READER");
//        flatFileItemReader.setLinesToSkip(1); // skip the header line
//        flatFileItemReader.setLineMapper(lineMapper());
//
//        return flatFileItemReader;
//    }

//    @Bean
//    public LineMapper<Employee> lineMapper() {
//        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(Employee.class);
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setNames(new String[]{"id", "name", "dept", "salary"});
//
//        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//
//        return lineMapper;
//    }

    @Bean
    @StepScope
    public StaxEventItemReader<Vehicle> itemReader() throws URISyntaxException {
        File file = new File(getClass().getClassLoader().getResource("A6-short.xml").toURI());
        Resource resource = new FileSystemResource(file);
//        return new StaxEventItemReaderBuilder<Vehicle>().name("xmlItemReader")
//                .resource(resource)
//                .addFragmentRootElements("v")
//                .unmarshaller(vehicleMarshaller())
//                .build();
        Jaxb2Marshaller xmlMarshaller = new Jaxb2Marshaller();
        xmlMarshaller.setClassesToBeBound(Vehicle.class);

        StaxEventItemReader<Vehicle> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setResource(resource);
        xmlFileReader.setFragmentRootElementName("v");
        xmlFileReader.setUnmarshaller(xmlMarshaller);

        return xmlFileReader;
    }

//    @Bean()
//    public StaxEventItemReader<Vehicle> vehicleMarshaller() {
//        Map<String, Class> aliases = new HashMap<>();
//        aliases.put("v", Vehicle.class);
//        aliases.put("Dealer_ID", String.class);
//        aliases.put("VIN", String.class);
//        aliases.put("Stock_ID", String.class);
//        aliases.put("Year", String.class);
//        aliases.put("Make", String.class);
//        aliases.put("Model", String.class);
//
//        XStreamMarshaller marshaller = new XStreamMarshaller();
//        marshaller.setAliases(aliases);

//        Jaxb2Marshaller xmlMarshaller = new Jaxb2Marshaller();
//        xmlMarshaller.setClassesToBeBound(Vehicle.class);
//
//        StaxEventItemReader<Vehicle> xmlFileReader = new StaxEventItemReader<>();
//        xmlFileReader.setFragmentRootElementName("v");
//        xmlFileReader.setUnmarshaller(xmlMarshaller);
//
//        return xmlFileReader;
//    }
}
