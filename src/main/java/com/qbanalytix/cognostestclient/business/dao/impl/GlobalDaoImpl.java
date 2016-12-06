package com.qbanalytix.cognostestclient.business.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ganesha.context.Context;
import com.qbanalytix.cognostestclient.business.dao.interfaces.IGlobalDao;
import com.qbanalytix.cognostestclient.resources.model.ClientReport;

public class GlobalDaoImpl implements IGlobalDao {

	private static final Map<Object, Object> dummyDB = Collections.synchronizedMap(new LinkedHashMap<>());
	private static final List<String> cognosReportURLs = new ArrayList<>();
	private static final List<ClientReport> clientReports = new ArrayList<>();

	@Override
	public void saveWebclient(Context context) {
		dummyDB.put("webclient", context.get("webclient"));
	}

	@Override
	public String getWebclient(Context context) {
		return (String) dummyDB.get("webclient");
	}

	@Override
	public void saveCognosURL(Context context) {
		dummyDB.put("cognosURL", context.get("cognosURL"));
	}

	@Override
	public String getCognosURL(Context context) {
		return (String) dummyDB.get("cognosURL");
	}

	@Override
	public void saveCognosUsernamePassword(Context context) {
		dummyDB.put("cognosUsername", context.get("cognosUsername"));
		dummyDB.put("cognosPassword", context.get("cognosPassword"));

	}

	@Override
	public String getCognosUsername(Context context) {
		return (String) dummyDB.get("cognosUsername");
	}

	@Override
	public String getCognosPassword(Context context) {
		return (String) dummyDB.get("cognosPassword");
	}

	@Override
	public void saveCognosReportURLs(Context context) {
		cognosReportURLs.clear();
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) context.get("cognosReportURLs");
		cognosReportURLs.addAll(list);
	}

	@Override
	public List<String> getCognosReportURLs(Context context) {
		return new ArrayList<>(cognosReportURLs);
	}

	@Override
	public void saveCognosLogoutURL(Context context) {
		dummyDB.put("cognosLogoutURL", context.get("cognosLogoutURL"));
	}

	@Override
	public String getCognosLogoutURL(Context context) {
		return (String) dummyDB.get("cognosLogoutURL");
	}

	@Override
	public void saveCognosReportTestCounter(Context context) {
		dummyDB.put("cognosReportTestCounter", ((Number) context.get("cognosReportTestCounter")).intValue());
	}

	@Override
	public int getCognosReportTestCounter(Context context) {
		return (int) dummyDB.get("cognosReportTestCounter");
	}

	@Override
	public void saveNumberOfThread(Context context) {
		dummyDB.put("numberOfThread", ((Number) context.get("numberOfThread")).intValue());
	}

	@Override
	public int getNumberOfThread(Context context) {
		return (int) dummyDB.get("numberOfThread");
	}

	@Override
	public void addClientReport(Context context) {
		clientReports.add((ClientReport) context.get("clientReport"));
	}

	@Override
	public List<ClientReport> getClientReports(Context context) {
		return new ArrayList<>(clientReports);
	}

	@Override
	public void clearClientReport(Context context) {
		clientReports.clear();
	}

}
