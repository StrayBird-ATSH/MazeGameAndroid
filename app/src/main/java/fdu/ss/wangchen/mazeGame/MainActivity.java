package fdu.ss.wangchen.mazeGame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static int mode;
    static int footprintLength;
    static int levelNumber = 1;
    static String userName = "";
    static ArrayList<Integer> previousSteps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.story_mode) {
            mode = 1;
            levelNumber = 1;
            MediaPlayer mediaPlayer;
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lindaxia);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0.4f, 0.4f);
            Intent intent = new Intent(MainActivity.this, GameViewActivity.class);
            intent.putExtra("fresh start", true);
            startActivity(intent);
        } else if (id == R.id.sandbox_mode) {
            mode = 2;
            Intent intent = new Intent(MainActivity.this,
                    SandboxLevelPickerActivity.class);
            startActivity(intent);
        } else if (id == R.id.restore_process) {
            restoreProcess();
        } else if (id == R.id.ranking_list) {
            SharedPreferences sharedPreferences = getSharedPreferences("rankingList", MODE_PRIVATE);
            String ranking = "";
            for (int i = 1; i <= sharedPreferences.getInt("length", 1); i++)
                ranking += sharedPreferences.getString("userName" + i, "") +
                        "\t" + sharedPreferences.getInt("mark" + i, 0) + "\n";
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("MazeGame Ranking List");
            alertDialog.setMessage(ranking);
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        } else if (id == R.id.instructions) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("MazeGame Instruction");
            alertDialog.setMessage("There are four levels in this maze game.\nYou can u"
                    + "se the story mode to try all the four levels or use the sandbox mode to try " +
                    "any of the four levels. \nEach level have different circumstances, from " +
                    "simple to complex.");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        } else if (id == R.id.about) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("MazeGame About");
            alertDialog.setMessage("Wang Chen\n" +
                    "School of Software\n" +
                    "Fudan University\n\nJan 10, 2018");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void restoreProcess() {
        SharedPreferences arrayList_data = getSharedPreferences("ArrayList_data", MODE_PRIVATE);
        SharedPreferences other_data = getSharedPreferences("Other_data", MODE_PRIVATE);
        if (arrayList_data.getInt("item_0", 0) == 0) Toast.makeText(
                MainActivity.this, "You have not stored anything yet",
                Toast.LENGTH_SHORT).show();
        else {
            mode = other_data.getInt("mode", 1);
            levelNumber = other_data.getInt("levelNumber", 1);
            previousSteps.clear();
            for (int i = 0; i < arrayList_data.getInt("Numbers", 0); i++)
                previousSteps.add(arrayList_data.getInt("item_" + i, 1));
            Intent intent;
            switch (levelNumber) {
                default:
                case 1:
                    intent = new Intent(MainActivity.this, GameViewActivity.class);
                    break;
                case 2:
                    intent = new Intent(MainActivity.this, GameViewLevelTwoActivity.class);
                    break;
                case 3:
                    intent = new Intent(MainActivity.this, GameViewLevelThreeActivity.class);
                    break;
                case 4:
                    intent = new Intent(MainActivity.this, GameViewLevelFourActivity.class);
            }
            intent.putExtra("extra_data", 2);
            startActivity(intent);
        }
    }
}