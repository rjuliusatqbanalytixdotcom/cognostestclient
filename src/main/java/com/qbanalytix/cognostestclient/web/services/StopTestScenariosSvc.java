package com.qbanalytix.cognostestclient.web.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ganesha.context.Context;
import com.qbanalytix.cognostestclient.application.CognosTester;
import com.qbanalytix.cognostestclient.web.AbstractRequestHandler;

public class StopTestScenariosSvc extends AbstractRequestHandler {

	public void execute(HttpServletRequest request, HttpServletResponse response, Context context) {
		CognosTester.running = false;
		sendSuccessResponse(null, response);
	}
}
