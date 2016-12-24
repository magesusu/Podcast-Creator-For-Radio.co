package com.soundstreetmusic.ftp_downloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

//Reference: https://github.com/mpatric/mp3agic
public class PodcastUpdate {

	private static String outputPass;

	public static void update(File csv,String outputPass) {
		PodcastUpdate.outputPass = outputPass;
		String propsFile = "config/podcast.txt";
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propsFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Expected argument 1 to be a valid properties file");
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		//String show = prop.getProperty("show");
		String headerXml = prop.getProperty("header");
		String footerXml = prop.getProperty("footer");

		LinkedList<String> items = new LinkedList<String>();

		items.add("\t<lastBuildDate>"+AddCSV.getDateAsRFC822String(new Date())+"</lastBuildDate>");

		List<RadioShow> list= AddCSV.locateShow(csv);

		addMp3sToPodcast(items,prop,list);

		writePodcastRss(items,headerXml,footerXml);

		try {
			PodcastCreater.ftpUpload(outputPass);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void writePodcastRss(LinkedList<String> items,String headerXml, String footerXml) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPass),"UTF-8"));
			writeFile(headerXml, bw);
			for (String s : items) {
				bw.write(s);
				bw.newLine();
			}
			writeFile(footerXml, bw);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addMp3sToPodcast(LinkedList<String> items,Properties prop,List<RadioShow> list) {

		for (int i = list.size() - 1; i >= 0; i--) {
			RadioShow mp3File = list.get(i);
			if (mp3File.getLocalPath().endsWith(".mp3")) {
				try {
					items.add("\t<item>");
					items.add("\t\t<title>"+Html.encode(mp3File.getTitle())+ "</title>");
					items.add("\t\t<description>"+Html.encode(mp3File.getDescription())+"</description>");
					items.add("\t\t<itunes:subtitle>"+Html.encode(mp3File.getDescription())+"</itunes:subtitle>");
					items.add("\t\t<itunes:summary>"+Html.encode(mp3File.getDescription())+"</itunes:summary>");
					items.add("\t\t<itunes:image href='"+Html.encode(mp3File.getCoverUrl())+"' />");
					items.add("\t\t<pubDate>" + Html.encode(mp3File.getPubDate()) + "</pubDate>");
					items.add("\t\t<enclosure url='"+ Html.encode(mp3File.getCloudUrl()) + "' length='"+ mp3File.getLength() + "' type='audio/mpeg'/>");
					items.add("\t\t<guid>"+ Html.encode(mp3File.getCloudUrl()) + "</guid>");
					items.add("\t</item>");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	private static void writeFile(String fileName, BufferedWriter bw)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			bw.write(line);
			bw.newLine();
		}
		br.close();
		bw.flush();
	}

}