package com.qbanalytix.cognostestclient.business.facades.interfaces;

import com.ganesha.context.Context;
import com.ganesha.core.exception.UserException;

public interface IScenarioFacade {

	public void execute(Context context) throws UserException;

}
