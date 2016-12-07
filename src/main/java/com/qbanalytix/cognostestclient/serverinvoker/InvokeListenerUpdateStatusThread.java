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

	private long timeConsumed;
<<<<<<< HEAD
	private long threadId;
=======
>>>>>>> branch 'master' of https://github.com/rjuliusatqbanalytixdotcom/cognostestclient.git

<<<<<<< HEAD
	public InvokeListenerUpdateStatusThread(IServerInvokerListener serverInvokerListener, long timeConsumed,
			long threadId) {
=======
	public InvokeListenerUpdateStatusThread(IServerInvokerListener serverInvokerListener, long timeConsumed) {
>>>>>>> branch 'master' of https://github.com/rjuliusatqbanalytixdotcom/cognostestclient.git
		super(serverInvokerListener);
		this.timeConsumed = timeConsumed;
<<<<<<< HEAD
		this.threadId = threadId;
=======
>>>>>>> branch 'master' of https://github.com/rjuliusatqbanalytixdotcom/cognostestclient.git
	}

	@Override
	public void run() {

		IHttpClient httpClient = new BasicHttpClient();
		String response = null;
		try {
			ServiceParameter params = new ServiceParameter();
			params.put("hostname", ClientInformation.INSTANCE.getHostname());
			params.put("port", String.valueOf(ClientInformation.INSTANCE.getPort()));
<<<<<<< HEAD
			params.put("threadId", String.valueOf(threadId));
=======
			params.put("threadId", String.valueOf(Thread.currentThread().getId()));
>>>>>>> branch 'master' of https://github.com/rjuliusatqbanalytixdotcom/cognostestclient.git
			params.put("timeConsumed", String.valueOf(timeConsumed));
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
