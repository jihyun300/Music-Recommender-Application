package com.example.anytimeanywhere.manager;

public class UserState{
	private static UserState instance;
	private double paceCurr;
	private int weatherCurr;
	
	public static UserState getInstance(){
		if(instance == null){
			instance = new UserState();
		}
		return instance;
	}

	private UserState() {
		
	}
	
	public void setPaceData(double walked){
		this.paceCurr = walked;
	}
	
	public double getPaceData(){
		return paceCurr;
	}
	
	public void setWeather(int weather){
		this.weatherCurr = weather;
	}
	
	public int getWeather(){
		return weatherCurr;
	}
	
}
