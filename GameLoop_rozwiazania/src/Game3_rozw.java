import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game3_rozw implements Runnable, KeyListener {

    private final int DESIRED_FPS = 60;
    private boolean running = true;
    public boolean[] keys = new boolean[120];

    private final int WIDTH = 1000;
    private final int HEIGHT = 700;

    private JFrame frame;
    private JPanel panel;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;

    private final int SPEED = 10;

    private int rectW;
    private int rectH;

    private Rectangle myRect;

    private int centerX;
    private int centerY;

    private int objXspeed = 5;
    private int objYspeed = 5;

    private int[][] speed_XY;
    private ArrayList<Rectangle> obstacles;
    private final int OBSTACLES_SPEED = 8;

    public Game3_rozw(){

        frame = new JFrame("Game Loop - zadanie 3");
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);      //center window

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();
        canvas.addKeyListener(this);

        rectW = 100;
        rectH = 100;

        centerX = panel.getWidth()/2 - rectW/2;         //zadanie opcjonalne 1
        centerY = panel.getHeight()/2 - rectH/2;
        myRect = new Rectangle(centerX, centerY, rectW, rectH);

        initObstacles();
    }

    private void initObstacles() {
        Rectangle obj = new Rectangle(100, 100, 50, 50);
        Rectangle obj2 = new Rectangle(600, 500, 50, 50);
        Rectangle obj3 = new Rectangle(800, 100, 50, 50);

        obstacles = new ArrayList<>();      //zadanie opcjonalne 2
        obstacles.add(obj);
        obstacles.add(obj2);
        obstacles.add(obj3);

        speed_XY = new int[obstacles.size()][2];
        for(int i = 0; i < speed_XY.length; i++){
            speed_XY[i][0] = OBSTACLES_SPEED;
            speed_XY[i][1] = OBSTACLES_SPEED;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode < keys.length)
            keys[keyCode] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode < keys.length)
            keys[keyCode] = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / (float)DESIRED_FPS;
        long timer = System.currentTimeMillis();
        int updateCount = 0;
        int frameCount = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update();
                updateCount++;
                delta--;
            }
            render();
            frameCount++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updateCount + " ups  |  " + frameCount + " fps");
                updateCount = 0;
                frameCount = 0;
            }
        }
    }

    private void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();
        frame.paint(g);

        //g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(myRect.x, myRect.y, myRect.width, myRect.height);
        g.setColor(Color.RED);
        for(Rectangle obj: obstacles) {
            g.fillRect(obj.x, obj.y, obj.width, obj.height);
        }

        g.dispose();
        bufferStrategy.show();
    }

    private void update() {

        if (myRect.y > 0) {
            if (up()) {
                myRect.y -= SPEED;
            }
        }
        if (myRect.y < panel.getHeight() - myRect.height) { //frame.getContentPane().getHeight() fails
            if (down()) {
                myRect.y += SPEED;
            }
        }

        if (myRect.x < panel.getWidth() - myRect.width) { //frame.getContentPane().getWidth() fails
            if (right()) {
                myRect.x += SPEED;
            }
        }
        if (myRect.x > 0) {
            if (left()) {
                myRect.x -= SPEED;
            }
        }

        updateObstacles();        //zadanie opcjonalne 2
        checkCollision();   //zadanie opcjonalne 2

    }

    private void checkCollision() {
        for(Rectangle obj: obstacles) {
            if (myRect.intersects(obj)) {
                myRect.x = centerX;
                myRect.y = centerY;
            }
        }
    }

    private void updateObstacles() {
        int i = 0;
        for(Rectangle obj: obstacles) {
            if (obj.x > frame.getWidth() - obj.width || obj.x < 0) {
                speed_XY[i][0] = -speed_XY[i][0];
            }
            if (obj.y > frame.getHeight() - rectH || obj.y < 0) {
                speed_XY[i][1] = -speed_XY[i][1];
            }
            obj.x += speed_XY[i][0];
            obj.y += speed_XY[i][1];
            i++;
        }
    }

    public boolean up()
    {
        return keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
    }

    public boolean down()
    {
        return keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
    }

    public boolean left()
    {
        return keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
    }

    public boolean right()
    {
        return keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
    }

    public static void main(String [] args){
        Game3_rozw ex = new Game3_rozw();
        new Thread(ex).start();
    }
}