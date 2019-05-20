import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game2 implements Runnable{
    private boolean running = true;

    private final int WIDTH = 1000;
    private final int HEIGHT = 700;

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;

    //rectangle coordinates
    private int x;
    private int y;

    private int rectW;
    private int rectH;

    private int DESIRED_FPS = 60;

    private int speedX = 2;
    private int speedY = 2;


    public Game2(){
        initFrame();

        rectW = 200;
        rectH = 200;
    }

    @Override
    public void run(){
        boolean running = true;

        //!TODO miejsce na twój kod


        int updateCount = 0;
        int frameCount = 0;
        long timer = System.currentTimeMillis();
        while (running) {

            //!TODO miejsce na twój kod

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updateCount + " ups | " + frameCount + " fps");
                updateCount = 0;
                frameCount = 0;
            }

        }
    }

    private void initFrame() {
        frame = new JFrame("Game Loop - zadanie 2");

        JPanel panel = (JPanel) frame.getContentPane();
        //new Jpanel()
        //add(panel)

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


        canvas.createBufferStrategy(3);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();
    }

    private void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();
        frame.paint(g);

        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.GREEN);
        g.fillRect((int)x, (int)y, 200, 200);
        g.setColor(Color.BLACK);
        g.drawRect((int)x, (int)y, 200, 200);

        g.dispose();
        bufferStrategy.show();
    }

    private void update() {
        if(x > frame.getWidth()-rectW || x < 0){
            speedX = -speedX;
        }
        if(y > frame.getHeight()-rectH || y < 0){
            speedY = -speedY;
        }
        x += speedX;
        y += speedY;
    }

    public static void main(String [] args){
        Game2 game2 = new Game2();
        new Thread(game2).start();
    }
}
