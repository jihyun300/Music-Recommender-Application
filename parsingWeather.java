package com.example.anytimeanywhere;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class parsingWeather extends Thread{
	
	public void run(){
		this.requestWeather();
	}
	int x;
	int y;
	String weather;
	String[] splitW;
	String weatherInfo;
	parsingWeather(int x, int y){
		this.x = x;
		this.y = y;
		//requestWeather();
	}
	public void requestWeather(){
		//http://www.kma.go.kr/wid/queryDFS.jsp?gridx=58&gridy=125
		String URLstring;
		URLstring = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx="
				+x+"&gridy="+y;
		try {
			URL url = new URL(URLstring);
			Scanner input = new Scanner(url.openStream());
			int i = 0;
			while(input.hasNext()){
				String line = input.nextLine();		
				if(line.matches(".*wfKor.*")){
					weather = line;
					break;
				}
			}
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("invalid URL");
			e.printStackTrace();
		}
		catch( IOException ex){
			System.out.println("I/O errors");
		}
		
		splitW = weather.split("[<>]");
		weatherInfo = splitW[2]; // 맑음
	}
	public String getWeatherInfo(){
		return weatherInfo;
	}
}
