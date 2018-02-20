package com.example.ozeki.fishing5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import me.drakeet.materialdialog.MaterialDialog;

public class FishingActivity extends AppCompatActivity {

    SharedPreferences pref;
    Bitmap target_location_frame;
    private FrameLayout.LayoutParams buttonLayoutParams;

    double target_radius;
    double target_location_pt_x;
    double target_location_pt_y;

    ImageView fishing_cat;
    ImageView light;
    ImageView turizao;

    Button result_button;

    private TranslateAnimation translateAnimation;

    ImageView location_frame;

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

        fishing_cat = (ImageView) findViewById(R.id.fishing_cat);
        fishing_cat.setVisibility(View.INVISIBLE);
        light = (ImageView) findViewById(R.id.light);
        light.setVisibility(View.INVISIBLE);
        turizao = (ImageView) findViewById(R.id.turizao);
        turizao.setVisibility(View.INVISIBLE);
        result_button = (Button) findViewById(R.id.result_button);
        result_button.setVisibility(View.INVISIBLE);

        location_frame = findViewById(R.id.target_location_view);
        location_frame.setImageBitmap(target_location_frame);

        ImageView target_location_button = findViewById(R.id.target_location_button);
        Intent intent = getIntent();
        target_radius = intent.getDoubleExtra("target_radius", 0);
        target_location_pt_x = intent.getDoubleExtra("target_location_pt_x", 0);
        target_location_pt_y = intent.getDoubleExtra("target_location_pt_y", 0);

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

        findViewById(R.id.start_fishing_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.start_fishing_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.return_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.target_location_button).setVisibility(View.INVISIBLE);
                start_fishing(view);
            }
        });

        findViewById(R.id.return_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FishingActivity.this, FindFishingGroundActivity.class);
                startActivity(intent);
            }
        });

    }

    private void start_fishing(View view){
        //釣り始め

        //猫のパラメータ
        int fishing_image_width = 350;
        int fishing_image_height = 350;
        FrameLayout.LayoutParams fishingImageLayoutParams = new FrameLayout.LayoutParams(fishing_image_width, fishing_image_height);

        fishingImageLayoutParams.setMargins(
                (int)(location_frame.getPaddingLeft() + target_location_pt_x + target_radius + (fishing_image_width/2)),
                (int)(location_frame.getPaddingTop() + target_location_pt_y - fishing_image_height),
                fishingImageLayoutParams.rightMargin,
                fishingImageLayoutParams.bottomMargin
        );

        turizao.setLayoutParams(fishingImageLayoutParams);
        turizao.setVisibility(View.VISIBLE);
        //釣竿のアニメーション
        turizao.setBackground(ContextCompat.getDrawable(FishingActivity.this, R.drawable.turizao_animation));
        // AnimationDrawableを取得
        AnimationDrawable turizaoAnimation = (AnimationDrawable) turizao.getBackground();
        // アニメーションの開始
        turizaoAnimation.start();

        result_button.setVisibility(View.VISIBLE);
        result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //画面遷移
                Intent intent = new Intent(FishingActivity.this, ResultActivity.class);
                intent.putExtra("target_radius", target_radius);
                startActivity(intent);
            }
        });
    }
}
