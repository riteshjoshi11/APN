package com.ANP.service.offline;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

/*
This class will serve as Super class for all offline processing
 */
@EnableScheduling
@Service
public abstract class GenericOfflineProcessor {
    public abstract void processOffline() ;
}
