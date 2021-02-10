package com.argr.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	private String[] menuOptions = {"Cores", "Alimentos", "Nomes"};
	private String[] options = {"", "Novo tema", "Sair"};
	private int currentOption, maxOption = menuOptions.length-1;
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
				currentOption = 0;
			}
		}else if(Game.gameState == "VITORIA" || Game.gameState == "GAME OVER" || Game.gameState == "FIM") {
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
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setColor(Color.black);
			g.setFont(new Font("arial", Font.BOLD, 60));
			g.drawString("Escolha o tema:", 30, 100);
			
			for(int i = 0; i < menuOptions.length; i++) {
				g.setFont(new Font("arial", Font.BOLD, 50));
				g.drawString(menuOptions[i], 560, 100 + (i*90));
				g.drawString(">", 520, 100 + (currentOption*90));
			}
		}else if(Game.gameState == "VITORIA" || Game.gameState == "GAME OVER" || Game.gameState == "FIM") {
			Graphics2D g2 = (Graphics2D) g;
			Font font = new Font("arial", Font.BOLD, 90);
			FontMetrics fm = g.getFontMetrics(font);
			String[] txt = {"Fim de jogo", "Você acertou!", "Você acertou todas as palavras!"};
			int txti;
			
			if(Game.gameState == "GAME OVER") {
				txti = 0;
				options[0] = "Reiniciar";
			}else if(Game.gameState == "VITORIA"){
				txti = 1;
				options[0] = "Continuar";
			}else {
				font = new Font("arial", Font.BOLD, 60);
				fm = g.getFontMetrics(font);
			    txti = 2;
			    options[0] = "Reiniciar";
			}
			
			g2.setColor(new Color(255, 255, 255, 240));
			g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			
			g.setColor(Color.black);
			g.setFont(font);
			g.drawString(txt[txti], Game.WIDTH/2 - (fm.stringWidth(txt[txti])/2), 200);
			
			for(int i = 0; i < options.length; i++) {
				font = new Font("arial", Font.BOLD, 50);
				fm = g.getFontMetrics(font);
				g.setFont(font);
				g.drawString(options[i], Game.WIDTH/2 - (fm.stringWidth(options[i])/2), 350 + (i*90));
				if(currentOption == i) {
					g.drawString(">", (Game.WIDTH/2 - (fm.stringWidth(options[i])/2)) - 50, 350 + (i*90));
				}
			}
			
		}
	}

}
