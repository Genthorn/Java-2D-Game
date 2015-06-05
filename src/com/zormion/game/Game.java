package com.zormion.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.zormion.game.entities.Player;
import com.zormion.game.entities.PlayerMP;
import com.zormion.game.gfx.Font;
import com.zormion.game.gfx.Screen;
import com.zormion.game.gfx.Spritesheet;
import com.zormion.game.input.Keyboard;
import com.zormion.game.level.Level;
import com.zormion.game.net.GameClient;
import com.zormion.game.net.GameServer;
import com.zormion.game.net.packets.Packet00Login;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 320;
    public static final int HEIGHT = WIDTH / 16 * 9;
    public static final int SCALE = 3;
    public static final String NAME = "The Stoopide Game: The Rise of Kim Heng";
    public static final Dimension DIMENSIONS = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
    public static Game game;

    public JFrame frame;
    private Image iconImage;

    private Thread thread;

    public boolean running = false;
    public int updateCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    
    private Screen screen;
    public Keyboard input;
    public WindowHandler windowHandler;
    public Level level;
    public Player player;

    public GameClient socketClient;
    public GameServer socketServer;

    public boolean debug = false;
    public boolean isApplet = false;
    
    private int frames;
    private int updates;
    
     /*TODO
  * ---------
  * -Do Player in Water Bobbing
  * -Save Player data on server
  * -Randomize tile animation
  *
  */
    
    public Game() {
    	setMinimumSize(DIMENSIONS);
		setMaximumSize(DIMENSIONS);
		setPreferredSize(DIMENSIONS);
		
		frame = new JFrame(NAME);
		iconImage = new ImageIcon("res/icon.png").getImage();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.requestFocus();
		frame.setIconImage(iconImage);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setUndecorated(true);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    public void init() {
        game = this;

        screen = new Screen(WIDTH, HEIGHT, new Spritesheet("res/spritesheet.png"));
        windowHandler = new WindowHandler(this);
        input = new Keyboard(this);
        level = new Level("res/levels/diagonal.txt");
        player = new PlayerMP(JOptionPane.showInputDialog(this, "Please enter a username"), level, 100, 100, input, null, -1);
        level.addEntity(player);
        if (!isApplet) {
            Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
            if (socketServer != null) {
                socketServer.addConnection((PlayerMP) player, loginPacket);
            }
            loginPacket.writeData(socketClient);
        }
    }

    public synchronized void start() {
        running = true;

        thread = new Thread(this, NAME + "_main");
        thread.start();
        if (!isApplet) {
            if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
                socketServer = new GameServer(this);
                socketServer.start();
            }
            
            //EVAN'S IP: 192.168.100.173
            socketClient = new GameClient(this, "localhost");
            socketClient.start();
        }
    }

    public synchronized void stop() {
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
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

            if (shouldRender) {
                frames++;
                render();
            }
            
            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                debug(DebugLevel.INFO, updates + " ticks, " + frames + " frames");
                
                frames = 0;
                updates = 0;
            }
        }
    }

    public void update() {
        updateCount++;
        if(input.escape.isPressed()) System.exit(-1);
        
        level.update();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) { createBufferStrategy(3); return; }
        Graphics g = bs.getDrawGraphics();
        
        int xOffset = player.x - (screen.width / 2);
        int yOffset = player.y - (screen.height / 2);

        level.renderTiles(screen, xOffset, yOffset);
        level.renderEntities(screen);
        
        player.renderNotOnServer(screen);
        
        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                pixels[x + y * WIDTH] = screen.pixels[x+y*screen.width];
            }
        }

        
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        
        g.dispose();
        bs.show();
    }
    
    public static long fact(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * fact(n - 1);
        }
    }

    public void debug(DebugLevel level, String msg) {
        switch (level) {
        default:
        case INFO:
            if (debug) {
                System.out.println("[" + NAME + "] " + msg);
            }
            break;
        case WARNING:
            System.out.println("[" + NAME + "] [WARNING] " + msg);
            break;
        case SEVERE:
            System.out.println("[" + NAME + "] [SEVERE]" + msg);
            this.stop();
            break;
        }
    }

    public static enum DebugLevel {
        INFO, WARNING, SEVERE;
    }
    
    public static void main(String args[]) {
    	new Game().start();
    }
    
    public Level getLevel() {
    	return level;
    }
    
    public Player getPlayer() {
    	return player;
    }
}