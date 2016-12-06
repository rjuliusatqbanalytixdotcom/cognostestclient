package com.qbanalytix.cognostestclient.serverinvoker;

import com.ganesha.httpclient.BasicHttpClient;
import com.ganesha.httpclient.IHttpClient;
import com.ganesha.httpclient.ServiceParameter;
import com.ganesha.messaging.utils.JsonUtils;
import com.qbanalytix.cognostestclient.business.dao.DaoCollection;
import com.qbanalytix.cognostestclient.context.ApplicationContext;
import com.qbanalytix.cognostestclient.resources.model.ClientInformation;
import com.qbanalytix.cognostestclient.serverinvoker.base.AbstractServerInvokerThread;
import com.qbanalytix.cognostestclient.serverinvoker.base.IServerInvokerListener;
import com.qbanalytix.cognostestclient.web.ServiceResponse;

public class InvokeListenerClientReportThread extends AbstractServerInvokerThread {

	private DaoCollection daoCollection = (DaoCollection) ApplicationContext.getBean("daoCollection");

	public InvokeListenerClientReportThread(IServerInvokerListener serverInvokerListener) {
		super(serverInvokerListener);
	}

	@Override
	public void run() {

		IHttpClient httpClient = new BasicHttpClient();
		String response = null;
		try {
			String clientReportsJson = new JsonUtils()
					.objectToJson(daoCollection.getGlobalDao().getClientReports(null));
			ServiceParameter params = new ServiceParameter();
			params.put("clientReports", clientReportsJson);
			params.put("hostname", ClientInformation.INSTANCE.getHostname());
			params.put("port", String.valueOf(ClientInformation.INSTANCE.getPort()));
			response = httpClient.postAndGetString(composeURL(), params);
		} catch (Exception e) {
			getServerInvokerListener().handleException(e);
			return;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}

		ServiceResponse serviceResponse = null;
		try {
			serviceResponse = new JsonUtils().jsonToObject(response, ServiceResponse.class);
		} catch (Exception e) {
			getServerInvokerListener().handleException(response);
			return;
		}

		try {
			getServerInvokerListener().handleResponse(serviceResponse);
		} catch (Exception e) {
			getServerInvokerListener().handleException(e);
			return;
		}
	}

	@Override
	public String getURL() {
		return "/ListenerSvc/clientReport";
	}
}
