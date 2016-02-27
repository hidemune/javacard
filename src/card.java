
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import java.sql.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hdm
 */
public class card {
    public static String version = "2.0.0.9 (rev 20160227)";
    /*  【修正履歴】
     * SJISは機種依存文字や全角ハイフンが文字化けするため修正：Ver 2.0.0.1          2015.5.16
     * ヘルプのVersion表記の修正忘れを修正：Ver 2.0.0.2                            2015.5.16
     * ファイル名指定しないで参照ボタンを押した場合、初期表示ディレクトリがHomeDirに
     * なってしまうのをカレントディレクトリに変更 Ver 2.0.0.2                       2015.5.16
     * ファイル選択ダイアログでファイル名・フォルダ名の変更モードを抑止:Ver 2.0.0.2     2013.5.17
     * ファイル選択ダイアログでcsvファイルのみ指定：Ver 2.0.0.2                     2013.5.17
     * ポップアップメニューを追加：Ver 2.0.0.2                                      2013.5.17
     * HTML作成機能を追加：Ver 2.0.0.2                                              2013.5.17
     * テキストエリアにアンドゥ・リドゥを追加：Ver 2.0.0.2                          2013.5.17
     * メール送信機能追加：Ver 2.0.0.3                                              2013.5.24
     * LibreOffice Writer 連携機能追加：Ver 2.0.0.3                                 2013.5.27
     * LibreOffice Writer 連携で目次を自動で作成するよう修正：Ver 2.0.0.4               2013.5.28
     * リストの「＋」「−」キーで行の順序を入れ替えられるようにした：Ver 2.0.0.4             2013.5.28
     * HTML作成機能を、LegacyとFrameに分けた：Ver 2.0.0.4                           2013.5.30
     * Modyfied マークの表示に対応：Ver 2.0.0.4                                     2013.5.30
     * HTML(Frame版)の目次を入れ子構造に修正：Ver 2.0.0.5                               2013.5.31
     * LibreOfficeの文書作成時に、見出しの番号付けをした場合
     * 各レコード(CSVの行ごと)で１からになっていたのを修正：Ver 2.0.0.5                   2013.6.1
     * 画像の挿入に対応：Ver 2.0.0.5                                                2013.6.2
     * HTML作成のLegacyモードを廃止：Ver 2.0.0.5                                     2013.6.2
     * メイン画面のヘルプメニューに「ヘルプ」を追加：Ver 2.0.0.5                         2013.6.3
     * URLの抽出・変換（HTML作成機能）を追加: Ver 2.0.0.6                             2013.7.10
     * HTML作成機能をCSSフレームに変更: Ver 2.0.0.6                                  2013.7.20
     * HTMLを開く際に、ブラウザのキャッシュを無効化                                   2013.10.24  
     * HTMLのフレームトップに画像を表示するよう修正                                   2014.2.9
     * 項目1でEnterキーを押すと日付が設定されるよう修正。                               2014.3.3
     * HTML目次で、同じHタグは表示しないよう修正。                                      2014.6.24
     * 右クリックメニューに「画像取り込み」追加                                         2014.11.23
    * スマホでのWebページ表示に最適化                                                           2015.10.19
    * JDBC対応                                                                                                              2016.2.27
    */
    public static ConfigJFrame ConfFrm;
    public static CardJFrame CardFrm;
    public static HtmlJFrame HtmlFrm;   //html作成：Ver 2.0.0.2
    public static MailJFrame MailFrm;   //Mail送信：Ver 2.0.0.3
    public static ArrayList<String> ArrStr[];   //CSVの中身を全て退避
    //ポップアップメニューに対応：Ver 2.0.0.2
    public static int Seireki0Gengo1;
    public static String Editor;
    public static String LibreExePath;
    public static String fntName = "";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CardFrm = new CardJFrame();
        ConfFrm = new ConfigJFrame();
        HtmlFrm = new HtmlJFrame();     //html作成：Ver 2.0.0.2
        MailFrm = new MailJFrame();     //メール送信：Ver 2.0.0.3
        CardFrm.initView();
        ArrStr = new ArrayList[6];
        for (int i = 0; i < 6; i++) {
            ArrStr[i] = new ArrayList();
            ArrStr[i].clear();
        }
        //プロパティファイルの読み込み
        Properties config = new Properties();
        try {
            //config.load(new FileInputStream("card.properties"));
            config.load(new InputStreamReader(new FileInputStream("card.properties"), "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        int x = Integer.parseInt(config.getProperty("x", "100"));
        int y = Integer.parseInt(config.getProperty("y", "100"));
        int width = Integer.parseInt(config.getProperty("width", "400"));
        int height = Integer.parseInt(config.getProperty("height", "300"));
        
        CardFrm.setBounds(x, y, width, height);
        
        //初期化
        initFrame();
        
        //コンポーネントのサイズ
        Integer[] size = new Integer[30];
        size[0] = Integer.parseInt(config.getProperty("size0", "1"));
        size[1] = Integer.parseInt(config.getProperty("size1", "120"));
        size[2] = Integer.parseInt(config.getProperty("size2", "25"));
        size[3] = Integer.parseInt(config.getProperty("size3", "25"));
        size[4] = Integer.parseInt(config.getProperty("size4", "25"));
        size[5] = Integer.parseInt(config.getProperty("size5", "25"));
        size[6] = Integer.parseInt(config.getProperty("size6", "25"));
        size[7] = Integer.parseInt(config.getProperty("size7", "100"));
        size[8] = Integer.parseInt(config.getProperty("size8", "100"));
        size[9] = Integer.parseInt(config.getProperty("size9", "100"));
        size[10] = Integer.parseInt(config.getProperty("size10", "100"));
        size[11] = Integer.parseInt(config.getProperty("size11", "100"));
        //分割サイズ設定
        CardFrm.setSize(size);
        
        //表示キー項目
        CardFrm.setLKey(Integer.parseInt(config.getProperty("LKey", "0")));
        CardFrm.setRKey(Integer.parseInt(config.getProperty("RKey", "1")));
        //検索条件
        CardFrm.setKeyModeL(Integer.parseInt(config.getProperty("LKeyMode", "0")));
        CardFrm.setKeyModeR(Integer.parseInt(config.getProperty("RKeyMode", "0")));
        
        CardFrm.setSelectedTab(Integer.parseInt(config.getProperty("Tab", "0")));
        
        //文字コード
        ConfFrm.setMojiCode(Integer.parseInt(config.getProperty("MojiCode", "0")));
        
        //ポップアップメニューに対応：Ver 2.0.0.2
        Seireki0Gengo1 = Integer.parseInt(config.getProperty("Seireki0Gengo1", "1"));
        Editor = config.getProperty("Editor", "notepad.exe");
        LibreExePath = config.getProperty("LibreExePath", "");
        
        //メール送信に対応：Ver 2.0.0.3
        String[] str = {"","","","","","","","","",""};
        str[0] = config.getProperty("MailTitle", "");;
        str[1] = config.getProperty("MailTo", "");;
        str[2] = config.getProperty("MailFrom", "");;
        str[3] = config.getProperty("MailHost", "");;
        str[4] = config.getProperty("MailSmtpHost", "");;
        str[5] = config.getProperty("MailUser", "");;
        String src = config.getProperty("MailPass", "");
        str[6] = encode(src);
        
        MailFrm.setMailFrm(str);
        
        fntName = config.getProperty("FontFamily", "Dialog");
        System.out.println(fntName);
        Font fnt = new Font(fntName,Font.PLAIN,14);
        CardFrm.setFont(fnt);
        
        //CSVファイル名
        String csvF = config.getProperty("csvFile", "");
        //CSVファイルの読み込み
        String[] str2 = {"","","","","","","","","",""};
        str2[0] = csvF;
        ConfFrm.setText(str2);
        readCSV();
        
        CardFrm.setVisible(true);
    }
    
    //暗号化
    private static String encode(String text) {
        try {
            byte bt[] = text.getBytes("UTF-8");
            for (int i = 0; i < bt.length; i++) {
                bt[i] = (byte)((int)bt[i] ^ 10);
            }
            return new String(bt, "UTF-8");
        }catch (Exception e){
            return "";
        }
    }
    
    //html作成：Ver 2.0.0.2
    public static void writeHtml() {
        /*
         * このメソッドはVer 2.0.0.5で廃止されました
         * 
         */
        //すでにファイルまたはディレクトリあればエラー
        //環境設定画面の取得
        String[] strConf = {"","","","","","","","","",""};
        ConfFrm.getText(strConf);
        String dirS = strConf[0].replaceAll(".csv", "").replaceAll(".CSV", "");
        //JOptionPane.showMessageDialog(HtmlFrm, dirS);
        File dir = new File(dirS);
        if (dir.exists()) {
            JOptionPane.showMessageDialog(HtmlFrm, "この名前はすでに存在します。\n処理を中断しました。\n" + dirS);
            return;
        }
        
        //確認メッセージ
        if (JOptionPane.showConfirmDialog(HtmlFrm, "Htmlファイル(Legacy版)を作成します。\nよろしいですか？") != JOptionPane.YES_OPTION) {
            return;
        }
        //フォルダ作成
        System.out.println("フォルダを作成します。" + dir.getAbsolutePath());
        dir.mkdir();
        
        //繰り返し部分
        System.out.println("繰り返し部分を取得します。&&LOOP_START&&〜&&LOOP_END&&");
        String indexStr = HtmlFrm.getIndexText();
        String wk = indexStr.substring(indexStr.indexOf("&&LOOP_START&&") + "&&LOOP_START&&".length(), indexStr.indexOf("&&LOOP_END&&"));
        String loopStr = wk.trim();
        //JOptionPane.showMessageDialog(HtmlFrm, loopStr);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ArrStr[0].size(); i++) {
            System.out.println("Loop処理。Index:" + i);
            //index.html 繰り返し部分
            DecimalFormat f = new DecimalFormat("00000");
            String page = "page" + f.format(i + 1) + ".html";
            String strLine = loopStr.replaceAll("&&PAGE&&", Matcher.quoteReplacement(page));
            strLine = strLine.replaceAll("&&COL1&&", Matcher.quoteReplacement(Html.encode(ArrStr[0].get(i))));
            strLine = strLine.replaceAll("&&COL2&&", Matcher.quoteReplacement(Html.encode(ArrStr[1].get(i))));
            strLine = strLine.replaceAll("&&COL3&&", Matcher.quoteReplacement(Html.encode(ArrStr[2].get(i))));
            strLine = strLine.replaceAll("&&COL4&&", Matcher.quoteReplacement(Html.encode(ArrStr[3].get(i))));
            strLine = strLine.replaceAll("&&COL5&&", Matcher.quoteReplacement(Html.encode(ArrStr[4].get(i))));
            strLine = strLine.replaceAll("&&INDEX&&", Matcher.quoteReplacement(String.valueOf(i + 1)));
            sb.append(strLine);
            sb.append("\n");
            //page00000.html
            String pageTxt = HtmlFrm.getPageText();
            pageTxt = pageTxt.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
            pageTxt = pageTxt.replaceAll("&&COL1&&", Matcher.quoteReplacement(Html.encode(ArrStr[0].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL2&&", Matcher.quoteReplacement(Html.encode(ArrStr[1].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL3&&", Matcher.quoteReplacement(Html.encode(ArrStr[2].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL4&&", Matcher.quoteReplacement(Html.encode(ArrStr[3].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL5&&", Matcher.quoteReplacement(Html.encode(ArrStr[4].get(i))));
            pageTxt = pageTxt.replaceAll("&&TEXT&&", Matcher.quoteReplacement(ArrStr[5].get(i)));   //ここだけエンコード無し
            pageTxt = pageTxt.replaceAll("&&TEXTENC&&", Matcher.quoteReplacement(Html.encode(ArrStr[5].get(i))));
            pageTxt = pageTxt.replaceAll("&&INDEX&&", Matcher.quoteReplacement(String.valueOf(i + 1)));
            //前・次のファイル名
            String prePage;
            if (i > 0) {
                prePage = "href=\"page" + f.format(i) + ".html\"";
            }else {
                prePage = "";
            }
            String nextPage;
            if (i < ArrStr[0].size() - 1) {
                nextPage = "href=\"page" + f.format(i + 2) + ".html\"";
            }else {
                nextPage = "";
            }
            pageTxt = pageTxt.replaceAll("&&PRE&&", Matcher.quoteReplacement(prePage));
            pageTxt = pageTxt.replaceAll("&&NEXT&&", Matcher.quoteReplacement(nextPage));
            //JOptionPane.showMessageDialog(HtmlFrm, pageTxt);
            //page0000.html 保存
            try {
                File csv = new File(dirS + File.separator + page);
                // 常に新規作成
                PrintWriter bw;
                bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"UTF-8")));
                //html
                bw.write(pageTxt);
                bw.println();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(HtmlFrm, "エラーが発生しました\n" + page);
                return;
            }
        }
        //インデックスファイル書き込み
        indexStr = indexStr.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        
        String str = sb.toString();
        Matcher m = Pattern.compile("&&LOOP_START&&.*&&LOOP_END&&", Pattern.DOTALL).matcher(indexStr);
        indexStr = m.replaceAll(Matcher.quoteReplacement(str));
        //JOptionPane.showMessageDialog(HtmlFrm, indexStr);
        try {
            File csv = new File(dirS + File.separator + "index.html");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"UTF-8")));
            //html
            bw.write(indexStr);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(HtmlFrm, "エラーが発生しました\nindex.html");
            return;
        }
        
        //終了メッセージ
        JOptionPane.showMessageDialog(HtmlFrm, "HTML作成処理が完了しました。\n" + dir.getAbsolutePath());
    }
    
    //URL抽出・変換: Ver 2.0.0.6
    /** URLを抽出するための正規表現パターン */
    public static final Pattern convURLLinkPtn = 
        Pattern.compile
        ("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+",
        Pattern.CASE_INSENSITIVE);
    /**
     * 指定された文字列内のURLを、正規表現を使用し、
     * リンク（a href=...）に変換する。
     * @param str 指定の文字列。
     * @return リンクに変換された文字列。
     */
    public static String convURLLink(String str) {
        Matcher matcher = convURLLinkPtn.matcher(str);
        return matcher.replaceAll("<a href=\"$0\" target=\"_top\">$0</a>");
    }
    
    //html(Frame)作成：Ver 2.0.0.4
    public static void writeHtmlFrame() {
        //すでにファイルまたはディレクトリあればエラー
        //環境設定画面の取得
        String[] strConf = {"","","","","","","","","",""};
        ConfFrm.getText(strConf);
        String dirS = strConf[0].replaceAll(".csv", "").replaceAll(".CSV", "");
        String imgDirStr = dirS + "/image";
        //JOptionPane.showMessageDialog(HtmlFrm, dirS);
        File dir = new File(dirS);
        File imgdir = new File(imgDirStr);
        if (dir.exists()) {
            JOptionPane.showMessageDialog(ConfFrm, "この名前はすでに存在します。\n処理を中断しました。\n" + dirS);
            return;
        }
        
        //確認メッセージ
        if (JOptionPane.showConfirmDialog(ConfFrm, "Htmlファイル(Frame版)を作成します。\nよろしいですか？") != JOptionPane.YES_OPTION) {
            return;
        }
        //フォルダ作成
        System.out.println("フォルダを作成します。" + dir.getAbsolutePath());
        dir.mkdir();
        imgdir.mkdir();
        //繰り返し部分
        System.out.println("繰り返し部分を取得します。&&LOOP_START&&〜&&LOOP_END&&");
        String indexStr = FileToString("template/jcontents.html");
        //無い場合があるので例外処理追加
        String wk = "";
        try {
            wk = indexStr.substring(indexStr.indexOf("&&LOOP_START&&") + "&&LOOP_START&&".length(), indexStr.indexOf("&&LOOP_END&&"));
        }catch (Exception e) {
            //何もしない
            e.printStackTrace();
        }
        String loopStr = wk.trim();
        //JOptionPane.showMessageDialog(HtmlFrm, loopStr);
        StringBuilder sb = new StringBuilder();
        StringBuilder sbIreko = new StringBuilder();
        for (int i = 0; i < ArrStr[0].size(); i++) {
            System.out.println("Loop処理。Index:" + i);
            String m1maeWords[] = {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
            if (0 < i) {
                m1maeWords[0] = ArrStr[0].get(i - 1);
                m1maeWords[1] = ArrStr[1].get(i - 1);
                m1maeWords[2] = ArrStr[2].get(i - 1);
                m1maeWords[3] = ArrStr[3].get(i - 1);
                m1maeWords[4] = ArrStr[4].get(i - 1);
            }
            String mSakiWords[] = { ArrStr[0].get(i), ArrStr[1].get(i), ArrStr[2].get(i), ArrStr[3].get(i), ArrStr[4].get(i)};
            
            //index.html 繰り返し部分
            DecimalFormat f = new DecimalFormat("00000");
            String page = "page" + f.format(i + 1) + ".html";
            String strLine = loopStr.replaceAll("&&PAGE&&", Matcher.quoteReplacement(page));
            strLine = strLine.replaceAll("&&COL1&&", Matcher.quoteReplacement(Html.encode(ArrStr[0].get(i))));
            strLine = strLine.replaceAll("&&COL2&&", Matcher.quoteReplacement(Html.encode(ArrStr[1].get(i))));
            strLine = strLine.replaceAll("&&COL3&&", Matcher.quoteReplacement(Html.encode(ArrStr[2].get(i))));
            strLine = strLine.replaceAll("&&COL4&&", Matcher.quoteReplacement(Html.encode(ArrStr[3].get(i))));
            strLine = strLine.replaceAll("&&COL5&&", Matcher.quoteReplacement(Html.encode(ArrStr[4].get(i))));
            strLine = strLine.replaceAll("&&INDEX&&", Matcher.quoteReplacement(String.valueOf(i + 1)));
            sb.append(strLine);
            sb.append("\n");
            
            //入れ子構造対応の目次：Ver 2.0.0.5
            for (int j = 0; j < 5; j++) {
                //前と同じなら非表示
                String strOnaji = "";
                boolean isOnaji = true;     //見出し判定用初期値
                for (int k = 0; k < j + 1; k++) {
                    if (!mSakiWords[k].equals(m1maeWords[k])) {
                        //前まで含めて全部同じかどうか
                        isOnaji = false;
                    }
                }
                if (isOnaji) {
                    strOnaji = " id=\"onaji\"";
                }
                //最後のアイテムならリンクを表示
                boolean isLast = false;
                if (j >= 4) {
                    isLast = true;
                }else {
                    if (ArrStr[j + 1].get(i).equals("")) {
                        isLast = true;
                    }
                }
                String strLink = "";
                String strLinkE = "";
                if (isLast) {
                    //最後のアイテムは必ず表示
                    strOnaji = "";
                    strLink = "<a href=\"" + page + "\" target=\"right\">";
                    strLinkE = "</a>";
                }
                //大見出し変わった場合のみ改行
                String strBr = "";
                if ((j == 0) && ((!mSakiWords[j].equals(m1maeWords[j])))){
                    strBr = "<br>\n";
                }
                if (!Html.encode(ArrStr[j].get(i)).equals("")) {
                    sbIreko.append(strBr);
                    sbIreko.append("" + strLink + Html.encode(ArrStr[j].get(i)) + strLinkE + "");
                    sbIreko.append("\n");
                }
            }
            
            //画像挿入対応Ver 2.0.0.5
            String strSrcTxt = ArrStr[5].get(i);
            StringBuilder sbOrg = new StringBuilder();
            StringBuilder sbEnc = new StringBuilder();
            //イメージタグの行を編集　行ごとにエンコード
            String strList[] = strSrcTxt.split("\n");
            String imgtag = "&&IMAGE&&";
            for (int j = 0; j < strList.length; j++) {
                int Idx = strList[j].indexOf(imgtag);
                if (Idx == 0) {
                    //ファイル名取り出し
                    String fileName = strList[j].substring(imgtag.length());
                    //ファイルコピー
                    File moto = new File(fileName);
                    File saki = new File(dirS + "/" + fileName);
                    fileCopy(moto, saki);
                    //タグ作成
                    String tag = "<img border=\"0\" src=\"" + fileName + "\"><br>";
                    //バッファ追加
                    sbOrg.append(tag + "\n");
                    sbEnc.append(tag + "<br>\n");
                } else {
                    sbOrg.append(strList[j] + "\n");
                    sbEnc.append(Html.encode(strList[j]) + "<br>\n");
                }
            }
            //page00000.html
            String pageTxt = FileToString("template/page.html");
            pageTxt = pageTxt.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
            pageTxt = pageTxt.replaceAll("&&COL1&&", Matcher.quoteReplacement(Html.encode(ArrStr[0].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL2&&", Matcher.quoteReplacement(Html.encode(ArrStr[1].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL3&&", Matcher.quoteReplacement(Html.encode(ArrStr[2].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL4&&", Matcher.quoteReplacement(Html.encode(ArrStr[3].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL5&&", Matcher.quoteReplacement(Html.encode(ArrStr[4].get(i))));
            pageTxt = pageTxt.replaceAll("&&TEXT&&", Matcher.quoteReplacement(convURLLink(sbOrg.toString())));   //ここだけエンコード無し
            pageTxt = pageTxt.replaceAll("&&TEXTENC&&", Matcher.quoteReplacement(convURLLink(sbEnc.toString())));
            pageTxt = pageTxt.replaceAll("&&INDEX&&", Matcher.quoteReplacement(String.valueOf(i + 1)));
            //前・次のファイル名
            String prePage;
            if (i > 0) {
                prePage = "href=\"page" + f.format(i) + ".html\"";
            }else {
                prePage = "";
            }
            String nextPage;
            if (i < ArrStr[0].size() - 1) {
                nextPage = "href=\"page" + f.format(i + 2) + ".html\"";
            }else {
                nextPage = "";
            }
            pageTxt = pageTxt.replaceAll("&&PRE&&", Matcher.quoteReplacement(prePage));
            pageTxt = pageTxt.replaceAll("&&NEXT&&", Matcher.quoteReplacement(nextPage));
            //JOptionPane.showMessageDialog(HtmlFrm, pageTxt);
            //page0000.html 保存
            try {
                File csv = new File(dirS + File.separator + page);
                // 常に新規作成
                PrintWriter bw;
                bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"UTF-8")));
                //html
                bw.write(pageTxt);
                bw.println();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\n" + page);
                return;
            }
        }
        //目次ファイル書き込み
        indexStr = indexStr.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        
        String str = sb.toString();
        Matcher m = Pattern.compile("&&LOOP_START&&.*&&LOOP_END&&", Pattern.DOTALL).matcher(indexStr);
        indexStr = m.replaceAll(Matcher.quoteReplacement(str));
        indexStr = indexStr.replaceAll("&&LOOP&&", sbIreko.toString());
        //JOptionPane.showMessageDialog(HtmlFrm, indexStr);
        try {
            File csv = new File(dirS + File.separator + "jcontents.html");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"UTF-8")));
            //html
            bw.write(indexStr);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\njcontents.html");
            return;
        }
        
        //ファイル書き込み
        str = FileToString("template/index.html");
        str = str.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        try {
            File file = new File(dirS + File.separator + "index.html");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
            //html
            bw.write(str);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\nindex.html");
            return;
        }
        
        //ファイル書き込み
        str = FileToString("template/jframe_top.html");
        str = str.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        try {
            File file = new File(dirS + File.separator + "jframe_top.html");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
            //html
            bw.write(str);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\njframe_top.html");
            return;
        }
        
        //ファイル書き込み
        str = FileToString("template/page00000.html");
        str = str.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        try {
            File file = new File(dirS + File.separator + "page00000.html");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
            //html
            bw.write(str);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\npage00000.html");
            return;
        }
        
        //ファイル書き込み
        str = FileToString("template/jcard.css");
        str = str.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        try {
            File file = new File(dirS + File.separator + "jcard.css");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
            //html
            bw.write(str);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\njcard.css");
            return;
        }
        
        //終了メッセージ
        JOptionPane.showMessageDialog(ConfFrm, "HTML作成処理が完了しました。\n" + dir.getAbsolutePath());
    }
    
    //html(CSSFrame)作成：Ver 2.0.0.6
    public static void writeHtmlCSSFrame() {
        //タイムスタンプ
        long now = System.currentTimeMillis();
        //すでにファイルまたはディレクトリあればエラー
        //環境設定画面の取得
        String[] strConf = {"","","","","","","","","",""};
        ConfFrm.getText(strConf);
        String dirS = strConf[0].replaceAll(".csv", "").replaceAll(".CSV", "");
        String imgDirStr = dirS + "/image";
        //JOptionPane.showMessageDialog(HtmlFrm, dirS);
        File dir = new File(dirS);
        File imgdir = new File(imgDirStr);
        if (dir.exists()) {
            JOptionPane.showMessageDialog(ConfFrm, "この名前はすでに存在します。\n処理を中断しました。\n" + dirS);
            return;
        }
        
        //確認メッセージ
        if (JOptionPane.showConfirmDialog(ConfFrm, "Htmlファイル(CSSFrame版)を作成します。\nよろしいですか？") != JOptionPane.YES_OPTION) {
            return;
        }
        //フォルダ作成
        System.out.println("フォルダを作成します。" + dir.getAbsolutePath());
        dir.mkdir();
        imgdir.mkdir();
        //繰り返し部分
        System.out.println("繰り返し部分を取得します。&&LOOP_START&&〜&&LOOP_END&&");
        String indexStr = FileToString("template/jcontents.html");
        //無い場合があるので例外処理追加
        String wk = "";
        try {
            wk = indexStr.substring(indexStr.indexOf("&&LOOP_START&&") + "&&LOOP_START&&".length(), indexStr.indexOf("&&LOOP_END&&"));
        }catch (Exception e) {
            //何もしない
            e.printStackTrace();
        }
        String loopStr = wk.trim();
        //JOptionPane.showMessageDialog(HtmlFrm, loopStr);
        StringBuilder sb = new StringBuilder();
        StringBuilder sbIreko = new StringBuilder();
        for (int i = 0; i < ArrStr[0].size(); i++) {
            System.out.println("Loop処理。Index:" + i);
            String m1maeWords[] = {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
            if (0 < i) {
                m1maeWords[0] = ArrStr[0].get(i - 1);
                m1maeWords[1] = ArrStr[1].get(i - 1);
                m1maeWords[2] = ArrStr[2].get(i - 1);
                m1maeWords[3] = ArrStr[3].get(i - 1);
                m1maeWords[4] = ArrStr[4].get(i - 1);
            }
            String mSakiWords[] = { ArrStr[0].get(i), ArrStr[1].get(i), ArrStr[2].get(i), ArrStr[3].get(i), ArrStr[4].get(i)};
            
            //jcontents.html 繰り返し部分
            /*
            DecimalFormat f = new DecimalFormat("00000");
            String page = "page" + f.format(i + 1) + ".html";
            if (i == 0) {
                page = "index.html";
            }
            */
            String page = ArrStr[4].get(i) + ".html";   //ファイル名を使用
            
            String strLine = loopStr.replaceAll("&&PAGE&&", Matcher.quoteReplacement(page));
            strLine = strLine.replaceAll("&&COL1&&", Matcher.quoteReplacement(Html.encode(ArrStr[0].get(i))));
            strLine = strLine.replaceAll("&&COL2&&", Matcher.quoteReplacement(Html.encode(ArrStr[1].get(i))));
            strLine = strLine.replaceAll("&&COL3&&", Matcher.quoteReplacement(Html.encode(ArrStr[2].get(i))));
            strLine = strLine.replaceAll("&&COL4&&", Matcher.quoteReplacement(Html.encode(ArrStr[3].get(i))));
            strLine = strLine.replaceAll("&&COL5&&", ""); //ファイル名として使用
            strLine = strLine.replaceAll("&&INDEX&&", Matcher.quoteReplacement(String.valueOf(i + 1)));
            sb.append(strLine);
            sb.append("\n");
            
            //入れ子構造対応の目次：Ver 2.0.0.5
            for (int j = 0; j < 4; j++) {
                //前と同じなら非表示
                String strOnaji = "";
                boolean isOnaji = true;     //見出し判定用初期値
                for (int k = 0; k < j + 1; k++) {
                    if (!mSakiWords[k].equals(m1maeWords[k])) {
                        //前まで含めて全部同じかどうか
                        isOnaji = false;
                    }
                }
                if (isOnaji) {
                    strOnaji = " id=\"onaji\"";
                }
                //最後のアイテムならリンクを表示
                boolean isLast = false;
                if (j >= 3) {
                    isLast = true;
                }else {
                    if (ArrStr[j + 1].get(i).equals("")) {
                        isLast = true;
                    }
                }
                String strLink = "";
                String strLinkE = "";
                if (isLast) {
                    //最後のアイテムは必ず表示
                    strOnaji = "";
                    strLink = "<a href=\"" + page + "?=" + Long.toString(now) + "\" target=\"_top\">";
                    strLinkE = "</a>";
                }
                //大見出し変わった場合のみ改行
                String strBr = "";
                if ((j == 0) && ((!mSakiWords[j].equals(m1maeWords[j])))){
                    strBr = "<br>\n";
                }
                if (strOnaji.equals("")) {
                    if (!Html.encode(ArrStr[j].get(i)).equals("")) {
                        sbIreko.append(strBr);
                        //sbIreko.append("<h" + (j+1) + strOnaji + ">" + strLink + Html.encode(ArrStr[j].get(i)) + strLinkE + "</h" + (j+1) + ">");
                        //sbIreko.append("<br/>");
                        sbIreko.append("" + strLink + Html.encode(ArrStr[j].get(i)) + strLinkE + "");
                        sbIreko.append("<br/>");
                        sbIreko.append("\n");
                    }
                }
            }
            
            //画像挿入対応Ver 2.0.0.5
            String strSrcTxt = ArrStr[5].get(i);
            StringBuilder sbOrg = new StringBuilder();
            StringBuilder sbEnc = new StringBuilder();
            
            //ファイルコピー(画面トップ画像)
            String fileName = "image/HP_Back.png";
            File moto = new File(fileName);
            File saki = new File(dirS + "/" + fileName);
            fileCopy(moto, saki);
            
            //イメージタグの行を編集　行ごとにエンコード
            String strList[] = strSrcTxt.split("\n");
            String imgtag = "&&IMAGE&&";
            for (int j = 0; j < strList.length; j++) {
                int Idx = strList[j].indexOf(imgtag);
                if (Idx == 0) {
                    //ファイル名取り出し
                    fileName = strList[j].substring(imgtag.length());
                    //ファイルコピー
                    moto = new File(fileName);
                    saki = new File(dirS + "/" + fileName);
                    fileCopy(moto, saki);
                    //タグ作成
                    String tag = "<img border=\"0\" src=\"" + fileName + "\"><br>";
                    //バッファ追加
                    sbOrg.append(tag + "\n");
                    sbEnc.append(tag + "<br>\n");
                } else {
                    sbOrg.append(strList[j] + "\n");
                    sbEnc.append(Html.encode(strList[j]) + "<br>\n");
                }
            }
            //page00000.html
            String pageTxt = FileToString("template/page.html");
            pageTxt = pageTxt.replaceAll("&&TIMESTAMP&&", Long.toString(now));
            pageTxt = pageTxt.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
            pageTxt = pageTxt.replaceAll("&&COL1&&", Matcher.quoteReplacement(Html.encode(ArrStr[0].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL2&&", Matcher.quoteReplacement(Html.encode(ArrStr[1].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL3&&", Matcher.quoteReplacement(Html.encode(ArrStr[2].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL4&&", Matcher.quoteReplacement(Html.encode(ArrStr[3].get(i))));
            pageTxt = pageTxt.replaceAll("&&COL5&&", Matcher.quoteReplacement(Html.encode(ArrStr[4].get(i))));
            pageTxt = pageTxt.replaceAll("&&TEXT&&", Matcher.quoteReplacement(convURLLink(sbOrg.toString())));   //ここだけエンコード無し
            pageTxt = pageTxt.replaceAll("&&TEXTENC&&", Matcher.quoteReplacement(convURLLink(sbEnc.toString())));
            pageTxt = pageTxt.replaceAll("&&INDEX&&", Matcher.quoteReplacement(String.valueOf(i + 1)));
            //前・次のファイル名
            String prePage;
            if (i > 0) {
                prePage = "href=\"" + ArrStr[4].get(i - 1) + ".html\"";
            }else {
                prePage = "";
            }
            String nextPage;
            if (i < ArrStr[0].size() - 1) {
                nextPage = "href=\"" + ArrStr[4].get(i + 1) + ".html\"";
            }else {
                nextPage = "";
            }
            
            pageTxt = pageTxt.replaceAll("&&PRE&&", Matcher.quoteReplacement(prePage));
            pageTxt = pageTxt.replaceAll("&&NEXT&&", Matcher.quoteReplacement(nextPage));
            //JOptionPane.showMessageDialog(HtmlFrm, pageTxt);
            //page0000.html 保存
            try {
                File csv = new File(dirS + File.separator + page);
                // 常に新規作成
                PrintWriter bw;
                bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"UTF-8")));
                //html
                bw.write(pageTxt);
                bw.println();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\n" + page);
                return;
            }
        }
        //目次ファイル書き込み
        indexStr = indexStr.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        
        String str = sb.toString();
        Matcher m = Pattern.compile("&&LOOP_START&&.*&&LOOP_END&&", Pattern.DOTALL).matcher(indexStr);
        indexStr = m.replaceAll(Matcher.quoteReplacement(str));
        indexStr = indexStr.replaceAll("&&LOOP&&", sbIreko.toString());
        //JOptionPane.showMessageDialog(HtmlFrm, indexStr);
        try {
            File csv = new File(dirS + File.separator + "jcontents.html");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"UTF-8")));
            //html
            bw.write(indexStr);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\njcontents.html");
            return;
        }
        
        //ファイル書き込み
        str = FileToString("template/pure-drawer.css");
        str = str.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        try {
            File file = new File(dirS + File.separator + "pure-drawer.css");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
            //html
            bw.write(str);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\npure-drawer.css");
            return;
        }
        str = FileToString("template/pure-drawer.min.css");
        str = str.replaceAll("&&TITLE&&", Matcher.quoteReplacement(Html.encode(strConf[1])));
        try {
            File file = new File(dirS + File.separator + "pure-drawer.min.css");
            // 常に新規作成
            PrintWriter bw;
            bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
            //html
            bw.write(str);
            bw.println();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\npure-drawer.min.css");
            return;
        }
        
        //終了メッセージ
        JOptionPane.showMessageDialog(ConfFrm, "HTML作成処理が完了しました。\n" + dir.getAbsolutePath());
    }
    
    /**
     * ファイルコピーします。
     *
     * @param srcPath コピー元
     * @param destPath コピー先
     */
    public static void fileCopy(File srcPath, File destPath) {

        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        
        //同じファイルなら抜ける
        if (srcPath.getAbsolutePath().equals(destPath.getAbsolutePath())) {
            return;
        }
        
        //念の為、先ファイルをバックアップ
        if (destPath.exists()) {
            File fileB = new File(destPath.getAbsolutePath() + "~");
            if (fileB.exists()) {
                fileB.delete();
            }
            destPath.renameTo(fileB);
        }
        
        try {
            srcChannel = new FileInputStream(srcPath).getChannel();
            destChannel = new FileOutputStream(destPath).getChannel();

            srcChannel.transferTo(0, srcChannel.size(), destChannel);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (srcChannel != null) {
                try {
                    srcChannel.close();
                } catch (IOException e) {
                }
            }
            if (destChannel != null) {
                try {
                    destChannel.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    //ファイルの中身を文字列として返します。パスのセパレータは / を想定しています。
    public static String FileToString(String path) {
        //path = path.replaceAll("/", File.separator);
        
        File file = new File(path); // CSVデータファイル
        if (!file.exists()) {
            JOptionPane.showMessageDialog(ConfFrm, "ファイルが存在しません。\n" + path);
            return "";
        }
        
        //ファイルの読み込み
        try {
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            // 最終行まで読み込む
            String line = "";
            while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            br.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            // Fileオブジェクト生成時の例外捕捉
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました");
            return "";
        } catch (IOException e) {
            // BufferedReaderオブジェクトのクローズ時の例外捕捉
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました");
            return "";
        }
    }
    
    public static void delCard(int idx) {
        if (JOptionPane.showConfirmDialog(CardFrm, "削除してもよろしいですか？") != JOptionPane.YES_OPTION) {
            return;
        }
        
        //jdbc
        String[] strC = {"","","","","","","","","",""};
        ConfFrm.getText(strC);
        if (strC[0].startsWith("jdbc:postgresql")) {
                try {
                Properties prop = new Properties();
                prop.setProperty("user", "testuser");
                prop.setProperty("password", "");
                
                //  JDBC Driverのロード
                Class.forName("org.postgresql.Driver");
                // 接続
                Connection conn =
                DriverManager.getConnection
                (strC[0], prop);
                //実行
                String sql = "DELETE FROM  data WHERE id = ?;";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(ArrStr[4].get(idx)));
                int rows = pstmt.executeUpdate();
                
                //画面のリストに反映
                CardFrm.setList(true);

                // ステートメントをクローズ
                pstmt.close();
                // 接続をクローズ
                conn.close();

                card.readCSV();
            }catch (Exception e){
                JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\n" + e.getLocalizedMessage());
                e.printStackTrace();
            }
            return;
        }
        
        //CSVから１行削除
        for (int i = 0; i < ArrStr.length; i++) {
            ArrStr[i].remove(idx);
        }
        card.writeCSV(false, CardFrm);
        card.readCSV();
    }
    
    public static void updtCard(int idx) {
        if (JOptionPane.showConfirmDialog(CardFrm, "更新してもよろしいですか？") != JOptionPane.YES_OPTION) {
            return;
        }
        //１行変更して更新
        String[] str = {"","","","","","","","","",""};
        CardFrm.getText(str);
        
        //jdbc
        String[] strC = {"","","","","","","","","",""};
        ConfFrm.getText(strC);
        if (strC[0].startsWith("jdbc:postgresql")) {
                try {
                Properties prop = new Properties();
                prop.setProperty("user", "testuser");
                prop.setProperty("password", "");
                
                //  JDBC Driverのロード
                Class.forName("org.postgresql.Driver");
                // 接続
                Connection conn =
                DriverManager.getConnection
                (strC[0], prop);
                //実行
                String sql = "UPDATE data SET key1=?,key2=?,key3=?,key4=?,key5=?,honbun=?,sortkey=? WHERE id = ?;";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, str[0]);
                pstmt.setString(2, str[1]);
                pstmt.setString(3, str[2]);
                pstmt.setString(4, str[3]);
                pstmt.setString(5, str[4]);
                pstmt.setString(6, str[5]);
                pstmt.setString(7, str[4]);
                
                pstmt.setInt(8, Integer.parseInt(ArrStr[4].get(idx)));
                int rows = pstmt.executeUpdate();
                
                //画面のリストに反映
                CardFrm.setList(true);

                // ステートメントをクローズ
                pstmt.close();
                // 接続をクローズ
                conn.close();

                card.readCSV();
            }catch (Exception e){
                JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\n" + e.getLocalizedMessage());
                e.printStackTrace();
            }
            return;
        }
        
        for (int i = 0; i < ArrStr.length; i++) {
            //System.err.println(str[i]);
            ArrStr[i].remove(idx);
            ArrStr[i].add(idx, str[i]);
        }
        
        
        card.writeCSV(false, CardFrm);
        card.readCSV();
    }
    
    public static void addCard() {
        if (JOptionPane.showConfirmDialog(CardFrm, "登録してもよろしいですか？") != JOptionPane.YES_OPTION) {
            return;
        }
        //CSVに１行追加
        String[] str = {"","","","","","","","","",""};
        CardFrm.getText(str);
        
        //jdbc
        String[] strC = {"","","","","","","","","",""};
        ConfFrm.getText(strC);
        if (strC[0].startsWith("jdbc:postgresql")) {
                try {
                Properties prop = new Properties();
                prop.setProperty("user", "testuser");
                prop.setProperty("password", "");
                
                //  JDBC Driverのロード
                Class.forName("org.postgresql.Driver");
                // 接続
                Connection conn =
                DriverManager.getConnection
                (strC[0], prop);
                //実行
                String sql = "INSERT INTO data (sortkey,key1,key2,key3,key4,key5,honbun) VALUES (?,?,?,?,?,?,?);";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, str[4]);
                pstmt.setString(2, str[0]);
                pstmt.setString(3, str[1]);
                pstmt.setString(4, str[2]);
                pstmt.setString(5, str[3]);
                pstmt.setString(6, str[4]);
                pstmt.setString(7, str[5]);
                int rows = pstmt.executeUpdate();
                
                //画面のリストに反映
                CardFrm.setList(true);

                // ステートメントをクローズ
                pstmt.close();
                // 接続をクローズ
                conn.close();

                card.readCSV();
            }catch (Exception e){
                JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\n" + e.getLocalizedMessage());
                e.printStackTrace();
            }
            return;
        }
        
        for (int i = 0; i < ArrStr.length; i++) {
            //System.err.println(str[i]);
            ArrStr[i].add(str[i]);
        }
        card.writeCSV(false, CardFrm);
        card.readCSV();
    }
    
    public static void setCard(int idx) {
        //Card画面を設定
        String[] str = {"","","","","","","","","",""};
        if (idx == 0) {
            //新規登録モード
            if (CardFrm.getSelectedTab() == 0) {
                //編集モード
                CardFrm.setUpdtMode(false);
            }else {
                //表示モード
                CardFrm.setViewMode();
            }
            CardFrm.setText(str);
            return;
        }
        if (idx > 0) {
            //更新モード
            //表示タブか編集タブか判定
            if (CardFrm.getSelectedTab() == 0) {
                //編集モード
                CardFrm.setUpdtMode(true);
            }else {
                //表示モード
                CardFrm.setViewMode();
            }
            for (int i = 0; i < ArrStr.length; i++) {
                str[i] = ArrStr[i].get((Integer)CardFrm.ModelIdx.get(idx));
            }
        }
        CardFrm.setText(str);
    }
    
    public static void readCSV() {
        //環境設定画面の取得
        String[] strBefore = {"","","","","","","","","",""};
        ConfFrm.getText(strBefore);
        
        //JDBC対応
        if (strBefore[0].startsWith("jdbc:postgresql")) {
            try {
            Properties prop = new Properties();
            prop.setProperty("user", "testuser");
            prop.setProperty("password", "");

            //  JDBC Driverのロード
            Class.forName("org.postgresql.Driver");
            // 接続
            Connection conn =
            DriverManager.getConnection
            (strBefore[0], prop);
            // ステートメントを作成
            Statement stmt = conn.createStatement();
            // 問合せの実行
            ResultSet rset = stmt.executeQuery("SELECT * FROM data ORDER BY id;");

                //配列の初期化
                for (int i = 0; i < ArrStr.length; i++) {
                    ArrStr[i].clear();
                }
                //環境設定画面の更新
                //ConfFrm.setText(str);
                //Card画面の更新
                //CardFrm.setConf(str);
                //リストのクリア
                CardFrm.initList();
                CardFrm.setUpdtMode(false);

                // 問合せ結果の表示
                while ( rset.next() ) {
                    // 列番号による指定
                    System.out.println(rset.getInt(1) + "\t" + rset.getString(2));
                    String wk = rset.getString(3);
                    ArrStr[0].add(wk);
                    wk = rset.getString(4);
                    ArrStr[1].add(wk);
                    wk = rset.getString(5);
                    ArrStr[2].add(wk);
                    wk = rset.getString(6);
                    ArrStr[3].add(wk);
                    wk = rset.getString(1);
                    ArrStr[4].add(wk);
                    wk = rset.getString(8);
                    ArrStr[5].add(wk);
                }

                //画面のリストに反映
                CardFrm.setList(true);

                // 結果セットをクローズ
                rset.close();
                // ステートメントをクローズ
                stmt.close();
                // 接続をクローズ
                conn.close();
            }catch (Exception e){
                JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました\n" + e.getLocalizedMessage());
                e.printStackTrace();
            }
            //処理をここで抜ける
            return;
        }
        
        File csv = new File(strBefore[0]); // CSVデータファイル
        if (!csv.exists()) {
            return;
        }
        
        //CSVファイルの読み込み
        try {
            BufferedReader br;
            if (ConfFrm.getMojiCode() == 0) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "UTF-8"));
            }else {
                //SJISは機種依存文字や全角ハイフンが文字化けするため修正：Ver 2.0.0.1
                //br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "JISAutoDetect"));
                br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "MS932"));
            }
            
            String[] str = {"","","","","","","","","",""};
            String header = "";
            //ファイル名
            str[0] = strBefore[0];
            //DB名
            if ((header = br.readLine()) != null) {
                str[1] = header;
            }
            //項目名
            if ((header = br.readLine()) != null) {
                String[] strW = header.split("\t", 10);
                for (int i = 0; i < 6; i++) {
                    try {
                        str[i + 2] = strW[i];
                    }catch (Exception e){
                        str[i + 2] = "";
                        e.printStackTrace();
                    }
                }
            }            
            //環境設定画面の更新
            ConfFrm.setText(str);
            //Card画面の更新
            CardFrm.setConf(str);
            //リストのクリア
            CardFrm.initList();
            CardFrm.setUpdtMode(false);
            //配列の初期化
            for (int i = 0; i < ArrStr.length; i++) {
                ArrStr[i].clear();
            }
            // 最終行まで読み込む
            String line = "";
            while ((line = br.readLine()) != null) {
                // 1行をデータの要素に分割
                String[] strArr = {"","","","","","","","","",""};
                strArr = line.split("\t", 6);
                String wk;
                for (int i = 0; i < ArrStr.length; i++) {
                    try {
                        wk = strArr[i];
                    }catch (Exception e) {
                        wk = "";
                    }
                    ArrStr[i].add(wk);
                }
            }
            //画面のリストに反映
            CardFrm.setList(true);
            
            br.close();
            
        } catch (FileNotFoundException e) {
            // Fileオブジェクト生成時の例外捕捉
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました");
        } catch (IOException e) {
            // BufferedReaderオブジェクトのクローズ時の例外捕捉
            e.printStackTrace();
            JOptionPane.showMessageDialog(ConfFrm, "エラーが発生しました");
        }
    }
    
    public static void writeCSV(boolean flg, Component parent) {
        //環境設定画面の取得
        String[] str = {"","","","","","","","","",""};
        ConfFrm.getText(str);
        
        if (flg) {
            String errMsg = "";
            if (str[0].equals("")) {
                errMsg = errMsg + "CSVファイル名を指定してください\n";
            }
            if (str[1].equals("")) {
                errMsg = errMsg + "データベースタイトルを指定してください\n";
            }
            if (str[2].equals("")) {
                errMsg = errMsg + "項目名は少なくとも１つ指定してください\n";
            }
            if (!errMsg.equals("")) {
                JOptionPane.showMessageDialog(parent, errMsg);
                return;
            }
            if (JOptionPane.showConfirmDialog(parent, "登録してもよろしいですか？") != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        //JDBC対応
        if (str[0].startsWith("jdbc:postgresql")) {
            try {
                Properties prop = new Properties();
                prop.setProperty("user", "testuser");
                prop.setProperty("password", "");
                
                //  JDBC Driverのロード
                Class.forName("org.postgresql.Driver");
                // 接続
                Connection conn =
                DriverManager.getConnection
                (str[0], prop);
                // ステートメントを作成
                Statement stmt = conn.createStatement();
                // 問合せの実行
                ResultSet rset = stmt.executeQuery("SELECT * FROM data");
                // 問合せ結果の表示
                while ( rset.next() ) {
                  // 列番号による指定
                  System.out.println(rset.getInt(1) + "\t" + rset.getString(2));
                       }
                // 結果セットをクローズ
                rset.close();
                // ステートメントをクローズ
                stmt.close();
                // 接続をクローズ
                conn.close();
            }catch (Exception e){
                JOptionPane.showMessageDialog(parent, "エラーが発生しました\n" + e.getLocalizedMessage());
                e.printStackTrace();
            }
            //処理をここで抜ける
            return;
        }
        
        //Card画面の更新
        CardFrm.setConf(str);
        
        //CSVの書き込み
        try {
            File csv = new File(str[0]); // CSVデータファイル
            //古いファイルのバックアップ
            if (csv.exists()) {
                File fileB = new File(csv.getAbsolutePath() + "~");
                if (fileB.exists()) {
                    fileB.delete();
                }
                csv.renameTo(fileB);
            }
            // 常に新規作成
            PrintWriter bw;
            if (ConfFrm.getMojiCode() == 0) {
                bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"UTF-8")));
            }else {
                //SJISは機種依存文字や全角ハイフンが文字化けするため修正：Ver 2.0.0.1
                //bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"SJIS")));
                bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv),"MS932")));
            }
            
            //DB名
            bw.write(str[1]);
            //bw.newLine();
            bw.println();
            //項目名１−５
            bw.write(str[2] + "\t");
            bw.write(str[3] + "\t");
            bw.write(str[4] + "\t");
            bw.write(str[5] + "\t");
            bw.write(str[6] + "\t");
            bw.println();
            //データ部
            for (int i = 0; i < ArrStr[0].size(); i++) {
                bw.write(ArrStr[0].get(i) + "\t");
                bw.write(ArrStr[1].get(i) + "\t");
                bw.write(ArrStr[2].get(i) + "\t");
                bw.write(ArrStr[3].get(i) + "\t");
                bw.write(ArrStr[4].get(i) + "\t");
                bw.write(ArrStr[5].get(i).replaceAll("\n", "\t"));
                bw.println();
            }
            bw.close();
        } catch (FileNotFoundException e) {
            // Fileオブジェクト生成時の例外捕捉
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "エラーが発生しました");
            return;
        } catch (IOException e) {
            // BufferedWriterオブジェクトのクローズ時の例外捕捉
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "エラーが発生しました");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "エラーが発生しました");
            return;
        }
        JOptionPane.showMessageDialog(parent, "CSVを更新しました");
    }
    
    public static void writeProp() {
        //プロパティファイルの書き込み
        Properties config = new Properties();
        Rectangle rect = CardFrm.getBounds();
        config.setProperty("x", String.valueOf(rect.x));
        config.setProperty("y", String.valueOf(rect.y));
        config.setProperty("width", String.valueOf(rect.width));
        config.setProperty("height", String.valueOf(rect.height));
        
        Integer size[] = new Integer[30];
        CardFrm.getSize(size);
        config.setProperty("size0", String.valueOf(size[0]));
        config.setProperty("size1", String.valueOf(size[1]));
        config.setProperty("size2", String.valueOf(size[2]));
        config.setProperty("size3", String.valueOf(size[3]));
        config.setProperty("size4", String.valueOf(size[4]));
        config.setProperty("size5", String.valueOf(size[5]));
        config.setProperty("size6", String.valueOf(size[6]));
        config.setProperty("size7", String.valueOf(size[7]));
        config.setProperty("size8", String.valueOf(size[8]));
        config.setProperty("size9", String.valueOf(size[9]));
        config.setProperty("size10", String.valueOf(size[10]));
        config.setProperty("size11", String.valueOf(size[11]));
        //表示キー
        config.setProperty("LKey", String.valueOf(CardFrm.getLKey()));
        config.setProperty("RKey", String.valueOf(CardFrm.getRKey()));
        //検索モード
        config.setProperty("LKeyMode", String.valueOf(CardFrm.getKeyModeL()));
        config.setProperty("RKeyMode", String.valueOf(CardFrm.getKeyModeR()));
        
        config.setProperty("Tab", String.valueOf(CardFrm.getSelectedTab()));
        
        //文字コード
        config.setProperty("MojiCode", String.valueOf(ConfFrm.getMojiCode()));
        
        //ポップアップメニューに対応：Ver 2.0.0.2
        config.setProperty("Seireki0Gengo1", String.valueOf(Seireki0Gengo1));
        config.setProperty("Editor", Editor);
        config.setProperty("LibreExePath", LibreExePath);
        
        config.setProperty("FontFamily", fntName);
        
        //メール送信に対応：Ver 2.0.0.3
        String[] str = {"","","","","","","","","",""};
        MailFrm.getMailFrm(str);
        config.setProperty("MailTitle", str[0]);;
        config.setProperty("MailTo", str[1]);;
        config.setProperty("MailFrom", str[2]);;
        config.setProperty("MailHost", str[3]);;
        config.setProperty("MailSmtpHost", str[4]);;
        config.setProperty("MailUser", str[5]);;
        config.setProperty("MailPass", encode(str[6]));
        //System.out.println(encode(MailPass));
        
        String[] str2 = {"","","","","","","","","",""};
        ConfFrm.getText(str2);
        config.setProperty("csvFile", str2[0]);
        
        try {
            config.store(new OutputStreamWriter(new FileOutputStream("card.properties"),"UTF-8"), "by HDM");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void initFrame() {
        //画面クリア
        String[] str = {"","","","","","","","","","","","","","","",""};
        CardFrm.setText(str);
        ConfFrm.setText(str);
        
    }
}
