package com.example.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Iterator;

public class Tank {
    private int TankX;
    private int TankY;
    private Bitmap TankImage;
    private Bitmap tankUp, tankDown, tankLeft, tankRight;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private static final int MAX_BULLETS = 3;
    private long lastFireTime;
    private static final long FIRE_DELAY = 1000;
    private boolean alive = true; // Thêm trạng thái sống

    // Getters và setters
    public Bitmap getTankImage() {
        return TankImage;
    }

    public void setTankImage(Bitmap tankImage) {
        TankImage = tankImage;
    }

    public int getTankX() {
        return TankX;
    }

    public void setTankX(int tankX) {
        TankX = tankX;
    }

    public int getTankY() {
        return TankY;
    }

    public void setTankY(int tankY) {
        TankY = tankY;
    }

    public Bitmap getTankDown() {
        return tankDown;
    }

    public void setTankDown(Bitmap tankDown) {
        this.tankDown = tankDown;
    }

    public Bitmap getTankLeft() {
        return tankLeft;
    }

    public void setTankLeft(Bitmap tankLeft) {
        this.tankLeft = tankLeft;
    }

    public Bitmap getTankRight() {
        return tankRight;
    }

    public void setTankRight(Bitmap tankRight) {
        this.tankRight = tankRight;
    }

    public Bitmap getTankUp() {
        return tankUp;
    }

    public void setTankUp(Bitmap tankUp) {
        this.tankUp = tankUp;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Tank(Context context, int tankX, int tankY, Bitmap tankUp, Bitmap tankDown,
                Bitmap tankLeft, Bitmap tankRight) {
        TankX = tankX;
        TankY = tankY;
        this.tankUp = tankUp;
        this.tankDown = tankDown;
        this.tankLeft = tankLeft;
        this.tankRight = tankRight;
        TankImage = tankUp;
        lastFireTime = 0;
    }

    public void draw(Canvas canvas) {
        if (alive) { // Chỉ vẽ nếu còn sống
            canvas.drawBitmap(TankImage, TankX, TankY, null);
        }
    }

    public void moveTank(int deltaX, int deltaY, Brick brick) {
        if (!alive) return;
        int newX = TankX + deltaX;
        int newY = TankY + deltaY;

        // Giới hạn trong 25x9
        if (brick.checkTankCollision(newX, newY) || brick.checkTankSolidCollision(newX, newY) ||
                newX < 0 || newX > 150 * 25 || newY < 0 || newY > 150 * 9) {
            if (deltaX > 0) TankImage = tankRight;
            else if (deltaX < 0) TankImage = tankLeft;
            else if (deltaY > 0) TankImage = tankDown;
            else if (deltaY < 0) TankImage = tankUp;
        } else {
            setTankX(TankX += deltaX);
            setTankY(TankY += deltaY);
            if (deltaX > 0) TankImage = tankRight;
            else if (deltaX < 0) TankImage = tankLeft;
            else if (deltaY > 0) TankImage = tankDown;
            else if (deltaY < 0) TankImage = tankUp;
        }
    }

    public boolean canFire() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastFireTime >= FIRE_DELAY && bullets.size() < MAX_BULLETS && alive);
    }

    public void fire(final View2Player view2Player) {
        if (canFire()) {
            Bullet newBullet = new Bullet();

            if (TankImage == tankUp) {
                newBullet.setX(TankX + 40);
                newBullet.setY(TankY);
                newBullet.setDirection("up");
            } else if (TankImage == tankDown) {
                newBullet.setX(TankX + 40);
                newBullet.setY(TankY + 104);
                newBullet.setDirection("down");
            } else if (TankImage == tankLeft) {
                newBullet.setX(TankX);
                newBullet.setY(TankY + 40);
                newBullet.setDirection("left");
            } else if (TankImage == tankRight) {
                newBullet.setX(TankX + 104);
                newBullet.setY(TankY + 45);
                newBullet.setDirection("right");
            }
            bullets.add(newBullet);
            moveBullets(view2Player);
            lastFireTime = System.currentTimeMillis();
        }
    }

    private Handler bulletHandler = new Handler();
    private Runnable bulletRunnable;

    public void moveBullets(final View2Player view2Player) {
        if (bulletRunnable == null) {
            bulletRunnable = new Runnable() {
                @Override
                public void run() {
                    for (Bullet bullet : bullets) {
                        bullet.move(bullet.getDirection());
                    }
                    view2Player.invalidate();

                    Iterator<Bullet> iterator = bullets.iterator();
                    while (iterator.hasNext()) {
                        Bullet bullet = iterator.next();
                        if (bulletOutOfScreen(view2Player, bullet)) {
                            iterator.remove();
                        }
                    }
                    bulletHandler.postDelayed(this, 50);
                }
            };
            bulletHandler.post(bulletRunnable);
        }
        view2Player.invalidate();
    }

    private boolean bulletOutOfScreen(View2Player view2Player, Bullet bullet) {
        return bullet.getX() < 0 || bullet.getX() > 150 * 25 ||
                bullet.getY() < 0 || bullet.getY() > 150 * 8 ||
                view2Player.getBrick().checkCollision(bullet.getX(), bullet.getY()) ||
                view2Player.getBrick().checkSolidCollision(bullet.getX(), bullet.getY());
    }
}