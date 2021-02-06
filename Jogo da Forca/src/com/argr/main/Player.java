package com.argr.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Player {
	
	private double x, y;
	private int WIDTH, HEIGHT;
	
	private BufferedImage[] sprites;
	
	private boolean fail = false;

	public char[] lastKeys;
	public char key = ' ';
	public boolean isPressed = false;
	
	public int playerLife = 0;
	public int posCount = 0;
	
	
	public Player(int x, int y, int WIDTH, int HEIGHT) {
		this.x = x;
		this.y = y;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		
		sprites = new BufferedImage[7];
		
		for(int i = 0; i < 7; i++) {
			sprites[i] = Game.spritesheet.getSprite(0 + (64*i), 0, 64, 64);
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
	
	public void tick() {
		
		if(isPressed) {
			isPressed = false;
			int count = 0;
			for(int i = 0; i < Game.completeWord.length(); i++) {
				if(key == Game.completeWord.charAt(i)) {
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
		g.drawImage(sprites[playerLife], getX(), getY(), WIDTH*8, HEIGHT*8, null);
		g.setColor(Color.black);
	}
}
