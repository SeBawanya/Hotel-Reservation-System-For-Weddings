package com.example.hotelreservationsysyemforweddings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener {

    // Using a logger is better practice than System.out.println
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("=====================================================================");
        logger.info("  Hotel Reservation System has started successfully!");
        logger.info("  Access the application here: http://localhost:8080");
        logger.info("=====================================================================");
    }
}
