package com.argr.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;
	
	private JFrame frame;
	private BufferedImage image;
	public static Spritesheet spritesheet;
	
	private boolean isRunning = false;
	private Thread thread;
	
	public Player player;
	
	private int number = 0;
	private int lastNumber = 0;
	

	public String[] words;
	public static String currentWord;
	public static String completeWord;
	private char[] emptySpaces;
	
	
	public static String gameState = "NOVA PALAVRA";
	
	public static Random rand;
	
	public Game(){
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		initFrame();
		
		//Inicializando objetos
		rand = new Random();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(250, 0, 64, 64);
		words = new String[] {"coxinha", "azul", "famoso"};
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void initFrame() {
		frame = new JFrame("Jogo da Forca");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		if(gameState == "NORMAL") {
			player.tick();
		}else if(gameState == "NOVA PALAVRA") {
			if(Game.gameState == "NOVA PALAVRA") {
				while(number == lastNumber) {
					number = Game.rand.nextInt(words.length);
				}
				emptySpaces = new char[words[number].length()];
				char spaces = '*';
				Arrays.fill(emptySpaces, spaces);
				lastNumber = number;
				completeWord = words[number];
				currentWord = new String(emptySpaces);
				System.out.println(currentWord);
				Game.gameState = "NORMAL";
			}
		}
		
		
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		/**/
		if(gameState == "NORMAL") {
			player.render(g);
			Rectangle rect = new Rectangle(new Dimension(WIDTH, HEIGHT));
			renderWord(g, currentWord, rect, new Font("arial", Font.BOLD, 60));
		}else if(gameState == "GAME OVER") {
			g.setColor(Color.black);
			g.setFont(new Font("arial", Font.BOLD, 100));
			g.drawString("GAME OVER", 200, 350);
		}
		/**/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		bs.show();
	}
	
	public void renderWord(Graphics g, String text, Rectangle rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = 500;
	    g.setFont(font);
	    g.drawString(text, x, y);	
	}

	@Override
	public void run() {
		
		while(isRunning) {
			tick();
			render();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		stop();
		
	}

	public void keyPressed(KeyEvent e) {
			
		player.key = e.getKeyChar();
		if(Character.isAlphabetic(player.key)) {
			player.key = Character.toLowerCase(player.key);
			player.isPressed = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		player.key = ' ';
		player.isPressed = false;
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
