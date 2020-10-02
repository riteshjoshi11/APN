package com.ANP.offline;

import org.springframework.scheduling.annotation.EnableScheduling;

/*
This class will serve as Super class for all offline processing
 */
public abstract class GenericOfflineProcessor {
    public abstract void processOffline() ;
}
