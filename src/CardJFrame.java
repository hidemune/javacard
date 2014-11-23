
import com.sun.org.apache.xerces.internal.xs.StringList;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javafx.collections.transformation.FilteredList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hdm
 */
public class CardJFrame extends javax.swing.JFrame {

    //画面のリストの中身退避用
    DefaultListModel ModelL;
    DefaultListModel ModelR;
    DefaultListModel ModelIdx;
    
    JViewport viewL;
    JViewport viewR;
    
    //Ver 2.0.0.2 追加
    //ポップアップメニュー
    JPopupMenu popupmenu;
    JPopupMenu popupmenuV;
    JMenuItem menuCut = new JMenuItem("切り取り");
    JMenuItem menuCopy = new JMenuItem("コピー");
    JMenuItem menuPaste = new JMenuItem("貼り付け");
    JMenuItem menuSelectAll = new JMenuItem("すべて選択");
    JMenuItem menuDate = new JMenuItem("日付を入力");
    JMenuItem menuTime = new JMenuItem("時刻を入力");
    JMenuItem menuDateTime = new JMenuItem("日付と時刻を入力");
    JMenuItem menuImage = new JMenuItem("画像を挿入");
    JMenuItem menuCopyV = new JMenuItem("コピー");
    JMenuItem menuSelectAllV = new JMenuItem("すべて選択");
    JMenuItem menuSendEditor = new JMenuItem("エディタに送る");
    JMenuItem menuSendMail = new JMenuItem("メール送信");
    //Ver 2.0.0.2 Undo機能追加
    UndoHelper helper;
    
    /**
     * Creates new form CardJFrame
     */
    public CardJFrame() {
        initComponents();
        
        /* ラジオボタングループ化 */
        ButtonGroup group0 = new ButtonGroup();
        group0.add(jRadioButtonMenuItemLKey1);
        group0.add(jRadioButtonMenuItemLKey2);
        group0.add(jRadioButtonMenuItemLKey3);
        group0.add(jRadioButtonMenuItemLKey4);
        group0.add(jRadioButtonMenuItemLKey5);
        group0.add(jRadioButtonMenuItemLKey6);
        /* ラジオボタングループ化 */
        ButtonGroup group1 = new ButtonGroup();
        group1.add(jRadioButtonMenuItemRKey1);
        group1.add(jRadioButtonMenuItemRKey2);
        group1.add(jRadioButtonMenuItemRKey3);
        group1.add(jRadioButtonMenuItemRKey4);
        group1.add(jRadioButtonMenuItemRKey5);
        group1.add(jRadioButtonMenuItemRKey6);
        /* ラジオボタングループ化 */
        ButtonGroup group2 = new ButtonGroup();
        group2.add(jRadioButtonMenuItemLFind1);
        group2.add(jRadioButtonMenuItemLFind2);
        /* ラジオボタングループ化 */
        ButtonGroup group3 = new ButtonGroup();
        group3.add(jRadioButtonMenuItemRFind1);
        group3.add(jRadioButtonMenuItemRFind2);
        
        //リストボックス初期化
        ModelL = new DefaultListModel();
        jListL.setModel(ModelL);
        //リストボックス初期化
        ModelR = new DefaultListModel();
        jListR.setModel(ModelR);
        //インデックス退避用
        ModelIdx = new DefaultListModel();
        initList();
        
        viewL = jScrollPaneL.getViewport();
        viewR = jScrollPaneR.getViewport();
        
        //Ver 2.0.0.2 追加
        //ポップアップメニュー
        popupmenu = new JPopupMenu();
        popupmenu.add(menuCut);
        popupmenu.add(menuCopy);
        popupmenu.add(menuPaste);
        popupmenu.add(new JSeparator());
        popupmenu.add(menuSelectAll);
        popupmenu.add(new JSeparator());
        popupmenu.add(menuDate);
        popupmenu.add(menuTime);
        popupmenu.add(menuDateTime);
        popupmenu.add(menuImage);
        
        popupmenuV = new JPopupMenu();
        popupmenuV.add(menuCopyV);
        popupmenuV.add(menuSelectAllV);
        popupmenuV.add(new JSeparator());
        popupmenuV.add(menuSendEditor);
        popupmenuV.add(menuSendMail);
        
        jTextAreaHon.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent event) {
                    popupMenu(event);
                }

                public void mouseReleased(MouseEvent event) {
                    popupMenu(event);
                }
            });
        jTextAreaHonV.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent event) {
                    popupMenuV(event);
                }

                public void mouseReleased(MouseEvent event) {
                    popupMenuV(event);
                }
            });
        menuCut.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextAreaHon.cut();
            }
        });
        menuCopy.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextAreaHon.copy();
            }
        });
        menuPaste.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextAreaHon.paste();
            }
        });
        menuSelectAll.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextAreaHon.selectAll();
            }
        });
        menuDate.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();	//現在日時でDateを作成
                DateFormat df;
                if (card.Seireki0Gengo1 == 0){
                    df = new SimpleDateFormat("yyyy/MM/dd");
                }else {
                    Locale locale = new Locale("ja", "JP", "JP");
                    df = new SimpleDateFormat("Gy/MM/dd", locale);
                }
                jTextAreaHon.insert(df.format(now), jTextAreaHon.getSelectionStart());
            }
        });
        menuTime.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();	//現在日時でDateを作成
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                jTextAreaHon.insert(df.format(now), jTextAreaHon.getSelectionStart());
            }
        });
        menuDateTime.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();	//現在日時でDateを作成
                DateFormat df;
                if (card.Seireki0Gengo1 == 0){
                    df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                }else {
                    Locale locale = new Locale("ja", "JP", "JP");
                    df = new SimpleDateFormat("Gy/MM/dd HH:mm:ss", locale);
                }
                Locale locale = new Locale("ja", "JP", "JP");
                jTextAreaHon.insert(df.format(now), jTextAreaHon.getSelectionStart());
            }
        });
        menuImage.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                //File filePath = new File("");
                String path = "";
                //ファイル選択ダイアログでファイル名・フォルダ名の変更モードを抑止　Ver 2.0.0.2     2013.5.17
                UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                JFileChooser filechooser = new JFileChooser(path);
                //ファイル選択ダイアログでcsvファイルのみ指定：Ver 2.0.0.2  2013.5.17
                //CsvFilter filter = new CsvFilter();
                //filechooser.addChoosableFileFilter(filter);
                //filechooser.setFileFilter(filter);
                //ファイルダイアログ呼び出し
                if (filechooser.showOpenDialog(menuImage.getComponent()) == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    //jTextFieldCSV.setText(file.getPath());
                    //ファイルが存在しない場合は新規作成モード
                    if (file.exists()) {
                            java.util.List fileList = new ArrayList<String>();
                            fileList.add(file);
                            setFileName(fileList);
                        }
                    }
            }
        });
        menuCopyV.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextAreaHonV.copy();
            }
        });
        menuSelectAllV.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextAreaHonV.selectAll();
            }
        });
        menuSendEditor.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                //メモ帳で開く
                //tmpファイルに保存
                try {
                    File tmp = new File("./tmp.txt"); // CSVデータファイル
                    // 常に新規作成
                    PrintWriter bw;
                    bw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp))));
                    //DB名
                    String crlf = System.getProperty("line.separator");
                    bw.write(jTextAreaHonV.getText().replaceAll("\n", crlf));
                    bw.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                //エディタを開く
                try {
                    Runtime r = Runtime.getRuntime();
                    r.exec(new String[] {card.Editor , "./tmp.txt"});
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        });
        helper = new UndoHelper(jTextAreaHon);
        //Ver 2.0.0.3 追加
        menuSendMail.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                //メール送信
                MailTransfer.sendMail();
            }
        });
        //Ver 2.0.0.4 追加
        jTextAreaHon.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent de){
            setModified(true);
            }
            public void insertUpdate(DocumentEvent de){
            setModified(true);
            }
            public void removeUpdate(DocumentEvent de){
            setModified(true);
            }
            });
        //Ver 2.0.0.5
        DropTarget target = new DropTarget(jTextAreaHon, new MyDropTargetAdapter());
        
        //ヘルプファイルをブラウザで見れない環境がある
        try {
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                jMenuItemHelp.setVisible(false);
            }
        }catch (Exception e) {
            jMenuItemHelp.setVisible(false);
            e.printStackTrace();
        }
    }
    
    //Ver 2.0.0.6 フォント指定
    public void setFont(Font f) {
        jTextAreaHon.setFont(f);
        jTextAreaHonV.setFont(f);
        jTextFieldCol1.setFont(f);
        jTextFieldCol2.setFont(f);
        jTextFieldCol3.setFont(f);
        jTextFieldCol4.setFont(f);
        jTextFieldCol5.setFont(f);
        jListL.setFont(f);
        jListR.setFont(f);
        jTextFieldL.setFont(f);
        jTextFieldR.setFont(f);
        
    }
    
    /**
     * ドラッグ＆ドロップイベントを受け取る。
     * @author s.s.k.
     */
    private class MyDropTargetAdapter extends DropTargetAdapter {
        /*
         * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
         */
        public void drop(DropTargetDropEvent e) {
            System.out.println("Drop!");
            try {
                Transferable transfer = e.getTransferable();
                if (transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    //For Windows
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    java.util.List fileList = (java.util.List) (transfer.getTransferData(DataFlavor.javaFileListFlavor));
                    setFileName(fileList);
                } else if (transfer.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    //For Linux (KDE Dolphin)
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    String str = (String)(transfer.getTransferData(DataFlavor.stringFlavor));
                    str.replace("\r", "");
                    String fileListStr[] = str.split("\n");
                    java.util.ArrayList fileList = new java.util.ArrayList();
                    for (int i = 0; i < fileListStr.length; i++) {
                        String path = fileListStr[i].replaceAll("file://", "");
                        System.out.println("ドロップされたファイル名" + path);
                        File file = new File(path);
                        fileList.add(file);
                    }
                    setFileName(fileList);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setFileName(java.util.List fileList) {
        for (int i = 0; i < fileList.size(); i++) {
            File file = (File) fileList.get(i);
            //buffer.append(file.getAbsolutePath());
            String str = file.getAbsolutePath();
            //image でなければ処理しない　.bmp .png .gif .jpg .jpeg のみ
            String ext = "";
            int Idx = str.lastIndexOf(".");
            boolean flg = true;
            if (Idx > 0) {
                ext = str.substring(Idx).trim().toLowerCase();
                System.out.println("ext:" + ext);
                if (ext.equals(".bmp")) {
                    //OK
                } else if (ext.equals(".gif")) {
                    //OK
                } else if (ext.equals(".png")) {
                    //OK
                } else if (ext.equals(".jpg")) {
                    //OK
                } else if (ext.equals(".jpeg")) {
                    //OK
                } else {
                    //NG
                    flg = false;
                    JOptionPane.showMessageDialog(this, ext + "形式には対応していません。\n（画像取り込み）");
                }
            } else {
                //NG
                flg = false;
                JOptionPane.showMessageDialog(this, "拡張子を取得できません。\n（画像取り込み）");
            }
            //ディレクトリならNG
            if (file.isDirectory()) {
                flg = false;
            }
            if (flg) {
                //image フォルダあるか確認
                File dir = new File("image");
                flg = false;
                if (dir.exists()) {
                    if (dir.isDirectory()) {
                        //OK
                        flg = true;
                    }
                }
                if (!flg) {
                    //MSG
                    JOptionPane.showMessageDialog(this, "カレントディレクトリに「image」フォルダがありません。\n処理を中断します。");
                    return;
                }
                //相対パスから新ファイル名作成
                String filename = "image/" + file.getName();
                File newFile = new File(filename);
                
                //既に存在すれば確認
                if (newFile.exists()) {
                    int ret = JOptionPane.showConfirmDialog(this, "すでにimageフォルダに同名のファイルが存在します。\n上書きしてもよろしいですか？");
                    if (ret == JOptionPane.YES_OPTION) {
                        //ファイルコピー
                        card.fileCopy(file, newFile);
                    } else if (ret == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                } else {
                    //なければコピー
                    card.fileCopy(file, newFile);
                }
                String tag = "&&IMAGE&&" + filename;
                //タグつけてテキストエリア編集
                jTextAreaHon.insert(tag + "\n", jTextAreaHon.getSelectionStart());
            }
        }
    }

    //Ver 2.0.0.4 追加
    public void setModified(boolean modified) {
        if (modified) {
            String str = "Card  * modified *";
            if (!((JFrame)this).getTitle().equals(str)) {
                this.setTitle(str);
            }
        } else {
            this.setTitle("Card");
        }
    }
    
    //Ver 2.0.0.2 追加
    private void popupMenu(MouseEvent event) {
        if (event.isPopupTrigger()) {
            popupmenu.show(event.getComponent(), event.getX(), event.getY());
        }
    }
    private void popupMenuV(MouseEvent event) {
        if (event.isPopupTrigger()) {
            popupmenuV.show(event.getComponent(), event.getX(), event.getY());
        }
    }
    
    //スクロールの同期
    public void initView() {
        jScrollPaneL.getVerticalScrollBar().getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                //もう片方のスクロール
                Point point = viewL.getViewPosition();
                try {
                    viewR.setViewPosition(point);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        jScrollPaneR.getVerticalScrollBar().getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                //もう片方のスクロール
                Point point = viewR.getViewPosition();
                try {
                    viewL.setViewPosition(point);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void initListTitle() {
        jLabelL.setText("");
        jLabelR.setText("");
    }
    
    public void setKeyModeL(int idx) {
        if (idx == 0) {
            jRadioButtonMenuItemLFind1.setSelected(true);
        }else {
            jRadioButtonMenuItemLFind2.setSelected(true);
        }
    }
    public void setKeyModeR(int idx) {
        if (idx == 0) {
            jRadioButtonMenuItemRFind1.setSelected(true);
        }else {
            jRadioButtonMenuItemRFind2.setSelected(true);
        }
    }
    
    public int getKeyModeL() {
        int idx = 0;
        if (jRadioButtonMenuItemLFind1.isSelected()) {
            idx = 0;
        }else if (jRadioButtonMenuItemLFind2.isSelected()) {
            idx = 1;
        }
        return idx;
    }
    public int getKeyModeR() {
        int idx = 0;
        if (jRadioButtonMenuItemRFind1.isSelected()) {
            idx = 0;
        }else if (jRadioButtonMenuItemRFind2.isSelected()) {
            idx = 1;
        }
        return idx;
    }
    
    public void setSelectedTab(int idx) {
        //表示タブか編集タブか
        jTabbedPaneEorV.setSelectedIndex(idx);
    }
    
    public int getSelectedTab() {
        //表示タブか編集タブか
        return jTabbedPaneEorV.getSelectedIndex();
    }
    
    public void setViewMode(){
        jButtonDel.setEnabled(false);
        jButtonInst.setEnabled(false);
    }
    public void setUpdtMode(boolean flg){
        jButtonInst.setEnabled(true);
        if (flg) {
            //更新モード
            jButtonDel.setEnabled(true);
        }else {
            //新規モード
            jButtonDel.setEnabled(false);
        }
    }
    
    public void setLKey(int idx) {
        if (idx == 0) {
            jRadioButtonMenuItemLKey1.setSelected(true);
        }else if (idx == 1) {
            jRadioButtonMenuItemLKey2.setSelected(true);
        }else if (idx == 2) {
            jRadioButtonMenuItemLKey3.setSelected(true);
        }else if (idx == 3) {
            jRadioButtonMenuItemLKey4.setSelected(true);
        }else if (idx == 4) {
            jRadioButtonMenuItemLKey5.setSelected(true);
        }else if (idx == 5) {
            jRadioButtonMenuItemLKey6.setSelected(true);
        }
    }
    public void setRKey(int idx) {
        if (idx == 0) {
            jRadioButtonMenuItemRKey1.setSelected(true);
        }else if (idx == 1) {
            jRadioButtonMenuItemRKey2.setSelected(true);
        }else if (idx == 2) {
            jRadioButtonMenuItemRKey3.setSelected(true);
        }else if (idx == 3) {
            jRadioButtonMenuItemRKey4.setSelected(true);
        }else if (idx == 4) {
            jRadioButtonMenuItemRKey5.setSelected(true);
        }else if (idx == 5) {
            jRadioButtonMenuItemRKey6.setSelected(true);
        }
    }
    public int getLKey() {
        int idx = 0;
        if (jRadioButtonMenuItemLKey1.isSelected()) {
            idx = 0;
        }else if (jRadioButtonMenuItemLKey2.isSelected()) {
            idx = 1;
        }else if (jRadioButtonMenuItemLKey3.isSelected()) {
            idx = 2;
        }else if (jRadioButtonMenuItemLKey4.isSelected()) {
            idx = 3;
        }else if (jRadioButtonMenuItemLKey5.isSelected()) {
            idx = 4;
        }else if (jRadioButtonMenuItemLKey6.isSelected()) {
            idx = 5;
        }
        return idx;
    }
    public int getRKey() {
        int idx = 0;
        if (jRadioButtonMenuItemRKey1.isSelected()) {
            idx = 0;
        }else if (jRadioButtonMenuItemRKey2.isSelected()) {
            idx = 1;
        }else if (jRadioButtonMenuItemRKey3.isSelected()) {
            idx = 2;
        }else if (jRadioButtonMenuItemRKey4.isSelected()) {
            idx = 3;
        }else if (jRadioButtonMenuItemRKey5.isSelected()) {
            idx = 4;
        }else if (jRadioButtonMenuItemRKey6.isSelected()) {
            idx = 5;
        }
        return idx;
    }
    
    public void initList() {
        ModelL.clear();
        ModelL.addElement(" ");
        jListL.setSelectedIndex(0);
        
        ModelR.clear();
        ModelR.addElement(" ");
        jListR.setSelectedIndex(0);
        
        ModelIdx.clear();
        ModelIdx.addElement(new Integer(-1));
    }
    
    public void setList(boolean flg) {
        //画面から呼ばれる時はflgはfalse
        if (!flg) {
            //画面から呼ばれた場合、非表示時は機能させない
            if (!this.isShowing()) {
                return;
            }
        }
        //検索キータイトル
        String[] str = {"","","","","","","","","",""};
        card.ConfFrm.getText(str);
        jLabelL.setText(str[getLKey() + 2]);
        jLabelR.setText(str[getRKey() + 2]);
        //リストクリア
        initList();
        //リストに設定
        for (int i = 0; i < card.ArrStr[0].size(); i++) {
            //検索キーのチェック
            boolean flgV = true;
            if (!jTextFieldL.getText().equals("")) {
                //検索キーが設定されている場合チェック
                String strKey = jTextFieldL.getText();
                String strLine = card.ArrStr[getLKey()].get(i);
                if (getKeyModeL() == 0) {
                    //前方一致検索
                    if (!strLine.startsWith(strKey)) {
                        flgV = false;
                    }
                }else {
                    //部分一致検索
                    if (strLine.indexOf(strKey) == -1) {
                        flgV = false;
                    }
                }
            }
            if (!jTextFieldR.getText().equals("")) {
                //検索キーが設定されている場合チェック
                String strKey = jTextFieldR.getText();
                String strLine = card.ArrStr[getRKey()].get(i);
                if (getKeyModeR() == 0) {
                    //前方一致検索
                    if (!strLine.startsWith(strKey)) {
                        flgV = false;
                    }
                }else {
                    //部分一致検索
                    if (strLine.indexOf(strKey) == -1) {
                        flgV = false;
                    }
                }
            }
            if (flgV) {
                String wL = card.ArrStr[getLKey()].get(i);
                if (wL.equals("")) {
                    wL = " ";
                }
                ModelL.addElement(wL);
                String wR = card.ArrStr[getRKey()].get(i);
                if (wR.equals("")) {
                    wR = " ";
                }
                ModelR.addElement(wR);
                ModelIdx.addElement(new Integer(i));
            }
        }
    }
    
    public void setConf(String[] str) {
        jLabelDBName.setText(str[1]);
        jLabelCol1.setText(str[2]);
        jLabelCol2.setText(str[3]);
        jLabelCol3.setText(str[4]);
        jLabelCol4.setText(str[5]);
        jLabelCol5.setText(str[6]);
    }
    
    public void setText(String[] str) {
        jTextFieldCol1.setText(str[0]);
        jTextFieldCol2.setText(str[1]);
        jTextFieldCol3.setText(str[2]);
        jTextFieldCol4.setText(str[3]);
        jTextFieldCol5.setText(str[4]);
        jTextAreaHon.setText(str[5]);
        jTextAreaHon.setSelectionStart(0);
        jTextAreaHon.setSelectionEnd(0);
        //Ver 2.0.0.2 Undo機能追加
        helper.clearUndoEdit();
        
        //表示用タブ
        String view = "";
        if (!str[0].equals("")){
            String[] strArr = {"","","","","","","","","",""};
            card.ConfFrm.getText(strArr);
            
            //文字列編集
            StringBuilder sb = new StringBuilder("");
            if ((!strArr[2].equals("")) && (!str[0].equals(""))) {
                sb.append("【");
                sb.append(strArr[2]);
                sb.append("】\n");
            }
            if (!str[0].equals("")) {
                sb.append(str[0]);
                sb.append("\n");
            }
            if ((!strArr[3].equals("")) && (!str[1].equals(""))) {
                sb.append("【");
                sb.append(strArr[3]);
                sb.append("】\n");
            }
            if (!str[1].equals("")) {
                sb.append(str[1]);
                sb.append("\n");
            }
            if ((!strArr[4].equals("")) && (!str[2].equals(""))) {
                sb.append("【");
                sb.append(strArr[4]);
                sb.append("】\n");
            }
            if (!str[2].equals("")) {
                sb.append(str[2]);
                sb.append("\n");
            }
            if ((!strArr[5].equals("")) && (!str[3].equals(""))) {
                sb.append("【");
                sb.append(strArr[5]);
                sb.append("】\n");
            }
            if (!str[3].equals("")) {
                sb.append(str[3]);
                sb.append("\n");
            }
            if ((!strArr[6].equals("")) && (!str[4].equals(""))) {
                sb.append("【");
                sb.append(strArr[6]);
                sb.append("】\n");
            }
            if (!str[4].equals("")) {
                sb.append(str[4]);
                sb.append("\n");
            }
            sb.append("\n");
            sb.append(str[5]);
            view = sb.toString();
        }
        jTextAreaHonV.setText(view);
        jTextAreaHonV.setSelectionStart(0);
        jTextAreaHonV.setSelectionEnd(0);
        //Modified マークの表示に対応：Ver 2.0.0.4
        card.CardFrm.setModified(false);
    }
    
    public void getText(String[] str) {
        str[0] = jTextFieldCol1.getText();
        str[1] = jTextFieldCol2.getText();
        str[2] = jTextFieldCol3.getText();
        str[3] = jTextFieldCol4.getText();
        str[4] = jTextFieldCol5.getText();
        str[5] = jTextAreaHon.getText();
    }
    public String getTextHonV() {
        return jTextAreaHonV.getText();
    }
    
    public void setSize(Integer[] size) {
        jSplitPaneKey1.setDividerLocation(size[0]);
        jSplitPaneKey2.setDividerLocation(size[1]);
        jSplitPaneStr1.setDividerLocation(size[2]);
        jSplitPaneStr2.setDividerLocation(size[3]);
        jSplitPaneStr3.setDividerLocation(size[4]);
        jSplitPaneStr4.setDividerLocation(size[5]);
        jSplitPaneStr5.setDividerLocation(size[6]);
        jSplitPaneCol1.setDividerLocation(size[7]);
        jSplitPaneCol2.setDividerLocation(size[8]);
        jSplitPaneCol3.setDividerLocation(size[9]);
        jSplitPaneCol4.setDividerLocation(size[10]);
        jSplitPaneCol5.setDividerLocation(size[11]);
    }
    
    public void getSize(Integer[] size) {
        size[0] = jSplitPaneKey1.getDividerLocation();
        size[1] = jSplitPaneKey2.getDividerLocation();
        size[2] = jSplitPaneStr1.getDividerLocation();
        size[3] = jSplitPaneStr2.getDividerLocation();
        size[4] = jSplitPaneStr3.getDividerLocation();
        size[5] = jSplitPaneStr4.getDividerLocation();
        size[6] = jSplitPaneStr5.getDividerLocation();
        size[7] = jSplitPaneCol1.getDividerLocation();
        size[8] = jSplitPaneCol2.getDividerLocation();
        size[9] = jSplitPaneCol3.getDividerLocation();
        size[10] = jSplitPaneCol4.getDividerLocation();
        size[11] = jSplitPaneCol5.getDividerLocation();
    }
    
    private void swap(int idx1, int idx2, int select) {
        //抽出条件が入っていれば抜ける
        if (!jTextFieldL.getText().equals("") || !jTextFieldR.getText().equals("")) {
            return;
        }
        if (ModelIdx.size() - 1 != card.ArrStr[0].size()) {
            return;
        }
        //範囲外なら抜ける select は交換後のListIndex
        if (select < 1 || ModelIdx.size() <= select) {
            return;
        }
        //JOptionPane.showMessageDialog(this, "行の交換を行う", "debug", JOptionPane.INFORMATION_MESSAGE);
        for (int i = 0; i < 6; i++) {
            String work = card.ArrStr[i].get((Integer)ModelIdx.getElementAt(idx1));
            card.ArrStr[i].set((Integer)ModelIdx.getElementAt(idx1), card.ArrStr[i].get((Integer)ModelIdx.getElementAt(idx2)));
            card.ArrStr[i].set((Integer)ModelIdx.getElementAt(idx2), work);
        }
        //再描画
        setList(false);
        jListL.setSelectedIndex(select);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jButtonDel = new javax.swing.JButton();
        jButtonInst = new javax.swing.JButton();
        jLabelDBName = new javax.swing.JLabel();
        jSplitPaneKey2 = new javax.swing.JSplitPane();
        jSplitPaneKey1 = new javax.swing.JSplitPane();
        jSplitPane6 = new javax.swing.JSplitPane();
        jTextFieldL = new javax.swing.JTextField();
        jSplitPane7 = new javax.swing.JSplitPane();
        jLabelL = new javax.swing.JLabel();
        jScrollPaneL = new javax.swing.JScrollPane();
        jListL = new javax.swing.JList();
        jSplitPane8 = new javax.swing.JSplitPane();
        jTextFieldR = new javax.swing.JTextField();
        jSplitPane9 = new javax.swing.JSplitPane();
        jLabelR = new javax.swing.JLabel();
        jScrollPaneR = new javax.swing.JScrollPane();
        jListR = new javax.swing.JList();
        jTabbedPaneEorV = new javax.swing.JTabbedPane();
        jSplitPaneStr1 = new javax.swing.JSplitPane();
        jSplitPaneStr2 = new javax.swing.JSplitPane();
        jSplitPaneStr3 = new javax.swing.JSplitPane();
        jSplitPaneStr4 = new javax.swing.JSplitPane();
        jSplitPaneStr5 = new javax.swing.JSplitPane();
        jSplitPaneCol5 = new javax.swing.JSplitPane();
        jLabelCol5 = new javax.swing.JLabel();
        jTextFieldCol5 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaHon = new javax.swing.JTextArea();
        jSplitPaneCol4 = new javax.swing.JSplitPane();
        jLabelCol4 = new javax.swing.JLabel();
        jTextFieldCol4 = new javax.swing.JTextField();
        jSplitPaneCol3 = new javax.swing.JSplitPane();
        jLabelCol3 = new javax.swing.JLabel();
        jTextFieldCol3 = new javax.swing.JTextField();
        jSplitPaneCol2 = new javax.swing.JSplitPane();
        jLabelCol2 = new javax.swing.JLabel();
        jTextFieldCol2 = new javax.swing.JTextField();
        jSplitPaneCol1 = new javax.swing.JSplitPane();
        jLabelCol1 = new javax.swing.JLabel();
        jTextFieldCol1 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaHonV = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemConfig = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jRadioButtonMenuItemLKey1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemLKey2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemLKey3 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemLKey4 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemLKey5 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemLKey6 = new javax.swing.JRadioButtonMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jRadioButtonMenuItemRKey1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemRKey2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemRKey3 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemRKey4 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemRKey5 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemRKey6 = new javax.swing.JRadioButtonMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jRadioButtonMenuItemLFind1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemLFind2 = new javax.swing.JRadioButtonMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jRadioButtonMenuItemRFind1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItemRFind2 = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemHelp = new javax.swing.JMenuItem();
        jMenuItemVersion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Card");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jButtonDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/document-close.png"))); // NOI18N
        jButtonDel.setText("削除");
        jButtonDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelActionPerformed(evt);
            }
        });
        jSplitPane3.setLeftComponent(jButtonDel);

        jButtonInst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/checkbox.png"))); // NOI18N
        jButtonInst.setText("登録");
        jButtonInst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonInst.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jButtonInstComponentResized(evt);
            }
        });
        jButtonInst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInstActionPerformed(evt);
            }
        });
        jSplitPane3.setRightComponent(jButtonInst);

        jSplitPane2.setRightComponent(jSplitPane3);

        jLabelDBName.setText("データベース名称");
        jLabelDBName.setFocusable(false);
        jLabelDBName.setMaximumSize(new java.awt.Dimension(30000, 23));
        jLabelDBName.setMinimumSize(new java.awt.Dimension(98, 23));
        jSplitPane2.setLeftComponent(jLabelDBName);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jSplitPaneKey2.setOneTouchExpandable(true);

        jSplitPaneKey1.setMinimumSize(new java.awt.Dimension(30, 1));
        jSplitPaneKey1.setOneTouchExpandable(true);

        jSplitPane6.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTextFieldL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLActionPerformed(evt);
            }
        });
        jSplitPane6.setTopComponent(jTextFieldL);

        jSplitPane7.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jLabelL.setText("項目１");
        jSplitPane7.setTopComponent(jLabelL);

        jScrollPaneL.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneL.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jScrollPaneLVetoableChange(evt);
            }
        });

        jListL.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListL.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListL.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListLValueChanged(evt);
            }
        });
        jListL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jListLKeyPressed(evt);
            }
        });
        jScrollPaneL.setViewportView(jListL);

        jSplitPane7.setRightComponent(jScrollPaneL);

        jSplitPane6.setRightComponent(jSplitPane7);

        jSplitPaneKey1.setLeftComponent(jSplitPane6);

        jSplitPane8.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTextFieldR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRActionPerformed(evt);
            }
        });
        jSplitPane8.setTopComponent(jTextFieldR);

        jSplitPane9.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jLabelR.setText("項目２");
        jSplitPane9.setTopComponent(jLabelR);

        jScrollPaneR.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jListR.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListR.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListR.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListRValueChanged(evt);
            }
        });
        jListR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jListRKeyPressed(evt);
            }
        });
        jScrollPaneR.setViewportView(jListR);

        jSplitPane9.setRightComponent(jScrollPaneR);

        jSplitPane8.setRightComponent(jSplitPane9);

        jSplitPaneKey1.setRightComponent(jSplitPane8);

        jSplitPaneKey2.setLeftComponent(jSplitPaneKey1);

        jTabbedPaneEorV.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneEorVStateChanged(evt);
            }
        });

        jSplitPaneStr1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneStr1.setMinimumSize(new java.awt.Dimension(91, 1));
        jSplitPaneStr1.setOneTouchExpandable(true);

        jSplitPaneStr2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneStr2.setOneTouchExpandable(true);

        jSplitPaneStr3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneStr3.setOneTouchExpandable(true);

        jSplitPaneStr4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneStr4.setOneTouchExpandable(true);

        jSplitPaneStr5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneStr5.setOneTouchExpandable(true);

        jLabelCol5.setText("項目５");
        jSplitPaneCol5.setLeftComponent(jLabelCol5);

        jTextFieldCol5.setText("jTextField5");
        jSplitPaneCol5.setRightComponent(jTextFieldCol5);

        jSplitPaneStr5.setTopComponent(jSplitPaneCol5);

        jTextAreaHon.setColumns(20);
        jTextAreaHon.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jTextAreaHon.setLineWrap(true);
        jTextAreaHon.setRows(5);
        jScrollPane3.setViewportView(jTextAreaHon);

        jSplitPaneStr5.setRightComponent(jScrollPane3);

        jSplitPaneStr4.setBottomComponent(jSplitPaneStr5);

        jLabelCol4.setText("項目４");
        jSplitPaneCol4.setLeftComponent(jLabelCol4);

        jTextFieldCol4.setText("jTextField4");
        jSplitPaneCol4.setRightComponent(jTextFieldCol4);

        jSplitPaneStr4.setLeftComponent(jSplitPaneCol4);

        jSplitPaneStr3.setBottomComponent(jSplitPaneStr4);

        jLabelCol3.setText("項目３");
        jSplitPaneCol3.setLeftComponent(jLabelCol3);

        jTextFieldCol3.setText("jTextField3");
        jSplitPaneCol3.setRightComponent(jTextFieldCol3);

        jSplitPaneStr3.setLeftComponent(jSplitPaneCol3);

        jSplitPaneStr2.setBottomComponent(jSplitPaneStr3);

        jLabelCol2.setText("項目２");
        jSplitPaneCol2.setLeftComponent(jLabelCol2);

        jTextFieldCol2.setText("jTextField2");
        jSplitPaneCol2.setRightComponent(jTextFieldCol2);

        jSplitPaneStr2.setLeftComponent(jSplitPaneCol2);

        jSplitPaneStr1.setBottomComponent(jSplitPaneStr2);

        jLabelCol1.setText("項目１");
        jSplitPaneCol1.setLeftComponent(jLabelCol1);

        jTextFieldCol1.setText("jTextField1");
        jTextFieldCol1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCol1ActionPerformed(evt);
            }
        });
        jSplitPaneCol1.setRightComponent(jTextFieldCol1);

        jSplitPaneStr1.setLeftComponent(jSplitPaneCol1);

        jTabbedPaneEorV.addTab("編集", jSplitPaneStr1);

        jTextAreaHonV.setEditable(false);
        jTextAreaHonV.setColumns(20);
        jTextAreaHonV.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jTextAreaHonV.setLineWrap(true);
        jTextAreaHonV.setRows(5);
        jScrollPane4.setViewportView(jTextAreaHonV);

        jTabbedPaneEorV.addTab("表示", jScrollPane4);

        jSplitPaneKey2.setRightComponent(jTabbedPaneEorV);

        jSplitPane1.setRightComponent(jSplitPaneKey2);

        jMenuFile.setText("ファイル");

        jMenuItemConfig.setText("環境設定");
        jMenuItemConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConfigActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemConfig);
        jMenuFile.add(jSeparator1);

        jMenuItemClose.setText("閉じる");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);

        jMenuBar1.add(jMenuFile);

        jMenu2.setText("表示");

        jMenu1.setText("表示キー項目（左）");

        jRadioButtonMenuItemLKey1.setSelected(true);
        jRadioButtonMenuItemLKey1.setText("キー項目１");
        jRadioButtonMenuItemLKey1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemLKey1ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItemLKey1);

        jRadioButtonMenuItemLKey2.setSelected(true);
        jRadioButtonMenuItemLKey2.setText("キー項目２");
        jRadioButtonMenuItemLKey2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemLKey2ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItemLKey2);

        jRadioButtonMenuItemLKey3.setSelected(true);
        jRadioButtonMenuItemLKey3.setText("キー項目３");
        jRadioButtonMenuItemLKey3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemLKey3ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItemLKey3);

        jRadioButtonMenuItemLKey4.setSelected(true);
        jRadioButtonMenuItemLKey4.setText("キー項目４");
        jRadioButtonMenuItemLKey4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemLKey4ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItemLKey4);

        jRadioButtonMenuItemLKey5.setSelected(true);
        jRadioButtonMenuItemLKey5.setText("キー項目５");
        jRadioButtonMenuItemLKey5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemLKey5ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItemLKey5);

        jRadioButtonMenuItemLKey6.setSelected(true);
        jRadioButtonMenuItemLKey6.setText("データ本文");
        jRadioButtonMenuItemLKey6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemLKey6ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItemLKey6);

        jMenu2.add(jMenu1);

        jMenu4.setText("表示キー項目（右）");

        jRadioButtonMenuItemRKey1.setSelected(true);
        jRadioButtonMenuItemRKey1.setText("キー項目１");
        jRadioButtonMenuItemRKey1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemRKey1ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItemRKey1);

        jRadioButtonMenuItemRKey2.setSelected(true);
        jRadioButtonMenuItemRKey2.setText("キー項目２");
        jRadioButtonMenuItemRKey2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemRKey2ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItemRKey2);

        jRadioButtonMenuItemRKey3.setSelected(true);
        jRadioButtonMenuItemRKey3.setText("キー項目３");
        jRadioButtonMenuItemRKey3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemRKey3ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItemRKey3);

        jRadioButtonMenuItemRKey4.setSelected(true);
        jRadioButtonMenuItemRKey4.setText("キー項目４");
        jRadioButtonMenuItemRKey4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemRKey4ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItemRKey4);

        jRadioButtonMenuItemRKey5.setSelected(true);
        jRadioButtonMenuItemRKey5.setText("キー項目５");
        jRadioButtonMenuItemRKey5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemRKey5ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItemRKey5);

        jRadioButtonMenuItemRKey6.setSelected(true);
        jRadioButtonMenuItemRKey6.setText("データ本文");
        jRadioButtonMenuItemRKey6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemRKey6ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItemRKey6);

        jMenu2.add(jMenu4);

        jMenu5.setText("検索条件（左）");

        jRadioButtonMenuItemLFind1.setSelected(true);
        jRadioButtonMenuItemLFind1.setText("前方一致検索");
        jMenu5.add(jRadioButtonMenuItemLFind1);

        jRadioButtonMenuItemLFind2.setSelected(true);
        jRadioButtonMenuItemLFind2.setText("部分一致検索");
        jRadioButtonMenuItemLFind2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItemLFind2ActionPerformed(evt);
            }
        });
        jMenu5.add(jRadioButtonMenuItemLFind2);

        jMenu2.add(jMenu5);

        jMenu7.setText("検索条件（右）");

        jRadioButtonMenuItemRFind1.setSelected(true);
        jRadioButtonMenuItemRFind1.setText("前方一致検索");
        jMenu7.add(jRadioButtonMenuItemRFind1);

        jRadioButtonMenuItemRFind2.setSelected(true);
        jRadioButtonMenuItemRFind2.setText("部分一致検索");
        jMenu7.add(jRadioButtonMenuItemRFind2);

        jMenu2.add(jMenu7);
        jMenu2.add(jSeparator2);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("ヘルプ");

        jMenuItemHelp.setText("ヘルプ");
        jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHelpActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemHelp);

        jMenuItemVersion.setText("バージョン情報");
        jMenuItemVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemVersionActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemVersion);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConfigActionPerformed
        card.ConfFrm.setVisible(true);
    }//GEN-LAST:event_jMenuItemConfigActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        card.writeProp();
        System.exit(0);
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jRadioButtonMenuItemRKey1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemRKey1ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemRKey1ActionPerformed

    private void jRadioButtonMenuItemRKey2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemRKey2ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemRKey2ActionPerformed

    private void jRadioButtonMenuItemLFind2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemLFind2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonMenuItemLFind2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        card.writeProp();
    }//GEN-LAST:event_formWindowClosing

    private void jButtonDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelActionPerformed
        if (jListL.getSelectedIndex() <= 0) {
            //新規追加
            //card.addCard();
        } else {
            //更新
            card.delCard((Integer)ModelIdx.getElementAt(jListL.getSelectedIndex()));
        }
    }//GEN-LAST:event_jButtonDelActionPerformed

    private void jButtonInstComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jButtonInstComponentResized

    }//GEN-LAST:event_jButtonInstComponentResized

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        jSplitPane1.setDividerLocation(jSplitPane2.getMinimumSize().height);
        jSplitPane3.setDividerLocation(jButtonDel.getMinimumSize().width + 20);
        jSplitPane2.setDividerLocation(jSplitPane2.getWidth() - jButtonDel.getMinimumSize().width * 2 - 60);
    }//GEN-LAST:event_formComponentResized

    private void jListLValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListLValueChanged
        if (this.isVisible()) {
            //もう片方のスクロール
            Point point = viewL.getViewPosition();
            try {
                viewR.setViewPosition(point);
            }catch (Exception e) {
                e.printStackTrace();
            }
            jListR.setSelectedIndex(jListL.getSelectedIndex());
            if ((jListL.getSelectedIndex() >= 0) && (jListR.getSelectedIndex() >= 0)) {
                card.setCard(jListL.getSelectedIndex());
            }
            //System.out.println("x：" + point.x + ", y:" + point.y);
        }
    }//GEN-LAST:event_jListLValueChanged

    private void jListRValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListRValueChanged
        if (jListR.getSelectedIndex() >= 0) {
            //System.out.println(evt.getSource().toString());
            try {
                if (viewR == null) {
                    return;
                }
                //もう片方のスクロール
                Point point = viewR.getViewPosition();
                viewL.setViewPosition(point);
                //インデックスを同期
                jListL.setSelectedIndex(jListR.getSelectedIndex());
            }catch (Exception e) {
                //何もしない
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jListRValueChanged

    private void jButtonInstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInstActionPerformed
        if (jListL.getSelectedIndex() <= 0) {
            //新規追加
            card.addCard();
        } else {
            //更新
            int tmpIdx = jListL.getSelectedIndex();
            card.updtCard((Integer) ModelIdx.getElementAt(jListL.getSelectedIndex()));
            jListL.setSelectedIndex(tmpIdx);
        }
    }//GEN-LAST:event_jButtonInstActionPerformed

    private void jRadioButtonMenuItemLKey1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemLKey1ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemLKey1ActionPerformed

    private void jRadioButtonMenuItemLKey2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemLKey2ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemLKey2ActionPerformed

    private void jRadioButtonMenuItemLKey3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemLKey3ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemLKey3ActionPerformed

    private void jRadioButtonMenuItemLKey4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemLKey4ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemLKey4ActionPerformed

    private void jRadioButtonMenuItemLKey5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemLKey5ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemLKey5ActionPerformed

    private void jRadioButtonMenuItemLKey6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemLKey6ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemLKey6ActionPerformed

    private void jRadioButtonMenuItemRKey3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemRKey3ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemRKey3ActionPerformed

    private void jRadioButtonMenuItemRKey4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemRKey4ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemRKey4ActionPerformed

    private void jRadioButtonMenuItemRKey5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemRKey5ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemRKey5ActionPerformed

    private void jRadioButtonMenuItemRKey6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemRKey6ActionPerformed
        setList(false);
    }//GEN-LAST:event_jRadioButtonMenuItemRKey6ActionPerformed

    private void jTabbedPaneEorVStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneEorVStateChanged
        if (!this.isVisible()) {
            return;
        }
        int idx = jTabbedPaneEorV.getSelectedIndex();
        if (idx == 0) {
            //エディットモード
            //リスト選択と同じ処理
           if (jListL.getSelectedIndex() == 0) {
               card.CardFrm.setUpdtMode(false);
           }else {
               card.CardFrm.setUpdtMode(true);
           }
        }else {
            //表示モード
            card.CardFrm.setViewMode();
        }
    }//GEN-LAST:event_jTabbedPaneEorVStateChanged

    private void jTextFieldLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLActionPerformed
        setList(false);
    }//GEN-LAST:event_jTextFieldLActionPerformed

    private void jTextFieldRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRActionPerformed
        setList(false);
    }//GEN-LAST:event_jTextFieldRActionPerformed

    private void jScrollPaneLVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jScrollPaneLVetoableChange
        //System.out.println("aaaa!");
    }//GEN-LAST:event_jScrollPaneLVetoableChange

    private void jMenuItemVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemVersionActionPerformed
        JOptionPane.showMessageDialog(this, "Ver " + card.version + "\n\n作者：田中 秀宗\n　", "バージョンとか", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItemVersionActionPerformed

    private void jListRKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListRKeyPressed
        char chr = evt.getKeyChar();
        int idx = jListR.getSelectedIndex();
        if (chr == '+') {
            swap(idx, idx + 1, idx + 1);
        } else if (chr == '-'){
            swap(idx, idx - 1, idx - 1);
        }
    }//GEN-LAST:event_jListRKeyPressed

    private void jListLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListLKeyPressed
        char chr = evt.getKeyChar();
        int idx = jListL.getSelectedIndex();
        if (chr == '+') {
            swap(idx, idx + 1, idx + 1);
        } else if (chr == '-'){
            swap(idx, idx - 1, idx - 1);
        }
    }//GEN-LAST:event_jListLKeyPressed

    private void jMenuItemHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHelpActionPerformed
        try {
            Desktop desktop = Desktop.getDesktop();
            String jarPath = System.getProperty("java.class.path");
            String dirPath = jarPath.substring(0, jarPath.lastIndexOf(File.separator)+1);
            //props.load(new FileInputStream(dirPath + "hoge.properties"));
            File file = new File(dirPath + "/CardHELP/index.html");
            URI uri = file.toURI();
            desktop.browse(uri);
        }catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ヘルプファイルがみつかりません。", "ヘルプ", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemHelpActionPerformed

    private void jTextFieldCol1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCol1ActionPerformed
        if (jTextFieldCol1.getText().equals("")) {
            Date now = new Date();	//現在日時でDateを作成
            DateFormat df;
            if (card.Seireki0Gengo1 == 0){
                df = new SimpleDateFormat("yyyy/MM/dd (E)");
            }else {
                Locale locale = new Locale("ja", "JP", "JP");
                df = new SimpleDateFormat("Gy/MM/dd (E)", locale);
            }
            jTextFieldCol1.setText(df.format(now));
        }
    }//GEN-LAST:event_jTextFieldCol1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CardJFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDel;
    private javax.swing.JButton jButtonInst;
    private javax.swing.JLabel jLabelCol1;
    private javax.swing.JLabel jLabelCol2;
    private javax.swing.JLabel jLabelCol3;
    private javax.swing.JLabel jLabelCol4;
    private javax.swing.JLabel jLabelCol5;
    private javax.swing.JLabel jLabelDBName;
    private javax.swing.JLabel jLabelL;
    private javax.swing.JLabel jLabelR;
    private javax.swing.JList jListL;
    private javax.swing.JList jListR;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemConfig;
    private javax.swing.JMenuItem jMenuItemHelp;
    private javax.swing.JMenuItem jMenuItemVersion;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLFind1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLFind2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLKey1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLKey2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLKey3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLKey4;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLKey5;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemLKey6;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRFind1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRFind2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRKey1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRKey2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRKey3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRKey4;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRKey5;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemRKey6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPaneL;
    private javax.swing.JScrollPane jScrollPaneR;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JSplitPane jSplitPane7;
    private javax.swing.JSplitPane jSplitPane8;
    private javax.swing.JSplitPane jSplitPane9;
    private javax.swing.JSplitPane jSplitPaneCol1;
    private javax.swing.JSplitPane jSplitPaneCol2;
    private javax.swing.JSplitPane jSplitPaneCol3;
    private javax.swing.JSplitPane jSplitPaneCol4;
    private javax.swing.JSplitPane jSplitPaneCol5;
    private javax.swing.JSplitPane jSplitPaneKey1;
    private javax.swing.JSplitPane jSplitPaneKey2;
    private javax.swing.JSplitPane jSplitPaneStr1;
    private javax.swing.JSplitPane jSplitPaneStr2;
    private javax.swing.JSplitPane jSplitPaneStr3;
    private javax.swing.JSplitPane jSplitPaneStr4;
    private javax.swing.JSplitPane jSplitPaneStr5;
    private javax.swing.JTabbedPane jTabbedPaneEorV;
    private javax.swing.JTextArea jTextAreaHon;
    private javax.swing.JTextArea jTextAreaHonV;
    private javax.swing.JTextField jTextFieldCol1;
    private javax.swing.JTextField jTextFieldCol2;
    private javax.swing.JTextField jTextFieldCol3;
    private javax.swing.JTextField jTextFieldCol4;
    private javax.swing.JTextField jTextFieldCol5;
    private javax.swing.JTextField jTextFieldL;
    private javax.swing.JTextField jTextFieldR;
    // End of variables declaration//GEN-END:variables

}
