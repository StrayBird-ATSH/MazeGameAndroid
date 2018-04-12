package fdu.ss.wangchen.mazeGame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class GameViewLevelTwoActivity extends GameViewActivity {
    final Treasure treasure = new Treasure(1, 9);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view_level_two);
        Intent intent = getIntent();
        if (MainActivity.levelNumber == 2)
            switch (intent.getIntExtra("extra_data", 1)) {
                case 0:
                    moveToEnd();
                    break;
                case 2:
                    loadProcess();
                    loadTreasure();
                    loadPlayer();
                    addPreviousSteps();
            }
        final Button btCrack = findViewById(R.id.crackButton),
                btUp = findViewById(R.id.upButton),
                btLeft = findViewById(R.id.leftButton),
                btRight = findViewById(R.id.rightButton),
                btDown = findViewById(R.id.downButton),
                btInfo = findViewById(R.id.infoButton),
                btBack = findViewById(R.id.backButton);
        btBack.setOnClickListener(this);
        btRight.setOnClickListener(this);
        btLeft.setOnClickListener(this);
        btInfo.setOnClickListener(this);
        btCrack.setOnClickListener(this);
        btUp.setOnClickListener(this);
        btDown.setOnClickListener(this);
    }

    @Override
    void checkTreasure() {
        if ((Math.abs(player.getColumn() - treasure.getColumn()) == 1 &&
                player.getRow() == treasure.getRow()) ||
                (Math.abs(player.getRow() - treasure.getRow()) == 1 &&
                        player.getColumn() == treasure.getColumn()) &&
                        treasure.getStatus() == 1) {
            mediaPlayer = MediaPlayer.create(GameViewLevelTwoActivity.this, R.raw.treasure);
            mediaPlayer.start();
            treasure.setStatus(0);
            removeTreasureImage();
            treasureCollectedNumber++;
            Toast.makeText(GameViewLevelTwoActivity.this,
                    "You have collected one treasure.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void saveTreasure() {
        SharedPreferences.Editor editor1 = getSharedPreferences("Other_data", MODE_PRIVATE).edit();
        editor1.putInt("treasureRow", treasure.getRow());
        editor1.putInt("treasureColumn", treasure.getColumn());
        editor1.putInt("treasureStatus", treasure.getStatus());
        editor1.apply();
    }

    @Override
    void loadTreasure() {
        SharedPreferences other_data = getSharedPreferences("Other_data", MODE_PRIVATE);
        treasure.setStatus(other_data.getInt("treasureStatus", 0));
        treasure.setRow(other_data.getInt("treasureRow", 0));
        treasure.setColumn(other_data.getInt("treasureColumn", 1));
        if (treasure.getStatus() == 1) {
            ImageView treasureImage = findViewById(R.id.treasure_picture);
            ImageView playerImage = findViewById(R.id.playerImage);
            treasureImage.setX(playerImage.getX() + (treasure.getColumn() - 1) * 72);
            treasureImage.setY(playerImage.getY() + (treasure.getRow() - 1) * 55);
        } else removeTreasureImage();
    }

    void removeTreasureImage() {
        final ImageView treasurePicture = findViewById(R.id.treasure_picture);
        final FrameLayout frameLayout = findViewById(R.id.level_two_frame);
        frameLayout.removeView(treasurePicture);
    }
}
