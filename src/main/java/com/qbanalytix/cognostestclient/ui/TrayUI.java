package com.qbanalytix.cognostestclient.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ganesha.core.SystemSetting;
import com.ganesha.core.exception.AppException;
import com.ganesha.core.utils.ResourceUtils;

public class TrayUI {

	public static void createTray() {

		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		PopupMenu popup = new PopupMenu();
		popup.add(exit);

		TrayIcon trayIcon = new TrayIcon(createImage());
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("QB Analytix Tools: Cognos Performance Test");
		trayIcon.setPopupMenu(popup);

		trayIcon.displayMessage("Title", "MESSAGE HERE", TrayIcon.MessageType.ERROR);

		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			throw new AppException(e);
		}
	}

	private static Image createImage() {
		try {
			return ImageIO.read(new File(ResourceUtils.getImageBase(), SystemSetting.getProperty("image.icon.20")));
		} catch (IOException e) {
			throw new AppException(e);
		}
	}
}
