package com.qbanalytix.cognostestclient.serverinvoker;

import com.ganesha.httpclient.BasicHttpClient;
import com.ganesha.httpclient.IHttpClient;
import com.ganesha.httpclient.ServiceParameter;
import com.ganesha.messaging.utils.JsonUtils;
import com.qbanalytix.cognostestclient.resources.model.ClientInformation;
import com.qbanalytix.cognostestclient.serverinvoker.base.AbstractServerInvokerThread;
import com.qbanalytix.cognostestclient.serverinvoker.base.IServerInvokerListener;
import com.qbanalytix.cognostestclient.web.ServiceResponse;

public class InvokeListenerUpdateStatusThread extends AbstractServerInvokerThread {

	private String current;
	private String total;

	public InvokeListenerUpdateStatusThread(IServerInvokerListener serverInvokerListener, String current,
			String total) {
		super(serverInvokerListener);
		this.current = current;
		this.total = total;
	}

	@Override
	public void run() {

		IHttpClient httpClient = new BasicHttpClient();
		String response = null;
		try {
			ServiceParameter params = new ServiceParameter();
			params.put("current", current);
			params.put("total", total);
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
		return "/ListenerSvc/clientUpdateStatus";
	}
}
