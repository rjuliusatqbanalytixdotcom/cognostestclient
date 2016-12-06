package com.qbanalytix.cognostestclient.webdriver.interfaces;

import org.openqa.selenium.WebDriver;

public interface WebDriverConstrutor<T extends WebDriver> {

	public T createInstance();
	
}
