
import java.awt.Event;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

/**
 * 指定されたテキストコンポーネントにUndo/Redo機能をつけます。
 * 
 * @author a-san        クリア機能追加：hdm
 */
public class UndoHelper {
    public static final String ACTION_KEY_UNDO = "undo";
    public static final String ACTION_KEY_REDO = "redo";
    UndoManager undoManager = new UndoManager();
    
    /** 指定されたテキストコンポーネントにUndo/Redo機能をつけます。　*/
    public UndoHelper(JTextComponent textComponent) {
        ActionMap amap = textComponent.getActionMap();
        InputMap imap = textComponent.getInputMap();
        if (amap.get(ACTION_KEY_UNDO) == null) {
            UndoAction undoAction = new UndoAction();
            amap.put(ACTION_KEY_UNDO, undoAction);
            imap.put((KeyStroke) undoAction.getValue(Action.ACCELERATOR_KEY), ACTION_KEY_UNDO);
        }
        if (amap.get(ACTION_KEY_REDO) == null) {
            RedoAction redoAction = new RedoAction();
            amap.put(ACTION_KEY_REDO, redoAction);
            imap.put((KeyStroke) redoAction.getValue(Action.ACCELERATOR_KEY), ACTION_KEY_REDO);
        }
        // リスナを登録
        textComponent.getDocument().addDocumentListener(new DocListener());
    }
    public UndoManager getUndoManager() { return undoManager; }
    
    /** UNDO情報をクリアする */
    public void clearUndoEdit() {
            undoManager.discardAllEdits();
    }
    
    /**
     * 元に戻す
     */
    class UndoAction extends AbstractAction {
        UndoAction() {
            super("元に戻す(U)");
            putValue(MNEMONIC_KEY, new Integer('U'));
            putValue(SHORT_DESCRIPTION, "元に戻す");
            putValue(LONG_DESCRIPTION, "元に戻す");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Z', Event.CTRL_MASK));
            //putValue(SMALL_ICON, SwingUtil.getImageIcon("/resources/EditUndo.png"));
        }
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }
    }

    /**
     * やり直し
     */
    class RedoAction extends AbstractAction {
        RedoAction() {
            super("やり直し(R)");
            putValue(MNEMONIC_KEY, new Integer('R'));
            putValue(SHORT_DESCRIPTION, "やり直し");
            putValue(LONG_DESCRIPTION, "やり直し");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Y', Event.CTRL_MASK));
            //putValue(SMALL_ICON, SwingUtil.getImageIcon("/resources/EditRedo.png"));
        }
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        }
    }
    /** ドキュメントが変更されたときのリスナー. */
    private class DocListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            if (e instanceof DefaultDocumentEvent) {
                DefaultDocumentEvent de = (DefaultDocumentEvent) e;
                undoManager.addEdit(de);
            }
        }
        public void removeUpdate(DocumentEvent e) {
            if (e instanceof DefaultDocumentEvent) {
                DefaultDocumentEvent de = (DefaultDocumentEvent) e;
                undoManager.addEdit(de);
            }
        }
        public void changedUpdate(DocumentEvent e) {
            // 属性が変わったときは、何もしなくてよい。
        }
    }   
}
/*  使い方
    JTextField nameField = new JTextField();
    UndoHelper helper = new UndoHelper(nameField);
 */