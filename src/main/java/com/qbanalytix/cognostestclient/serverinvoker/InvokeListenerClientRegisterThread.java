package com.qbanalytix.cognostestclient.serverinvoker;

import com.ganesha.httpclient.BasicHttpClient;
import com.ganesha.httpclient.IHttpClient;
import com.ganesha.messaging.utils.JsonUtils;
import com.qbanalytix.cognostestclient.resources.model.ClientInformation;
import com.qbanalytix.cognostestclient.serverinvoker.base.AbstractServerInvokerThread;
import com.qbanalytix.cognostestclient.serverinvoker.base.IServerInvokerListener;
import com.qbanalytix.cognostestclient.web.ServiceResponse;

public class InvokeListenerClientRegisterThread extends AbstractServerInvokerThread {

	public InvokeListenerClientRegisterThread(IServerInvokerListener serverInvokerListener) {
		super(serverInvokerListener);
	}

	@Override
	public void run() {
		
		IHttpClient httpClient = new BasicHttpClient();
		String response = null;
		try {
			String jsonString = new JsonUtils().objectToJson(ClientInformation.INSTANCE);
			response = httpClient.postAndGetString(composeURL(), jsonString.getBytes());
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
		return "/ListenerSvc/clientRegister";
	}
}
