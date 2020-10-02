package com.ANP.service.offline;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OfflineGSTReportProcessor extends GenericOfflineProcessor {
    @Override
    @Scheduled(cron="${cron.schedule.OfflineGSTReportProcessor}")
    public void processOffline() {

    }
}
