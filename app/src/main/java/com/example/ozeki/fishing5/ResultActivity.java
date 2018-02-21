package com.example.ozeki.fishing5;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ResultActivity extends AppCompatActivity {

    double target_radius;

    int screen_width;
    int screen_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        target_radius = intent.getDoubleExtra("target_radius", 0);

        //画面サイズの取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        android.graphics.Point realSize = new android.graphics.Point();
        disp.getRealSize(realSize);

        screen_width = realSize.x;
        screen_height = realSize.y;

        int item_list[] = new int[16];
        String item_text_list[] = new String[16];

        item_list[0] = R.drawable.coffee; item_text_list[0] = "コーヒー が釣れた！";
        item_list[1] = R.drawable.onigiri;item_text_list[1] = "おにぎり が釣れた！";
        item_list[2] = R.drawable.hotdog;item_text_list[2] = "ホットドッグ が釣れた！";


        item_list[3] = R.drawable.beer;item_text_list[3] = "ビール が釣れた！";
        item_list[4] = R.drawable.hotdog;item_text_list[4] = "ホットドッグ が釣れた！";
        item_list[5] = R.drawable.kasa;item_text_list[5] = "【レア】 傘 が釣れた！";
        item_list[6] = R.drawable.kingyo;item_text_list[6] = "【レア】 金魚 が釣れた！";
        item_list[7] = R.drawable.tako;item_text_list[7] = "【レア】 タコ が釣れた！";
        item_list[8] = R.drawable.ika;item_text_list[8] = "【レア】イカ が釣れた！";
        item_list[9] = R.drawable.fish1;item_text_list[9] = "【レア】魚 が釣れた！";
        item_list[10] = R.drawable.bousi;item_text_list[10] = "【レア】魔法使いの帽子 が釣れた！";

        item_list[11] = R.drawable.kinko;item_text_list[11] = "【SSレア】 金庫 が釣れた！";
        item_list[12] = R.drawable.fish3;item_text_list[12] = "【Sレア】 魚 が釣れた！";
        item_list[13] = R.drawable.fish2;item_text_list[13] = "【Sレア】 魚 が釣れた！";
        item_list[14] = R.drawable.fish1;item_text_list[14] = "【レア】 魚 が釣れた！";
        item_list[15] = R.drawable.controller;item_text_list[15] = "【SSレア】コントローラー が釣れた！";

        ImageView result_item = (ImageView) findViewById(R.id.resultItem);
        TextView result_item_text = (TextView) findViewById(R.id.itemText);
        int item_num = get_item_num();
        result_item.setImageResource(item_list[item_num]);
        result_item_text.setText(item_text_list[item_num]);

        if(item_num > 4){
            KonfettiView viewKonfetti = findViewById(R.id.viewKonfetti);
            viewKonfetti.build()
                    .addColors(
                            Color.parseColor("#f95b4e"),
                            Color.parseColor("#FFF9D44E"),
                            Color.parseColor("#FF73F94E"))
                    .setDirection(3590.0, 0)
                    .setSpeed(1f, 10f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(10000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(15, 40))
                    .setPosition(-50f,  screen_width + 50f, -50f, -50f )
                    .stream(300, 5000L);
        }
        findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, FindFishingGroundActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.top_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private int get_item_num(){

        int randomNumber = 0;

        if(target_radius < screen_height / 5){
            Random rand = new Random();
            randomNumber = rand.nextInt(3);

            return randomNumber;
        }

        if(target_radius < screen_height / 4) {
            Random rand = new Random();
            randomNumber = rand.nextInt(11);

            return (randomNumber);
        }
        Random rand = new Random();
        randomNumber = rand.nextInt(15);
        return (randomNumber);
    }
}
