package hex_pc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static hex_pc.Search.*;

public class MyActivity {

    private static Point rectSize = new Point(140, 100);
    private static double diagonal = Math.sqrt(rectSize.x * rectSize.x + rectSize.y * rectSize.y);
    static Point mapSize = new Point(rectSize.x + 2, rectSize.y + 2);
    static Square[][] map = new Square[mapSize.x][mapSize.y];
    //    private static int hexDiagonal = Square.hexDistance(new Point(1, 1), rectSize);
    private static int dt = 1;
    private static Graph graph = new Graph();

    public static void main(String[] args) {
        initMap();
        setCommunication();
        createMap();
    }

    private static void createMap() {
        int lowLayer = 5;

        Square centralSquare1 = new Square(lowLayer, lowLayer);
        Square centralSquare2 = new Square(rectSize.x - lowLayer, rectSize.y - lowLayer);

        addFirstChain(centralSquare1, centralSquare2);
        addOtherChains(centralSquare1, centralSquare2);

        addWater(centralSquare1, centralSquare2);

        addForest();

        addDeserts(centralSquare1, centralSquare2);

        addGrass();
    }

    private static void addGrass() {
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                if (map[i][j].terrain == Terrain.VOID)
                    map[i][j].terrain = Terrain.GRASS;
        updateGraph(1);
    }

    private static void addDeserts(Point centralPoint1, Point centralPoint2) {
        int desertsAmount = rectSize.x * rectSize.y / 600 + 1;
        ArrayList<Square> deserts = new ArrayList<>();

        for (int i = centralPoint1.x; i <= centralPoint2.x; i++)
            for (int j = centralPoint1.y; j <= centralPoint2.y; j++)
                if (map[i][j].terrain == Terrain.VOID)
                    deserts.add(map[i][j]);

        for (int i = 0; i < desertsAmount; i++) {
            Square desert = findSquareInArray(deserts);
            deserts.remove(desert);
            desert.terrain = Terrain.DESERT;
        }

        ArrayList<ArrayList<Square>> candidates = new ArrayList<>();
        for (int i = 0; i <= 6; i++)
            candidates.add(new ArrayList<>());

        desertsAmount = 0;
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                if (map[i][j].terrain == Terrain.VOID) {
                    desertsAmount++;
                    candidates.get(map[i][j].getUnluckyEnvironment(new ArrayList<>(Collections.singletonList(Terrain.DESERT))).size()).add(map[i][j]);
                }
        desertsAmount /= 20;
        Point[] groups;
        for (int i = 0; i < desertsAmount; i++) {
            if (i < desertsAmount * 4 / 5)
                groups = new Point[]{new Point(0, 3), new Point(1, 5), new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0)};
            else
                groups = new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 1), new Point(3, 1), new Point(4, 1), new Point(5, 3), new Point(6, 7)};
            chooseCandidate(Terrain.DESERT, groups, candidates);
            updateGraph(dt);
        }
    }

    private static void addWater(Point centralPoint1, Point centralPoint2) {
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                if (map[i][j].terrain == Terrain.VOID && map[i][j].getUnluckyEnvironment(new ArrayList<>(Collections.singletonList(Terrain.MOUNTAIN))).size() > 4)
                    map[i][j].terrain = Terrain.SEA;

        int springsAmount = rectSize.x * rectSize.y / 315 + 1;

        ArrayList<Square> springs = new ArrayList<>();

        for (int i = centralPoint1.x; i <= centralPoint2.x; i++)
            for (int j = centralPoint1.y; j <= centralPoint2.y; j++)
                if (map[i][j].terrain == Terrain.VOID && map[i][j].getUnluckyEnvironment(new ArrayList<>(Collections.singletonList(Terrain.MOUNTAIN))).size() == 0)
                    springs.add(map[i][j]);

        for (int i = 0; i < springsAmount; i++) {
            Square spring = findSquareInArray(springs);
            springs.remove(spring);
            spring.terrain = Terrain.SEA;
        }

        ArrayList<ArrayList<Square>> candidates = new ArrayList<>();
        for (int i = 0; i <= 6; i++)
            candidates.add(new ArrayList<>());

        int seasAmount = 0;
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                if (map[i][j].terrain == Terrain.VOID) {
                    seasAmount++;
                    ArrayList<Square> envi = map[i][j].getUnluckyEnvironment(new ArrayList<>(Collections.singletonList(Terrain.SEA)));
                    if (map[i][j].getUnluckyEnvironment(new ArrayList<>(Collections.singletonList(Terrain.MOUNTAIN))).size() == 0)
                        candidates.get(envi.size()).add(map[i][j]);
                }
        seasAmount /= 10;
        Point[] groups;
        for (int i = 0; i < seasAmount; i++) {
            if (i < seasAmount * 3 / 4)
                groups = new Point[]{new Point(0, 1), new Point(1, 3), new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0)};
            else
                groups = new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 1), new Point(3, 1), new Point(4, 1), new Point(5, 3), new Point(6, 7)};
            chooseCandidate(Terrain.SEA, groups, candidates);
            updateGraph(dt);
        }
    }

    private static void addForest() {
        ArrayList<ArrayList<Square>> candidates = new ArrayList<>();
        for (int i = 0; i <= 6; i++)
            candidates.add(new ArrayList<>());

        int treesAmount = 0;
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++) {
                int enviSize = map[i][j].getUnluckyEnvironment(new ArrayList<>(Collections.singletonList(Terrain.MOUNTAIN))).size();
                if (map[i][j].terrain == Terrain.VOID && enviSize != 0) {
                    candidates.get(enviSize).add(map[i][j]);
                    treesAmount++;
                }
            }

        treesAmount = treesAmount / 3;
        Point[] groups = new Point[]{new Point(0, 2), new Point(1, 3), new Point(2, 5), new Point(3, 6), new Point(4, 7), new Point(5, 7), new Point(6, 7)};
        for (int i = 0; i < treesAmount; i++) {
            chooseCandidate(Terrain.FOREST, groups, candidates);
            updateGraph(dt);
        }

        for (int i = 0; i <= 6; i++)
            candidates.add(new ArrayList<>());
        treesAmount = 0;

        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                if (map[i][j].terrain == Terrain.VOID) {
                    treesAmount++;
                    ArrayList<Square> envi = map[i][j].getUnluckyEnvironment(new ArrayList<>(Arrays.asList(Terrain.MOUNTAIN, Terrain.FOREST)));
                    candidates.get(envi.size()).add(map[i][j]);
                }
        treesAmount = Math.min(treesAmount * 5 / 9, rectSize.x * rectSize.y / 5);           //analyze????

        for (int i = 0; i < treesAmount; i++) {
            if (i < treesAmount / 2 && candidates.get(0).size() != 0)
                groups = new Point[]{new Point(0, 3), new Point(1, 1), new Point(2, 1), new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0)};
            else if (i < treesAmount * 4 / 5)
                groups = new Point[]{new Point(0, 0), new Point(1, 1), new Point(2, 1), new Point(3, 1), new Point(4, 0), new Point(5, 0), new Point(6, 0)};
            else
                groups = new Point[]{new Point(0, 0), new Point(1, 1), new Point(2, 5), new Point(3, 2), new Point(4, 2), new Point(5, 10), new Point(6, 10)};
            chooseCandidate(Terrain.FOREST, groups, candidates);
            updateGraph(dt);
        }
    }

    private static void addOtherChains(Point centralPoint1, Point centralPoint2) {
        while (true) {
            ArrayList<Square> mountains = getTerrain(Terrain.MOUNTAIN);

            ArrayList<Square> starts = new ArrayList<>();
            for (int i = centralPoint1.x; i <= centralPoint2.x; i++)
                for (int j = centralPoint1.y; j <= centralPoint2.y; j++) {
                    boolean far = true;
                    for (Square mountain : mountains)
                        if (Square.distance(map[i][j], mountain) < 7) {             //please no more
                            far = false;
                            break;
                        }
                    if (far)
                        starts.add(map[i][j]);
                }
            if (starts.size() == 0)
                break;
            Square start = findSquareInArray(starts);

            Square finish = mountains.get(0);
            for (Square mountain : mountains)
                if (Square.distance(start, mountain) < Square.distance(start, finish))
                    finish = mountain;

            map[start.x][start.y].terrain = Terrain.CANDIDATE;
            map[finish.x][finish.y].terrain = Terrain.CANDIDATE;

            addChainCandidates(start, finish);
            deleteExtraChainCandidates(start, finish, getLength(start, finish, Terrain.MOUNTAIN));
        }
    }

    private static void addFirstChain(Square centralSquare1, Square centralSquare2) {
        Square s1 = findInArea(centralSquare1, centralSquare2);
        Square s2;
        do
            s2 = findInArea(centralSquare1, centralSquare2);
        while (s1.equals(s2));
        map[s1.x][s1.y].terrain = Terrain.CANDIDATE;
        map[s2.x][s2.y].terrain = Terrain.CANDIDATE;
        addChainCandidates(s1, s2);
        deleteExtraChainCandidates(s1, s2, getLength(s1, s2, Terrain.CANDIDATE));
    }         //////////

    private static void deleteExtraChainCandidates(Point p1, Point p2, int length) {
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                if (map[i][j].terrain == Terrain.CANDIDATE) {
                    map[i][j].terrain = Terrain.VOID;
                    if (getLength(p1, p2, Terrain.MOUNTAIN) != length)
                        map[i][j].terrain = Terrain.MOUNTAIN;
                    updateGraph(dt);
                }
    }

    private static void addChainCandidates(Point p1, Point p2) {
        double d = Square.distance(p1, p2) * 0.57;
        if (Square.hexDistance(p1, p2) >= 12)
            d = Square.distance(p1, p2) * 0.57;
        double e = diagonal;
        ArrayList<Point> pts = new ArrayList<>();
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++) {
                if (Math.max(Math.abs(Square.distance(map[i][j], map[p1.x][p1.y]) - d), Math.abs(Square.distance(map[i][j], map[p2.x][p2.y]) - d)) == e)
                    pts.add(new Point(i, j));
                if (Math.max(Math.abs(Square.distance(map[i][j], map[p1.x][p1.y]) - d), Math.abs(Square.distance(map[i][j], map[p2.x][p2.y]) - d)) < e) {
                    e = Math.max(Math.abs(Square.distance(map[i][j], map[p1.x][p1.y]) - d), Math.abs(Square.distance(map[i][j], map[p2.x][p2.y]) - d));
                    pts.clear();
                    pts.add(new Point(i, j));
                }
            }
        Point p = Search.findPointInArray(pts);
        map[p.x][p.y].terrain = Terrain.CANDIDATE;

        updateGraph(dt);

        if (Square.hexDistance(p, p1) > 1)
            addChainCandidates(p, p1);
        if (Square.hexDistance(p, p2) > 1)
            addChainCandidates(p, p2);
    }

    private static void unluck() {
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                map[i][j].lucky = false;
    }

    private static void updateGraph(int t) {
        graph.repaint();
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void setCommunication() {
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++) {
                if (j % 2 == 1) {
                    map[i][j].envi.topRight = map[i + 1][j + 1];
                    map[i][j].envi.topLeft = map[i][j + 1];
                    map[i][j].envi.bottomRight = map[i + 1][j - 1];
                    map[i][j].envi.bottomLeft = map[i][j - 1];
                } else {
                    map[i][j].envi.topRight = map[i][j + 1];
                    map[i][j].envi.topLeft = map[i - 1][j + 1];
                    map[i][j].envi.bottomRight = map[i][j - 1];
                    map[i][j].envi.bottomLeft = map[i - 1][j - 1];
                }
                map[i][j].envi.right = map[i + 1][j];
                map[i][j].envi.left = map[i - 1][j];
            }
    }

    private static void initMap() {
        for (int i = 0; i < mapSize.x; i++)
            for (int j = 0; j < mapSize.y; j++)
                if (i * j * (i - mapSize.x + 1) * (j - mapSize.y + 1) == 0)
                    map[i][j] = new Square();
                else
                    map[i][j] = new Square(i, j);
    }

    private static int getLength(Point p1, Point p2, Terrain terrain) {         //lucky = in path
        ArrayList<Point> tails = new ArrayList<>();
        ArrayList<Square> newTails;
        if (map[p1.x][p1.y].terrain == Terrain.CANDIDATE || map[p1.x][p1.y].terrain == Terrain.MOUNTAIN)
            tails.add(p1);
        map[p1.x][p1.y].lucky = true;
        boolean end = false;
        int length = 0;
        while (tails.size() > 0) {
            length++;
            newTails = new ArrayList<>();
            for (Point tail : tails)
                newTails.addAll(map[tail.x][tail.y].getUnluckyEnvironment(new ArrayList<>(Arrays.asList(terrain, Terrain.CANDIDATE))));
            tails = new ArrayList<>();
            for (Square newTail : newTails) {
                newTail.lucky = true;
                if (!tails.contains(newTail.getLocation()))
                    tails.add(newTail.getLocation());
                if (newTail.getLocation().equals(p2))
                    end = true;
            }
        }
        unluck();
        if (end)
            return length - 1;
        else
            return -1;
    }

    private static ArrayList<Square> getTerrain(Terrain terrain) {
        ArrayList<Square> terrains = new ArrayList<>();
        for (int i = 1; i <= rectSize.x; i++)
            for (int j = 1; j <= rectSize.y; j++)
                if (map[i][j].terrain == terrain)
                    terrains.add(map[i][j]);
        return terrains;
    }
}