package com.argr.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Player {
	
	private double x, y;
	
	private BufferedImage[] sprites;

	public char[] lastKeys;
	public char key = ' ';
	public boolean isPressed = false;
	
	public int playerLife = 0;
	public int posCount = 0;
	
	
	public Player(int x, int y, int WIDTH, int HEIGHT) {
		this.x = x;
		this.y = y;
		
		sprites = new BufferedImage[7];
		
		for(int i = 0; i < 7; i++) {
			sprites[i] = Game.spritesheet.getSprite(0 + (700*i), 0, 700, 700);
		}
		
		lastKeys = new char[6];
		
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	//Método para ver se a tecla que o jogador clicou é repetida ou não
	public boolean isRepeatedKey() {
		for(int i = 0; i < lastKeys.length; i++) {
			if(key == lastKeys[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String removeAccent(String str) {
		String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(normalizedString).replaceAll("");
	}
	
	public void tick() {
		
		if(isPressed) {
			isPressed = false;
			String cwna = removeAccent(Game.completeWord);
			int count = 0;
			for(int i = 0; i < cwna.length(); i++) {
				if(key == cwna.charAt(i)) {
					//Adicionando a tecla certa na palavra atual
					char[] newCurrent = Game.currentWord.toCharArray();
					newCurrent[i] = Game.completeWord.charAt(i);
					Game.currentWord = new String(newCurrent);
					count++;
				}
			}
			
			//Verificando se a tecla estava errada
			if(count == 0 && !isRepeatedKey()) {
				playerLife++;
				lastKeys[posCount] = key;
				posCount++;
			}
			
		}
		
		if(playerLife >= 6) {
			Game.gameState = "GAME OVER";
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(sprites[playerLife], -80, getY(), null);
		g.setColor(Color.black);
	}
}
