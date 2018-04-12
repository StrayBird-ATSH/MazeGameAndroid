package fdu.ss.wangchen.mazeGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class SandboxLevelPickerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox_level_picker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hi, I am Wang Chen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        NumberPicker numberPicker = findViewById(R.id.number_selector);
        numberPicker.setMaxValue(4);
        numberPicker.setMinValue(1);
        Button button = findViewById(R.id.selectMap);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        NumberPicker numberPicker = findViewById(R.id.number_selector);
        switch (numberPicker.getValue()) {
            case 1:
                MainActivity.levelNumber = 1;
                Intent intent = new Intent(SandboxLevelPickerActivity.this,
                        GameViewActivity.class);
                startActivity(intent);
                break;
            case 2:
                MainActivity.levelNumber = 2;
                Intent intent2 = new Intent(SandboxLevelPickerActivity.this,
                        GameViewLevelTwoActivity.class);
                startActivity(intent2);
                break;
            case 3:
                MainActivity.levelNumber = 3;
                Intent intent3 = new Intent(SandboxLevelPickerActivity.this,
                        GameViewLevelThreeActivity.class);
                startActivity(intent3);
                break;
            case 4:
                MainActivity.levelNumber = 4;
                Intent intent4 = new Intent(SandboxLevelPickerActivity.this,
                        GameViewLevelFourActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
