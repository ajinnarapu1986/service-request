package com.sr.schedulingtasks;

import static com.sr.requestinfo.RequestStatus.PENDING_L1;
import static com.sr.requestinfo.RequestStatus.PENDING_L2;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sr.exception.ApplicationException;
import com.sr.requestinfo.RequestInfo;
import com.sr.requestinfo.RequestInfoService;
import com.sr.utility.MailUtility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	private final RequestInfoService requestInfoService;
	private final MailUtility mailUtility;

	@Value("${l1.escalation.sla.mins}")
	private Long l1EscalationSlaMins;
	
	@Value("${l2.escalation.sla.mins}")
	private Long l2EscalationSlaMins;
	
	
	/**
	 * Scheduler to Send Mail for L1 Approval Escalation
	 * 
	 */
	@Scheduled(cron = "${l1.scheduler.cron}")
	public void checkForSLABreachforPendingL1() {
		long start = System.currentTimeMillis();
		System.out.println("Entered into checkForSLABreachforPendingL1() :: date = " + dateFormat.format(new Date()));

		List<RequestInfo> list = null;
		try {
			list = requestInfoService.findByStatusAndL1EscalationMailSentNull(PENDING_L1.name());
			list.stream().parallel().forEach(reqInfo -> {
				Duration duration = Duration.between(LocalDateTime.now(), reqInfo.getCreatedDate());
				long mins = Math.abs(duration.toMinutes());
				log.info(">>>>>>>>>>>>>>>>" + mins);

				if (mins > l1EscalationSlaMins && null == reqInfo.getL1EscalationMailSent()) {
					log.info(">>>>>>>>>>>>>>>>" + mins);
					// Sending Escalation Mail for L1 Approve SLA
					try {
						mailUtility.sendEscalationMail(reqInfo, "L1 Escalation");
					} catch (ApplicationException e) {
						log.error(":: Exception occured in checkForSLABreachforPendingL1() {}::", e);
						System.exit(0);
					}

					reqInfo.setL1EscalationMailSent(LocalDateTime.now());
					try {
						requestInfoService.save(reqInfo);
					} catch (ApplicationException e) {
						log.error(":: Exception occured in checkForSLABreachforPendingL1() {}::", e);
						System.exit(0);					
					}
				}
			});
		} catch (Exception e) {
			log.error(":: Exception occured in checkForSLABreachforPendingL1() {}::", e);
			System.exit(0);
		} finally {
			long end = System.currentTimeMillis();
			System.out.println("Exited from checkForSLABreachforPendingL1() :: time taken = " + (end - start));
		}
	}

	/**
	 * Scheduler to Send Mail for L2 Approval Escalation
	 * 
	 */
	@Scheduled(cron = "${l2.scheduler.cron}")
	public void checkForSLABreachforPendingL2() {
		long start = System.currentTimeMillis();
		System.out.println("Entered into checkForSLABreachforPendingL2() :: date = " + dateFormat.format(new Date()));

		List<RequestInfo> list = null;
		try {
			list = requestInfoService
					.findByStatusAndL2EscalationMailSentNullAndL1ApprovedDateNotNull(PENDING_L2.name());
			list.stream().parallel().forEach(reqInfo -> {
				Duration duration = Duration.between(LocalDateTime.now(), reqInfo.getL1ApprovedDate());
				long mins = Math.abs(duration.toMinutes());
				log.info(">>>>>>>>>>>>>>>>" + mins);

				if (mins > l2EscalationSlaMins && null == reqInfo.getL2EscalationMailSent()) {
					log.info(">>>>>>>>>>>>>>>>" + mins);
					// Sending Escalation Mail for L2 Approve SLA
					try {
						mailUtility.sendEscalationMail(reqInfo, "L2 Escalation");
					} catch (ApplicationException e) {
						log.error(":: Exception occured in checkForSLABreachforPendingL2() {}::", e);
						System.exit(0);
					}

					reqInfo.setL2EscalationMailSent(LocalDateTime.now());
					try {
						requestInfoService.save(reqInfo);
					} catch (ApplicationException e) {
						log.error(":: Exception occured in checkForSLABreachforPendingL2() {}::", e);
						System.exit(0);
					}
				}
			});
		} catch (Exception e) {
			log.error(":: Exception occured in checkForSLABreachforPendingL2() {}::", e);
			System.exit(0);
		} finally {
			long end = System.currentTimeMillis();
			System.out.println("Exited from checkForSLABreachforPendingL2() :: time taken = " + (end - start));
		}
	}
}
