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
	
	public BufferedImage[] sprites;
	
	public boolean fail = false;
	
	
	
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
		
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public boolean isRepeatedKey() {
		for(int i = 0; i < Game.lastKeys.length; i++) {
			if(key == Game.lastKeys[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	public void tick() {
		
		if(isPressed && Game.gameState == "NORMAL") {
			isPressed = false;
			int count = 0;
			for(int i = 0; i < Game.completeWord.length(); i++) {
				if(key == Game.completeWord.charAt(i)) {
					System.out.println("Você acertou!!!");
					char[] newCurrent = Game.currentWord.toCharArray();
					newCurrent[i] = Game.completeWord.charAt(i);
					Game.currentWord = new String(newCurrent);
					count++;
				}
			}
			if(count == 0 && !isRepeatedKey()) {
				playerLife++;
				Game.lastKeys[posCount] = key;
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
		/*
		g.fillRect(50, 300, 60, 10);
		g.setFont(new Font("arial", Font.BOLD, 75));
		g.drawString("K", 50+4, 295);*/
	}
}
