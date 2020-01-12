package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ca.ubc.cs304.delegates.Delegate;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class MainUI {
    private ImageIcon background = new ImageIcon("src\\bkgImg.jpg");
    private JFrame frame = new JFrame("super rent");
    private Dimension dim = frame.getToolkit().getScreenSize();
    private JPanel contentPane = (JPanel) frame.getContentPane();
    private CardLayout cardLayout;
    private JPanel cards;
    private Delegate delegate;
    public static Font myFont = new Font("SansSerif", Font.BOLD, 80);
    private InputStream music;
    private AudioStream audios;
    private JButton play = new JButton ();
    private boolean isPlay;

    // buttons on the main page
    private JButton servicebtn = new JButton("Services");
    private JButton manipulationbtn = new JButton("Manipulations");
    private JButton quitbtn = new JButton("Quit");

    //initialize 'service' card
    private ServicePanel servicePanel;

    //initialize 'manipulation' card
    private ManipulationPanel manipulationPane;

    /**dominant functions*/
    public MainUI(){

    }
    public void showMainUI(Delegate delegate) {
        this.delegate = delegate;
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        contentPane.setOpaque(false);


        // set frame location
        setFrameLocation();

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setOpaque(false);

        cards.add("cardHome",cardHome());
        cards.add("cardManipulation",cardManipulation());
        cards.add("cardService",cardService());

        cardLayout.show(cards,"cardHome");
        contentPane.add(cards, BorderLayout.CENTER);

        // set buttons
        setButtonAction();
        // set background image
        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.add(setBackgroundImage(),new Integer(Integer.MIN_VALUE));
        layeredPane.setOpaque(true);
    }

    /**preparation for cards*/
    // EFFECTS: initialize hintPanel and btnPanel
    private JPanel cardHome() {
        JPanel cardHome = new JPanel(new GridLayout(3,1));

        // initialize homePanel
        JPanel labelPanel_Home = new JPanel();
        labelPanel_Home.setOpaque(false);
        JLabel label_Home = new JLabel("What do you want to do with Super Rent?");
        label_Home.setBorder(BorderFactory.createEmptyBorder(200,20,0,20));
        labelPanel_Home.add(label_Home);

        JLabel jLabel = new JLabel();
        jLabel.setBorder(BorderFactory.createEmptyBorder(10,200,0,20));
        jLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
        jLabel.setText("This is a small system to for reservation, rent and return, as well as manipulate CUSTOMER table");


        GridLayout gd = new GridLayout(1,3);
        gd.setHgap(50);
        JPanel buttonPanel_Home = new JPanel(gd);
        buttonPanel_Home.setBorder(BorderFactory.createEmptyBorder(50,100,200,100));
        label_Home.setFont(myFont);
        buttonPanel_Home.setOpaque(false);

        // initialize btnPanel
        servicebtn.setPreferredSize(new Dimension(150,80));
        servicebtn.setFont(new Font("SansSerif", Font.BOLD, 40));
        buttonPanel_Home.add(servicebtn);

        manipulationbtn.setPreferredSize(new Dimension(150,800));
        manipulationbtn.setFont(new Font("SansSerif", Font.BOLD, 40));
        buttonPanel_Home.add(manipulationbtn);

        quitbtn.setPreferredSize(new Dimension(150,80));
        quitbtn.setFont(new Font("SansSerif", Font.BOLD, 40));
        buttonPanel_Home.add(quitbtn);

        play.setPreferredSize(new Dimension(150,80));
        play.setFont(new Font("SansSerif", Font.BOLD, 40));
        play.setText("Music");
        buttonPanel_Home.add(play);
        isPlay = false;

        // add both panels to home panel
        cardHome.add(labelPanel_Home);
        cardHome.add(jLabel);
        cardHome.add(buttonPanel_Home);
        cardHome.setOpaque(false);
        return cardHome;
    }

    // EFFECTS: initialize and return manipulation card
    private JPanel cardManipulation() {
        manipulationPane = new ManipulationPanel(this.delegate);
        return manipulationPane.getManipulationPanel();
    }

    // EFFECTS: initialize and return service card
    private JPanel cardService() {
        servicePanel = new ServicePanel(delegate);
        return servicePanel.getServicePanel();
    }

    /**logistic functions*/
    private void setFrameLocation(){
        frame.setSize(background.getIconWidth(),background.getIconHeight());
        frame.setLocation(dim.width/2,dim.height/2);
        frame.setLocationRelativeTo(null);
    }
    private JLabel setBackgroundImage(){
        JLabel imgLabel = new JLabel(background);
        imgLabel.setBounds(0,0,background.getIconWidth(), background.getIconHeight());
        imgLabel.setOpaque(true);
        return imgLabel;
    }
    private void setButtonAction() {
        JButton mBackBtn = manipulationPane.getBackBtn();
        JButton sBackBtn = servicePanel.getBackBtn();

        mBackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards,"cardHome");
            }
        });

        sBackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardHome");
            }
        });

        servicebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards,"cardService");
            }
        });

        manipulationbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards,"cardManipulation");
            }
        });

        quitbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        try {
            music = new FileInputStream(new File("src\\bkgMusic.wav"));
            audios = new AudioStream(music);
        }catch(IOException e1){
            JOptionPane.showMessageDialog(null,"error");
        }

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!isPlay) {
                        isPlay = true;
                        play.setText("sounds on");
                        System.out.println("isPlay state = " + isPlay);
                        AudioPlayer.player.start(audios);
                    }else if (isPlay) {
                        isPlay = false;
                        play.setText("sounds off");
                        System.out.println("isPlay state = " + isPlay);
                        AudioPlayer.player.stop(audios);
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "error");
                }
            }
        });

    }

}
