package com.lesson8.mcsin.lab8;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Main2Activity extends Activity implements View.OnClickListener {

    private GameView gv;
    private Button shootButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //gets rid of the title at the top of the app

        gv = new GameView(this);

        shootButton = new Button(this);
        shootButton.setWidth(350);
        shootButton.setHeight(100);
        shootButton.setBackgroundColor(Color.LTGRAY);
        shootButton.setTextColor(Color.RED);
        shootButton.setTextSize(20);
        shootButton.setBackgroundResource(R.drawable.aircraft);
        shootButton.setText("Shoot it!");



        DisplayMetrics dm=getResources().getDisplayMetrics();
        final int screenWidth=dm.widthPixels;
        final int screenHeight=dm.heightPixels-50;

        shootButton.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);

                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int l = v.getLeft() + dx;
                        int b = v.getBottom() + dy;
                        int r = v.getRight() + dx;
                        int t = v.getTop() + dy;


                        if (l < 0) {
                            l = 0;
                            r = l + v.getWidth();
                        }

                        if (t < 0) {
                            t = 0;
                            b = t + v.getHeight();
                        }

                        if (r > screenWidth) {
                            r = screenWidth;
                            l = r - v.getWidth();
                        }

                        if (b > screenHeight) {
                            b = screenHeight;
                            t = b - v.getHeight();
                        }
                        v.layout(l, t, r, b);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        Toast.makeText(Main2Activity.this,
                                "Currently Position：" + l + "," + t + "," + r + "," + b,
                                Toast.LENGTH_SHORT).show();
                        v.postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }


                return false;
            }
        });



        shootButton.setOnClickListener(this);
        shootButton.setGravity(Gravity.CENTER);

        FrameLayout GameLayout = new FrameLayout(this);
        LinearLayout ButtonLayout = new LinearLayout(this);
        ButtonLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        ButtonLayout.addView(shootButton);

        GameLayout.addView(gv);
        GameLayout.addView(ButtonLayout);

        setContentView(GameLayout);


    }
    @Override
    protected void onPause()
    {
        super.onPause();
        gv.killThread(); //Notice this reaches into the GameView object and runs the killThread mehtod.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gv.onDestroy();
    }

    @Override
    public void onClick(View v) {

        gv.fire(shootButton.getX(), shootButton.getY(), shootButton.getWidth(), shootButton.getHeight());

    }
}
