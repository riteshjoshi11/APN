package com.ANP.offline;

import com.ANP.repository.ArchiveAndPurgeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OfflineDataGrowthController extends GenericOfflineProcessor{
    @Autowired
    ArchiveAndPurgeDAO archiveAndPurgeDAO;

    @Override
    @Scheduled(cron="${cron.schedule.OrgDataControl}")
    public void processOffline() {
        archiveAndPurgeDAO.controlOrgDataGrowth();
    }
}
