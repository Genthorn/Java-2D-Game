package com.zormion.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.zormion.game.entities.Blob;
import com.zormion.game.entities.Player;
import com.zormion.game.entities.PlayerMP;
import com.zormion.game.gfx.Colors;
import com.zormion.game.gfx.Font;
import com.zormion.game.gfx.Screen;
import com.zormion.game.gfx.Spritesheet;
import com.zormion.game.input.InputHandler;
import com.zormion.game.level.Level;
import com.zormion.game.net.GameClient;
import com.zormion.game.net.GameServer;
import com.zormion.game.net.packets.Packet00Login;
import com.zormion.game.states.GameState;
import com.zormion.game.states.State;
import com.zormion.game.states.StateManager;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static Game game;
	private static final int WIDTH = 220;
	private static final int HEIGHT = WIDTH / 16 * 9;
	private static final int SCALE = 3;
	private static final String NAME = "Game2D";
	private static final double UPS = 60D;
	
	public JFrame frame;
	private Screen screen;
	private GameState gameState;
	private StateManager stateManager;
	
	private InputHandler input;
	private WindowHandler windowHandler;
	private Level level;
	private Player player;
	
	private Blob blob;
	
	private boolean running = false;
	
	private int updateCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	public int[] colors = new int[6 * 6 * 6];
	
	///BUGS///
	//////////
	/* Scaling sprites doesn't work properly
	 * If 4 players on server player 2 and 3 can't see player 4
	 * 
	 * 
	 * 
	 * 
	 */
	
	///TO-DO///
	///////////
	/* Add states
	 *    -Title screen
	 * Send swimming, animation, ... through server
	 * Mob Collision
	 * 
	 * 
	 */
	
	public GameClient socketClient;
	public GameServer socketServer;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.requestFocus();
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		gameState = new GameState("GameState");
		stateManager = new StateManager();
	}
	
	public void init() {
		int index = 0;
		for(int r = 0; r < 6; r++) {
			for(int g = 0; g < 6; g++) {
				for(int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					
					colors[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
		
		
		game = this;
		screen = new Screen(WIDTH, HEIGHT, new Spritesheet("res/spritesheet.png"));
		input = new InputHandler(this);
		windowHandler = new WindowHandler(this);
		level = new Level("res/levels/world1.png");
		player = new PlayerMP(JOptionPane.showInputDialog(this, "Enter Username"), level, 200, 200, input, null, -1);
		level.addEntity(player);
		
		stateManager.addState(gameState);
		stateManager.setState("GameState");
		
		blob = new Blob(level, "Blob", 20, 20, 2);
		level.addEntity(blob);
		
		stateManager.getCurrentState().init();
		
		Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
		if(socketServer != null) {
			socketServer.addConnection((PlayerMP) player, loginPacket);
		}
		
		loginPacket.writeData(socketClient);
	}
	
	public synchronized void start() {
		if(JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
			socketServer = new GameServer(this);
			socketServer.start();
		}
		
		socketClient = new GameClient(this, "127.0.0.1");
		socketClient.start();
		
		running = true;
		new Thread(this).start();
		
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerUpdate = 1000000000D / UPS;
		
		int frames = 0;
		int updates = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerUpdate;
			lastTime = now;
			boolean shouldRender = true;
			
			while(delta >= 1) {
				updates++;
				update();
				delta -= 1;
				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(shouldRender) {
				frames++;
				render();
			}
			
			if(System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				frame.setTitle(NAME + " | FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
			
		}
	}
	
	public void update() {
		updateCount++;
		stateManager.getCurrentState().update();
		
		level.update();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		int xOffset = player.x - (screen.width / 2);
		int yOffset = player.y - (screen.height / 2);
		
		stateManager.getCurrentState().render(screen);

		level.renderTiles(screen, xOffset, yOffset);
		level.renderEntities(screen);
		
		Font.render("Hello", screen, 10, 10, Colors.get(-1, -1, 244, -1));
		
		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int ColourCode = screen.pixels[x + y * screen.width];
				if (ColourCode < 255) {
					pixels[x + y * WIDTH] = colors[ColourCode];

				}
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String args[]) {
		new Game().start();
	}
	
	public Level getLevel() {
		return level;
	}
	
	public InputHandler getInput() {
		return input;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
