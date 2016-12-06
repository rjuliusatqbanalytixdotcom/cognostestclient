package com.qbanalytix.cognostestclient.application;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ganesha.context.Context;
import com.ganesha.core.exception.AppException;
import com.ganesha.core.exception.UserException;
import com.ganesha.core.utils.ResourceUtils;
import com.qbanalytix.cognostestclient.business.dao.DaoCollection;
import com.qbanalytix.cognostestclient.context.ApplicationContext;
import com.qbanalytix.cognostestclient.context.ThreadContext;
import com.qbanalytix.cognostestclient.serverinvoker.InvokeListenerClientReportThread;
import com.qbanalytix.cognostestclient.serverinvoker.base.IServerInvokerListener;
import com.qbanalytix.cognostestclient.web.ServiceResponse;

public class CognosTester extends Thread {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private DaoCollection daoCollection = (DaoCollection) ApplicationContext.getBean("daoCollection");
	private Context parentContext;

	public CognosTester(Context parentContext) {
		this.parentContext = parentContext;
	}

	@Override
	public void run() {
		ThreadContext context = new ThreadContext(parentContext);
		try {
			runTest(context);
		} catch (UserException e) {
			throw new AppException(e.getCause());
		}
	}

	private void runTest(Context context) throws UserException {

		System.setProperty("webdriver.chrome.driver",
				new File(ResourceUtils.getResourceBase(), "chromedriver.exe").getAbsolutePath());

		System.setProperty("webdriver.gecko.driver",
				new File(ResourceUtils.getResourceBase(), "geckodriver.exe").getAbsolutePath());

		System.setProperty("webdriver.ie.driver",
				new File(ResourceUtils.getResourceBase(), "IEDriverServer.exe").getAbsolutePath());
		
		System.setProperty("webdriver.opera.driver",
				new File(ResourceUtils.getResourceBase(), "operadriver.exe").getAbsolutePath());

		daoCollection.getGlobalDao().clearClientReport(context);

		int numberOfThread = daoCollection.getGlobalDao().getNumberOfThread(context);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);

		for (int i = 0; i < numberOfThread; ++i) {
			CognosTesterThread thread = new CognosTesterThread(context);
			executor.execute(thread);
		}

		executor.shutdown();

		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new AppException(e);
		}

		sendReports();
	}

	private void sendReports() {
		new InvokeListenerClientReportThread(new IServerInvokerListener() {

			@Override
			public void handleResponse(ServiceResponse serviceResponse) {
				logger.debug("Report sends successfully");
			}

			@Override
			public void handleException(Exception e) {
				logger.error(e.toString(), e);
				System.exit(1);
			}

			@Override
			public void handleException(String response) {
				logger.error(response);
				System.exit(1);
			}
		}).start();
	}
}
