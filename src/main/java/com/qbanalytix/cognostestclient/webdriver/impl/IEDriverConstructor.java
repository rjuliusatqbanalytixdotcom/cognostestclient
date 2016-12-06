package com.qbanalytix.cognostestclient.webdriver.impl;

import org.openqa.selenium.ie.InternetExplorerDriver;

import com.qbanalytix.cognostestclient.webdriver.interfaces.WebDriverConstrutor;

public class IEDriverConstructor implements WebDriverConstrutor<InternetExplorerDriver> {

	@Override
	public InternetExplorerDriver createInstance() {
		return new InternetExplorerDriver();
	}

}
