package com.example.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class View2Player extends View {
    private List<Bot> bots;
    private Tank tank1;
    private Tank tank2;
    private Brick brick;
    private Paint paint;
    private Bitmap botIcon;

    // Joystick variables
    private float joystickX = 150 * 23; // Vị trí trung tâm joystick
    private float joystickY = 150 * 9;
    private float buttonRadius = 100; // Bán kính mỗi nút
    private float buttonSpacing = 180; // Khoảng cách từ tâm đến nút
    private boolean movingUp, movingDown, movingLeft, movingRight;
    private int moveSpeed = 15;

    // Paint cho nút
    private Paint buttonPaint;
    private Paint borderPaint;
    private Paint activePaint;

    public Tank getTank1() { return tank1; }
    public List<Bot> getBots() { return bots; }
    public void setBots(List<Bot> bots) { this.bots = bots; }
    public void setTank1(Tank tank1) { this.tank1 = tank1; }
    public Tank getTank2() { return tank2; }
    public void setTank2(Tank tank2) { this.tank2 = tank2; }
    public Brick getBrick() { return brick; }
    public void setBrick(Brick brick) { this.brick = brick; }

    public View2Player(Context context, AttributeSet attrs) {
        super(context, attrs);
        brick = new Brick(context);
        tank1 = new Tank(context, 150*3, 150*6,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_up),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_down),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_left),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_right));
        tank2 = new Tank(context, 150*4, 150*3,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_up),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_down),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_left),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_right));

        bots = new ArrayList<>();
        int[][] botPositions = {
                {150*6, 150*3}, {150*9, 150*2}, {150*15, 150*4}, {150*20, 150*3},
                {150*8, 150*6}, {150*13, 150*5}, {150*18, 150*7}, {150*20, 150*6}
        };

        for (int[] pos : botPositions) {
            bots.add(new Bot(context, pos[0], pos[1],
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_up),
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_down),
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_left),
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_right),
                    tank1, brick, this));
        }

        paint = new Paint();
        botIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_up);

        // Khởi tạo Paint cho nút
        buttonPaint = new Paint();
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setAntiAlias(true);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setAntiAlias(true);

        activePaint = new Paint();
        activePaint.setStyle(Paint.Style.FILL);
        activePaint.setColor(Color.YELLOW);
        activePaint.setAntiAlias(true);

        startMovement();
    }

    private void startMovement() {
        new Thread(() -> {
            while (true) {
                if (movingUp) tank1.moveTank(0, -moveSpeed, brick);
                if (movingDown) tank1.moveTank(0, moveSpeed, brick);
                if (movingLeft) tank1.moveTank(-moveSpeed, 0, brick);
                if (movingRight) tank1.moveTank(moveSpeed, 0, brick);
                postInvalidate();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(0.6f, 0.6f);

        // Vẽ nền màu đen
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, 150*21, 150*12, paint);

        // Vẽ vùng màu cyan
        paint.setColor(Color.GRAY);
        canvas.drawRect(150*21, 0, 150*26, 150*12, paint);

        // Vẽ joystick với nút đẹp hơn
        drawJoystickButton(canvas, joystickX, joystickY - buttonSpacing, movingUp); // Nút lên
        drawJoystickButton(canvas, joystickX, joystickY + buttonSpacing, movingDown); // Nút xuống
        drawJoystickButton(canvas, joystickX - buttonSpacing, joystickY, movingLeft); // Nút trái
        drawJoystickButton(canvas, joystickX + buttonSpacing, joystickY, movingRight); // Nút phải

        // Vẽ 8 xe tăng nhỏ
        int iconSize = 75; // Tăng kích thước biểu tượng lên 75 cho phù hợp 150
        for (int i = 0; i < 8; i++) {
            if (i < bots.size() && bots.get(i).isAlive()) {
                int row = 3 - (i / 2);
                int x = 150*21 + (i % 2) * iconSize + 40;
                int y = 150 * row + 20;
                canvas.drawBitmap(botIcon, x, y, null);
            }
        }

        // Tính số bot còn sống
        int aliveBots = 0;
        for (Bot bot : bots) {
            if (bot.isAlive()) aliveBots++;
        }

        // Hiển thị số bot còn lại
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        String botsText = "Bots: " + aliveBots;
        canvas.drawText(botsText, 150*22.5f + 75, 150*5.5f, paint);

        brick.draw(canvas);
        brick.drawSolids(canvas);

        tank1.draw(canvas);

        for (Bot bot : bots) {
            bot.draw(canvas);
        }

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        ArrayList<Bullet> tank1Bullets = new ArrayList<>(tank1.getBullets());
        for (Bullet b : tank1Bullets) {
            b.draw(canvas, paint);
            for (Bot bot : bots) {
                if (bot.isAlive() && checkBulletCollision(b, bot)) {
                    bot.setAlive(false);
                    tank1.getBullets().remove(b);
                    break;
                }
            }
        }

        paint.setColor(Color.RED);
        for (Bot bot : bots) {
            if (bot.isAlive()) {
                ArrayList<Bullet> botBullets = new ArrayList<>(bot.getBullets());
                for (Bullet b : botBullets) {
                    b.draw(canvas, paint);
                    if (tank1.isAlive() && checkBulletCollision(b, tank1)) {
                        tank1.setAlive(false);
                        bot.getBullets().remove(b);
                    }
                }
            }
        }

        paint.setColor(Color.YELLOW);
        for (Bullet b : tank2.getBullets()) {
            b.draw(canvas, paint);
        }
    }

    private void drawJoystickButton(Canvas canvas, float x, float y, boolean isActive) {
        // Gradient từ xanh nhạt đến xanh đậm
        LinearGradient gradient = new LinearGradient(
                x, y - buttonRadius, x, y + buttonRadius,
                Color.parseColor("#00CED1"), Color.parseColor("#006400"),
                Shader.TileMode.CLAMP);
        buttonPaint.setShader(gradient);

        // Vẽ nút
        if (isActive) {
            canvas.drawCircle(x, y, buttonRadius, activePaint); // Màu vàng khi nhấn
        } else {
            canvas.drawCircle(x, y, buttonRadius, buttonPaint); // Gradient khi không nhấn
        }

        // Vẽ viền
        canvas.drawCircle(x, y, buttonRadius, borderPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() / 0.6f;
        float y = event.getY() / 0.6f;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                movingUp = movingDown = movingLeft = movingRight = false;

                if (isInCircle(x, y, joystickX, joystickY - buttonSpacing, buttonRadius)) {
                    movingUp = true;
                } else if (isInCircle(x, y, joystickX, joystickY + buttonSpacing, buttonRadius)) {
                    movingDown = true;
                } else if (isInCircle(x, y, joystickX - buttonSpacing, joystickY, buttonRadius)) {
                    movingLeft = true;
                } else if (isInCircle(x, y, joystickX + buttonSpacing, joystickY, buttonRadius)) {
                    movingRight = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                movingUp = movingDown = movingLeft = movingRight = false;
                break;
        }
        invalidate();
        return true;
    }

    private boolean isInCircle(float x, float y, float centerX, float centerY, float radius) {
        float dx = x - centerX;
        float dy = y - centerY;
        return dx * dx + dy * dy <= radius * radius;
    }

    private boolean checkBulletCollision(Bullet bullet, Tank target) {
        Rect bulletRect = new Rect(bullet.getX(), bullet.getY(),
                bullet.getX() + 52, bullet.getY() + 52);
        Rect targetRect = new Rect(target.getTankX(), target.getTankY(),
                target.getTankX() + 150, target.getTankY() + 150);
        return bulletRect.intersect(targetRect);
    }
}