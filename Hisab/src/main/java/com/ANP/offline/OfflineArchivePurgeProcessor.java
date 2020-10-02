package com.ANP.offline;

import com.ANP.repository.ArchiveAndPurgeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

public class OfflineArchivePurgeProcessor extends GenericOfflineProcessor {
    @Autowired
    ArchiveAndPurgeDAO archiveAndPurgeDAO;

    @Override
    @Scheduled(cron="${cron.expressionForArchiveAndPurge}")
    public void processOffline() {
        archiveAndPurgeDAO.invokeArchiveAndPurgeProcess();
    }
}
