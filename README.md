#Podcast Creater For Radio.co

Using Radio.co's FTP account, a Dropbox account and a server to put a podcast playlist, you are easy to create a podcast.
Radio.co have nothing to do with this application.


#Instllation

This software is created by Java. If you haven't this, please install before running this. https://java.com/download/ 

1. Go to https://github.com/magesusu/podcast-creater-for-radio.co/releases

2. Download the least version zip and extract it.

3. Set all files in config folder. (Please look Settings section)

4. Run run.bat (Windows) or run.sh (Mac or Linux).

#Settings

settings.txt	Set Radio.co's FTP account, local media folder and another FTP account to put a podcast playlist.

podcast.txt	Set your podcast's show name.

dropbox_conf.app	Set your API key to use dropbox service. You can get here https://www.dropbox.com/developers/apps

header.xml and footer.xml	Set this for your station. KEEP IN MIND: After edit footer.xml, please delete the first two bytes by using binary editer. (Generally, you don't have to edit footer.xml) 

dropbox_auth.auth	This file will be genelated by this software. You don't need to edit.


#Reference Sites

Apache2.0

This software includes the work that is distributed in the Apache License 2.0.

https://commons.apache.org/proper/commons-net

http://commons.apache.org/proper/commons-beanutils

https://commons.apache.org/proper/commons-lang

https://github.com/FasterXML/jackson-core

http://opencsv.sourceforge.net

MIT

Code released under the MIT license https://opensource.org/licenses/mit-license.php

Copyright (c) 2006-2015 Michael Patricios. https://github.com/mpatric/mp3agic

Copyright (c) 2015 Dropbox Inc., http://www.dropbox.com/ https://github.com/dropbox/dropbox-sdk-java

Others

Copyright (C)1995-2002 ASH multimedia lab. http://ash.jp/java/htmlencode.htm

Copyright (C) 2016 ぱーくん plus idea All Rights Reserved. http://web.plus-idea.net/2011/06/javaftp

Copyright (C) Benjamin Nold. https://github.com/brnold/RSSFeedGenerator

Copyright c sinsengumi血風録 All Rights Reserved. http://sinsengumi.net/blog/2011/02/java%E3%81%A7ftp%E3%82%A2%E3%83%83%E3%83%97%E3%83%AD%E3%83%BC%E3%83%89%E3%82%92%E8%A1%8C%E3%81%86%E3%80%82
