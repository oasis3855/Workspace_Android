package com.example.activitysubscreentest01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Subアクティビティを開くボタン
        findViewById(R.id.buttonSubActivity).setOnClickListener(this);
    }

    // *****
    // Subアクティビティを開くボタンを押したときの処理
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSubActivity) {
            Intent intent = new Intent(this, SubActivity.class);
            startActivity(intent);
        }
    }
}
