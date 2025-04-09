package com.example.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Bot extends Tank {
    private Tank playerTank;
    private Handler movementHandler = new Handler();
    private Handler pathFindingHandler = new Handler();
    private int moveSpeed = 15;
    private List<Node> currentPath;
    private int pathIndex;
    private Brick brick;
    private View2Player view2Player;
    private int lastTargetX; // Lưu vị trí đích cuối cùng
    private int lastTargetY;

    public Bot(Context context, int tankX, int tankY, Bitmap tankUp, Bitmap tankDown,
               Bitmap tankLeft, Bitmap tankRight, Tank playerTank, Brick brick, View2Player view2Player) {
        super(context, tankX, tankY, tankUp, tankDown, tankLeft, tankRight);
        this.playerTank = playerTank;
        this.brick = brick;
        this.view2Player = view2Player;
        startMovement();
        startFiring();
        startPathFinding();
    }

    private void startMovement() {
        movementHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAlive()) {
                    updateMovement();
                }
                movementHandler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void startFiring() {
        movementHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAlive() && canFire()) {
                    fire(view2Player);
                }
                movementHandler.postDelayed(this, 500);
            }
        }, 500);
    }

    private void startPathFinding() {
        pathFindingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAlive()) {
                    // Chỉ tìm đường mới nếu đường cũ không hợp lệ hoặc tank1 di chuyển xa
                    if (shouldFindNewPath()) {
                        currentPath = findPathToPlayer();
                        if (currentPath != null && !currentPath.isEmpty()) {
                            pathIndex = 0;
                            lastTargetX = currentPath.get(currentPath.size() - 1).x * 150;
                            lastTargetY = currentPath.get(currentPath.size() - 1).y * 150;
                        }
                    }
                }
                pathFindingHandler.postDelayed(this, 500); // Kiểm tra mỗi 500ms
            }
        }, 500);
    }

    private boolean shouldFindNewPath() {
        // Tìm đường mới nếu:
        // 1. Chưa có đường
        // 2. Đã đi hết đường
        // 3. Tank1 di chuyển xa khỏi đích cuối cùng của đường cũ (khoảng cách > 300)
        if (currentPath == null || pathIndex >= currentPath.size()) {
            return true;
        }

        int currentTargetX = playerTank.getTankX();
        int currentTargetY = playerTank.getTankY();
        int distance = (int) Math.sqrt(Math.pow(currentTargetX - lastTargetX, 2) +
                Math.pow(currentTargetY - lastTargetY, 2));
        return distance > 300; // Ngưỡng 300 pixel
    }

    private void updateMovement() {
        if (currentPath != null && pathIndex < currentPath.size()) {
            Node nextNode = currentPath.get(pathIndex);
            int targetX = nextNode.x * 150;
            int targetY = nextNode.y * 150;

            int deltaX = Integer.compare(targetX, getTankX()) * moveSpeed;
            int deltaY = Integer.compare(targetY, getTankY()) * moveSpeed;

            if (deltaX != 0 || deltaY != 0) {
                moveTank(deltaX, deltaY, brick);
            }

            if (Math.abs(getTankX() - targetX) < moveSpeed &&
                    Math.abs(getTankY() - targetY) < moveSpeed) {
                pathIndex++;
            }
        }
    }

    @Override
    public void fire(final View2Player view2Player) {
        if (canFire()) {
            Bullet newBullet = new Bullet();

            if (getTankImage() == getTankUp()) {
                newBullet.setX(getTankX() + 50);
                newBullet.setY(getTankY());
                newBullet.setDirection("up");
            } else if (getTankImage() == getTankDown()) {
                newBullet.setX(getTankX() + 50);
                newBullet.setY(getTankY() + 126); // 150 - 24
                newBullet.setDirection("down");
            } else if (getTankImage() == getTankLeft()) {
                newBullet.setX(getTankX());
                newBullet.setY(getTankY() + 50);
                newBullet.setDirection("left");
            } else if (getTankImage() == getTankRight()) {
                newBullet.setX(getTankX() + 126); // 150 - 24
                newBullet.setY(getTankY() + 50);
                newBullet.setDirection("right");
            }
            getBullets().add(newBullet);
            moveBullets(view2Player);
        }
    }

    private List<Node> findPathToPlayer() {
        int startX = getTankX() / 150;
        int startY = getTankY() / 150;
        int endX = playerTank.getTankX() / 150;
        int endY = playerTank.getTankY() / 150;

        int[][] grid = new int[25][9];

        for (int i = 0; i < brick.solidBricksXPos.length; i++) {
            int bx = brick.solidBricksXPos[i] / 150;
            int by = brick.solidBricksYPos[i] / 150;
            if (bx < 25 && by < 9) grid[bx][by] = 1;
        }
        for (int i = 0; i < brick.bricksXPos.length; i++) {
            if (brick.brickON[i] == 1) {
                int bx = brick.bricksXPos[i] / 150;
                int by = brick.bricksYPos[i] / 150;
                if (bx < 25 && by < 9) grid[bx][by] = 1;
            }
        }

        return BFS.findPath(startX, startY, endX, endY, grid);
    }
}

// Node và BFS giữ nguyên
class Node {
    int x, y;
    Node parent;

    public Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }
}

class BFS {
    private static final int[] DX = {0, 0, -1, 1};
    private static final int[] DY = {-1, 1, 0, 0};

    public static List<Node> findPath(int startX, int startY, int endX, int endY, int[][] grid) {
        Queue<Node> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        queue.add(new Node(startX, startY, null));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.x == endX && current.y == endY) {
                return constructPath(current);
            }

            for (int i = 0; i < 4; i++) {
                int nx = current.x + DX[i];
                int ny = current.y + DY[i];

                if (isValid(nx, ny, grid, visited)) {
                    visited[nx][ny] = true;
                    queue.add(new Node(nx, ny, current));
                }
            }
        }
        return null;
    }

    private static boolean isValid(int x, int y, int[][] grid, boolean[][] visited) {
        return x >= 0 && x < grid.length &&
                y >= 0 && y < grid[0].length &&
                grid[x][y] == 0 &&
                !visited[x][y];
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