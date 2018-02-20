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

        findViewById(R.id.start_fishing_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.start_fishing_button).setVisibility(View.INVISIBLE);
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
        int fishing_cat_width = 250;
        int fishing_cat_height = 250;
        FrameLayout.LayoutParams fishingCatLayoutParams = new FrameLayout.LayoutParams(fishing_cat_width, fishing_cat_height);

        //釣竿のパラメータ
        int turizao_width = (int)(target_radius + 30);
        int turizao_height = (int)((target_radius + 30) * 2 /3);
        FrameLayout.LayoutParams turizaoLayoutParams = new FrameLayout.LayoutParams(turizao_width, turizao_height);

        fishingCatLayoutParams.setMargins(
                (int)(location_frame.getPaddingLeft() + target_location_pt_x + fishing_cat_width + turizao_width),
                (int)(location_frame.getPaddingTop() + target_location_pt_y - (turizao_height * 1 /3) -fishing_cat_height),
                fishingCatLayoutParams.rightMargin,
                fishingCatLayoutParams.bottomMargin
        );

        turizaoLayoutParams.setMargins(
                (int)(location_frame.getPaddingLeft() + target_location_pt_x + turizao_width),
                (int)(location_frame.getPaddingTop() + target_location_pt_y - turizao_height),
                turizaoLayoutParams.rightMargin,
                turizaoLayoutParams.bottomMargin
        );

        Log.d("cat_left",""+(int)(location_frame.getPaddingLeft() + target_location_pt_x + fishing_cat_width + turizao_width));
        Log.d("cat_top", ""+(int)(location_frame.getPaddingTop() + target_location_pt_y - (turizao_height * 1 /3) -fishing_cat_height));
        Log.d("turizao_left",""+(int)(location_frame.getPaddingLeft() + target_location_pt_x + turizao_width));
        Log.d("turizao_top",""+(int)(location_frame.getPaddingTop() + target_location_pt_y - turizao_height));

        Log.d("target_x",""+(int)(location_frame.getPaddingLeft() + target_location_pt_x));
        Log.d("target_y",""+(int)(location_frame.getPaddingTop() + target_location_pt_y));

        fishing_cat.setImageResource(R.drawable.neko_sit_gray);
        fishing_cat.setLayoutParams(fishingCatLayoutParams);
        fishing_cat.setVisibility(View.VISIBLE);

        turizao.setLayoutParams(turizaoLayoutParams);
        turizao.setVisibility(View.VISIBLE);
        //釣竿のアニメーション
        turizao.setBackground(ContextCompat.getDrawable(FishingActivity.this, R.drawable.turizao_animation));
        // AnimationDrawableを取得
        AnimationDrawable turizaoAnimation = (AnimationDrawable) turizao.getBackground();
        // アニメーションの開始
        turizaoAnimation.start();

        //電球つける
        int light_width = 80;
        int light_height = 80;
        FrameLayout.LayoutParams lightLayoutParams = new FrameLayout.LayoutParams(light_width, light_height);

        lightLayoutParams.setMargins(
                (int)(location_frame.getPaddingLeft() + target_location_pt_x + target_radius + fishing_cat_width + turizao_width),
                (int)(location_frame.getPaddingTop() + target_location_pt_y - turizao_height - light_height),
                turizaoLayoutParams.rightMargin,
                turizaoLayoutParams.bottomMargin
        );


        light.setLayoutParams(lightLayoutParams);
        light.setVisibility(View.VISIBLE);

        //ライトのアニメーション
        light.setBackground(ContextCompat.getDrawable(FishingActivity.this, R.drawable.light_animation));
        // AnimationDrawableを取得
        AnimationDrawable lightAnimation = (AnimationDrawable) turizao.getBackground();
        // アニメーションの開始
        lightAnimation.start();

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
