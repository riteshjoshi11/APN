package com.ANP.service.offline;

import com.ANP.bean.TransactionReportBean;
import com.ANP.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfflineTransactionReportProcessor extends GenericOfflineProcessor{
    private static final Logger logger = LoggerFactory.getLogger(OfflineTransactionReportProcessor.class);


    @Autowired
    ReportService reportService ;

    @Value("${offline.gen.txnReport.BatchSize}")
    private Integer batchSize;

    @Override
    @Scheduled(cron="${cron.schedule.OfflineUserDataReportProcessor}")
    public void processOffline() {
        logger.trace("Entering processOffline()");
        List<TransactionReportBean> txnReportBeanList =  reportService.getUnprocessedTransactionReportRequestBatch(batchSize);

        if(txnReportBeanList!=null && txnReportBeanList.size()>0) {
            for (TransactionReportBean gstReportBean : txnReportBeanList) {
                reportService.processTransactionReport(gstReportBean);
            }
        }

        logger.trace("Exiting processOffline()");
    }
}
