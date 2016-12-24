package com.soundstreetmusic.ftp_downloader;

//Copyright (C)1995-2002 ASH multimedia lab. http://ash.jp/
/** HTMLユーティリティ **/
public class Html {
  /** HTMLエンコードが必要な文字 **/
  static char[] htmlEncChar = {'&', '"', '<', '>'};
  /** HTMLエンコードした文字列 **/
  static String[] htmlEncStr = {"&amp;", "&quot;", "&lt;", "&gt;"};

  /**
  * HTMLエンコード処理。
  *   &,",<,>の置換
  **/
  public static String encode (String strIn) {
    if (strIn == null) {
      return(null);
    }

    // HTMLエンコード処理
    StringBuffer strOut = new StringBuffer(strIn);
    // エンコードが必要な文字を順番に処理
    for (int i = 0; i < htmlEncChar.length; i++) {
      // エンコードが必要な文字の検索
      int idx = strOut.toString().indexOf(htmlEncChar[i]);

      while (idx != -1) {
        // エンコードが必要な文字の置換
        strOut.setCharAt(idx, htmlEncStr[i].charAt(0));
        strOut.insert(idx + 1, htmlEncStr[i].substring(1));

        // 次のエンコードが必要な文字の検索
        idx = idx + htmlEncStr[i].length();
        idx = strOut.toString().indexOf(htmlEncChar[i], idx);
      }
    }
    return(strOut.toString());
  }
}