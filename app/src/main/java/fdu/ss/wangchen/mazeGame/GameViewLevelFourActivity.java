package fdu.ss.wangchen.mazeGame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GameViewLevelFourActivity extends GameViewLevelThreeActivity {
    final private Monster monster = new Monster(5, 5);
    final private Treasure treasure = new Treasure(1, 9);
    final private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view_level_four_activiry);
        Intent intent = getIntent();
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
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                monsterMove();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
    }

    private void monsterMove() {
        ImageView monsterPicture = findViewById(R.id.monster_picture);
        int direction = (int) (Math.random() * 4) + 1;
        switch (direction) {
            case 1:
                if (monster.moveUp(4))
                    monsterPicture.setY(monsterPicture.getY() - 55);
                break;
            case 2:
                if (monster.moveDown(4))
                    monsterPicture.setY(monsterPicture.getY() + 55);
                break;
            case 3:
                if (monster.moveLeft(4))
                    monsterPicture.setX(monsterPicture.getX() - 72);
                break;
            case 4:
                if (monster.moveRight(4))
                    monsterPicture.setX(monsterPicture.getX() + 72);
                break;
        }
    }

    @Override
    void checkTreasure() {
        if ((Math.abs(player.getColumn() - treasure.getColumn()) == 1 &&
                player.getRow() == treasure.getRow()) ||
                (Math.abs(player.getRow() - treasure.getRow()) == 1 &&
                        player.getColumn() == treasure.getColumn()) &&
                        treasure.getStatus() == 1) {
            mediaPlayer = MediaPlayer.create(GameViewLevelFourActivity.this, R.raw.treasure);
            mediaPlayer.start();
            treasure.setStatus(0);
            removeTreasureImage();
            treasureCollectedNumber++;
            Toast.makeText(GameViewLevelFourActivity.this,
                    "You have collected one treasure.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void killMonster() {
        if ((Math.abs(player.getColumn() - monster.getColumn()) == 1 &&
                player.getRow() == monster.getRow()) ||
                (Math.abs(player.getRow() - monster.getRow()) == 1
                        && player.getColumn() == monster.getColumn())
                        && monster.getStatus() == 1) {
            mediaPlayer = MediaPlayer.create(GameViewLevelFourActivity.this, R.raw.kill);
            mediaPlayer.start();
            monster.setStatus(0);
            timer.cancel();
            removeMonsterImage();
            monsterKilledNumber++;
            Toast.makeText(GameViewLevelFourActivity.this,
                    "You have killed one treasure.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void checkMonster() {
        if (player.getRow() == monster.getRow() &&
                player.getColumn() == monster.getColumn() &&
                monster.getStatus() == 1) {
            mediaPlayer = MediaPlayer.create(GameViewLevelFourActivity.this, R.raw.dead);
            mediaPlayer.start();
            Toast.makeText(GameViewLevelFourActivity.this,
                    "Sorry, you are killed by treasure.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    void removeTreasureImage() {
        final ImageView treasurePicture = findViewById(R.id.treasure_picture);
        final FrameLayout frameLayout = findViewById(R.id.level_four_frame);
        frameLayout.removeView(treasurePicture);
    }

    @Override
    void removeMonsterImage() {
        final ImageView monsterPicture = findViewById(R.id.monster_picture);
        final FrameLayout frameLayout = findViewById(R.id.level_four_frame);
        frameLayout.removeView(monsterPicture);
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

    void loadMonster() {
        SharedPreferences other_data = getSharedPreferences("Other_data", MODE_PRIVATE);
        ImageView monsterImage = findViewById(R.id.monster_picture);
        ImageView playerImage = findViewById(R.id.playerImage);
        monster.setStatus(other_data.getInt("monsterStatus", 0));
        monster.setRow(other_data.getInt("monsterRow", 0));
        monster.setColumn(other_data.getInt("monsterColumn", 1));
        if (monster.getStatus() == 1) {
            monsterImage.setX(playerImage.getX() + (monster.getColumn() - 1) * 72);
            monsterImage.setY(playerImage.getY() + (monster.getRow() - 1) * 55);
        } else removeMonsterImage();
    }
}