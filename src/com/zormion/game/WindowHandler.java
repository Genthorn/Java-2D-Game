package com.zormion.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.zormion.game.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener {
	
	private final Game game;
	
	public WindowHandler(Game game) {
		this.game = game;
		this.game.frame.addWindowListener(this);
	}
	
	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		Packet01Disconnect packet = new Packet01Disconnect(this.game.getPlayer().getUsername());
		packet.writeData(this.game.socketClient);
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

}
