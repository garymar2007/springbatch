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
public class UnlockFileTasklet implements Tasklet {

    @Autowired
    private FeedFileInfoRepository feedFileInfoRepository;

    private String feedFileName = null;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        if(feedFileName !=null ) {
            FeedFileInfo feedFileInfo = feedFileInfoRepository.getFeedFileInfoByFeedFileName(feedFileName);
            if(feedFileInfo != null) {
                feedFileInfo.setLocked(false);
                feedFileInfo.setProcessedDone(true);
            }
        }
        return RepeatStatus.FINISHED;
    }

}
