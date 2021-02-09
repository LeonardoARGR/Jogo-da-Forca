package com.argr.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	private String[] options = {"Cores", "Alimentos", "Nomes"};
	private int currentOption, maxOption = options.length-1;
	public boolean up, down, enter;
	
	public Menu() {
		
	}
	
	public void tick() {
		if(Game.gameState == "MENU") {	
			if(up) {
				up = false;
				currentOption--;
				if(currentOption < 0) {
					currentOption = maxOption;
				}
			}else if(down) {
				down = false;
				currentOption++;
				if(currentOption > maxOption) {
					currentOption = 0;
				}
			}
			
			if(enter) {
				enter = false;
				Game.currentTheme = currentOption;
				Game.gameState = "NOVA PALAVRA";	
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 60));
		g.drawString("Escolha o tema:", 30, 100);
		
		for(int i = 0; i < options.length; i++) {
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString(options[i], 560, 100 + (i*90));
			g.drawString(">", 520, 100 + (currentOption*90));
		}
	}

}
