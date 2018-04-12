package fdu.ss.wangchen.mazeGame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class GameViewActivity extends AppCompatActivity implements View.OnClickListener {
    final Player player = new Player();
    static int treasureCollectedNumber = 0;
    static int monsterKilledNumber = 0;
    static int steps = 0;
    static int backSteps = 0;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        Intent intent = getIntent();
        if (MainActivity.levelNumber == 1)
            switch (intent.getIntExtra("extra_data", 1)) {
                case 0:
                    moveToEnd();
                    break;
                case 2:
                    loadProcess();
                    loadPlayer();
                    addPreviousSteps();
            }
        if (intent.getBooleanExtra("fresh_start", false)) {
            treasureCollectedNumber = 0;
            monsterKilledNumber = 0;
            steps = 0;
            backSteps = 0;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meunu_in_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quit_and_save:
                saveProgress();
                finish();
                break;
            case R.id.quit_without_save:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        final Toast toastWall = Toast.makeText(GameViewActivity.this,
                "There is wall, so you cannot move", Toast.LENGTH_SHORT);
        final ImageView imageView = findViewById(R.id.playerImage);
        System.out.println(imageView.getX());
        System.out.println(imageView.getY());
        switch (view.getId()) {
            case R.id.backButton:
                goBack();
                break;
            case R.id.crackButton:
                killMonster();
                checkTreasure();
                break;
            case R.id.downButton:
                if (player.moveDown(MainActivity.levelNumber)) {
                    addSteps();
                    imageView.setY(imageView.getY() + 55);
                    steps++;
                    MainActivity.previousSteps.add(1);
                    checkFinal(MainActivity.levelNumber);
                } else toastWall.show();
                checkMonster();
                break;
            case R.id.infoButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(GameViewActivity.this);
                builder.setTitle("MazeGame info");
                builder.setMessage(getInfo());
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
                break;
            case R.id.leftButton:
                if (player.moveLeft(MainActivity.levelNumber)) {
                    addSteps();
                    imageView.setX(imageView.getX() - 72);
                    steps++;
                    MainActivity.previousSteps.add(2);
                } else toastWall.show();
                checkMonster();
                break;
            case R.id.rightButton:
                if (player.moveRight(MainActivity.levelNumber)) {
                    addSteps();
                    imageView.setX(imageView.getX() + 72);
                    steps++;
                    MainActivity.previousSteps.add(3);
                } else toastWall.show();
                checkMonster();
                break;
            case R.id.upButton:
                if (player.moveUp(MainActivity.levelNumber)) {
                    addSteps();
                    imageView.setY(imageView.getY() - 55);
                    steps++;
                    MainActivity.previousSteps.add(4);
                } else toastWall.show();
                checkMonster();
                break;
        }
    }

    void checkFinal(int levelNumber) {
        if (player.getColumn() == 13 && player.getRow() == 19) {
            Toast.makeText(GameViewActivity.this,
                    "You have finished this level.", Toast.LENGTH_LONG).show();
            mediaPlayer = MediaPlayer.create(GameViewActivity.this, R.raw.finish);
            mediaPlayer.start();
            if (MainActivity.mode == 2) finish();
            else {
                switch (levelNumber) {
                    case 1:
                        Intent intent = new Intent(GameViewActivity.this,
                                GameViewLevelTwoActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        Intent intent1 = new Intent(GameViewActivity.this,
                                GameViewLevelThreeActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case 3:
                        Intent intent2 = new Intent(GameViewActivity.this,
                                GameViewLevelFourActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case 4:
                        SharedPreferences sharedPreferences =
                                getSharedPreferences("rankingList", MODE_PRIVATE);
                        int length = sharedPreferences.getInt("length", 1);
                        SharedPreferences.Editor editor =
                                getSharedPreferences("rankingList", MODE_PRIVATE).edit();
                        editor.putInt("length", ++length);
                        editor.putString("userName" + length, MainActivity.userName);
                        editor.putInt("mark" + length, (monsterKilledNumber +
                                treasureCollectedNumber) * 100 - (steps + backSteps));
                        editor.apply();
                        finish();
                        break;
                }
                MainActivity.levelNumber++;
            }
        }
    }

    void checkTreasure() {
    }

    void killMonster() {
    }

    void checkMonster() {
    }

    String getInfo() {
        return "User name: " + MainActivity.userName +
                "\nYou are in level " + MainActivity.levelNumber + "\n" +
                "Total step is " + steps + "\n" +
                "You have killed " + monsterKilledNumber + " monsters\n" +
                "You have collected " + treasureCollectedNumber + " treasure\n" +
                "You have gone " + backSteps + " steps backwards";
    }

    void moveToEnd() {
        ImageView playerImage = findViewById(R.id.playerImage);
        ImageView imageView1 = findViewById(R.id.footprint_1);
        ImageView imageView2 = findViewById(R.id.footprint_2);
        ImageView imageView3 = findViewById(R.id.footprint_3);
        ImageView imageView4 = findViewById(R.id.footprint_4);
        ImageView imageView5 = findViewById(R.id.footprint_5);
        ImageView imageView6 = findViewById(R.id.footprint_6);
        ImageView imageView7 = findViewById(R.id.footprint_7);
        ImageView imageView8 = findViewById(R.id.footprint_8);
        ImageView imageView9 = findViewById(R.id.footprint_9);
        ImageView imageView10 = findViewById(R.id.footprint_10);
        playerImage.setX(playerImage.getX() + 12 * 72);
        playerImage.setY(playerImage.getY() + 17 * 55);
        imageView1.setX(playerImage.getX());
        imageView1.setY(playerImage.getY());
        imageView2.setX(playerImage.getX());
        imageView2.setY(playerImage.getY());
        imageView3.setX(playerImage.getX());
        imageView3.setY(playerImage.getY());
        imageView4.setX(playerImage.getX());
        imageView4.setY(playerImage.getY());
        imageView5.setX(playerImage.getX());
        imageView5.setY(playerImage.getY());
        imageView6.setX(playerImage.getX());
        imageView6.setY(playerImage.getY());
        imageView7.setX(playerImage.getX());
        imageView7.setY(playerImage.getY());
        imageView8.setX(playerImage.getX());
        imageView8.setY(playerImage.getY());
        imageView9.setX(playerImage.getX());
        imageView9.setY(playerImage.getY());
        imageView10.setX(playerImage.getX());
        imageView10.setY(playerImage.getY());
        player.setRow(18);
        player.setColumn(13);
    }

    void goBack() {
        if (!MainActivity.previousSteps.isEmpty()) {
            if (!(player.getColumn() == 1 && player.getRow() == 1)) {
                final ImageView imageView = findViewById(R.id.playerImage);
                addSteps();
                switch (MainActivity.previousSteps.get(MainActivity.previousSteps.size() - 1)) {
                    case 1:
                        imageView.setY(imageView.getY() - 55);
                        player.setRow(player.getRow() - 1);
                        break;
                    case 2:
                        imageView.setX(imageView.getX() + 72);
                        player.setColumn(player.getColumn() + 1);
                        break;
                    case 3:
                        imageView.setX(imageView.getX() - 72);
                        player.setColumn(player.getColumn() - 1);
                        break;
                    case 4:
                        imageView.setY(imageView.getY() + 55);
                        player.setRow(player.getRow() + 1);
                        break;
                }
            } else {
                switch (MainActivity.levelNumber) {
                    case 2:
                        Intent intent = new Intent(GameViewActivity.this,
                                GameViewActivity.class);
                        intent.putExtra("extra_data", 0);
                        startActivity(intent);
                        finish();
                        break;
                    case 3:
                        Intent intent1 = new Intent(GameViewActivity.this,
                                GameViewLevelTwoActivity.class);
                        intent1.putExtra("extra_data", 0);
                        startActivity(intent1);
                        finish();
                        break;
                    case 4:
                        Intent intent2 = new Intent(GameViewActivity.this,
                                GameViewLevelThreeActivity.class);
                        intent2.putExtra("extra_data", 0);
                        startActivity(intent2);
                        finish();
                        break;
                }
                MainActivity.levelNumber--;
            }
            backSteps++;
            MainActivity.previousSteps.remove(MainActivity.previousSteps.size() - 1);
            checkMonster();
        } else Toast.makeText(GameViewActivity.this, "You are at the start point\n" +
                "thus unable to go back", Toast.LENGTH_SHORT).show();
    }

    void addSteps() {
        ImageView playerImage = findViewById(R.id.playerImage);
        ImageView imageView1 = findViewById(R.id.footprint_1);
        ImageView imageView2 = findViewById(R.id.footprint_2);
        ImageView imageView3 = findViewById(R.id.footprint_3);
        ImageView imageView4 = findViewById(R.id.footprint_4);
        ImageView imageView5 = findViewById(R.id.footprint_5);
        ImageView imageView6 = findViewById(R.id.footprint_6);
        ImageView imageView7 = findViewById(R.id.footprint_7);
        ImageView imageView8 = findViewById(R.id.footprint_8);
        ImageView imageView9 = findViewById(R.id.footprint_9);
        ImageView imageView10 = findViewById(R.id.footprint_10);
        switch (Math.min(MainActivity.previousSteps.size() + 1, MainActivity.footprintLength)) {
            case 10:
                imageView10.setVisibility(View.VISIBLE);
                imageView10.setY(imageView9.getY());
                imageView10.setX(imageView9.getX());
            case 9:
                imageView9.setVisibility(View.VISIBLE);
                imageView9.setY(imageView8.getY());
                imageView9.setX(imageView8.getX());
            case 8:
                imageView8.setVisibility(View.VISIBLE);
                imageView8.setY(imageView7.getY());
                imageView8.setX(imageView7.getX());
            case 7:
                imageView7.setVisibility(View.VISIBLE);
                imageView7.setY(imageView6.getY());
                imageView7.setX(imageView6.getX());
            case 6:
                imageView6.setVisibility(View.VISIBLE);
                imageView6.setY(imageView5.getY());
                imageView6.setX(imageView5.getX());
            case 5:
                imageView5.setVisibility(View.VISIBLE);
                imageView5.setY(imageView4.getY());
                imageView5.setX(imageView4.getX());
            case 4:
                imageView4.setVisibility(View.VISIBLE);
                imageView4.setY(imageView3.getY());
                imageView4.setX(imageView3.getX());
            case 3:
                imageView3.setVisibility(View.VISIBLE);
                imageView3.setY(imageView2.getY());
                imageView3.setX(imageView2.getX());
            case 2:
                imageView2.setVisibility(View.VISIBLE);
                imageView2.setX(imageView1.getX());
                imageView2.setY(imageView1.getY());
            case 1:
                imageView1.setVisibility(View.VISIBLE);
                imageView1.setX(playerImage.getX());
                imageView1.setY(playerImage.getY());
        }
    }

    void saveProgress() {
        SharedPreferences.Editor editor = getSharedPreferences("ArrayList_data", MODE_PRIVATE).edit();
        for (int i = 0; i < MainActivity.previousSteps.size(); i++)
            editor.putInt("item_" + i, MainActivity.previousSteps.get(i));
        editor.putInt("Numbers", MainActivity.previousSteps.size());
        editor.apply();
        SharedPreferences.Editor editor1 = getSharedPreferences("Other_data", MODE_PRIVATE).edit();
        editor1.putInt("footprintLength", MainActivity.footprintLength);
        editor1.putInt("treasureCollectedNumber", treasureCollectedNumber);
        editor1.putInt("monstersKilledNumber", monsterKilledNumber);
        editor1.putInt("steps", steps);
        editor1.putInt("backSteps", backSteps);
        editor1.putInt("levelNumber", MainActivity.levelNumber);
        editor1.putInt("mode", MainActivity.mode);
        editor1.putInt("playerRow", player.getRow());
        editor1.putInt("playerColumn", player.getColumn());
        saveTreasure();
        saveMonster();
        editor1.apply();
    }

    void loadProcess() {
        SharedPreferences other_data = getSharedPreferences("Other_data", MODE_PRIVATE);
        MainActivity.footprintLength = other_data.getInt("footprintLength", 2);
        treasureCollectedNumber = other_data.getInt("treasureCollectedNumber", 0);
        monsterKilledNumber = other_data.getInt("monstersKilledNumber", monsterKilledNumber);
        steps = other_data.getInt("steps", 0);
        backSteps = other_data.getInt("backSteps", 0);
        player.setColumn(other_data.getInt("playerColumn", 1));
        player.setRow(other_data.getInt("playerRow", 1));
    }

    void saveTreasure() {
    }

    void loadTreasure() {
    }

    void saveMonster() {
    }

    void loadMonster() {
    }

    void loadPlayer() {
        ImageView playerImage = findViewById(R.id.playerImage);
        playerImage.setX(playerImage.getX() + (player.getColumn() - 1) * 72);
        playerImage.setY(playerImage.getY() + (player.getRow() - 1) * 55);
    }

    void addPreviousSteps() {
        ImageView imageView1 = findViewById(R.id.footprint_1);
        ImageView imageView2 = findViewById(R.id.footprint_2);
        ImageView imageView3 = findViewById(R.id.footprint_3);
        ImageView imageView4 = findViewById(R.id.footprint_4);
        ImageView imageView5 = findViewById(R.id.footprint_5);
        ImageView imageView6 = findViewById(R.id.footprint_6);
        ImageView imageView7 = findViewById(R.id.footprint_7);
        ImageView imageView8 = findViewById(R.id.footprint_8);
        ImageView imageView9 = findViewById(R.id.footprint_9);
        ImageView imageView10 = findViewById(R.id.footprint_10);
        ImageView playerImage = findViewById(R.id.playerImage);
        switch (Math.min(MainActivity.previousSteps.size() + 1, MainActivity.footprintLength)) {
            case 10:
                imageView10.setVisibility(View.VISIBLE);
                imageView10.setY(playerImage.getY());
                imageView10.setX(playerImage.getX());
            case 9:
                imageView9.setVisibility(View.VISIBLE);
                imageView9.setY(playerImage.getY());
                imageView9.setX(playerImage.getX());
            case 8:
                imageView8.setVisibility(View.VISIBLE);
                imageView8.setY(playerImage.getY());
                imageView8.setX(playerImage.getX());
            case 7:
                imageView7.setVisibility(View.VISIBLE);
                imageView7.setY(playerImage.getY());
                imageView7.setX(playerImage.getX());
            case 6:
                imageView6.setVisibility(View.VISIBLE);
                imageView6.setY(playerImage.getY());
                imageView6.setX(playerImage.getX());
            case 5:
                imageView5.setVisibility(View.VISIBLE);
                imageView5.setY(playerImage.getY());
                imageView5.setX(playerImage.getX());
            case 4:
                imageView4.setVisibility(View.VISIBLE);
                imageView4.setY(playerImage.getY());
                imageView4.setX(playerImage.getX());
            case 3:
                imageView3.setVisibility(View.VISIBLE);
                imageView3.setY(playerImage.getY());
                imageView3.setX(playerImage.getX());
            case 2:
                imageView2.setVisibility(View.VISIBLE);
                imageView2.setX(playerImage.getX());
                imageView2.setY(playerImage.getY());
            case 1:
                imageView1.setVisibility(View.VISIBLE);
                imageView1.setX(playerImage.getX());
                imageView1.setY(playerImage.getY());
        }
    }
}