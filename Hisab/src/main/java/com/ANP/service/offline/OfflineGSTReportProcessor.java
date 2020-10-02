package com.ANP.service.offline;

import com.ANP.bean.GSTReportBean;
import com.ANP.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfflineGSTReportProcessor extends GenericOfflineProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OfflineArchivePurgeProcessor.class);

    @Autowired
    ReportService reportService ;

    @Value("${offline.gen.gstreport.BatchSize}")
    private Integer batchSize;

    @Override
    @Scheduled(cron="${cron.schedule.OfflineGSTReportProcessor}")
    public void processOffline() {
        logger.trace("Entering processOffline()");
        List<GSTReportBean> gstReportBeanList =  reportService.getUnprocessedGSTReportRequestBatch(batchSize);

        if(gstReportBeanList!=null && gstReportBeanList.size()>0) {
            for (GSTReportBean gstReportBean : gstReportBeanList) {
                reportService.processGSTReport(gstReportBean);
            }
        }

        logger.trace("Exiting processOffline()");

    }
}
