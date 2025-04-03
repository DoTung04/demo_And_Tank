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

    public Tank(Context context, int tankX, int tankY, Bitmap tankUp, Bitmap tankDown, Bitmap tankLeft, Bitmap tankRight){
        TankX = tankX;
        TankY = tankY;

        this.tankUp = tankUp;
        this.tankDown = tankDown;
        this.tankLeft = tankLeft;
        this.tankRight = tankRight;

        TankImage = tankUp;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(TankImage, TankX, TankY, null);
    }

    public void moveTank(int deltaX, int deltaY, Brick brick) {
        int newX = TankX + deltaX;
        int newY = TankY + deltaY;

        // Kiểm tra va chạm với tường
        if (brick.checkTankCollision(newX, newY) || brick.checkTankSolidCollision(newX, newY) ||
                newX < 0 || newX >  130*25||
                newY < 0 || newY > 130*8) {
            // Nếu có va chạm, xe quay vào hướng tường mà không di chuyển qua
            if (deltaX > 0) {
                TankImage = tankRight; // Quay sang phải
            } else if (deltaX < 0) {
                TankImage = tankLeft; // Quay sang trái
            } else if (deltaY > 0) {
                TankImage = tankDown; // Quay xuống
            } else if (deltaY < 0) {
                TankImage = tankUp; // Quay lên
            }
        } else {
            // Nếu không va chạm, xe di chuyển bình thường
            setTankX(TankX += deltaX);
            setTankY(TankY += deltaY);

            // Cập nhật hình ảnh của xe theo hướng di chuyển
            if (deltaX > 0) {
                TankImage = tankRight; // Quay sang phải
            } else if (deltaX < 0) {
                TankImage = tankLeft; // Quay sang trái
            } else if (deltaY > 0) {
                TankImage = tankDown; // Quay xuống
            } else if (deltaY < 0) {
                TankImage = tankUp; // Quay lên
            }
        }
    }

    public void fire(final View2Player view2Player) {
        if (bullets.size() < MAX_BULLETS) {
            Bullet newBullet = new Bullet();

            // Đặt vị trí đạn theo hướng xe
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
            bullets.add(newBullet); // Thêm đạn ngay lập tức
            moveBullets(view2Player); // Gọi hàm di chuyển đạn ngay lập tức
        }
    }

    private Handler bulletHandler = new Handler();
    private Runnable bulletRunnable;
    public void moveBullets(final View2Player view2Player) {
        if (bulletRunnable == null) { // Đảm bảo không tạo nhiều luồng
            bulletRunnable = new Runnable() {
                @Override
                public void run() {
                    for (Bullet bullet : bullets) {
                        bullet.move(bullet.getDirection()); // Di chuyển tất cả đạn
                    }
                    view2Player.invalidate(); // Vẽ lại

                    Iterator<Bullet> iterator = bullets.iterator();
                    while (iterator.hasNext()) {
                        Bullet bullet = iterator.next();
                        if (bulletOutOfScreen(view2Player, bullet)) { // Kiểm tra nếu đạn bay ra khỏi màn hình
                            iterator.remove(); // Xóa viên đạn khỏi danh sách
                        }
                    }


                    bulletHandler.postDelayed(this, 50); // Lặp lại mỗi 50ms
                }
            };
            bulletHandler.post(bulletRunnable);
        }
        view2Player.invalidate(); // Vẽ lại màn hình
    }

    private boolean bulletOutOfScreen(View2Player view2Player, Bullet bullet) {
        return bullet.getX() < 0 || bullet.getX() > view2Player.getWidth() ||
                bullet.getY() < 0 || bullet.getY() > view2Player.getHeight() ||
                view2Player.getBrick().checkCollision(bullet.getX(), bullet.getY()) ||
                view2Player.getBrick().checkSolidCollision(bullet.getX(), bullet.getY());
    }

}
