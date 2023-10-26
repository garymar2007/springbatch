package com.gary.services;

import com.gary.model.Vehicle;
import com.gary.repository.VehicleRepository;
import com.gary.utils.HashCodeHelper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FeedFileLoaderScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private VehicleRepository vehicleRepository;

    private boolean isFirstTime = true;

    @Scheduled(fixedRate = 30000)
    public void perform() throws Exception{
        if(isFirstTime) {
            preloadData();
            isFirstTime = false;
        }

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("timestamp", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
    }

    private void preloadData() {
        Vehicle v = new Vehicle("JTEBT14R740044691", "1195", "11070","2004","Toyota","4Runner");
        v.setHashCode(HashCodeHelper.getHashCode(v)); //0e9b4a1f755d30a4040e4b31351b958f7142ed628405ab9e8bf58d0e8184562e
        vehicleRepository.save(v);

        Vehicle v1 = new Vehicle("JTEBT14R430008617", "1196", "11085","2003","Toyota","4Runner");
        v1.setHashCode(HashCodeHelper.getHashCode(v1)); //7bf35510dde85116f8d4e2a39cf90c2e16ab4cfec733a87edca5f35573320b52
        vehicleRepository.save(v1);
    }

}
