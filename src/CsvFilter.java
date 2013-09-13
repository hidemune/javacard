
import java.io.File;
import javax.swing.filechooser.FileFilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hdm
 */
//ファイル選択ダイアログでcsvファイルのみ指定：Ver 2.0.0.2  2013.5.17
public class CsvFilter extends FileFilter{
      public boolean accept(File f){
        /* ディレクトリなら無条件で表示する */
        if (f.isDirectory()){
          return true;
        }

        /* 拡張子を取り出し、csvだったら表示する */
        String ext = getExtension(f);
        if (ext != null){
          if (ext.equals("csv")){   //小文字で指定すること
            return true;
          }else{
            return false;
          }
        }

        return false;
      }

      public String getDescription(){
        return "CSVファイル";
      }

      /* 拡張子を取り出す */
      private String getExtension(File f){
        String ext = null;
        String filename = f.getName();
        int dotIndex = filename.lastIndexOf('.');

        if ((dotIndex > 0) && (dotIndex < filename.length() - 1)){
          ext = filename.substring(dotIndex + 1).toLowerCase();
        }

        return ext;
      }
}
