package com.qbanalytix.cognostestclient.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ganesha.core.exception.UserException;
import com.qbanalytix.cognostestclient.application.CognosTester;
import com.qbanalytix.cognostestclient.serverinvoker.InvokeListenerIsReadyThread;
import com.qbanalytix.cognostestclient.serverinvoker.base.IServerInvokerListener;
import com.qbanalytix.cognostestclient.web.ServiceResponse;

public class InvokeListenerIsReayJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(InvokeListenerIsReayJob.class);

	@Override
	public void execute(JobExecutionContext context) {

		logger.debug("Scheduler " + InvokeListenerIsReayJob.class.getName() + " is started");

		try {
			start();
		} catch (UserException e) {
			logger.error(e.toString(), e);
		}

		logger.debug("Scheduler " + InvokeListenerIsReayJob.class.getName() + " is finished");
	}

	private void start() throws UserException {

		new InvokeListenerIsReadyThread(new IServerInvokerListener() {

			@Override
			public void handleResponse(ServiceResponse serviceResponse) {
				/*
				 * Do nothing
				 */
			}

			@Override
			public void handleException(Exception e) {
				logger.error(e.toString(), e);
				CognosTester.running = false;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					/*
					 * Do nothing
					 */
				} finally {
					System.exit(1);
				}
			}

			@Override
			public void handleException(String response) {
				logger.error(response);
				CognosTester.running = false;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					/*
					 * Do nothing
					 */
				} finally {
					System.exit(1);
				}
			}
		}).start();
	}
}