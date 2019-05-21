import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game1_rozw implements Runnable {

    private boolean running = true;

    private final int WIDTH = 1000;
    private final int HEIGHT = 700;

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;

    //rectangle coordinates
    private double x;       //add
    private double y;       //add

    private int rectW;
    private int rectH;

    private int speedX = 2;
    private int speedY = 2;

    public Game1_rozw(){
        initFrame();

        rectW = 200;
        rectH = 100;

//        this.x = frame.getWidth()/2.0 - rectW/2.0;
//        this.y = frame.getHeight()/2.0 - rectH/2.0;
    }

    private void initFrame() {
        frame = new JFrame("Game Loop - zadanie 1");

        JPanel panel = (JPanel) frame.getContentPane();
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
    }

    @Override
    public void run() {
        while (running){
            update();
            render();
        }
    }
    /* !TODO
     * Twoim zadaniem będzie napisanie prostej animacji prostokąta, w tym celu:
     *      - w metodzie render() używając metody g.fillRect(int x, int y, int rectWidth, int rectHeight)
     *       wyrenderuj prostokąt w dowolnych miejscu, pamiętaj o tym żeby nie deklarować x i y jako zmienne lokalne,
     *       ponieważ potrzebujemy do nich dostęp w metodzie update()
     *      - zmodyfikuj metodę update() tak aby przy każdym jej wywołaniu pozycja prostokąta zmieniała się, ponadto
     *      jeżeli prostokąt osiągnie koniec panelu powinien się pojawić z przeciwnej strony (ruch "od lewej do prawej", "góra/dół")
     *      (opcjonalnie) napisz metodę update tak aby nasz prostokąt "odbijał się" od krawędzi panelu
     */
    private void update() {
//        if(x > frame.getWidth()){
//            x = -rectW;
//        }
//        x += speedX;

        // rozwiązanie opcjonalne
        if(x > frame.getWidth()-rectW || x < 0){
            speedX = -speedX;
        }
        if(y > frame.getHeight()-rectH || y < 0){
            speedY = -speedY;
        }
        x += speedX;
        y += speedY;


    }



    private void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();
        frame.paint(g);
        //************************************
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect((int)x, (int)y, rectW, rectH);

        //************************************
        g.dispose();
        bufferStrategy.show();
    }

    public static void main(String [] args){
        Game1_rozw game1 = new Game1_rozw();
        new Thread(game1).start();
    }
}
