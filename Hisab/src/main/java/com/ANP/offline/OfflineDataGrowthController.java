package com.ANP.offline;

import com.ANP.repository.ArchiveAndPurgeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class OfflineDataGrowthController extends GenericOfflineProcessor{
    @Autowired
    ArchiveAndPurgeDAO archiveAndPurgeDAO;

    @Override
    @Scheduled(cron="${cron.expressionForOrgDataControl}")
    public void processOffline() {
        archiveAndPurgeDAO.controlOrgDataGrowth();
    }
}
