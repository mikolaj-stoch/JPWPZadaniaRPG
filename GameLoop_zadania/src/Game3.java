import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

/***
 * Zadanie 1:
 * - w konstruktorze zainicjalizuj zmienne centerX, centerY tak aby ich wartość pozwalała na narysowanie prostokąta na środku ekranu
 * - aby otrzymać wysokość(szerokość) panelu użyj metody panel.getHeight()
 * - następnie w metodzie render() zmodyfikuj g.fillRect(), przykładowo odczyt szerokości naszego prostokąta: myRect.width
 *
 * Zadanie 2, 3:
 *  - dokończ metodę update() aby można było poruszać prostokątem za pomocą wciskania klawiszy (W, S, A, D) metody up(), down(),
 *  right(), left() zwracają true jeżeli odpowiedni klawisz został wciśnięty
 *  - upewnij się, że nie jest możliwe opuszczenie obszaru okna
 *  - kod dla współrzędnej 'y' został już napisany
 *
 *  (Opcjonalnie)
 *  -dodatkowo wyrenderuj kilka prostokątów i za pomocą myRect.intersects(jakis_inny_prostokąt) sprawdź
 *  czy czasem prostokąty się nie nakładają (detekcja kolizji), jeśli tak jest to ustaw współrzędne myRect tak
 *  aby został wyrenderowany na środku panelu
 *
 *  (Opcjonalne 2)
 *  -spraw by wygenerowane prostokąty w poprzednim zadaniu poruszały się swobodnie, to znaczy dochodząc do końca ekranu
 *  zmieniały kierunek(odbijały się od krawędzi) poruszania się na przeciwny (speedX = -speedX)
 *
 *  w taki sposób uzyskaliśmy szkielet prostej gry w której zadaniem gracza jest unikanie poruszających się elementów
 */

public class Game3 implements Runnable, KeyListener {

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

    Rectangle myRect;


    private int centerX;
    private int centerY;

    public Game3(){

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

        //!TODO <--------- #1
        centerX = 0; //??
        centerY = 0; //??
        myRect = new Rectangle(centerX, centerY, rectW, rectH);     //new Rect(x, y, width, height)

        //initObstacles() //zadanie opcjonalne
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
        g.fillRect(0,0, myRect.width,0);  //!TODO <------------- #1

        g.dispose();
        bufferStrategy.show();
    }

    private void update() {

        if (myRect.y > 0) {
            if (up()) {
                myRect.y -= SPEED;
            }
        }
        if (myRect.y < panel.getHeight() - myRect.height) {
            if (down()) {
                myRect.y += SPEED;
            }
        }

        //!TODO <------------ #2, #3
        if (true) {     //analogicznie do przykładu powyżej

        }

        if (true) {

        }


        //!TODO <------------ #Opcjonalne
        //updateObstacles();
        //checkCollision();


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
        Game3 ex = new Game3();
        new Thread(ex).start();
    }
}
