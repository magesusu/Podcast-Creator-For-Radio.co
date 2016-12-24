package com.soundstreetmusic.ftp_downloader;

public class RadioShow {
	    // 名前,ふりがな,アドレス,性別,誕生日
	    private String localPath, cloudUrl,description, coverUrl, pubDate, title, length ;

		public String getLocalPath() {
			return localPath;
		}

		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}

		public String getCloudUrl() {
			return cloudUrl;
		}

		public void setCloudUrl(String cloudUrl) {
			this.cloudUrl = cloudUrl;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getCoverUrl() {
			return coverUrl;
		}

		public void setCoverUrl(String coverUrl) {
			this.coverUrl = coverUrl;
		}

		public String getPubDate() {
			return pubDate;
		}

		public void setPubDate(String pubDate) {
			this.pubDate = pubDate;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLength() {
			return length;
		}

		public void setLength(String length) {
			this.length = length;
		}


}
