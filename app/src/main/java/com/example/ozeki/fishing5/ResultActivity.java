package com.example.ozeki.fishing5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    double target_radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        target_radius = intent.getDoubleExtra("target_radius", 0);

        int item_list[] = new int[16];
        String item_text_list[] = new String[16];

        item_list[0] = R.drawable.coffee; item_text_list[0] = "コーヒー が釣れた！";
        item_list[1] = R.drawable.onigiri;item_text_list[1] = "おにぎり が釣れた！";
        item_list[2] = R.drawable.hotdog;item_text_list[2] = "ホットドッグ が釣れた！";


        item_list[3] = R.drawable.beer;item_text_list[3] = "ビール が釣れた！";
        item_list[4] = R.drawable.hotdog;item_text_list[4] = "ホットドッグ が釣れた！";
        item_list[5] = R.drawable.kasa;item_text_list[5] = "傘 が釣れた！";
        item_list[6] = R.drawable.kingyo;item_text_list[6] = "金魚 が釣れた！";
        item_list[7] = R.drawable.tako;item_text_list[7] = "タコ が釣れた！";
        item_list[8] = R.drawable.ika;item_text_list[8] = "イカ が釣れた！";
        item_list[9] = R.drawable.fish1;item_text_list[9] = "魚 が釣れた！";
        item_list[10] = R.drawable.bousi;item_text_list[10] = "魔法使いの帽子 が釣れた！";

        item_list[11] = R.drawable.kinko;item_text_list[11] = "金庫 が釣れた！";
        item_list[12] = R.drawable.fish3;item_text_list[12] = "魚 が釣れた！";
        item_list[13] = R.drawable.fish2;item_text_list[13] = "魚 が釣れた！";
        item_list[14] = R.drawable.fish1;item_text_list[14] = "魚 が釣れた！";
        item_list[15] = R.drawable.controller;item_text_list[15] = "コントローラー が釣れた！";

        ImageView result_item = (ImageView) findViewById(R.id.resultItem);
        TextView result_item_text = (TextView) findViewById(R.id.itemText);
        int item_num = get_item_num();
        result_item.setImageResource(item_list[item_num]);
        result_item_text.setText(item_text_list[item_num]);

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

        //画面サイズの取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        android.graphics.Point realSize = new android.graphics.Point();
        disp.getRealSize(realSize);

        int screen_width = realSize.x;
        int screen_height = realSize.y;

        int randomNumber = 0;

        if(target_radius < screen_height / 5){
            Random rand = new Random();
            randomNumber = rand.nextInt(3);
            return randomNumber;
        }

        if(target_radius < screen_height / 4) {
            Random rand = new Random();
            randomNumber = rand.nextInt(8);
            return (randomNumber + 3);
        }
        Random rand = new Random();
        randomNumber = rand.nextInt(5);
        return (randomNumber + 11);
    }
}
