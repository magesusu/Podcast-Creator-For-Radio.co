package com.soundstreetmusic.ftp_downloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

public class AddCSV {

	public static List<RadioShow> locateShow(File csvFile){
		CSVReader reader = null;
		List<RadioShow> list= null;
		try {
			reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));

		ColumnPositionMappingStrategy<RadioShow> strat = new ColumnPositionMappingStrategy<RadioShow>();
		strat.setType(RadioShow.class);
		String[] columns = new String[] {"localPath", "cloudUrl","description", "coverUrl", "pubDate", "title", "length"};
		strat.setColumnMapping(columns);

		CsvToBean<RadioShow> csv = new CsvToBean<RadioShow>();
		list = csv.parse(strat, reader);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return list;
	}

	//Reference: https://github.com/mpatric/mp3agic
	public static void addShow(String url, String localPath, File csvFile){
		try {
			FileOutputStream input = new FileOutputStream(csvFile,true);
			OutputStreamWriter outWriter = null;
			outWriter = new OutputStreamWriter(input, "UTF-8");

			Mp3File mp3file = null;
			try {
				mp3file = new Mp3File(localPath);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}

			System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
			System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
			System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
			System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
			System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
			System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
			ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			System.out.println("Track: " + id3v1Tag.getTrack());
			System.out.println("Artist: " + id3v1Tag.getArtist());
			System.out.println("Title: " + id3v1Tag.getTitle());
			System.out.println("Album: " + id3v1Tag.getAlbum());
			System.out.println("Year: " + id3v1Tag.getYear());
			System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
			System.out.println("Comment: " + id3v1Tag.getComment());

			String description = id3v1Tag.getArtist();
			String title = id3v1Tag.getTitle();
			String cover = "http://soundstreetradio.com/wp-content/uploads/2016/12/cropped-Sフォント＿13n-1.jpg";
			String time = getDateAsRFC822String(new Date(mp3file.getLastModified()));
			String length = String.valueOf(mp3file.getLength());
			String filename = mp3file.getFilename();

			CSVWriter writer = new CSVWriter(outWriter);
			String str[] = {filename,url,description,cover,time,title,length};
			writer.writeNext(str);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Reference: https://github.com/mpatric/mp3agic
	public static SimpleDateFormat RFC822DATEFORMAT = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);

	//Reference: https://github.com/mpatric/mp3agic
	public static String getDateAsRFC822String(Date date) {
		return RFC822DATEFORMAT.format(date);
	}

}
