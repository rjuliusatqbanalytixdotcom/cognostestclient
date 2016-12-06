package com.qbanalytix.cognostestclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ganesha.core.SystemSetting;
import com.ganesha.core.utils.InetUtils;
import com.ganesha.core.utils.ResourceUtils;
import com.ganesha.desktop.component.XComponentProperties;
import com.qbanalytix.cognostestclient.resources.model.ClientInformation;
import com.qbanalytix.cognostestclient.serverinvoker.InvokeListenerClientRegisterThread;
import com.qbanalytix.cognostestclient.serverinvoker.base.IServerInvokerListener;
import com.qbanalytix.cognostestclient.web.RequestHandler;
import com.qbanalytix.cognostestclient.web.ServiceResponse;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					checkAndSetEnvironmentVariables();

					setDefaultUncaughtExceptionHandler();
					setLookAndFeel();
					// initHibernate();

					initClientInformation();
					registerSelf();

					runQuartz();

					startJetty();

				} catch (Exception e) {
					logger.error(e.toString(), e);
				}
			}
		});
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			setUIStyles();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			logger.error(e.toString(), e);
		}
	}

	private static void setDefaultUncaughtExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.error(e.toString(), e);
			}
		});
	}

	private static void setUIStyles() {
		InputStream inputStream = null;
		try {
			inputStream = Main.class.getClassLoader().getResourceAsStream("xcomponent.properties");
			XComponentProperties.loadProperties(inputStream);

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	private static void checkAndSetEnvironmentVariables() {
		if (System.getenv("cognostest.home") == null || System.getenv("cognostest.home").trim().isEmpty()) {
			Exception e = new Exception("Variable \"cognostest.home\" must be set in the Environment Variables");
			logger.error(e.toString(), e);
			System.exit(1);
		}

		setupLogging();
	}

	private static void setupLogging() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.reset();

		InputStream inputStream = null;
		try {
			inputStream = FileUtils.openInputStream(
					new File(ResourceUtils.getConfigBase(), SystemSetting.getProperty("logging.file.config")));

			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);
			configurator.doConfigure(inputStream);

		} catch (IOException e) {
			logger.error(e.toString(), e);
		} catch (JoranException e) {
			logger.error(e.toString(), e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private static void startJetty() throws Exception {
		Server server = new Server(ClientInformation.INSTANCE.getPort());
		server.setHandler(new RequestHandler());
		server.start();
		server.join();
	}

	private static void initClientInformation() throws UnknownHostException {
		int port = InetUtils.scanFreePort(20000, 300000);
		String ipAddress = InetAddress.getLocalHost().getHostAddress();
		String hostname = InetAddress.getLocalHost().getHostName();
		ClientInformation.init(hostname, ipAddress, port);
	}

	private static void registerSelf() {
		new InvokeListenerClientRegisterThread(new IServerInvokerListener() {

			@Override
			public void handleResponse(ServiceResponse serviceResponse) {
				logger.debug("Self registered successully");
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

	private static void runQuartz() throws SchedulerException {
		SchedulerFactory factory = new StdSchedulerFactory("quartz.properties");
		Scheduler scheduler = factory.getScheduler();
		scheduler.start();
	}
}
