import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.awt.Image;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * 菜单单击事件
 */
public class MenuItemMonitor implements ActionListener {

    public static final String COPY_CONTENT = "copy_content";
    public static final String COPY_IMAGE = "copy_image";

    private JLabel content;

    public MenuItemMonitor(JLabel content) {
        this.content = content;
    }
 
    @Override 
    public void actionPerformed(ActionEvent event) {
        // 获取String格式的ActionCommand 
        String command = ((JMenuItem)event.getSource()).getActionCommand();
        if(command.equalsIgnoreCase(COPY_CONTENT)) {
            // 复制文字到剪切板
            String context = content.getText();
            context = context.replaceAll("<br />", "\r\n");
            context = context.replaceAll("<[^>]+>|<[^>]+","");
            if(context != null && context.trim().length() > 0) {
                StringSelection stringSelection = new StringSelection(context);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        } else if(command.equalsIgnoreCase(COPY_IMAGE)) {
            // 复制图片到剪切板
            ImageIcon icon = (ImageIcon)content.getIcon();
            Image img = (Image)icon.getImage();
            if(img != null) {
                ImageTransferable transferable = new ImageTransferable(img);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(transferable, null);
            }
        }
    }
}