package com.brandtnewtonsoftware.asle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brandtnewtonsoftware.asle.ASLEGame;

import java.util.logging.Logger;

public class DesktopLauncher {
	public static void main(String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 1366;
		config.height = 768;

		// fullscreen
//		config.fullscreen = true;
		new LwjglApplication(new ASLEGame(), config);
	}
}
