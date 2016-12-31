package com.soundstreetmusic.ftp_downloader;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


public class PodcastCreater {

	final static String configFile = "config/settings.txt";
	private static String hostname;
	private static int portno;
	private static String username;
	private static String password;
	private static String localRootFolder;
	private static File csv;
	private static String podcastOutputPass;
	private static String dropboxMediaDirectory;

	private static String xHostname;
	private static String xUserName;
	private static String xPassword;
	private static String podcastXmlUploadPass;

    //Binary転送Modeを利用?(true=Yes、false=No)
    private static final boolean binaryTransfer = true;

    //PASV Modeを利用?(true=Yes、false=No)
    private static final boolean usePassiveMode = true;


    public static void main(String[] args) throws Exception {

    	loadSetting();
    	csv = new File("temp/show.csv");
    	System.out.println("******** Podcast Creater For Radio.co ********");

    	if(args.length == 0){
    		System.out.println("No Command Input. Run all mode.");
            runAll();
    	}else if(args[0] == null){
            System.out.println("No Command Input. Run all mode.");
            runAll();
        }else switch(args[0]){
            case "all":
                runAll();
                break;
            case "media":
                System.out.println("We will download media.");
                break;
            case "build":
                System.out.println("We will build a podcast csv.");
                break;
            case "set":
            	System.out.println("Write Settings");
            	hostname = args[1];
            	username = args[2];
            	password = args[3];
            	xHostname = args[6];
            	xUserName = args[7];
            	xPassword = args[8];
            	podcastXmlUploadPass = args[9];
            	saveSetting();
            	saveJsonSetting(args[4],args[5]);
            	break;
            default:
                System.out.println("Unknown Command");
        }
    	System.out.println("");
    	System.out.println("Process completed!! Thank you for using.");
    	
    }
    
    private static void runAll() throws Exception {
        execute(hostname, username, password, usePassiveMode);
    	PodcastUpdate.update(csv,podcastOutputPass);
    }

    private static void loadSetting() {
        Properties prop = new Properties();

        try {
          prop.load(new FileInputStream(configFile));
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Error: Make sure settings.txt Exit.");
          System.exit(-1);
        }
        hostname = prop.getProperty("hostname");
        username = prop.getProperty("username");
        password = prop.getProperty("password");
        portno = Integer.parseInt(prop.getProperty("portno"));
        localRootFolder = prop.getProperty("localMediaFolder");
        podcastOutputPass = prop.getProperty("podcastOutputPass");

        xHostname = prop.getProperty("xdomainHostname");
        xUserName = prop.getProperty("xdomainUsername");
        xPassword = prop.getProperty("xdomainPassword");
        podcastXmlUploadPass = prop.getProperty("podcastXmlUploadPass");
        dropboxMediaDirectory = prop.getProperty("dropboxMediaDirectory");

	}
    private static void saveSetting(){
    	Properties prop = new Properties();
    	
    	prop.setProperty("hostname", hostname);
        prop.setProperty("username", username);
        prop.setProperty("password", password);
        prop.setProperty("portno", String.valueOf(portno));
        prop.setProperty("localMediaFolder", localRootFolder);
        prop.setProperty("podcastOutputPass", podcastOutputPass);
        prop.setProperty("xdomainHostname", xHostname);
        prop.setProperty("xdomainUsername", xUserName);
        prop.setProperty("xdomainPassword", xPassword);
        prop.setProperty("podcastXmlUploadPass", podcastXmlUploadPass);
        prop.setProperty("dropboxMediaDirectory", dropboxMediaDirectory);

        try {
			prop.store(new FileOutputStream(configFile), "Edited By Lancher");
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
        System.out.println("Saved FTP settings.");
    }

    private static void saveJsonSetting(String key, String secret){
        try{
            File file = new File("config/dropbox_conf.app");

              PrintWriter json = new PrintWriter(new BufferedWriter(new FileWriter(file)));
              json.println("{");
              json.println("  \"key\": \""+key+"\",");
              json.println("  \"secret\": \""+secret+"\"");
              json.println("}");
              json.close();
              
          }catch(IOException e){
            e.printStackTrace();
          }
        System.out.println("Saved Dropbox settings.");
    }
    
	private static boolean execute(String address, String username,
            String password, boolean usePassiveMode) throws IOException{
        boolean success = false;
        FTPClient client = new FTPClient();
        try {

        	client = connectToFTP(address,username,password,usePassiveMode,client);
        	success = FTPReply.isPositiveCompletion(client.getReplyCode());
            System.out.println("Connection test => " + (success ? "OK" : "NG"));

            String fileNames[] = client.listNames();
            System.out.println(client.getReplyString());

            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++) {
                    System.out.println("Name=" +fileNames[i]);
                }
            }


            System.out.println("-----------------------------------");
            for (FTPFile f : client.listFiles()) {
                getRemoteFiles(f, client);
            }



            client.logout();
            System.out.println(client.getReplyString());

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (client.isConnected()) client.disconnect();

        }
        return success;
    }

	//Reference: http://web.plus-idea.net/2011/06/javaftp/
    private static FTPClient connectToFTP(String address, String username,
            String password, boolean usePassiveMode, FTPClient client){
    	try{
        System.out.println("connect....");
        client.setControlEncoding("MS932");
        client.connect(address, portno);
        System.out.println("Connected to Server:" + address + " on "+client.getRemotePort());

        System.out.println(client.getReplyString());

        client.login(username, password);
        System.out.println(client.getReplyString());
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {

            System.out.println("Login Failed");
            client.disconnect();
            return null;
        }

        if (binaryTransfer){
            client.setFileType(FTP.BINARY_FILE_TYPE);
            System.out.println("Mode binaryTransfer = true");
        }

        if (usePassiveMode) {
            client.enterLocalPassiveMode();
            System.out.println("Mode usePassiveMode = ON");
        } else {
            client.enterLocalActiveMode();
            System.out.println("Mode usePassiveMode = OFF");
        }

    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
        return client;
    }

    //Reference: http://web.plus-idea.net/2011/06/javaftp/
    private static void getRemoteFiles(FTPFile file, FTPClient client)throws Exception{

        if (!file.getName().equals(".")&&!file.getName().equals("..")) {

            String currentDir = client.printWorkingDirectory();
            if(file.isFile()){
                //If File
                String filename = client.printWorkingDirectory()+ (currentDir.endsWith("/") ? "" : "/")+file.getName();
                filename = new String(filename.getBytes("MS932"), "UTF-8");

                String utf8filename = client.printWorkingDirectory()+ (currentDir.endsWith("/") ? "" : "/")+file.getName();

                System.out.println("FileName="+utf8filename);
                String fileDirectName = new String(file.getName().getBytes("MS932"), "UTF-8");
                System.out.println(fileDirectName);

                //FILE GET
                FileOutputStream os = null;
                try{
                	String localPass = localRootFolder + fileDirectName;

                	//File Exsist CHECK
                	java.io.File check = new java.io.File(localPass);
                	if(check.exists()){
                		System.out.println("File Already exsists. SKIP");
                		return;
                	}
                	//File SEND
                	System.out.println("Downloading File...");
                    os = new FileOutputStream(localPass);
                    client.retrieveFile(utf8filename, os);
                    os.close();
                    System.out.println("FTP GET COMPLETED");

                    //java.io.File original = new java.io.File(localPass);
                    //UPLOAD Gdrive.uploadG(service, fileDirectName, original);
                    String link = DriveUpload.upload(localPass,dropboxMediaDirectory+fileDirectName);
                    if(!link.equals(null)){
                    	AddCSV.addShow(link,localPass,csv);
                    }

                }catch(Exception ex){
                	ex.printStackTrace();

                }

                return;
            }else if(file.isDirectory()){
                //If Directory
                client.doCommand("CWD", client.printWorkingDirectory()+ (currentDir.endsWith("/") ? "" : "/")+file.getName());
                for (FTPFile f : client.listFiles()) {
                    getRemoteFiles(f, client);
                }
                client.doCommand("CDUP", "");

                return;
            }else{
                //No Action
            }

        }else{

            return;
        }
    }


    //Reference: http://sinsengumi.net/blog/2011/02/java%E3%81%A7ftp%E3%82%A2%E3%83%83%E3%83%97%E3%83%AD%E3%83%BC%E3%83%89%E3%82%92%E8%A1%8C%E3%81%86%E3%80%82/
    public static void ftpUpload(String file){
    	FTPClient ftpClient = new FTPClient();
    	try{
	        ftpClient.setControlEncoding("SJIS");
	        ftpClient.connect(xHostname);
	        printFtpReply(ftpClient);
	        ftpClient.login(xUserName, xPassword);
	        printFtpReply(ftpClient);
	        ftpClient.pasv();
	        printFtpReply(ftpClient);

	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        printFtpReply(ftpClient);
	        FileInputStream fis = new FileInputStream(file);
	        ftpClient.storeFile(podcastXmlUploadPass, fis);
	        printFtpReply(ftpClient);
	        ftpClient.logout();
	        printFtpReply(ftpClient);
	        ftpClient.disconnect();
	        printFtpReply(ftpClient);

    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

  //Reference: http://sinsengumi.net/blog/2011/02/java%E3%81%A7ftp%E3%82%A2%E3%83%83%E3%83%97%E3%83%AD%E3%83%BC%E3%83%89%E3%82%92%E8%A1%8C%E3%81%86%E3%80%82/
    private static void printFtpReply(FTPClient ftpClient) {

        System.out.print(ftpClient.getReplyString());

        int replyCode = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(replyCode)) {
            System.out.println("COMMAND SENDING ERROR");
        }
    }
}