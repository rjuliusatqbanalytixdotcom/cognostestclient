package com.qbanalytix.cognostestclient.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ganesha.context.Context;
import com.ganesha.core.exception.AppException;
import com.ganesha.core.exception.UserException;
import com.qbanalytix.cognostestclient.business.dao.DaoCollection;
import com.qbanalytix.cognostestclient.business.facades.impl.LoadDashboardScenarioFacadeImpl;
import com.qbanalytix.cognostestclient.business.facades.impl.LoginScenarioFacadeImpl;
import com.qbanalytix.cognostestclient.business.facades.impl.LogoutScenarioFacadeImpl;
import com.qbanalytix.cognostestclient.business.facades.interfaces.IScenarioFacade;
import com.qbanalytix.cognostestclient.context.ApplicationContext;
import com.qbanalytix.cognostestclient.context.ThreadContext;
import com.qbanalytix.cognostestclient.serverinvoker.InvokeListenerUpdateStatusThread;
import com.qbanalytix.cognostestclient.serverinvoker.base.IServerInvokerListener;
import com.qbanalytix.cognostestclient.web.ServiceResponse;
import com.qbanalytix.cognostestclient.webdriver.impl.ChromeDriverConstructor;
import com.qbanalytix.cognostestclient.webdriver.impl.FirefoxDriverConstructor;
import com.qbanalytix.cognostestclient.webdriver.impl.IEDriverConstructor;
import com.qbanalytix.cognostestclient.webdriver.impl.OperaDriverConstructor;
import com.qbanalytix.cognostestclient.webdriver.interfaces.WebDriverConstrutor;

public class CognosTesterThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(CognosTesterThread.class);

	private static final Map<String, WebDriverConstrutor<?>> CONSTRUCTOR = new HashMap<>();

	static {
		CONSTRUCTOR.put("Internet Explorer", new IEDriverConstructor());
		CONSTRUCTOR.put("Mozilla Firefox", new FirefoxDriverConstructor());
		CONSTRUCTOR.put("Google Chrome", new ChromeDriverConstructor());
		CONSTRUCTOR.put("Opera", new OperaDriverConstructor());
	}

	private IScenarioFacade loginScenarioFacade = (LoginScenarioFacadeImpl) ApplicationContext
			.getBean("loginScenarioFacade");

	private IScenarioFacade loadDashboardScenarioFacade = (LoadDashboardScenarioFacadeImpl) ApplicationContext
			.getBean("loadDashboardScenarioFacade");

	private IScenarioFacade logoutScenarioFacade = (LogoutScenarioFacadeImpl) ApplicationContext
			.getBean("logoutScenarioFacade");

	private DaoCollection daoCollection = (DaoCollection) ApplicationContext.getBean("daoCollection");

	private Context parentContext;

	public CognosTesterThread(Context parentContext) {
		this.parentContext = parentContext;
	}

	@Override
	public void run() {

		WebDriver webDriver = null;
		try {

			Context context = new ThreadContext(parentContext);

			webDriver = createInstance();
			context.put("webDriver", webDriver);

			runTest(context);

		} catch (UserException e) {
			throw new AppException(e.getCause());
		} finally {
			try {

				if (webDriver != null) {
					webDriver.quit();
				}

			} catch (Exception e) {
				/*
				 * Do nothing
				 */
			}
		}
	}

	private void runTest(Context context) throws UserException {
		// loginScenarioFacade.execute(context);
		testLoadDashboards(context);
		// logoutScenarioFacade.execute(context);
	}

	private void testLoadDashboards(Context context) throws UserException {

		List<String> cognosReportURLs = daoCollection.getGlobalDao().getCognosReportURLs(context);

		while (CognosTester.running) {
			long start = System.currentTimeMillis();
			for (String cognosReportURL : cognosReportURLs) {
				context.put("cognosReportURL", cognosReportURL);
				loadDashboardScenarioFacade.execute(context);
			}
			long timeConsumed = System.currentTimeMillis() - start;
			updateStatusOnServer(timeConsumed);
		}
	}

	private void updateStatusOnServer(long timeConsumed) throws UserException {

		Thread thread = new InvokeListenerUpdateStatusThread(new IServerInvokerListener() {

			@Override
			public void handleResponse(ServiceResponse serviceResponse) {
				/*
				 * Do nothing
				 */
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
		}, timeConsumed);

		thread.start();
	}

	private WebDriver createInstance() {
		try {
			return CONSTRUCTOR.get(daoCollection.getGlobalDao().getWebclient(null)).createInstance();
		} catch (UserException e) {
			throw new AppException(e.getCause());
		}
	}
}
