package com.ANP.offline;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OfflineGSTReportProcessor extends GenericOfflineProcessor {
    @Override
    @Scheduled(cron="${cron.schedule.OfflineGSTReportProcessor}")
    public void processOffline() {

    }
}
