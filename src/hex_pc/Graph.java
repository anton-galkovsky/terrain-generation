package hex_pc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static hex_pc.MyActivity.*;

class Graph extends JFrame implements MouseWheelListener, MouseMotionListener, KeyListener {

    private int width = 1600;
    private int height = 1000;
    private int x0 = 50;
    private int y0 = 50;
//    private int l = 15;                                                     //half of hex width
//    private int h = (int) Math.round(l / Math.sqrt(3));

    private int h = 3;
    private int l = (int) Math.round(h * Math.sqrt(3));
    private boolean recreate = false;

    Graph() {
        super("Hex");
        setSize(width, height);
        setVisible(true);
        addKeyListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                y0 -= 10;
                break;
            case KeyEvent.VK_DOWN:
                y0 += 10;
                break;
            case KeyEvent.VK_RIGHT:
                x0 -= 10;
                break;
            case KeyEvent.VK_LEFT:
                x0 += 10;
                break;
        }
        recreate();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        x0 += e.getX();
//        y0 += e.getY();
        e.translatePoint(10, 10);
        recreate();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        h += e.getWheelRotation();
        l = (int) Math.round(h * Math.sqrt(3));
        recreate();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graph = (Graphics2D) g;
        if (recreate) {
            recreate = false;
            graph.clearRect(0, 0, width, height);
        }
        for (int i = 0; i < mapSize.x; i++)
            for (int j = 0; j < mapSize.y; j++)
                if (map[i][j].terrain != Terrain.NOTEXIST) {
                    graph.setPaint(map[i][j].getColor());
                    drawHex(x(i, j), y(j), graph);
                }
    }

    private void recreate() {
        this.recreate = true;
        repaint();
    }

    private void drawHex(int x, int y, Graphics2D graph) {
        int[] xArr = {x - l, x - l, x, x + l, x + l, x};
        int[] yArr = {y - h, y + h, y + 2 * h, y + h, y - h, y - 2 * h};
        graph.fillPolygon(xArr, yArr, 6);
        graph.setPaint(Color.BLACK);                                            //edge
        graph.drawPolygon(xArr, yArr, 6);
    }

    private int x(int x, int y) {
        return (int) Math.round((x + (y % 2) / 2.0) * 2 * l) + x0;
    }

    private int y(int y) {
        return -y * 3 * h + height - y0;
    }

//    int width = 1200;
//    int height = 800;
//    int r = 21;
//    int k1 = (int) Math.round(r * Math.sqrt(3));
//    //    int k2 = (int) (r / Math.sqrt(3));
//    int k2 = (int) Math.round(r / Math.sqrt(3));
//
//    Graph() {
//        super("DFT");
//        setSize(width, height);
//        setVisible(true);
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        Graphics2D graph = (Graphics2D) g;
//        for (int i = 0; i < mapSize.x; i++)
//            for (int j = 0; j < mapSize.y; j++) {
//                if (!map[i][j].exist)
//                    graph.setPaint(Color.LIGHT_GRAY);
//                else
//                    graph.setPaint(Color.WHITE);
//                if (map[i][j].terrain == Terrain.MOUNTAIN)
//                    graph.setPaint(Color.BLACK);
//                //if (i == 2 & j == 2)
//                drawHex(x(i, j), y(j), graph);
////                else {
////                    graph.fillOval(x(i, j), y(j), k, k);
////                    graph.setPaint(Color.BLACK);
////                    graph.drawOval(x(i, j), y(j), k, k);
////                }
////                graph.drawOval(x(i, j), y(j), 5, 5);
//            }
//        //graph.drawOval(x(0, 1), y(1), 5, 5);
//    }
//
//    private void drawHex(int x, int y, Graphics2D graph) {
////        int q = (int) Math.round(k /2.0);
////        int p = (int) Math.round(k / (2.0 *Math.sqrt(3)));
////        int[] xArr = {x + q, x + 2*q, x+2*q, x + q, x , x};
////        int[] yArr = {y , y + p, y + 3*p, y + 4*p, y +3*p, y + p};
////        graph.fillPolygon(xArr, yArr, 6);
////        graph.setPaint(Color.BLACK);
////        graph.drawPolygon(xArr, yArr, 6);
//        int q = r;
//        int p = k2;
//        int[] xArr = {x - q, x - q, x, x + q, x + q, x};
//        int[] yArr = {y - p, y + p, y + 2 * p, y + p, y - p, y - 2 * p};
//        graph.fillPolygon(xArr, yArr, 6);
//        graph.setPaint(Color.BLACK);
//        graph.drawPolygon(xArr, yArr, 6);
//    }
//
//    private int x(int x, int y) {
//        return (int) Math.round((2 * x + (y % 2)) * r) + 200;
//    }
//
//    private int y(int y) {
//        return -(int) Math.round(y * k1) + height - 200;
//    }
//
//    int width = 1500;
//    int height = 800;
//    int k = 40;
//    int l = (int) (k * Math.sqrt(3) / 2);
//
//    Graph() {
//        super("DFT");
//        setSize(width, height);
//        setVisible(true);
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        Graphics2D graph = (Graphics2D) g;
//        for (int i = 0; i < skewMapSize.x; i++)
//            for (int j = 0; j < skewMapSize.y; j++) {
////                    if (i == 7 && j == 9)
////                        graph.setPaint(Color.RED);
////                    if (Square.HexDistance(new Square(7, 9), new Square(i, j)) == 1)
////                        graph.setPaint(Color.ORANGE);
////                    if (Square.HexDistance(new Square(7, 9), new Square(i, j)) == 2)
////                        graph.setPaint(Color.YELLOW);
////                    if (Square.HexDistance(new Square(7, 9), new Square(i, j)) == 3)
////                        graph.setPaint(Color.GREEN);
////                    if (Square.HexDistance(new Square(7, 9), new Square(i, j)) == 4)
////                        graph.setPaint(Color.BLUE);
////                    if (Square.HexDistance(new Square(7, 9), new Square(i, j)) == 5)
////                        graph.setPaint(Color.MAGENTA);
//                //     graph.fillOval(x(i, j), y(j)+j, k, k);
//                if (!skewMap[i][j].exist)
//                    graph.setPaint(Color.LIGHT_GRAY);
//                else
//                    graph.setPaint(Color.WHITE);
//                if (skewMap[i][j].terrain == Terrain.MOUNTAIN)
//                    graph.setPaint(Color.BLACK);
//                if (i == 10 && j == 1)
//                    graph.setPaint(Color.RED);
//                drawHex(x(i, j), y(j) + j, graph);
//            }
//    }
//
//    private void drawHex(int x, int y, Graphics2D graph) {
//        int q = k / 2;
//        int p = (int) (k / (2 * Math.sqrt(3)));
//        int[] xArr = {x - q, x - q, x, x + q, x + q, x};
//        int[] yArr = {y - p, y + p, y + 2 * p, y + p, y - p, y - 2 * p};
//        graph.fillPolygon(xArr, yArr, 6);
//        graph.setPaint(Color.BLACK);
//        graph.drawPolygon(xArr, yArr, 6);
//    }
//
//    private int x(int x, int y) {
//        return (int) ((x - y / 2.0) * k) + 400;
//    }
//
//    private int y(int y) {
//        return -(y * l) + height - 200;
//    }
}