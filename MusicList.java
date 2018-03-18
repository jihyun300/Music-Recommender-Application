package com.example.anytimeanywhere;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import android.app.Activity;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicList extends Activity{

	private static final Uri sArtworkUri=Uri.parse("content://media/external/audio/albumart");

	int index; // now playing song's index
	ArrayList<Music> musiclist = new ArrayList<Music>();

	public MusicList() {
		index=0;
	}

	public MusicList(Cursor musicCursor) {
		index=0;

		
		if(musicCursor!= null && musicCursor.moveToFirst()) {
			do {
				Music music = new Music();
				
				int musicIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
				int albumIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
				int musicTitleCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
				int artistCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
				int dataCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
				
				String musicID = musicCursor.getString(musicIDCol);
				String albumID = musicCursor.getString(albumIDCol);
				String data = musicCursor.getString(dataCol);

				music.Title = musicCursor.getString(musicTitleCol);
				music.Artist = musicCursor.getString(artistCol);
				music.musicUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + musicID);
				music.albumImgUri = ContentUris.withAppendedId(sArtworkUri, Integer.parseInt(albumID));
				
				File tempFile = new File(data); // 1) """" + data + """" 2) data
				
				try {
					MP3File mp3 = (MP3File)AudioFileIO.read(tempFile);
					Tag tag = mp3.getTag();

					String temp = tag.getFirst(FieldKey.COMMENT);
					String comment[] = temp.split("/");

					music.BPM = Integer.parseInt(comment[0]);
					music.Weather = Integer.parseInt(comment[1]);
					music.Time = Integer.parseInt(comment[2]);
					for(int i=0; i<9; i++) {
						music.Genre[i] = (int)(comment[3].charAt(i)-'0');
						System.out.print(music.Genre[i]);
					}
					music.Tag = Integer.parseInt(comment[4]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				musiclist.add(music);
				
			}while(musicCursor.moveToNext());
			
		}
		
		// 제일 위에 있는 곡은 랜덤으로 지정
		Random r = new Random();
		int randomnumber = r.nextInt(this.musiclist.size());
		
		Music a = this.musiclist.get(0);
		this.musiclist.set(0, this.musiclist.get(randomnumber));
		this.musiclist.set(randomnumber, a);
	}
}
