package com.argr.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	private String[] menuOptions = {"Cores", "Alimentos", "Nomes"};
	private String[] options = {"", "Novo tema", "Sair"};
	private int currentOption, maxOption = menuOptions.length-1;
	public boolean up, down, enter;
	
	public Menu() {
		
	}
	
	public void tick() {
		if(Game.gameState == "MENU") {	
			Game.word.numbers.clear();
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
				currentOption = 0;
				Game.gameState = "NOVA PALAVRA";
			}
		}else {
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
				if(Game.gameState == "VITORIA") {
					if(currentOption == 0) {
						Game.gameState = "NOVA PALAVRA";
					}else if(currentOption == 1) {
						Game.gameState = "MENU";
						currentOption = 0;
					}else {
						System.exit(1);
					}
				}else {
					if(currentOption == 0) {
						Game.word.numbers.clear();
						Game.gameState = "NOVA PALAVRA";
					}else if(currentOption == 1) {
						Game.gameState = "MENU";
						currentOption = 0;
					}else {
						System.exit(1);
					}
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(Game.gameState == "MENU") {
			g.setColor(Color.white);
			g.setFont(Game.font.deriveFont(60f));
			g.drawString("Escolha o tema:", 150, 100);
			
			for(int i = 0; i < menuOptions.length; i++) {
				g.setFont(Game.font.deriveFont(50f));
				g.drawString(menuOptions[i], 560, 100 + (i*90));
				g.drawString(">", 520, 100 + (currentOption*90));
			}
		}else if(Game.gameState == "VITORIA" || Game.gameState == "GAME OVER" || Game.gameState == "FIM") {
			Font font = Game.font.deriveFont(100f);
			String[] txt = {"Fim de jogo", "Você acertou!", "Você acertou todas as palavras!"};
			int txti;
			
			if(Game.gameState == "GAME OVER") {
				txti = 0;
				options[0] = "Reiniciar";
			}else if(Game.gameState == "VITORIA"){
				txti = 1;
				options[0] = "Continuar";
			}else {
				font = Game.font.deriveFont(60f);
			    txti = 2;
			    options[0] = "Reiniciar";
			}
			g.setColor(Color.white);
			g.setFont(font);
			Game.metrics = g.getFontMetrics(font);
			g.drawString(txt[txti], Game.WIDTH/2 - (Game.metrics.stringWidth(txt[txti])/2), 150);
			
			font = Game.font.deriveFont(40f);
			Game.metrics = g.getFontMetrics(font);
			String str = "A palavra completa era " + Game.completeWord;
			g.setFont(font);
			g.drawString(str, Game.WIDTH/2 - (Game.metrics.stringWidth(str)/2), 250);
			g.fillRect(Game.WIDTH/2 - (Game.metrics.stringWidth(str)/2), 259, Game.metrics.stringWidth(str), 5);
			
			for(int i = 0; i < options.length; i++) {
				font = Game.font.deriveFont(50f);
				Game.metrics = g.getFontMetrics(font);
				g.setFont(font);
				g.drawString(options[i], Game.WIDTH/2 - (Game.metrics.stringWidth(options[i])/2), 350 + (i*90));
				if(currentOption == i) {
					g.drawString(">", (Game.WIDTH/2 - (Game.metrics.stringWidth(options[i])/2)) - 50, 350 + (i*90));
				}
			}
			
		}
	}

}
