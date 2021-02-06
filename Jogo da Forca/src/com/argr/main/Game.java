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
	
	private Player player;
	
	private int number = 0;
	private int lastNumber = 0;
	

	private String[] words;
	public static String currentWord;
	public static String completeWord;
	private char[] emptySpaces;
	
	private boolean restartGame = false;
	private boolean quitGame = false;
	
	public static String gameState = "NOVA PALAVRA";
	
	private Random rand;
	
	private int count = 0, maxCount = 35;
	private boolean draw = false;
	
	
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
			if(currentWord.equals(completeWord)) {
				gameState = "VITORIA";
			}
		}else if(gameState == "NOVA PALAVRA") {
			//Resetando o player
			Arrays.fill(player.lastKeys, ' ');
			player.posCount = 0;
			player.playerLife = 0;
			
			//Selecionando a palavra
			while(number == lastNumber) {
				number = rand.nextInt(words.length);
			}
			lastNumber = number;
			
			//Criando a palavra atual com os espa�os sem letras
			emptySpaces = new char[words[number].length()];
			Arrays.fill(emptySpaces, '*');
			currentWord = new String(emptySpaces);
			
			//Salvando a palavra completa e iniciando o jogo
			completeWord = words[number];
			gameState = "NORMAL";
		}else if(gameState == "GAME OVER" || gameState == "VITORIA") {
			count++;
			if(count == maxCount) {
				count = 0;
				if(draw) {
					draw = false;
				}else {
					draw = true;
				}
			}
			if(restartGame) {	
				gameState = "NOVA PALAVRA";
				restartGame = false;
			}else if(quitGame) {
				System.exit(1);
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
		/* Renderizando o jogo */
		
		if(gameState == "NORMAL") {
			player.render(g);
			
			//Renderizando a palavra centralizada no eixo x
			Rectangle rect = new Rectangle(new Dimension(WIDTH, HEIGHT));
			renderWord(g, currentWord, rect, new Font("arial", Font.BOLD, 60));
			
			//Escrevendo as letras que o player errou na tela
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.black);
			g.drawString("Letras erradas: ", 10, 30);
			String lk = new String(player.lastKeys);
			g.drawString(lk, 230, 30);
		}else if(gameState == "GAME OVER") {
			g.setColor(Color.black);
			g.setFont(new Font("arial", Font.BOLD, 100));
			g.drawString("GAME OVER", 180, 300);
			g.setFont(new Font("arial", Font.BOLD, 35));
			if(draw) {
				g.drawString("Presione ENTER para recome�ar ou ESC para sair", 80, 400);
			}
			
		}else if(gameState == "VITORIA") {
			g.setColor(Color.black);
			g.setFont(new Font("arial", Font.BOLD, 90));
			g.drawString("VOC� ACERTOU!", 125, 280);
			g.setFont(new Font("arial", Font.BOLD, 35));
			if(draw) {
				g.drawString("Precione ENTER para gerar outra palavra ou ESC para sair", 15, 400);
			}
		}
		/****/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		bs.show();
	}
	
	//M�todo para renderizar a palavra centralizada no eixo x
	public void renderWord(Graphics g, String text, Rectangle rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = 500;
	    g.setFont(font);
	    g.drawString(text, x, y);	
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
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
		}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			quitGame = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		player.key = ' ';
		restartGame = false;
		quitGame = false;
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
