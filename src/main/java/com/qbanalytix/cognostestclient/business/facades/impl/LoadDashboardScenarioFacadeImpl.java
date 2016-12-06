package com.qbanalytix.cognostestclient.business.facades.impl;

import org.openqa.selenium.WebDriver;

import com.ganesha.context.Context;
import com.ganesha.core.exception.UserException;

public class LoadDashboardScenarioFacadeImpl extends AbstractScenarioImpl {

	@Override
	public void run(Context context) throws UserException {
		WebDriver webDriver = (WebDriver) context.get("webDriver");
		webDriver.get(getURL(context));
	}

	@Override
	public String getURL(Context context) throws UserException {
		return context.getString("cognosReportURL");
	}

}
