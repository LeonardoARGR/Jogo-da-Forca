package com.argr.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 700;
	
	private JFrame frame;
	private BufferedImage image;
	public static Spritesheet spritesheet;
	
	private boolean isRunning = false;
	private Thread thread;
	
	private Player player;

	public static Words word;
	public static String currentWord;
	public static String completeWord;
	private char[] emptySpaces;
	
	public static String gameState = "MENU";
	
	public static Random rand;
	
	public static FontMetrics metrics;
	
	private String path;

	public String[] themes = {"cor", "alimento", "nome"};
	
	private Menu menu;
	
	public static int currentTheme;
	
	private boolean tip = false;
	private String strTip = "aperte SHIFT";
	private int tipCount = 0;
	
	
	public Game(){
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		initFrame();
		
		//Inicializando objetos
		rand = new Random();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		menu = new Menu();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(250, 0, 64, 64);
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
				strTip = "Tem a letra " + Player.removeAccent(completeWord).substring(pos, pos+1).toUpperCase();
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
			Arrays.fill(emptySpaces, '*');
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
		
		/* Renderizando o jogo */
		player.render(g);
		
		Font font;	
		//Renderizando a palavra centralizada no eixo x
		if(gameState == "NORMAL") {
			font = new Font("arial", Font.BOLD, 60);
			metrics = g.getFontMetrics(font);
			g.setFont(font);
			g.drawString(currentWord, WIDTH/2 - (metrics.stringWidth(currentWord)/2), 500);
		}
		//Escrevendo as letras que o player errou na tela
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.setColor(Color.black);
		g.drawString("Letras erradas: ", 10, 30);
		String lk = new String(player.lastKeys);
		g.drawString(lk, 230, 30);
		
		g.drawString("Dica: " + strTip, 710, 30);
		
		if(gameState != "NOVA PALAVRA") {
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
