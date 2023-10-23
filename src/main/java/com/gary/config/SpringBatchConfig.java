package com.gary.config;

import com.gary.services.FileProcessorTasklet;
import com.gary.model.Vehicle;
import com.gary.services.UnlockFileTasklet;
import com.gary.services.WaitForLatestFeedFilesTasklet;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.File;
import java.net.URISyntaxException;


/*
 * Chunks of data: instead of reading, processing and writing all the lines at once, it’ll read,
 * process and write a fixed amount of records (chunk) at a time
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    public static final String JOB_NAME = "Feed-File-Processing-Job";
    public static final String STEP_NAME = "Feed-File-Processing-Step";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private FileProcessorTasklet fileProcessorTasklet;

    @Autowired
    private UnlockFileTasklet unlockFileTasklet;

    @Autowired
    private WaitForLatestFeedFilesTasklet waitForLatestFeedFilesTasklet;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(ItemReader<Vehicle> itemReader,
                   ItemProcessor<Vehicle, Vehicle> itemProcessor,
                   ItemWriter<Vehicle> itemWriter) {
        Job job = jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(fileLockTaskLet())
                .next(conditionalDecider()).on("COMPLETED").to(parseXmlSaveToDB(itemReader, itemProcessor, itemWriter))
                .next(conditionalDecider()).on("QUIET").to(waitForNewFeedFiles())
                .next(fileUnlockTasklet())
                .end()
                .build();
        return job;
    }

    @Bean
    public Step waitForNewFeedFiles() {
        return stepBuilderFactory.get("waitForNewFeedFiles")
                .tasklet(waitForLatestFeedFilesTasklet)
                .build();
    }

    @Bean
    public Step fileUnlockTasklet() {
        return stepBuilderFactory.get("fileUnlocker")
                .tasklet(unlockFileTasklet)
                .build();
    }

    @Bean
    public JobExecutionDecider conditionalDecider(){
        return (JobExecution jobExecution, StepExecution stepExecution) -> {
            boolean isFileToBeProcessed = fileProcessorTasklet.getToBeProcessed() != null;
            if (isFileToBeProcessed){
                unlockFileTasklet.setFeedFileName(fileProcessorTasklet.getToBeProcessed().getName());
            }
            return isFileToBeProcessed ? new FlowExecutionStatus("COMPLETED") : new FlowExecutionStatus("QUIET");
        };
    }

    @Bean
    public Step parseXmlSaveToDB(ItemReader<Vehicle> itemReader,
                                 ItemProcessor<Vehicle, Vehicle> itemProcessor,
                                 ItemWriter<Vehicle> itemWriter) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Vehicle, Vehicle>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step fileLockTaskLet() {
        return stepBuilderFactory.get("fileLocker")
                .tasklet(fileProcessorTasklet)
                .build();
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
        File toBeProcessed = fileProcessorTasklet.getToBeProcessed();

        if(toBeProcessed != null) {
            Resource resource = new FileSystemResource(toBeProcessed);
            Jaxb2Marshaller xmlMarshaller = new Jaxb2Marshaller();
            xmlMarshaller.setClassesToBeBound(Vehicle.class);

            StaxEventItemReader<Vehicle> xmlFileReader = new StaxEventItemReader<>();
            xmlFileReader.setResource(resource);
            xmlFileReader.setFragmentRootElementName("v");
            xmlFileReader.setUnmarshaller(xmlMarshaller);

            fileProcessorTasklet.setToBeProcessed(null);
            return xmlFileReader;
        }
        return null;

//        return new StaxEventItemReaderBuilder<Vehicle>().name("xmlItemReader")
//                .resource(resource)
//                .addFragmentRootElements("v")
//                .unmarshaller(vehicleMarshaller())
//                .build();
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
