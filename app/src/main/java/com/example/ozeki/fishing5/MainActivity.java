package com.example.ozeki.fishing5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.find_fishing_ground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_fishing_ground(view);
            }
        });

    }

    private void find_fishing_ground(View view) {
        Intent intent = new Intent(MainActivity.this, FindFishingGroundActivity.class);
        startActivity(intent);
    }
}
