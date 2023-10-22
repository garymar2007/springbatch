package com.gary.repository;

import com.gary.model.FeedFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedFileInfoRepository extends JpaRepository<FeedFileInfo, Integer> {
    FeedFileInfo getFeedFileInfoByFeedFileName(String feedFileName);
}
