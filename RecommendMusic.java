package com.example.anytimeanywhere;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RecommendMusic {
	
	//UserState state;
	
	private MusicList MusicData; // ��ü ���� ����.
	
	public RecommendMusic(MusicList mMusicList)
	{
		MusicData = mMusicList;
	}
	
	public int classifyTime(int time){
		int ret = 0;
		if (6 <= time && time < 9)		ret= 0;
		else if (9<=time && time < 12)	ret= 1;
		else if (12<=time && time < 14)	ret= 2;
		else if (14<=time && time < 18)	ret= 3;
		else if (18<=time && time < 22)	ret= 4;
		else if (22<=time && time < 2)	ret= 5;
		else if (2<=time && time < 6)	ret= 6;
		return ret;
	}
	
	public int classifySpeed(double avg_speed, double cur_speed){
		int ret=0;
		double sigma = 10;
		if(cur_speed >= avg_speed+2*sigma)	ret = 4;
		else if(cur_speed >= avg_speed+0.5*sigma && cur_speed < avg_speed+2*sigma)	ret = 3;
		else if(cur_speed >= avg_speed-0.5*sigma && cur_speed < avg_speed+0.5*sigma)ret = 2;
		else if(cur_speed >= avg_speed-2*sigma && cur_speed < avg_speed-0.5*sigma)	ret = 1;
		else if(cur_speed < avg_speed-2*sigma)	ret = 0;
		return ret;
	}

	public int classifyBpm(int bpm){
		int ret =0;
		if(bpm>=130)	ret=4;
		else if(bpm>=120 && bpm<130)ret=3;
		else if(bpm>=105 && bpm<120)ret=2;
		else if(bpm>=90 && bpm<105)	ret=1;
		else if(bpm<90)	ret=0;
		return ret;
	}

	public double calcWeatherPoint(int weather,int cur_weather){
		double ret = 0.0;
		if(cur_weather==weather)	ret = 100;
		else if((cur_weather==0 && weather==1) || (cur_weather == 1 && weather == 0))	ret = 30;
		else if((cur_weather==1 && weather==2) || (cur_weather == 2 && weather == 1))	ret = 70;
		else if((cur_weather==2 && weather==3) || (cur_weather == 3 && weather == 2))	ret = 50;
		else if((cur_weather==1 && weather==3) || (cur_weather == 3 && weather == 1))	ret = 40;
		else	ret = 10;
		return ret;
	}
	
	public double calcTimePoint(int time, int cur_time){
		double ret = 0.0;
		if(time == cur_time)	ret = 100;
		else if( Math.abs(time-cur_time) == 1 || Math.abs(time-cur_time) == 6) ret = 70;
		else if( Math.abs(time-cur_time) == 2 || Math.abs(time-cur_time) == 5) ret = 30;
		else if( Math.abs(time-cur_time) == 3 || Math.abs(time-cur_time) == 4 ) ret = 10;		
		return ret;
	}
	
	public double calcGenrePoint(int[] cur_genre,int[] genre)
	{
		double ret = 0.0;
		int cur_genre_cnt=0,genre_cnt=0,same_cnt=0;
		for(int i=0;i<cur_genre.length;i++){
			if(cur_genre[i]==1){	cur_genre_cnt++;}
			if(genre[i]==1){	genre_cnt++;}
			if(cur_genre[i] == genre[i]){	same_cnt++;}
		}
		
		if(cur_genre_cnt == 1){
			if(genre_cnt == 1){
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 100;
			}
			else if(genre_cnt == 2){
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 90;				
			}
			else{
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 70;	
			}
		}
		else if(cur_genre_cnt == 2){
			if(genre_cnt == 1){
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 90;				
			}
			else if(genre_cnt == 2){
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 70;
				else if(same_cnt == 2)	ret = 100;
			}
			else{
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 60;
				else if(same_cnt == 2)	ret = 90;	
			}
		}
		else{
			if(genre_cnt == 1){
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 70;						
			}
			else if(genre_cnt == 2){
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 60;
				else if(same_cnt == 2)	ret = 90;	
			}
			else{
				if(same_cnt == 0) 		ret = 10;
				else if(same_cnt == 1)	ret = 40;
				else if(same_cnt == 2)	ret = 70;
				else					ret = 100;	
			}
		}
		return ret;	
	}
	
	public double calcTagPoint(int cur_tag,int tag)
	{
		double ret = 0.0;
		if(cur_tag == tag) 	ret = 100;
		else				ret = 10;
		return ret;
	}
	
	public double calcExternalInfoPoint(Music m1,Music m2){
		double ret;
		double weatherPoint;
		double timePoint;
		double genrePoint;
		double tagPoint;
		int cur_time,cur_weather;
		Random r = new Random();
		SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
		cur_time = Integer.parseInt(sdfNow.format(new Date(System.currentTimeMillis())));
		cur_time = classifyTime(20);
		cur_weather = 0;
		weatherPoint = calcWeatherPoint(m2.Weather,cur_weather);
		timePoint = calcTimePoint(m2.Time,cur_time);
		genrePoint = calcGenrePoint(m1.Genre,m2.Genre);
		tagPoint = calcTagPoint(m1.Tag,m2.Tag);
		ret = weatherPoint + timePoint + genrePoint + tagPoint;
		return ret;
	}
	
	public double calcCurrentBpm(Music m2){
		double ret=10.0;
		double cur_speed = 80;		//user's current speed.
		double avgUserSpeed = 80;	//user's initial speed.
		int curUserSpeed;
		int curBpm;
		curUserSpeed = classifySpeed(cur_speed,avgUserSpeed);
		curBpm = classifyBpm(m2.BPM);
		
		if(curUserSpeed==curBpm)	ret=100;
		else if(Math.abs(curUserSpeed - curBpm)==1)	ret=60;
		else if(Math.abs(curUserSpeed - curBpm)==2)	ret=30;
		else ret=10;
		
		return ret;
	}
	
	public double calcPoint(int type, Music m1, Music m2)
	{
		if(type==0)
		{ // type1. current bpm + external information
			double type1Point=0;
			type1Point = calcExternalInfoPoint(m1, m2) * 0.15 + calcCurrentBpm(m2) * 0.4;
			return type1Point;
		}
		else if(type==1)
		{ //type2. external information
			double type2Point=0;
			type2Point = calcExternalInfoPoint(m1,m2) * 0.25;
			return type2Point;
		}
		else
		{ // type3. random-based recommend system
			Random r = new Random();
			
			return r.nextDouble() * 10;
		}
	}
	
	// type: ���� ��õ Ÿ�� (3���� ���� ����)
	// mlist: ���� �÷������� ���� ����Ʈ
	MusicList getRecommList(int type, MusicList mlist)
	{
		if( 0>type  || type>2 ) // ���� ��õ Ÿ���� 1~3������ ���� 
			return null;
		
		MusicList ret = new MusicList();
		ret.musiclist.clear();
		Music currentMusic = mlist.musiclist.get(mlist.index);
		
		ret.musiclist = new ArrayList<Music>(mlist.musiclist.subList(0, mlist.index+1));
		ret.index = mlist.index;
		
		ArrayList<Music> recommList = 
				new ArrayList<Music>(mlist.musiclist.subList(mlist.index+1, mlist.musiclist.size()));
		
		for(int i=0 ; i<recommList.size() ; i++)
		{
			Music m = recommList.get(i);
			m.point = this.calcPoint(type, currentMusic, m);
		}	
		
		Collections.sort(recommList, Music.comp_by_point);
		
		
		ret.musiclist.addAll(recommList);
		
		return ret;
	}
}
