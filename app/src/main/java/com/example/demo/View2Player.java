package com.example.demo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class View2Player extends View {
    private Bot bot;
    private Tank tank1;
    private Tank tank2;
    private Brick brick;
    private Paint paint;
    public Tank getTank1() {
        return tank1;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public void setTank1(Tank tank1) {
        this.tank1 = tank1;
    }

    public Tank getTank2() {
        return tank2;
    }

    public void setTank2(Tank tank2) {
        this.tank2 = tank2;
    }

    public Brick getBrick() {
        return brick;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
    }

    public View2Player(Context context, AttributeSet attrs) {
        super(context, attrs);
        brick = new Brick(context);
        tank1 = new Tank(context, 130*4, 130*2,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_up),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_down),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_left),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player1_tank_right));
        tank2 = new Tank(context, 130*4, 130*3,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_up),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_down),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_left),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player2_tank_right));
        bot = new Bot(context, 130*5, 130*3,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_up),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_down),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_left),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bot_tank_right),
                tank1);

        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(0.6f, 0.6f);

        brick.draw(canvas);
        brick.drawSolids(canvas);

        tank1.draw(canvas);
        tank2.draw(canvas);

        bot.draw(canvas);
        bot.moveRight(brick);

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        for (Bullet b : tank1.getBullets()) {
            b.draw(canvas, paint);
        }

        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        for (Bullet b : tank2.getBullets()) {
            b.draw(canvas, paint);
        }

        invalidate();
    }
}
