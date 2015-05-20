package com.zormion.game.entities;

import java.net.InetAddress;

import com.zormion.game.input.InputHandler;
import com.zormion.game.level.Level;

public class PlayerMP extends Player {
	
	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(String username, Level level, int x, int y, InputHandler input, InetAddress ipAddress, int port) {
		super(username, level, x, y, input);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public PlayerMP(String username, Level level, int x, int y, InetAddress ipAddress, int port) {
		super(username, level, x, y, null);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	@Override
	public void update() {
		super.update();
	}
}
