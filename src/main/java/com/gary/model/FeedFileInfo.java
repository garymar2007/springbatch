package com.gary.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column
    private String feedFileName;
    @Column
    private boolean isLocked;
    @Column
    private boolean isProcessedDone;
    @Column
    private LocalDateTime lastCheckedTimeStamp;
    @Column
    private LocalDateTime lastDownloadedTimeStamp;

}
