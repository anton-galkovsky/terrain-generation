package hex_pc;

import java.awt.*;
import java.util.ArrayList;

class Square extends Point {

    Terrain terrain = Terrain.VOID;
    Environment envi = new Environment();
    boolean lucky = false;

    Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Square() {
        terrain = Terrain.NOTEXIST;
        x = y = -1;
    }

    Color getColor() {
        switch (terrain) {
            case VOID:
                return Color.WHITE;
            case NOTEXIST:
                return Color.LIGHT_GRAY;
            case MOUNTAIN:
                return new Color(123, 120, 94);
            case FOREST:
                return new Color(0, 133, 0);
            case SEA:
                return new Color(0, 187, 255);
            case DESERT:
                return new Color(202, 202, 104);
            case GRASS:
                return new Color(147, 255, 76);
            case CANDIDATE:
                return new Color(255, 127, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    static double distance(Point p1, Point p2) {
        double x1 = p1.x + (p1.y % 2) / 2.0;
        double y1 = p1.y * Math.sqrt(3) / 2;
        double x2 = p2.x + (p2.y % 2) / 2.0;
        double y2 = p2.y * Math.sqrt(3) / 2;
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    static int hexDistance(Point p1, Point p2) {
        int dy = p2.y - p1.y;
        int dx = p2.x - p1.x + (p2.y + 1) / 2 - (p1.y + 1) / 2;
        if (dx * dy <= 0)
            return Math.abs(dx) + Math.abs(dy);
        else
            return Math.max(Math.abs(dx), Math.abs(dy));
    }

    ArrayList<Square> getUnluckyEnvironment(ArrayList<Terrain> terrains) {
        ArrayList<Square> environment = new ArrayList<>();
        if (terrains.contains(envi.right.terrain) && !envi.right.lucky)
            environment.add(envi.right);
        if (terrains.contains(envi.left.terrain) && !envi.left.lucky)
            environment.add(envi.left);
        if (terrains.contains(envi.topRight.terrain) && !envi.topRight.lucky)
            environment.add(envi.topRight);
        if (terrains.contains(envi.topLeft.terrain) && !envi.topLeft.lucky)
            environment.add(envi.topLeft);
        if (terrains.contains(envi.bottomRight.terrain) && !envi.bottomRight.lucky)
            environment.add(envi.bottomRight);
        if (terrains.contains(envi.bottomLeft.terrain) && !envi.bottomLeft.lucky)
            environment.add(envi.bottomLeft);
        return environment;
    }

//    static double distance(Square s1, Square s2) {
//        double x1 = s1.x + (s1.y % 2) / 2.0;
//        double y1 = s1.y * Math.sqrt(3) / 2;
//        double x2 = s2.x + (s2.y % 2) / 2.0;
//        double y2 = s2.y * Math.sqrt(3) / 2;
//        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
//    }
//    static int hexDistance(Square s1, Square s2) {
//        int dy = s2.y - s1.y;
//        int dx = s2.x - s1.x + (s2.y + 1) / 2 - (s1.y + 1) / 2;
//        if (dx * dy <= 0)
//            return Math.abs(dx) + Math.abs(dy);
//        else
//            return Math.max(Math.abs(dx), Math.abs(dy));
//    }

//    ArrayList<Square> getEnvironment(Terrain terrain) {
//        ArrayList<Square> environment = new ArrayList<>();
//        if (envi.right.terrain == terrain)
//            environment.add(envi.right);
//        if (envi.left.terrain == terrain)
//            environment.add(envi.left);
//        if (envi.topRight.terrain == terrain)
//            environment.add(envi.topRight);
//        if (envi.topLeft.terrain == terrain)
//            environment.add(envi.topLeft);
//        if (envi.bottomRight.terrain == terrain)
//            environment.add(envi.bottomRight);
//        if (envi.bottomLeft.terrain == terrain)
//            environment.add(envi.bottomLeft);
//        return environment;
//    }
//    ArrayList<Square> getLuckyEnvironment(Terrain terrain, boolean luck) {
//        ArrayList<Square> environment = new ArrayList<>();
//        if (envi.right.terrain == terrain && luck == envi.right.lucky)
//            environment.add(envi.right);
//        if (envi.left.terrain == terrain && luck == envi.left.lucky)
//            environment.add(envi.left);
//        if (envi.topRight.terrain == terrain && luck == envi.topRight.lucky)
//            environment.add(envi.topRight);
//        if (envi.topLeft.terrain == terrain && luck == envi.topLeft.lucky)
//            environment.add(envi.topLeft);
//        if (envi.bottomRight.terrain == terrain && luck == envi.bottomRight.lucky)
//            environment.add(envi.bottomRight);
//        if (envi.bottomLeft.terrain == terrain && luck == envi.bottomLeft.lucky)
//            environment.add(envi.bottomLeft);
//        return environment;
//    }
//
//    ArrayList<Point> getLuckyEnvironment(Terrain terrain, boolean luck) {
//        ArrayList<Point> environment = new ArrayList<>();
//        if (envi.right.terrain == terrain && luck == envi.right.lucky)
//            environment.add(envi.right.coor);
//        if (envi.left.terrain == terrain && luck == envi.left.lucky)
//            environment.add(envi.left.coor);
//        if (envi.topRight.terrain == terrain && luck == envi.topRight.lucky)
//            environment.add(envi.topRight.coor);
//        if (envi.topLeft.terrain == terrain && luck == envi.topLeft.lucky)
//            environment.add(envi.topLeft.coor);
//        if (envi.bottomRight.terrain == terrain && luck == envi.bottomRight.lucky)
//            environment.add(envi.bottomRight.coor);
//        if (envi.bottomLeft.terrain == terrain && luck == envi.bottomLeft.lucky)
//            environment.add(envi.bottomLeft.coor);
//        return environment;
//    }
}
