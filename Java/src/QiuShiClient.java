import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
import java.lang.Math;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

public class QiuShiClient extends JDialog {

    private int locationX, locationY;
    private boolean isDraging = false;  //用于指示拖动
    private JLabel header, author, content;
    private JButton thumb;
    private DataHandler dataHandler;
    private QiuShi current;
    private ImageIcon currentBigImage;
    private ImageDisplayWindow imagePlayer;
    private Font customFont;

    public QiuShiClient() {
        super();
    }

    public QiuShiClient(DataHandler dh) {
        dataHandler = dh;

        // 检查当前目录是否有上一次保存的QiuShi对象文件
        int idx = initReloadQiuShi();

        QiuShi firstQS = null;
        try {
            firstQS = dataHandler.getDataByIdx(idx);
            if(firstQS==null) {
                System.out.println("数据库中无糗事！");
                exitSystem();
            }
        } catch(Exception e) {
            System.out.println("初始化获取糗事失败！");
            exitSystem();
        }
        // 加载字体
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("custom_font.ttf"));
        } catch(Exception e) {
            e.printStackTrace();
            
        }
        
        // 设定默认的高度,与居中
        this.setSize(300, 500);
        this.setLocationRelativeTo(null);
        // 不能变换大小与去掉窗体标题
        setResizable(false);
        setUndecorated(true);
        // 给当前窗体添加鼠标移动事件
        addMoveWindowAttribute(this);
        // 初始化拖盘工具
        initializeTray();

        JRootPane rootPane= new JRootPane();
        rootPane.setLayout(new BorderLayout());

        //==================顶部面板==================
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(getWidth(), 30));
        topPanel.setLayout(null);

        // 图标
        JLabel logo = getTextJLabel("<html><font color='#000000'>糗事百科</font></html>");
        ImageIcon logIcon = new ImageIcon(this.getClass().getResource("logo.png"));
        logo.setIcon(logIcon);
        logo.setIconTextGap(JLabel.LEFT);
        logo.setFont(customFont.deriveFont(Font.PLAIN, 14));
        topPanel.add(logo);
        logo.setBounds(5, 2, 100, 28);

        //添加最小化和关闭按钮
        JButton minButton = getJButton(new String[]{"mini.png", null, null}, "最小化", "toMinimize");
        topPanel.add(minButton);
        minButton.setBounds(getWidth()-57, 2, 28, 28);

        JButton closeButton = getJButton(new String[]{"close.png", null, null}, "关闭", "exitSystem");
        topPanel.add(closeButton);
        closeButton.setBounds(getWidth()-30, 2, 28, 28);

        rootPane.add(topPanel, BorderLayout.NORTH);
        //============================================

        //================中间面板====================
        JPanel centerPane = new JPanel();
        centerPane.setOpaque(false);
        centerPane.setPreferredSize(new Dimension(getWidth(), 100));
        // centerPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        centerPane.setLayout(null);

        // 用户头像与昵称
        String authorName = firstQS.getAuthor();
        int authorHeight = 10;
        header = new JLabel();
        centerPane.add(header);

        author = getTextJLabel("");
        author.setFont(customFont.deriveFont(Font.PLAIN, 15));
        centerPane.add(author);
        if(authorName!=null && !authorName.equals("")) {
            ImageIcon headerIcon = new ImageIcon();
            try {
                headerIcon = new ImageIcon(ImageIO.read(firstQS.getHeader()));
                headerIcon.setImage(headerIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                header.setIcon(headerIcon);
                header.setBounds(10, 5, 50, 50);
            } catch(Exception e) {}
            
            author.setText("<html><font color='#9b8878'>" + authorName + "</font></html>");
            author.setBounds(80, 5, getWidth()-80, 50);
            authorHeight = 50;
        } else {
            header.setIcon(new ImageIcon());
            header.setBounds(0, 0, 0, 0);
            author.setText("");
            author.setBounds(0, 0, 0, 0);
        }

        // 内容与图片
        content = getTextJLabel("");
        String text = firstQS.getContent();
        int cententHeight = (int)Math.ceil(text.length() * 18.0 / (getWidth() - 20)) * 18;
        String labelText = String.format("<html><div WIDTH=%d>%s</div><html>", getWidth()-20, text);
        content.setText(labelText);
        content.setFont(customFont.deriveFont(Font.PLAIN, 14));
        centerPane.add(content);
        content.setBounds(10, authorHeight + 10, getWidth()-20, cententHeight);

        int thumbHeight = 20;
        InputStream thumbStream = firstQS.getThumb();
        thumb = getJButton(new String[]{null, null, null}, "点击查看大图", "displayImage");
        centerPane.add(thumb);
        if(thumbStream!=null) {
            ImageIcon thumbIncon = new ImageIcon();
            try {
                currentBigImage = new ImageIcon(ImageIO.read(thumbStream));
                thumbIncon = new ImageIcon(currentBigImage.getImage());
                int thumbWidth = (int)(thumbIncon.getIconWidth() * 0.5);
                thumbHeight = (int)(thumbIncon.getIconHeight() * 0.5);
                thumbIncon.setImage(thumbIncon.getImage().getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_DEFAULT));
                thumb.setIcon(thumbIncon);
                thumb.setBounds((getWidth()-thumbWidth)/2, cententHeight + authorHeight + 20, thumbWidth, thumbHeight);
            } catch(Exception e) {e.printStackTrace();}
        } else {
            thumb.setIcon(new ImageIcon());
            thumb.setBounds(0, 0, 0, 0);
            currentBigImage = null;
        }

        rootPane.add(centerPane, BorderLayout.CENTER);
        //============================================

        //================底部面板====================
        JPanel bottomPane = new JPanel();
        bottomPane.setOpaque(false);
        bottomPane.setPreferredSize(new Dimension(getWidth(), 50));
        bottomPane.setLayout(new BorderLayout());

        JButton previousButton = getJButton(new String[]{"prev_normal.png", "prev_hover.png", "prev_pressed.png"}, "上一条", "toPrevious");
        bottomPane.add(previousButton, BorderLayout.WEST);

        JButton nextButton = getJButton(new String[]{"next_normal.png", "next_hover.png", "next_pressed.png"}, "下一条", "toNext");
        bottomPane.add(nextButton, BorderLayout.EAST);


        rootPane.add(bottomPane, BorderLayout.SOUTH);
        //============================================

        this.setSize(300, cententHeight + authorHeight + 80 + thumbHeight);
        setContentPane(rootPane);

        //setAlwaysOnTop(true);
        setVisible(true);

        setBackground(Color.white);
        // 添加键盘快捷键
        addShortKeyAttribute(rootPane);

        current = firstQS;

        imagePlayer = new ImageDisplayWindow(this);
    }

    //得到一个按钮
    public JButton getJButton(String[] iconPaths, String hit, final String method) {
        JButton button = new JButton();
        final Icon[] bicons = new ImageIcon[3];
        if(iconPaths != null && iconPaths.length == 3) {
            for(int i=0; i<3; i++) {
                if(iconPaths[i] != null)
                    bicons[i]= new ImageIcon(this.getClass().getResource(iconPaths[i]));
                else
                    bicons[i]= null;
            }
        }
        if(bicons[0]!=null)
            button.setIcon(bicons[0]);
        if(bicons[2]!=null)
            button.setPressedIcon(bicons[2]);
        if(bicons[1]!=null)
            button.setRolloverIcon(bicons[1]);
        button.setFocusable(false);
        button.setToolTipText(hit);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    QiuShiClient.this.getClass().getDeclaredMethod(method).invoke(QiuShiClient.this);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });
        return button;
    }

    // 得到一个文字标签(文字常常渲染光滑)
    private JLabel getTextJLabel(String text) {
        return new JLabel(text) {
            @Override
            public void paintComponent(Graphics g) {
                if(g instanceof Graphics2D) {  
                    Graphics2D g2 = (Graphics2D)g;  
                    g2.setRenderingHint (  
                        RenderingHints.KEY_TEXT_ANTIALIASING,  
                        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);  
                }
                super.paintComponent(g);
            }
        };
    }

    //添加移动
    public void addMoveWindowAttribute(JDialog comp) {
        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                isDraging = true;
                locationX = e.getX();
                locationY = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                isDraging = false;
            }
        });
        comp.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isDraging) {
                    int left = getLocation().x;
                    int top = getLocation().y;
                    setLocation(left + e.getX() - locationX, top + e.getY() - locationY);
                }
            }
        });
    }

    // 添加键盘监听事件
    private void addShortKeyAttribute(Component comp) {
        comp.setFocusable(true);
        comp.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_UP) {
                    toPrevious();
                } else if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN) {
                    toNext();
                }
            }
        });
        comp.requestFocus();
    }

    //初始化托盘
    public void initializeTray() {
        if(SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            URL imageUrl = getClass().getResource("logo.png");
            Image image = Toolkit.getDefaultToolkit().getImage(imageUrl);

            PopupMenu popup = new PopupMenu();
            MenuItem item = new MenuItem("退出");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exitSystem();
                }
            });
            popup.add(item);

            TrayIcon trayIcon = new TrayIcon(image, "糗事百科", popup);
            trayIcon.setImageAutoSize(true);
            // 添加单击显示事件
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    QiuShiClient.this.setVisible(true);
                    QiuShiClient.this.toFront();
                }
            });
            try {
                tray.add(trayIcon);
            } catch(AWTException e) {
                System.out.println("无法加载托盘!");
            }
        } else {
            System.out.println("系统不支持托盘!");
        }
    }

    //退出系统
    public void exitSystem() {
        // 关闭数据库连接
        if(dataHandler != null)
            dataHandler.closeConnection();
        // 将当前糗事对象序列化保存文件
        try {
            if(current!=null) {
                PrintWriter writer = new PrintWriter("qiushi.obj");
                writer.println(current.getId());
                writer.close();
            }
        } catch(Exception e){e.printStackTrace();}
        System.exit(0);
    }

    private int initReloadQiuShi() {
        try {
            File qiushi = new File("qiushi.obj");
            if(qiushi.exists() && qiushi.isFile()) {
                FileReader fr = new FileReader(qiushi);
                BufferedReader br = new BufferedReader(fr);
                int idx = Integer.parseInt(br.readLine());
                br.close();
                br.close();
                return idx;
            }
        } catch(Exception e) {e.printStackTrace();}
        return 0;
    }

    // 显示大图
    public void displayImage() {
        if(imagePlayer!=null && currentBigImage != null) {
            imagePlayer.showImage(currentBigImage);
        } else {
            System.out.println("没有图片显示");
        }
    }

    //最小化隐藏
    public void toMinimize() {
        setVisible(false);
    }

    // 上一条
    public void toPrevious() {
        try {
            QiuShi qs = dataHandler.getPrevious(current.getId());
            if(qs==null) {
                System.out.println("已是最新一条了，前面再无糗事！");
            } else {
                updateWindow(qs);
            }
        } catch(Exception e) {
            System.out.println("获取糗事失败！");
        }
    }

    // 下一条
    public void toNext() {
        try {
            QiuShi qs = dataHandler.getNext(current.getId());
            if(qs==null) {
                System.out.println("已是最后一条了，后面再无糗事！");
            } else {
                updateWindow(qs);
            }
        } catch(Exception e) {
            System.out.println("获取糗事失败！");
            e.printStackTrace();
        }
    }

    private void updateWindow(QiuShi qiushi) {
        // 更新用户头像与昵称
        String authorName = qiushi.getAuthor();
        int authorHeight = 10;
        if(authorName!=null && !authorName.equals("")) {
            ImageIcon headerIcon = new ImageIcon();
            try {
                headerIcon = new ImageIcon(ImageIO.read(qiushi.getHeader()));
                headerIcon.setImage(headerIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                header.setIcon(headerIcon);
                header.setBounds(10, 5, 50, 50);
            } catch(Exception e) {e.printStackTrace();}
            author.setText("<html><font color='#9b8878'>" + authorName + "</font></html>");
            author.setBounds(80, 5, getWidth()-80, 50);
            authorHeight = 50;
        } else {
            header.setIcon(new ImageIcon());
            author.setText("");
            header.setBounds(0, 0, 0, 0);
            author.setBounds(0, 0, 0, 0);
        }

        // 更新内容与附加图片
        String text = qiushi.getContent();
        int cententHeight = (int)Math.ceil(text.length() * 18.0 / (getWidth() - 20)) * 18;
        String labelText = String.format("<html><div WIDTH=%d>%s</div><html>", getWidth()-20, text);
        content.setText(labelText);
        content.setBounds(10, authorHeight + 10, getWidth()-20, cententHeight);

        int thumbHeight = 20;
        InputStream thumbStream = qiushi.getThumb();
        if(thumbStream!=null) {
            ImageIcon thumbIncon = new ImageIcon();
            try {
                currentBigImage = new ImageIcon(ImageIO.read(thumbStream));
                thumbIncon = new ImageIcon(currentBigImage.getImage());
                int thumbWidth = thumbIncon.getIconWidth();
                thumbHeight = thumbIncon.getIconHeight();
                double k = 1;
                if(thumbWidth > (getWidth() - 20)) {
                    k = ((double)(getWidth() - 20) / thumbWidth);
                    thumbWidth = (int)(thumbWidth * k);
                    thumbHeight = (int)(thumbHeight * k);
                } else if (thumbHeight > 400) {
                    k = 400 / ((double)thumbWidth);
                    thumbWidth = (int)(thumbWidth * k);
                    thumbHeight = (int)(thumbHeight * k);
                }
                thumbIncon.setImage(thumbIncon.getImage().getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_DEFAULT));
                thumb.setIcon(thumbIncon);
                thumb.setBounds((getWidth()-thumbWidth)/2, cententHeight + authorHeight + 20, thumbWidth, thumbHeight);
            } catch(Exception e) {e.printStackTrace();}
        } else {
            thumb.setIcon(new ImageIcon());
            thumb.setBounds(0, 0, 0, 0);
            currentBigImage = null;
        }

        // 更新主窗体高度
        this.setSize(300, cententHeight + authorHeight + 80 + thumbHeight);

        current = qiushi;
    }   
}
