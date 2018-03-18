package com.example.anytimeanywhere;



import java.util.Comparator;

import android.net.Uri;

public class Music {
	String Title; //mp3 tag ����
	String Artist; // mp3 tag ���� ���ǰ�
	int BPM; // �Ӽ� ������ �Ľ�
	int Speed; // BPM ���� ���� 0~4 �� 5�ܰ�� ���� 0 ���� 1 ���� ���� 2 ���� 3 ���� ���� 4 ����, ���Ѵ�� �ҽ��������� �˾Ƽ�
	int Weather; // 0 ���� 1 �帲 2 �� 3 ��Ÿ
	int Time; // 0 �̸� ��ħ 1 ��ħ 2 ���� 3 ���� 4 ���� 5 �� 6 ����
	int Genre[] = new int[9]; // 0 ~ 8 �� ��Ż �߶�� ���� ����ƽ �� Ŭ�� R&B ��Ÿ 0 false 1 true
	int Tag; // 0 �ų� 1 ��� 2 ��� 3 ���� 4 �׸���
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