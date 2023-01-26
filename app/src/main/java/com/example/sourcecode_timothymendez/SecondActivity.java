package com.example.sourcecode_timothymendez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private String selectedAction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle extras = getIntent().getExtras();

        Intent transitionToScreen1Intent = new Intent(this, MainActivity.class);
        Button previousScreenButton = (Button) findViewById(R.id.backScreen1);
        previousScreenButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(transitionToScreen1Intent);
                    }
                }
        );

        if (extras == null) {
            previousScreenButton.performClick();
        } else {
            selectedAction = extras.getString("selectedAction");
        }

        TextView textView = findViewById(R.id.textView2);
        textView.setText("Displaying Video For: " + selectedAction);
    }
}