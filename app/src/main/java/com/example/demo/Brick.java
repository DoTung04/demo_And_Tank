package com.example.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Brick {
    int bricksXPos[] = {130*5, 130*6, 130*8, 130*10, 130*12, 130*14, 130*16, 130*18, 130*20, 130*23, 130*25};

    int bricksYPos[] = {260, 260, 260, 260, 260, 130*3, 130*5, 130*6, 130*7, 130*8, 130*8};

    int solidBricksXPos[] = {0, 130};
    int solidBricksYPos[] = {0, 130};

    int brickON[] = new int[11];
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
                Rect brickRect = new Rect(bricksXPos[i], bricksYPos[i], bricksXPos[i] + 130,
                        bricksYPos[i] + 130);
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
            Rect brickRect = new Rect(solidBricksXPos[i], solidBricksYPos[i], solidBricksXPos[i] + 130,
                    solidBricksYPos[i] + 130);
            if (bulletRect.intersect(brickRect)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTankCollision(int x, int y) {
        for (int i = 0; i < bricksXPos.length; i++) {
            if (brickON[i] == 1) { // Nếu gạch còn tồn tại
                Rect tankRect = new Rect(x, y, x + 130, y + 130);
                Rect brickRect = new Rect(bricksXPos[i], bricksYPos[i], bricksXPos[i] + 130, bricksYPos[i] + 130);
                if (tankRect.intersect(brickRect)) {
                    return true; // Va chạm
                }
            }
        }
        return false; // Không va chạm
    }

    public boolean checkTankSolidCollision(int x, int y) {
        for (int i = 0; i < solidBricksXPos.length; i++) {
            Rect tankRect = new Rect(x, y, x + 130, y + 130);
            Rect brickRect = new Rect(solidBricksXPos[i], solidBricksYPos[i],
                    solidBricksXPos[i] + 130, solidBricksYPos[i] + 130);
            if (tankRect.intersect(brickRect)) {
                return true; // Va chạm với gạch cứng
            }
        }
        return false; // Không va chạm
    }

}


