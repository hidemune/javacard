import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class MailTransfer {
    
  public static void sendMail(){
    System.out.println();

    String str[] = {"","","","","","","","","",""};
    card.MailFrm.getMailFrm(str);
    
    String title = str[0];
    String to = str[1];
    String from = str[2];
    String host = str[3];
    String smtphost = str[4];
    //String user = str[5];
    //String pass = str[6];

    // SMTPサーバー設定
    Properties props = System.getProperties();
    props.setProperty( "mail.smtp.port", "587"); 
    props.setProperty("mail.smtp.socketFactory.port", "587"); 
    props.put( "mail.smtp.host", smtphost );
    props.setProperty("mail.smtp.auth", "true");
    Session session = Session.getInstance(props, new Authenticator(){ 
    protected PasswordAuthentication getPasswordAuthentication() { 
            String str[] = {"","","","","","","","","",""};
            card.MailFrm.getMailFrm(str);

            //String title = str[0];
            //String to = str[1];
            //String from = str[2];
            //String host = str[3];
            //String smtphost = str[4];
            String user = str[5];
            String pass = str[6];
            
            return new PasswordAuthentication( user, pass ); 
        }
    }); 
    // Get system properties
    Properties properties = System.getProperties();

    // Setup mail server
    properties.setProperty(smtphost, host);
    
    try{
        MimeMessage mimeMessage=new MimeMessage(session);
        // ヘッダ
        //mimeMessage.setHeader("Content-Type","text/plain; charset=iso-2022-jp"); 
        // 送信元メールアドレスと送信者名を指定
        mimeMessage.setFrom(new InternetAddress(from,from,"iso-2022-jp"));
        // 送信先メールアドレスを指定
        mimeMessage.setRecipients(Message.RecipientType.TO,to);
        // メールのタイトルを指定
        mimeMessage.setSubject(title,"iso-2022-jp");
        // メールの内容を指定
        mimeMessage.setText(card.CardFrm.getTextHonV().replaceAll("\n", "\r\n")+"\r\n", "ISO-2022-JP");
        // メールの形式を指定
        mimeMessage.setHeader("Content-Type","text/plain; charset=iso-2022-jp");
        // 送信日付を指定
        mimeMessage.setSentDate(new Date());
        // 送信します
        Transport.send(mimeMessage);
        JOptionPane.showMessageDialog(card.CardFrm, "メール送信処理が正常に終了しました。\n");
    }catch(Exception mex){
      System.out.println("¥n--Exception handling in msgsendsample.java");
      mex.printStackTrace();
      JOptionPane.showMessageDialog(card.CardFrm, "メール送信時にエラーが発生しました。\n設定をご確認ください。");
    }
  }
}
