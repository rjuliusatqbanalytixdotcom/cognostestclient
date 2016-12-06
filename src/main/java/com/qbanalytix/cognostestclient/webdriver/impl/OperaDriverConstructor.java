package com.qbanalytix.cognostestclient.webdriver.impl;

import org.openqa.selenium.opera.OperaDriver;

import com.qbanalytix.cognostestclient.webdriver.interfaces.WebDriverConstrutor;

public class OperaDriverConstructor implements WebDriverConstrutor<OperaDriver> {

	@Override
	public OperaDriver createInstance() {
		return new OperaDriver();
	}

}
