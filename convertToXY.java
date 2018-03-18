package com.example.anytimeanywhere;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.lang.Object;

import org.json.*;

import android.util.Log;
public class convertToXY extends Thread{
	
	
	public void run()
	{
		this.ACT();
	}
	

	double x;
	double y;
	HashMap<String, Integer> top = new HashMap<String, Integer>();
	int a,b;  // for matching region algorithm
	int result_X;
	int result_Y;
	String[] token;
	String address;
	String[] add;
	String t,m,l; // top middle leaf
	convertToXY(double x, double y){
		// Creator
		this.x = x; // latitude
		this.y = y; // longitude
		top.put("서울특별시",11);top.put("대구광역시",27);top.put("광주광역시",29);
		top.put("울산광역시",31);top.put("강원도",42);top.put("전라북도",45);
		top.put("경상북도",47);top.put("제주특별자치도",50);top.put("부산광역시",26);
		top.put("인천광역시",28);top.put("대전광역시",30);top.put("경기도",41);
		top.put("충청북도",43);top.put("충청남도",44);top.put("전라남도",46);
		top.put("경상남도",48);			
	}
	
	void ACT()
	{
		try{
		googleAPI();
		splitingAddress();
		topMatching();
		middleMatching();
		leafMatching();
		}
		catch( Exception e )
		{
			System.out.println(e.toString());
		}
	}
	
	public int getX(){
		return result_X;
	}
	public int getY(){
		return result_Y;
	}
	public void googleAPI(){
		String URLstring;
		URLstring = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
					+x+","+y+"&sensor=true_or_false&language=ko";//&language=ko
		try {
			URL url = new URL(URLstring);
			Log.e("URLstring",URLstring);
			Scanner input = new Scanner(url.openStream());
			int i = 0;
			while(input.hasNext())
			{
				String line = input.nextLine();
				// matches function
				if(line.matches(".*\"formatted_address\".*"))
				{
					//i++;
					//if(i==3){
						//System.out.println(line);
						token = line.split("[\"]");
						address = token[3];
						//System.out.println(address);
						break;
					//}
				}
				//System.out.println(line);
				//input.close();
			}
			input.close();
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("invalid URL");
			e.printStackTrace();
		}
		catch( IOException ex){
			System.out.println("I/O errors");
		}
		
	}
	public void splitingAddress(){
		add= address.split(" "); // e.g.
		t = add[1]; // 
		m = add[2]; //
		l = add[3]; //
	}
	public void topMatching(){
		a = top.get(t).intValue();
		//System.out.println(a);
	}
	
	public void middleMatching(){
		String URLstring;
		URLstring = "http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."
				+a+".json.txt";
		String line = null;
		JSONArray jarray = null;
		try {
			URL url = new URL(URLstring);
			Scanner input = new Scanner(url.openStream());
			line = input.nextLine();
			//System.out.println(line);
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("invalid URL");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// text file to jason_array
		try {
			jarray = new JSONArray(line);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("wrong url");
			e.printStackTrace();
		}
		String value; int code;
		HashMap<String, Integer> middle = new HashMap<String,Integer>();
		for(int i=0;i<jarray.length();i++){
			JSONObject j = jarray.optJSONObject(i);
			try {
				value = j.getString("value");
				code = j.getInt("code");
				middle.put(value, code);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		b = middle.get(m).intValue(); // m  =
		//System.out.println(b);
	}
	public void leafMatching(){
		String URLstring;
		URLstring = "http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."
			+b+".json.txt";
		String line = null;
		JSONArray jarray = null;
		try {
			URL url = new URL(URLstring);
			Scanner input = new Scanner(url.openStream());
			line = input.nextLine();
			//System.out.println(line);
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("invalid URL");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// text file to jason_array
		try {
			jarray = new JSONArray(line);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("wrong url");
			e.printStackTrace();
		}
		
		String value;
		//for(int i=0;i<jarray.length();i++){
		int i=0;
			JSONObject j = jarray.optJSONObject(i);
			try {
				value = j.getString("value");
				//if(value.equals(l)){
					// when l(1이 아닌 L이다) equals to value, get x and y;
					result_X = j.getInt("x");
					result_Y = j.getInt("y");
				//}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
		
	}
	
}
