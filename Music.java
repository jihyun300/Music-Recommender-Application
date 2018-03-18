package com.example.anytimeanywhere;



import java.util.Comparator;

import android.net.Uri;

public class Music {
	String Title; //mp3 tag 제목
	String Artist; // mp3 tag 참여 음악가
	int BPM; // 속성 값에서 파싱
	int Speed; // BPM 값에 따라 0~4 총 5단계로 구분 0 느림 1 조금 느림 2 보통 3 조금 빠름 4 빠름, 편한대로 소스구현가능 알아서
	int Weather; // 0 맑음 1 흐림 2 비 3 기타
	int Time; // 0 이른 아침 1 아침 2 점심 3 오후 4 저녁 5 밤 6 새벽
	int Genre[] = new int[9]; // 0 ~ 8 락 메탈 발라드 힙합 어쿠스틱 댄스 클럽 R&B 기타 0 false 1 true
	int Tag; // 0 신남 1 우울 2 사랑 3 위로 4 그리움
	Uri musicUri;
	Uri albumImgUri;
	
	double point;
	
	
	
	public final static Comparator<Music> comp_by_point= new Comparator<Music>() {
		@Override
		public int compare(Music arg0, Music arg1) {
			// TODO Auto-generated method stub
			
			int ret;
			if(arg0.point == arg1.point)
				ret = 0;
			else if(arg0.point<arg1.point)
				ret = -1;
			else
				ret = 1;
			
			return ret;
		}
	};

}