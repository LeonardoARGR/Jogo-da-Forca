package com.argr.main;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class Words {
	
	public int maxWords = 0;
	private int maxCount = 0;
	public List<Integer> numbers;
	
	public Words() {
		numbers = new ArrayList<>();
	}
	
	public String getWord(String path) {
		String word = "";
		File file = new File(path);
		LineNumberReader lnr;
		try {
			lnr = new LineNumberReader(new FileReader(file));
			lnr.skip(Long.MAX_VALUE);
			maxWords = lnr.getLineNumber()+1;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(maxWords);
		int count = 0;
		if(file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				try {
					maxCount = newNumber();
					//System.out.println(numbers);
					while((line = reader.readLine()) != null) {
						count++;
						if(count >= maxCount) {
							word = line;
							//System.out.println(numbers.size());
							//System.out.println(word);
							return word;
						}
						
					}
				}catch(IOException e) {}
				
			}catch(FileNotFoundException e) {}
			
		}
		return null;
	}
	
	public int newNumber() {
		int n = 0;
		if(numbers.isEmpty()) {
			n = Game.rand.nextInt(maxWords+1);
			if(n <= 0) {
				n = 1;
			}
			numbers.add(n);
			return n;
		}else {
			while(numbers.contains(n) || n == 0) {
				n = Game.rand.nextInt(maxWords+1);
			}
			numbers.add(n);
			return n;
		}
	}
	
	public void tick() {
		//System.out.println(count);
	}

}
