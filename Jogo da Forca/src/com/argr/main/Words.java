package com.argr.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Words {
	
	public Words() {
	}
	
	public String getWord(String path) {
		String word = "";
		File file = new File(path);
		if(file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				try {
					while((line = reader.readLine()) != null) {
						if(Game.rand.nextInt(100) < 30) {
							word = reader.readLine();
							System.out.println(word);
							return word;
						}
					}
				}catch(IOException e) {}
				
			}catch(FileNotFoundException e) {}
			
		}else {
			System.out.println("não existe");
		}
		return null;
	}
	
	public void tick() {
		//System.out.println(count);
	}

}
