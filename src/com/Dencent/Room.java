package com.Dencent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.Dencent.PicInfo;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import com.Dencent.PicInfo;


public class Room extends JFrame{
    private JTextPane screen = null;
    private JTextPane messageArea = null;
    private JButton emojiBtn = null;
    private JButton picBtn = null;
    private JButton sendBtn = null;
    private JComboBox selectObj = null;
    private JTextPane jpChat = null;
    private JPanel jPanel = null;
    private JScrollPane jsc1 = null;
    private JScrollPane jsc2 = null;
    private StyledDocument docChat = null;
    private StyledDocument docMsg = null;

    private PicsJWindow picWindow;
    private List<PicInfo> myPicInfo = new LinkedList<>();
    private List<PicInfo> receivePicInfo = new LinkedList<>();
    private StyledDocument doc = null;

    public Room(){
        setAlwaysOnTop(true);

        picBtn = new JButton();
        picBtn.setIcon(new ImageIcon("src/pic.png"));
        picBtn.setBounds(40,210,27,27);
        this.getContentPane().add(picBtn);
        picBtn.setBorderPainted(false);

        picBtn.addActionListener(new ActionListener() { // 插入图片事件
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser f = new JFileChooser(); // 查找文件
                f.showOpenDialog(null);
                insertIcon(f.getSelectedFile()); // 插入图片
            }
        });


        this.setTitle("聊天室主页面");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600,480);
        this.setResizable(false);

        this.setLayout(null);
        screen = new JTextPane();
        screen.setEditable(false);
        screen.setBounds(5,5,570,200);
        this.getContentPane().add(screen);
        screen.setBorder(BorderFactory.createBevelBorder(1));

        jsc1 = new JScrollPane(screen,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsc1.setBounds(5,5,570,200);
        this.getContentPane().add(jsc1);

        emojiBtn = new JButton();
        emojiBtn.setIcon(new ImageIcon("src/emoji.png"));
        emojiBtn.setBounds(5,210,27,27);
        this.getContentPane().add(emojiBtn);
        emojiBtn.setBorderPainted(false);

        JLabel chatObj = new JLabel("聊天对象：");
        chatObj.setBounds(400,210,70,27);
        this.getContentPane().add(chatObj);
        selectObj = new JComboBox();
        selectObj.setBounds(470,212,100,22);
        this.getContentPane().add(selectObj);

        messageArea = new JTextPane();
        messageArea.setBounds(5,240,570,150);
        this.getContentPane().add(messageArea);
        messageArea.setBorder(BorderFactory.createBevelBorder(1));

        jsc2 = new JScrollPane(messageArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsc2.setBounds(5,240,570,150);
        this.getContentPane().add(jsc2);

        sendBtn = new JButton("发送");
        sendBtn.setBounds(5,410,70,27);
        this.getContentPane().add(sendBtn);

        screenActionListener sendBtnAl = new screenActionListener();
        sendBtn.addActionListener(sendBtnAl);


        doc = screen.getStyledDocument();
    }


    class screenActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = messageArea.getText();
            String oldInfo = screen.getText();
            if(oldInfo.length()==0){
                String information = ">>> " + message;
                screen.setText(information);
            }
            else {
                String information = oldInfo + '\n' + ">>> " + message;
                screen.setText(information);
            }

            messageArea.setText("");

        }
    }

    public void insertSendPic(ImageIcon imgIc) {
        //jpMsg.setCaretPosition(docChat.getLength()); // 设置插入位置
        imgIc.setImage(imgIc.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
        messageArea.insertIcon(imgIc); // 插入图片
        System.out.print(imgIc.toString());
        //insert(new FontAttrib()); // 这样做可以换行
    }

    /*
     * 重组收到的表情信息串
     */
    public void receivedPicInfo(String picInfos) {
        String[] infos = picInfos.split("[+]");
        for (int i = 0; i < infos.length; i++) {
            String[] tem = infos[i].split("[&]");
            if (tem.length == 2) {
                PicInfo pic = new PicInfo(Integer.parseInt(tem[0]), tem[1]);
                receivePicInfo.add(pic);
            }
        }
    }

    /**
     * 重组发送的表情信息
     *
     * @return 重组后的信息串  格式为   位置|代号+位置|代号+……
     */
    private String buildPicInfo() {
        StringBuilder sb = new StringBuilder("");
        //遍历jtextpane找出所有的图片信息封装成指定格式
        for (int i = 0; i < this.messageArea.getText().length(); i++) {
            if (docMsg.getCharacterElement(i).getName().equals("icon")) {
                //ChatPic = (ChatPic)
                Icon icon = StyleConstants.getIcon(messageArea.getStyledDocument().getCharacterElement(i).getAttributes());
                ChatPic cupic = (ChatPic) icon;
                PicInfo picInfo = new PicInfo(i, cupic.getIm() + "");
                myPicInfo.add(picInfo);
                sb.append(i).append("&").append(cupic.getIm()).append("+");
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
        //return null;
    }

    public JButton getPicBtn(){
        return picBtn;
    }


    private void insertIcon(File file) {
        screen.setCaretPosition(doc.getLength()); // 设置插入位置
        ImageIcon img = new ImageIcon(file.getPath());
        screen.insertIcon(img); // 插入图片
    }




}
