package com.qbanalytix.cognostestclient.webdriver.impl;

import org.openqa.selenium.chrome.ChromeDriver;

import com.qbanalytix.cognostestclient.webdriver.interfaces.WebDriverConstrutor;

public class ChromeDriverConstructor implements WebDriverConstrutor<ChromeDriver> {

	@Override
	public ChromeDriver createInstance() {
		return new ChromeDriver();
	}

}
