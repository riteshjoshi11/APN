package com.ANP.service.offline;

import com.ANP.repository.ArchiveAndPurgeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class OfflineArchivePurgeProcessor extends GenericOfflineProcessor {
    @Autowired
    ArchiveAndPurgeDAO archiveAndPurgeDAO;

    private static final Logger logger = LoggerFactory.getLogger(OfflineArchivePurgeProcessor.class);

    @Override
    @Scheduled(cron="${cron.schedule.ArchiveAndPurge}")
    public void processOffline() {
        logger.trace("Entering processOffline()");
        try {
            archiveAndPurgeDAO.invokeArchiveAndPurgeProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.trace("Exiting processOffline()");

    }
}
