package com.example.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Bot extends Tank {
    private Tank playerTank; // Xe tăng của người chơi
    private Bitmap botImage;
    private Handler handler = new Handler();
    private Random random = new Random();
    private int moveSpeed = 1; // Tốc độ di chuyển của bot
    private boolean isMoving = false;

    public Bot(Context context, int tankX, int tankY, Bitmap tankUp, Bitmap tankDown, Bitmap tankLeft, Bitmap tankRight, Tank playerTank) {
        super(context, tankX, tankY, tankUp, tankDown, tankLeft, tankRight);
        this.playerTank = playerTank;
    }

    public void moveRight(Brick brick) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTank(moveSpeed, 0, brick); // Di chuyển sang trái
            }
        }, 1000);
    }

    public void moveLeft(Brick brick) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTank(-moveSpeed, 0, brick); // Di chuyển sang trái
            }
        }, 1000);
    }

    public void moveUp(Brick brick) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTank(0, -moveSpeed, brick); // Di chuyển sang trái
            }
        }, 1000);
    }

    public void moveDown(Brick brick) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTank(0, moveSpeed, brick); // Di chuyển sang trái
            }
        }, 1000);
    }

}

class Node implements Comparable<Node> {
    int x, y;   // Tọa độ ô
    int g, h, f; // g: cost từ start đến node này, h: heuristic, f = g + h
    Node parent; // Để truy vết đường đi

    public Node(int x, int y, int g, int h, Node parent) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = parent;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.f, other.f);
    }
}

class AStar {
    private static final int[] DX = {0, 0, -1, 1}; // Trái, Phải
    private static final int[] DY = {-1, 1, 0, 0}; // Lên, Xuống

    public static List<Node> findPath(int startX, int startY, int endX, int endY, int[][] grid) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        boolean[][] closedList = new boolean[grid.length][grid[0].length];

        openList.add(new Node(startX, startY, 0, heuristic(startX, startY, endX, endY), null));

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList[current.x][current.y] = true;

            if (current.x == endX && current.y == endY) {
                return constructPath(current);
            }

            for (int i = 0; i < 4; i++) {
                int nx = current.x + DX[i];
                int ny = current.y + DY[i];

                if (isValid(nx, ny, grid, closedList)) {
                    int g = current.g + 1;
                    int h = heuristic(nx, ny, endX, endY);
                    openList.add(new Node(nx, ny, g, h, current));
                }
            }
        }
        return null;
    }

    private static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan Distance
    }

    private static boolean isValid(int x, int y, int[][] grid, boolean[][] closedList) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length &&
                grid[x][y] == 0 && !closedList[x][y]; // Chỉ đi vào ô trống
    }

    private static List<Node> constructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }
}

