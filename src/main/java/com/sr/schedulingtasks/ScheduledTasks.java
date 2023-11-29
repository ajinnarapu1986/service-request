package com.sr.schedulingtasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 
	 */
	@Scheduled(cron = "${com.scheduled.cron}")
	public void checkForRequestInfo() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	}
	
}
