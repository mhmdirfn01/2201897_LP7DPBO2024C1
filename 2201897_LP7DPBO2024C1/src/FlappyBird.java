import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    //constructor

    int frameWidth = 360;
    int frameHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;
    int skor = 0;
    private App app;
    //constructor
    public FlappyBird(App app)
    {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        // setBackground(Color.blue);
        this.app = app;

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage );
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        gameLoop = new Timer(  1000/60,  this);
        gameLoop.start();

        pipesCooldown.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }

    }
    public void move() {
        int berakhir = 0;
        int ngurangiHitbox = 5; // untuk mengurangi hitbox pipa pipanya
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for(int i = 0; i< pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            // jika player menabrak pipa dan keluar dari frame
            if ((player.getPosX() < pipe.getPosX() + pipe.getWidth() - ngurangiHitbox && player.getPosX() + player.getWidth() > pipe.getPosX() + ngurangiHitbox &&
                    player.getPosY() < pipe.getPosY() + pipe.getHeight() - ngurangiHitbox && player.getPosY() + player.getHeight() > pipe.getPosY() + ngurangiHitbox) ||
                    (player.getPosY() < 0 || player.getPosY() + player.getHeight() > frameHeight + 50)) {
                berakhir = 1; // menjadi 1 karena terdapat tabrakan
                gameLoop.stop(); // game berhenti
                pipesCooldown.stop(); //  pipa berhenti
            }

            // melewati pipa
            if(player.getPosX() > pipe.getPosX() && player.getPosX() < pipe.getPosX() + pipe.getWidth())
            {
                if(!pipe.isPassed()) // jika pipa tersebut belum dilewati
                {
                    skor++; // skor bertambah
                    app.setSkor(skor); // memasukkan skor ke JLabel
                    pipe.setPassed(true);
                }
            }
        }
        if(berakhir == 1) // jika player menabrak
        {
            // maka akan memunculkan pesan game over
            JOptionPane.showMessageDialog(null, "dih jatoh!");

        }
    }
    public void RestartGame() // mengulang game
    {
        player.setPosX(playerStartPosX); // set pos x ke awal
        player.setPosY(playerStartPosY); // set pos y ke awal
        player.setVelocityY(0); // set velocity ke awal
        pipes.clear(); // menghapus pipa pipa
        skor = 0;
        app.setSkor(skor);

        gameLoop.start(); // memulai game lagi
        pipesCooldown.start(); // pipa akan dimunculkan kembali
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        }
        else if(e.getKeyCode() == KeyEvent.VK_R) // untuk restart
        {
            RestartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void placePipes() {
        int randomPipePosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPipePosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPipePosY + pipeHeight + openingSpace, pipeWidth, pipeHeight, lowerPipeImage);
        lowerPipe.setPassed(true); // membuat salah satu pipa menjadi true agar saat melewati pipa hanya terhitung satu skor
        pipes.add(lowerPipe);
    }
}


