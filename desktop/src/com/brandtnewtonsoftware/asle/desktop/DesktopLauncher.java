package com.brandtnewtonsoftware.asle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brandtnewtonsoftware.asle.ASLTutorGame;

import java.awt.*;

public class DesktopLauncher {
	public static void main(String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		config.resizable = false;

		config.width = width;
		config.height = height;
		config.title = "ASL Tutor";

		// fullscreen
		config.fullscreen = true;
		new LwjglApplication(new ASLTutorGame(), config);
	}
}
