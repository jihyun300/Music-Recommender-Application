package com.example.anytimeanywhere.manager;

public class UserManager {
	private static UserManager instance;
	public static UserManager getInstance(){
		if(instance == null){
			instance = new UserManager();
		}
		return instance;
	}
	
	private UserManager() {
		
	}
	
	private double paceSet = -1;
	
	public void setPaceData(double walked){
		this.paceSet = walked;
	}
	
	public double getPaceData(){
		return paceSet;
	}
}
