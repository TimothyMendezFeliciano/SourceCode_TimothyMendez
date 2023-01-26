package com.example.sourcecode_timothymendez;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Array;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String selectedAction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent transitionToScreen2Intent = new Intent(this, SecondActivity.class);

        Button nextScreenButton = (Button) findViewById(R.id.navigateButton);
        nextScreenButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transitionToScreen2Intent.putExtra("selectedAction", selectedAction);
                        startActivity(transitionToScreen2Intent);
                    }
                }
        );


        // SPinner
        Spinner spinner = (Spinner) findViewById(R.id.gesturesSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gestures_action, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedAction = parent.getItemAtPosition(position).toString();
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(selectedAction);
        Button navigateButton = (Button) findViewById(R.id.navigateButton);
        navigateButton.setEnabled(true);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}