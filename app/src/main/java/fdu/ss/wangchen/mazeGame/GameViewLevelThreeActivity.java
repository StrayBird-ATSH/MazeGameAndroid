package fdu.ss.wangchen.mazeGame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class GameViewLevelThreeActivity extends GameViewLevelTwoActivity {
    final private Monster monster = new Monster(5, 5);
    final private Treasure treasure = new Treasure(1, 9);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view_level_three);
        Intent intent = getIntent();
        if (MainActivity.levelNumber == 3)
            switch (intent.getIntExtra("extra_data", 1)) {
                case 0:
                    moveToEnd();
                    break;
                case 2:
                    loadProcess();
                    loadTreasure();
                    loadMonster();
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
    void killMonster() {
        final ImageView monsterPicture = findViewById(R.id.monster_picture);
        final FrameLayout frameLayout = findViewById(R.id.level_three_frame);
        if ((Math.abs(player.getColumn() - monster.getColumn()) == 1 &&
                player.getRow() == monster.getRow()) ||
                (Math.abs(player.getRow() - monster.getRow()) == 1
                        && player.getColumn() == monster.getColumn())
                        && monster.getStatus() == 1) {
            mediaPlayer = MediaPlayer.create(GameViewLevelThreeActivity.this, R.raw.kill);
            mediaPlayer.start();
            monster.setStatus(0);
            frameLayout.removeView(monsterPicture);
            monsterKilledNumber++;
            Toast.makeText(GameViewLevelThreeActivity.this,
                    "You have killed one treasure.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void checkMonster() {
        if (player.getRow() == monster.getRow() &&
                player.getColumn() == monster.getColumn() &&
                monster.getStatus() == 1) {
            mediaPlayer =MediaPlayer.create(GameViewLevelThreeActivity.this,R.raw.dead);
            mediaPlayer.start();
            Toast.makeText(GameViewLevelThreeActivity.this,
                    "Sorry, you are killed by monster.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    void loadMonster() {
        SharedPreferences other_data = getSharedPreferences("Other_data", MODE_PRIVATE);
        ImageView monsterImage = findViewById(R.id.monster_picture);
        monster.setStatus(other_data.getInt("monsterStatus", 0));
        monster.setRow(other_data.getInt("monsterRow", 0));
        monster.setColumn(other_data.getInt("monsterColumn", 1));
        if (monster.getStatus() == 1) {
            monsterImage.setX((monster.getColumn() - 5) * 72);
            monsterImage.setY((monster.getRow() - 5) * 55);
        } else removeMonsterImage();
    }

    @Override
    void saveMonster() {
        SharedPreferences.Editor editor1 = getSharedPreferences("Other_data", MODE_PRIVATE).edit();
        editor1.putInt("monsterRow", monster.getRow());
        editor1.putInt("monsterColumn", monster.getColumn());
        editor1.putInt("monsterStatus", monster.getStatus());
        editor1.apply();
    }

    @Override
    void checkTreasure() {
        if ((Math.abs(player.getColumn() - treasure.getColumn()) == 1 &&
                player.getRow() == treasure.getRow()) ||
                (Math.abs(player.getRow() - treasure.getRow()) == 1 &&
                        player.getColumn() == treasure.getColumn()) &&
                        treasure.getStatus() == 1) {
            mediaPlayer = MediaPlayer.create(GameViewLevelThreeActivity.this, R.raw.treasure);
            mediaPlayer.start();
            treasure.setStatus(0);
            removeTreasureImage();
            treasureCollectedNumber++;
            Toast.makeText(GameViewLevelThreeActivity.this,
                    "You have collected one treasure.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void removeTreasureImage() {
        final ImageView treasurePicture = findViewById(R.id.treasure_picture);
        final FrameLayout frameLayout = findViewById(R.id.level_three_frame);
        frameLayout.removeView(treasurePicture);
    }

    void removeMonsterImage() {
        final ImageView monsterPicture = findViewById(R.id.monster_picture);
        final FrameLayout frameLayout = findViewById(R.id.level_three_frame);
        frameLayout.removeView(monsterPicture);
    }

    @Override
    void loadTreasure() {
        SharedPreferences other_data = getSharedPreferences("Other_data", MODE_PRIVATE);
        treasure.setStatus(other_data.getInt("treasureStatus", 0));
        treasure.setRow(other_data.getInt("treasureRow", 0));
        treasure.setColumn(other_data.getInt("treasureColumn", 1));
        if (treasure.getStatus() != 1) removeTreasureImage();
    }
}