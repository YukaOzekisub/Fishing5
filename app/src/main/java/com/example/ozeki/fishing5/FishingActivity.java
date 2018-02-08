package com.example.ozeki.fishing5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FishingActivity extends AppCompatActivity {

    SharedPreferences pref;
    Bitmap target_location_frame;
    private FrameLayout.LayoutParams buttonLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing);

        pref = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        String s = pref.getString("fishing_ground", "");
        if (!s.equals("")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            byte[] b = Base64.decode(s, Base64.DEFAULT);
            target_location_frame = BitmapFactory.decodeByteArray(b, 0, b.length).copy(Bitmap.Config.ARGB_8888, true);
        }

        ImageView location_frame = findViewById(R.id.target_location_view);
        location_frame.setImageBitmap(target_location_frame);

        ImageView target_location_button = findViewById(R.id.target_location_button);
        Intent intent = getIntent();
        double target_radius = intent.getDoubleExtra("target_radius", 0);
        double target_location_pt_x = intent.getDoubleExtra("target_location_pt_x", 0);
        double target_location_pt_y = intent.getDoubleExtra("target_location_pt_y", 0);

        Log.d("ポイントx", ""+target_location_pt_x);
        Log.d("ポイントy", ""+target_location_pt_y);

        //ボタンのRelativeLayoutの子要素指定
        int buttonWidth = target_location_button.getLayoutParams().width;
        int buttonHeight = target_location_button.getLayoutParams().height;
        buttonLayoutParams = new FrameLayout.LayoutParams(buttonWidth, buttonHeight);

        buttonLayoutParams.setMargins(
                (int)(location_frame.getPaddingLeft() + target_location_pt_x + buttonWidth),
                (int)(location_frame.getPaddingTop() + target_location_pt_y - buttonHeight),
                buttonLayoutParams.rightMargin,
                buttonLayoutParams.bottomMargin
        );

        target_location_button.setLayoutParams(buttonLayoutParams);
        target_location_button.setVisibility(View.VISIBLE);

    }
}
