package fdu.ss.wangchen.mazeGame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        NumberPicker numberPicker = findViewById(R.id.footprint_number_selector);
        Button button = findViewById(R.id.confirm);
        button.setOnClickListener(this);
        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(10);
    }

    @Override
    public void onClick(View view) {
        NumberPicker numberPicker = findViewById(R.id.footprint_number_selector);
        EditText editText = findViewById(R.id.user_name);
        MainActivity.footprintLength = numberPicker.getValue();
        MainActivity.userName = editText.getText().toString();
        finish();
    }
}
