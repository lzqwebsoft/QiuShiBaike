import java.awt.*;
import javax.swing.*;

public class ImageDisplayWindow extends QiuShiClient {
    private QiuShiClient qiuShiClient;
    private JLabel imageLabel;
    private JButton closeButton;

    public ImageDisplayWindow(QiuShiClient owner) {
        qiuShiClient = owner;

        // 不能变换大小与去掉窗体标题
        setResizable(false);
        setUndecorated(true);

        // 添加移动属性
        addMoveWindowAttribute(this);
        
        JRootPane rootPane= new JRootPane();
        rootPane.setLayout(new BorderLayout());

        //==================顶部面板==================
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(getWidth(), 30));
        topPanel.setLayout(null);

        closeButton = getJButton(new String[]{"close.png", null, null}, "关闭", "exitImageDisplay");
        topPanel.add(closeButton);

        rootPane.add(topPanel, BorderLayout.NORTH);
        //============================================

        //================中间面板====================
        JPanel centerPane = new JPanel();
        centerPane.setOpaque(false);
        centerPane.setPreferredSize(new Dimension(getWidth(), 100));
        // centerPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        centerPane.setLayout(null);

        imageLabel = new JLabel();
        centerPane.add(imageLabel);
        //============================================

        rootPane.add(centerPane, BorderLayout.CENTER);

        setBackground(Color.white);

        setContentPane(rootPane);
    }

    public void exitImageDisplay() {
        setVisible(false);
    }

    public void showImage(ImageIcon icon) {
        int thumbWidth = icon.getIconWidth();
        int thumbHeight = icon.getIconHeight();
        imageLabel.setIcon(icon);
        imageLabel.setBounds(10, 0, thumbWidth, thumbHeight);

        closeButton.setBounds(thumbWidth - 10, 2, 28, 28);

        setLocationRelativeTo(qiuShiClient);
        
        setSize(thumbWidth + 20, thumbHeight + 50);
        setVisible(true);
    }
}
