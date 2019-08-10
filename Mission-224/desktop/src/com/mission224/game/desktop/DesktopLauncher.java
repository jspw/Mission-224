package com.mission224.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.mission224.game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new LwjglApplication(new Main(), Main.TITLE,Main.V_WIDTH,Main.V_HEIGHT);
	}
}
