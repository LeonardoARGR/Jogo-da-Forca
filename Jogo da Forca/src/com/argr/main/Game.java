package com.argr.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 700;
	
	private JFrame frame;
	private BufferedImage image;
	public static Spritesheet spritesheet;
	private BufferedImage background;
	
	private boolean isRunning = false;
	private Thread thread;
	
	private Player player;
	private Menu menu;

	public static Words word;
	public static String currentWord;
	public static String completeWord;
	private char[] emptySpaces;
	
	public static String gameState = "MENU";
	
	public static Random rand;
	public static FontMetrics metrics;
	
	private String path;
	public String[] themes = {"cor", "alimento", "nome"};
	public static int currentTheme;
	
	private boolean tip = false;
	private String strTip = "aperte SHIFT";
	private int tipCount = 0;
	
	private InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
	public static Font font;
	public static int fontScale;
	
	public Game(){
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		initFrame();
		
		//Inicializando objetos
		try {
			background = ImageIO.read(getClass().getResource("/lousa_background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		rand = new Random();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		menu = new Menu();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(250, 0, 600, 600);
		word = new Words();
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
		word.tick();
		
		if(gameState == "NORMAL") {
			player.tick();
			
			if(tip && tipCount < 1) {
				tip = false;
				int pos = rand.nextInt(completeWord.length());
				while(!((currentWord.substring(pos, pos+1)).equals("_"))) {
					pos = rand.nextInt(completeWord.length());
				}
				strTip = "Tem a letra " + Player.removeAccent(completeWord).substring(pos, pos+1);
				tipCount++;
				
			}
			
			if(currentWord.equals(completeWord)) {
				if(word.numbers.size() >= word.maxWords) {
					gameState = "FIM";
				}else {
					gameState = "VITORIA";
				}
			}
		}
		
		else if(gameState == "NOVA PALAVRA") {
			//Resetando o player
			Arrays.fill(player.lastKeys, ' ');
			player.posCount = 0;
			player.playerLife = 0;
			
			//Resetando a dica
			strTip = "aperte SHIFT";
			tipCount = 0;
			
			//Selecionando a palavra e o tema
			path = "C:\\Users\\LAG20\\eclipse-workspace\\Jogo da Forca\\res\\" + themes[currentTheme] + ".txt";
			//System.out.println(themes[currentTheme]);
			completeWord = word.getWord(path);
			
			//Criando a palavra atual com os espaços sem letras
			emptySpaces = new char[completeWord.length()];	
			Arrays.fill(emptySpaces, '_');
			currentWord = new String(emptySpaces);
			//Iniciando o jogo
			gameState = "NORMAL";
		}
		
		else {
			menu.tick();
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
		
		g.drawImage(background, 0, 0, null);
		
		/* Renderizando o jogo */
		if(gameState == "NORMAL") {
			String lk = new String(player.lastKeys);
			//font = new Font("arial", Font.BOLD, 60);
			
			g.setColor(Color.white);
			g.setFont(font.deriveFont(130f));
			g.drawString(currentWord, 210, 610);
			g.setFont(font.deriveFont(45f));
			g.drawString("Letras erradas: " + lk, 10, 40);
			//g.drawString(X, 230, 30);
			g.drawString("Dica: " + strTip, 650, 40);
			player.render(g);
		}
		
		else if(gameState != "NOVA PALAVRA") {
			menu.render(g);
		}
		/****/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		bs.show();
	}

	public void run() {
		requestFocus();
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
		
		if(gameState != "NORMAL" || gameState != "NOVA PALAVRA") {
			if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W) {
				menu.up = true;
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				menu.down = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				menu.enter = true;
			}
		}
		
		if(gameState == "NORMAL" && e.getKeyCode() == KeyEvent.VK_SHIFT) {
			tip = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		tip = false;
		
		player.key = ' ';
		player.isPressed = false;
		
		menu.up = false;
		menu.down = false;
		menu.enter = false;
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
