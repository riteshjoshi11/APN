package com.ANP.service.offline;

import com.ANP.repository.ArchiveAndPurgeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class OfflineArchivePurgeProcessor extends GenericOfflineProcessor {
    @Autowired
    ArchiveAndPurgeDAO archiveAndPurgeDAO;

    @Override
    @Scheduled(cron="${cron.schedule.ArchiveAndPurge}")
    public void processOffline() {
        archiveAndPurgeDAO.invokeArchiveAndPurgeProcess();
    }
}
