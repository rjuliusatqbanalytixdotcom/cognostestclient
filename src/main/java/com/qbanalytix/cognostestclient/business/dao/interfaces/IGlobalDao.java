package com.qbanalytix.cognostestclient.business.dao.interfaces;

import java.util.List;

import com.ganesha.context.Context;
import com.ganesha.core.exception.UserException;
import com.qbanalytix.cognostestclient.resources.model.ClientReport;

public interface IGlobalDao {

	public void saveWebclient(Context context) throws UserException;
	
	public String getWebclient(Context context) throws UserException;
	
	public void saveCognosURL(Context context) throws UserException;

	public String getCognosURL(Context context) throws UserException;

	public void saveCognosUsernamePassword(Context context) throws UserException;

	public String getCognosUsername(Context context) throws UserException;

	public String getCognosPassword(Context context) throws UserException;

	public void saveCognosReportURLs(Context context) throws UserException;

	public List<String> getCognosReportURLs(Context context) throws UserException;

	public void saveCognosLogoutURL(Context context) throws UserException;

	public String getCognosLogoutURL(Context context) throws UserException;

	public void saveCognosReportTestCounter(Context context) throws UserException;

	public int getCognosReportTestCounter(Context context) throws UserException;

	public void saveNumberOfThread(Context context) throws UserException;

	public int getNumberOfThread(Context context) throws UserException;

	public void clearClientReport(Context context) throws UserException;

	public void addClientReport(Context context) throws UserException;

	public List<ClientReport> getClientReports(Context context) throws UserException;
}
