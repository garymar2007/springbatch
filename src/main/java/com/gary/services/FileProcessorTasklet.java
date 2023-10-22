package com.gary.services;

import com.gary.model.FeedFileInfo;
import com.gary.repository.FeedFileInfoRepository;
import lombok.Data;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Tasklets are meant to perform a single task within a step
 */
@Component
@Data
public class FileProcessorTasklet implements Tasklet {

    @Value("${process.directory}")
    private String processDirectory;

    @Autowired
    private FeedFileInfoRepository feedFileInfoRepository;

    private File toBeProcessed = null;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Path path = Paths.get(processDirectory);
        AtomicBoolean yes = new AtomicBoolean(true);
        try(Stream<Path> filesToBeProcessed = Files.walk(path)) {
            filesToBeProcessed.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .takeWhile(f -> yes.get())
                    .forEach(f -> {
                        if(canProcessFile(f.getName())) {
                            toBeProcessed = f;
                            yes.set(false);
                        }
                    });
        }
        return RepeatStatus.FINISHED;
    }

    private boolean canProcessFile(String feedFileName) {
        FeedFileInfo feedFileInfo = feedFileInfoRepository.getFeedFileInfoByFeedFileName(feedFileName);
        if(feedFileInfo == null) {
            FeedFileInfo newFeedFileInfo = FeedFileInfo.builder().feedFileName(feedFileName).isProcessedDone(false).isLocked(true)
                    .lastCheckedTimeStamp(LocalDateTime.now()).lastDownloadedTimeStamp(LocalDateTime.now()).build();
            feedFileInfoRepository.save(newFeedFileInfo);
            return true;
        }
        return !feedFileInfo.isLocked() &&
                (System.currentTimeMillis() - feedFileInfo.getLastCheckedTimeStamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > 10000)
                && (System.currentTimeMillis() - feedFileInfo.getLastDownloadedTimeStamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > 10000);
    }
}
