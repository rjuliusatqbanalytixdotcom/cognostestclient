package com.qbanalytix.cognostestclient.serverinvoker.base;

import com.qbanalytix.cognostestclient.web.ServiceResponse;

public interface IServerInvokerListener {

	public void handleResponse(ServiceResponse serviceResponse);

	public void handleException(Exception e);

	public void handleException(String response);

}
