package com.ANP.service.offline;

import com.ANP.repository.ArchiveAndPurgeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OfflineDataGrowthController extends GenericOfflineProcessor{
    @Autowired
    ArchiveAndPurgeDAO archiveAndPurgeDAO;


    private static final Logger logger = LoggerFactory.getLogger(OfflineDataGrowthController.class);

    @Override
    @Scheduled(cron="${cron.schedule.OrgDataControl}")
    public void processOffline() {
        logger.trace("Entering processOffline()");
        try {
            archiveAndPurgeDAO.controlOrgDataGrowth();
        } catch(Exception e) {
            e.printStackTrace();
        }

        logger.trace("Exiting processOffline()");
    }
}
