package com.qbanalytix.cognostestclient.business.facades.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ganesha.context.Context;
import com.ganesha.core.exception.UserException;
import com.ganesha.core.interfaces.ILogger;
import com.qbanalytix.cognostestclient.business.dao.DaoCollection;
import com.qbanalytix.cognostestclient.business.facades.interfaces.IScenarioFacade;
import com.qbanalytix.cognostestclient.resources.model.ClientInformation;
import com.qbanalytix.cognostestclient.resources.model.ClientReport;

public abstract class AbstractScenarioImpl implements IScenarioFacade, ILogger {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private DaoCollection daoCollection;

	public abstract void run(Context context) throws UserException;

	public abstract String getURL(Context context) throws UserException;

	@Override
	public void execute(Context context) throws UserException {

		ClientReport clientReport = new ClientReport();
		clientReport.setClientInformation(ClientInformation.INSTANCE);
		clientReport.setThreadId(Thread.currentThread().getId());
		clientReport.setStartTime(System.currentTimeMillis());
		clientReport.setUrl(getURL(context));

		run(context);

		clientReport.setEndTime(System.currentTimeMillis());

		context.put("clientReport", clientReport);
		daoCollection.getGlobalDao().addClientReport(context);
	}

	public DaoCollection getDaoCollection() {
		return daoCollection;
	}

	public void setDaoCollection(DaoCollection daoCollection) {
		this.daoCollection = daoCollection;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

}
