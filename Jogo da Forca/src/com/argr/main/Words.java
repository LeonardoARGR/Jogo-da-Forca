package com.argr.main;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Words {
	
	private List<String> lastWords;
	private int wordCount = 0;
	private int maxWords = 10;
	private int maxCount = 0;
	
	public Words() {
		lastWords = new ArrayList<String>();
	}
	
	public String getWord(String path) {
		String word = "";
		File file = new File(path);
		int count = 0;
		if(file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				try {
					maxCount++;
					if(maxCount > maxWords) {
						maxCount = 1;
					}
					while((line = reader.readLine()) != null) {
						count++;
						if(count == maxCount) {
							word = line;
							lastWords.add(word);
							//System.out.println(lastWords);
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
