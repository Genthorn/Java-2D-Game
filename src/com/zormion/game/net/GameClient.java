package com.zormion.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.zormion.game.Game;
import com.zormion.game.entities.PlayerMP;
import com.zormion.game.net.packets.Packet;
import com.zormion.game.net.packets.Packet00Login;
import com.zormion.game.net.packets.Packet01Disconnect;
import com.zormion.game.net.packets.Packet.PacketTypes;
import com.zormion.game.net.packets.Packet02Move;

public class GameClient extends Thread {
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game, String ipAddress) {
		this.game = game;
		
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		
		Packet packet = null;
		
		switch(type) {
			default:
				
			case INVALID:
				break;
			
			case LOGIN:
				packet = new Packet00Login(data);
				handleLogin((Packet00Login)packet, address, port);
				break;
				
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
	            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
	                    + ((Packet01Disconnect) packet).getUsername() + " has left the game...");
	            game.getLevel().removePlayerMP(((Packet01Disconnect) packet).getUsername());

	            break;
	            
			case MOVE:
				packet = new Packet02Move(data);
				handleMove((Packet02Move) packet);
				break;
		}
	
	}
	
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                + ((Packet00Login) packet).getUsername() + " has joined the game...");
		
		PlayerMP player = new PlayerMP(packet.getUsername(), game.getLevel(), packet.getX(), packet.getY(), address, port);
		
		game.getLevel().addEntity(player);
	}
	
	private void handleMove(Packet02Move packet) {
		this.game.getLevel().movePlayer(packet.getUsername(), packet.getX(), packet.getY());
	}
}