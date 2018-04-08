package hex_pc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Search {

    static void chooseCandidate(Terrain terrain, Point[] groups, ArrayList<ArrayList<Square>> candidates) {
        for (int k = 0; k <= 6; k++)
            if (candidates.get(k).size() == 0)
                groups[k].y = 0;
        int groupNumber = findInPlot(groups);
        Square chosenSquare = findSquareInArray(candidates.get(groupNumber));
        chosenSquare.terrain = terrain;
        candidates.get(groupNumber).remove(chosenSquare);
        ArrayList<Square> envi = chosenSquare.getUnluckyEnvironment(new ArrayList<>(Collections.singletonList(Terrain.VOID)));
        for (Square env : envi)
            for (int k = 0; k <= 6; k++)
                if (candidates.get(k).contains(env)) {
                    candidates.get(k).remove(env);
                    if (k < 6)
                        candidates.get(k + 1).add(env);
                    break;
                }
    }

    static Point findPointInArray(ArrayList<Point> pts) {
        return pts.get(new Random().nextInt(pts.size()));
    }

    static Square findSquareInArray(ArrayList<Square> sqrs) {
        return sqrs.get(new Random().nextInt(sqrs.size()));
    }

    static Square findInArea(Square s1, Square s2) {
        Square s = new Square();
        s.y = new Random().nextInt(Math.abs(s2.y - s1.y) + 1) + Math.min(s2.y, s1.y);
        s.x = new Random().nextInt(Math.abs(s2.x - s1.x) + 1) + Math.min(s2.x, s1.x);
        return s;
    }

    private static int findInPlot(Point[] points) {
        ArrayList<Integer> candidates = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < points.length - 1; i++)
            for (int x = points[i].x; x < points[i + 1].x; x++) {
                int y = points[i].y + (x - points[i].x) * (points[i].y - points[i + 1].y) / (points[i].x - points[i + 1].x);
                candidates.add(y);
                sum += y;
            }
        int y = points[points.length - 1].y;
        candidates.add(y);
        sum += y;
        int winner = new Random().nextInt(sum) + 1;
        for (int i = 0; i < candidates.size(); i++) {
            winner -= candidates.get(i);
            if (winner <= 0)
                return points[0].x + i;
        }
        throw new ArithmeticException();
    }
}
