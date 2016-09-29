package com.allenliu.circlemenuview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleMenuView circleMenuView = (CircleMenuView) findViewById(R.id.view);
        circleMenuView.setOnClickListener(new CircleMenuView.onYuanPanClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
//       new CircleMenuView(this)
//                .setWidthAndHeight(300, 300)
//                .setCenterText()
//                .setCenterIcon()
//                .setGapColor()
//                .setGapSize()
//                .setMenuIcons()
//                .setMenuTexts()
//                .setMenuTextColor()
//                .setMenuTextSize()
//                .setMenuItemBackground()
//                .setInsideCircleRadius()
//                .setStrokeColor()
//                .setStrokeWidth()
//                .setOnClickListener();

    }
}
