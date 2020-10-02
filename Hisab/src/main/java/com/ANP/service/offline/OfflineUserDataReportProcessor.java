package com.ANP.service.offline;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OfflineUserDataReportProcessor extends GenericOfflineProcessor{

    @Override
    @Scheduled(cron="${cron.schedule.OfflineUserDataReportProcessor}")
    public void processOffline() {
    }
}
