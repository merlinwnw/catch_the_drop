package geekbrains.java_quick_start.catch_the_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame{

    private static GameWindow game_window;
    private static Image background;
    private static Image drop;
    private static Image game_over;
    private static float drop_left = 200;
    private static float drop_top = -160;
    private static long last_frame_time;
    private static float drop_v = 150;
    private static int score = 0;

    public static void main(String[] args) throws IOException{
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background1.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(240, 212);
        game_window.setSize(800, 600);
        game_window.setResizable(false);

        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if(is_drop){
                    drop_top = -200;
                    drop_left = (float)Math.random() * (game_window.getWidth() - drop.getWidth(null));
                    drop_v += 30;
                    score += 1;
                    game_window.setTitle("score: " + score);
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g){
        long curr_time = System.nanoTime();
        float delta_time = (curr_time - last_frame_time) * 0.000000001f;
        last_frame_time = curr_time;

        drop_top = drop_top + drop_v * delta_time;
        boolean b = g.drawImage(background, 0, 0, null);
        g.drawImage(drop, (int)drop_left, (int)drop_top, null);
        if (drop_top > game_window.getHeight()){
            g.drawImage(game_over, (game_window.getWidth() - game_over.getWidth(null)) / 2, (game_window.getHeight() - game_over.getHeight(null)) / 2, null);
        }

//        g.drawImage(game_over, 300, 300, null);
    }

    private static class GameField extends JPanel{
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
