package com.example.demo;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnUp, btnDown, btnLeft, btnRight, btnFire;
    View2Player view2Player;
    private final Handler handler = new Handler();
    private boolean isHolding = false; // Kiểm tra có đang giữ nút không
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnFire = findViewById(R.id.btnFire);
        view2Player = findViewById(R.id.view2Player);

        btnUp.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // Khi bắt đầu nhấn
                    isHolding = true;
                    moveTankUpFast();
                    return true;
                case MotionEvent.ACTION_UP: // Khi thả tay ra
                    isHolding = false;
                    return true;
            }
            return false;
        });

        btnUp.setOnClickListener(v -> {
            view2Player.getTank1().moveTank(0, -13, view2Player.getBrick());
            view2Player.invalidate();
        });

        btnDown.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // Khi bắt đầu nhấn
                    isHolding = true;
                    moveTankDownFast();
                    return true;
                case MotionEvent.ACTION_UP: // Khi thả tay ra
                    isHolding = false;
                    return true;
            }
            return false;
        });

        btnDown.setOnClickListener(v -> {
            view2Player.getTank1().moveTank(0, 13, view2Player.getBrick());
            view2Player.getTank1().setTankImage(view2Player.getTank1().getTankUp());
            view2Player.invalidate();
        });

        btnLeft.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // Khi bắt đầu nhấn
                    isHolding = true;
                    moveTankLeftFast();
                    return true;
                case MotionEvent.ACTION_UP: // Khi thả tay ra
                    isHolding = false;
                    return true;
            }
            return false;
        });

        btnLeft.setOnClickListener(v -> {
            view2Player.getTank1().moveTank(-13, 0, view2Player.getBrick());
            view2Player.invalidate();
        });

        btnRight.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // Khi bắt đầu nhấn
                    isHolding = true;
                    moveTankRightFast();
                    return true;
                case MotionEvent.ACTION_UP: // Khi thả tay ra
                    isHolding = false;
                    return true;
            }
            return false;
        });

        btnRight.setOnClickListener(v -> {
            view2Player.getTank1().moveTank(13, 0, view2Player.getBrick());
            view2Player.invalidate();
        });

        btnFire.setOnClickListener(v -> {
            // Gọi phương thức fireBullet() để bắn viên đạn
            view2Player.getTank1().fire(view2Player);  // Bắn viên đạn từ tank 1
            view2Player.invalidate();  // Cập nhật giao diện
        });

    }

    private void moveTankDownFast() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isHolding) {
                    view2Player.getTank1().moveTank(0, 13, view2Player.getBrick());
                    view2Player.invalidate(); // Cập nhật giao diện
                    handler.postDelayed(this, 50); // Lặp lại sau 50ms (tăng tốc)
                }
            }
        }, 50);
    }

    private void moveTankUpFast() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isHolding) {
                    view2Player.getTank1().moveTank(0, -13, view2Player.getBrick());
                    view2Player.invalidate(); // Cập nhật giao diện
                    handler.postDelayed(this, 50); // Lặp lại sau 50ms (tăng tốc)
                }
            }
        }, 50);
    }

    private void moveTankLeftFast() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isHolding) {
                    view2Player.getTank1().moveTank(-13, 0, view2Player.getBrick());
                    view2Player.invalidate(); // Cập nhật giao diện
                    handler.postDelayed(this, 50); // Lặp lại sau 50ms (tăng tốc)
                }
            }
        }, 50);
    }

    private void moveTankRightFast() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isHolding) {
                    view2Player.getTank1().moveTank(13, 0, view2Player.getBrick());
                    view2Player.invalidate(); // Cập nhật giao diện
                    handler.postDelayed(this, 50); // Lặp lại sau 50ms (tăng tốc)
                }
            }
        }, 50);
    }
}