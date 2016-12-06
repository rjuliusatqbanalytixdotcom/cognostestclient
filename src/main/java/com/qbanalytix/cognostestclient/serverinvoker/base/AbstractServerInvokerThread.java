package com.qbanalytix.cognostestclient.serverinvoker.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ganesha.core.SystemSetting;
import com.ganesha.core.interfaces.ILogger;

public abstract class AbstractServerInvokerThread extends Thread implements ILogger {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private IServerInvokerListener serverInvokerListener;

	public AbstractServerInvokerThread(IServerInvokerListener serverInvokerListener) {
		this.serverInvokerListener = serverInvokerListener;
	}

	public abstract String getURL();

	public String composeURL() {
		String url = getURL();
		StringBuilder builder = new StringBuilder("http://");
		builder.append(SystemSetting.getProperty("server.config.host")).append(":")
				.append(SystemSetting.getProperty("server.config.port"));
		if (url.startsWith("/")) {
			builder.append(url);
		} else {
			builder.append("/").append(url);
		}
		return builder.toString();
	}

	public IServerInvokerListener getServerInvokerListener() {
		return serverInvokerListener;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}
