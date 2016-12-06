package com.qbanalytix.cognostestclient.web.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ganesha.context.Context;
import com.ganesha.core.exception.UserException;
import com.qbanalytix.cognostestclient.business.dao.DaoCollection;
import com.qbanalytix.cognostestclient.context.ApplicationContext;
import com.qbanalytix.cognostestclient.web.AbstractRequestHandler;

public class ClientConfigurationSvc extends AbstractRequestHandler {

	private DaoCollection daoCollection = (DaoCollection) ApplicationContext.getBean("daoCollection");

	public void configure(HttpServletRequest request, HttpServletResponse response, Context context)
			throws UserException {

		daoCollection.getGlobalDao().saveWebclient(context);
		daoCollection.getGlobalDao().saveNumberOfThread(context);
		daoCollection.getGlobalDao().saveCognosURL(context);
		daoCollection.getGlobalDao().saveCognosUsernamePassword(context);
		daoCollection.getGlobalDao().saveCognosReportURLs(context);
		daoCollection.getGlobalDao().saveCognosReportTestCounter(context);
		daoCollection.getGlobalDao().saveCognosLogoutURL(context);

		sendSuccessResponse(null, response);
	}

	public void isReady(HttpServletRequest request, HttpServletResponse response, Context context)
			throws UserException {
		sendSuccessResponse(null, response);
	}
}
