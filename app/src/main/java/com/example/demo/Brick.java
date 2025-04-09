package com.example.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Brick {
    // Gạch phá được (breakable bricks) - xếp tùy ý
    int bricksXPos[] = {
            150*2, 150*3, 150*4,             // Nhóm trên trái
            150*10, 150*11, 150*12,          // Nhóm giữa trên
            150*6, 150*7, 150*8,            // Nhóm giữa trái
            150*14, 150*15,                 // Nhóm giữa dưới
            150*5, 150*5, 150*5,            // Cột ngắn trên

    };

    int bricksYPos[] = {
            150*1, 150*1, 150*1,             // Nhóm trên trái
            150*2, 150*2, 150*2,             // Nhóm giữa trên
            150*5, 150*5, 150*5,             // Nhóm giữa trái
            150*6, 150*6,                    // Nhóm giữa dưới
            150*3, 150*4, 150*5,             // Cột ngắn trên
    };

    // Gạch không phá được (solid bricks) - xếp tùy ý
    int solidBricksXPos[] = {
            150*1, 150*2, 150*3,             // Nhóm trên trái
            150*9, 150*10, 150*11,           // Nhóm trên giữa
            150*15,
            150*4, 150*4, 150*4,            // Cột trên
            150*12, 150*12, 150*12,          // Cột giữa
            150*7, 150*8, 150*9             // Nhóm dưới trái
    };

    int solidBricksYPos[] = {
            150*2, 150*2, 150*2,             // Nhóm trên trái
            150*1, 150*1, 150*1,             // Nhóm trên giữa
            150*3,
            150*6, 150*7, 150*8,             // Cột trên
            150*4, 150*5, 150*6,             // Cột giữa
            150*7, 150*7, 150*7              // Nhóm dưới trái
    };

    int brickON[] = new int[bricksXPos.length];
    private Bitmap breakBrickImage;
    private Bitmap solidBrickImage;

    public Brick(Context context) {
        breakBrickImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.break_brick);
        solidBrickImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.solid_brick);
        for (int i = 0; i < brickON.length; i++) {
            brickON[i] = 1;
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < brickON.length; i++) {
            if (brickON[i] == 1) {
                canvas.drawBitmap(breakBrickImage, bricksXPos[i], bricksYPos[i], null);
            }
        }
    }

    public void drawSolids(Canvas canvas) {
        for (int i = 0; i < solidBricksXPos.length; i++) {
            canvas.drawBitmap(solidBrickImage, solidBricksXPos[i], solidBricksYPos[i], null);
        }
    }

    public boolean checkCollision(int x, int y) {
        for (int i = 0; i < brickON.length; i++) {
            if (brickON[i] == 1) {
                Rect bulletRect = new Rect(x, y, x + 26, y + 26);
                Rect brickRect = new Rect(bricksXPos[i], bricksYPos[i], bricksXPos[i] + 150,
                        bricksYPos[i] + 150);
                if (bulletRect.intersect(brickRect)) {
                    brickON[i] = 0;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkSolidCollision(int x, int y) {
        for (int i = 0; i < solidBricksXPos.length; i++) {
            Rect bulletRect = new Rect(x, y, x + 26, y + 26);
            Rect brickRect = new Rect(solidBricksXPos[i], solidBricksYPos[i], solidBricksXPos[i] + 150,
                    solidBricksYPos[i] + 150);
            if (bulletRect.intersect(brickRect)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTankCollision(int x, int y) {
        for (int i = 0; i < bricksXPos.length; i++) {
            if (brickON[i] == 1) {
                Rect tankRect = new Rect(x, y, x + 150, y + 150);
                Rect brickRect = new Rect(bricksXPos[i], bricksYPos[i], bricksXPos[i] + 150, bricksYPos[i] + 150);
                if (tankRect.intersect(brickRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkTankSolidCollision(int x, int y) {
        for (int i = 0; i < solidBricksXPos.length; i++) {
            Rect tankRect = new Rect(x, y, x + 150, y + 150);
            Rect brickRect = new Rect(solidBricksXPos[i], solidBricksYPos[i],
                    solidBricksXPos[i] + 150, solidBricksYPos[i] + 150);
            if (tankRect.intersect(brickRect)) {
                return true;
            }
        }
        return false;
    }
}