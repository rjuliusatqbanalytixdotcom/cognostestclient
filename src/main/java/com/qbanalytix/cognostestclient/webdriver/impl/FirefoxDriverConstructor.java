package com.qbanalytix.cognostestclient.webdriver.impl;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.ganesha.core.SystemSetting;
import com.ganesha.core.utils.ResourceUtils;
import com.qbanalytix.cognostestclient.webdriver.interfaces.WebDriverConstrutor;

public class FirefoxDriverConstructor implements WebDriverConstrutor<FirefoxDriver> {

	@Override
	public FirefoxDriver createInstance() {
		File file = new File(ResourceUtils.getResourceBase(),
				SystemSetting.getProperty("webclient.binary.location.firefox"));
		FirefoxBinary binary = new FirefoxBinary(file);
		FirefoxProfile profile = new FirefoxProfile();
		return new FirefoxDriver(binary, profile);
	}

}
