package com.example.chesscaroandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Obj_HumVsHum extends AppCompatActivity {
    ActionBar mAcBAr;
    Intent intentMusicBg,homeIntent,newgameIntentSing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_humvshum);
        mAcBAr = getActionBar();
//        mAcBAr.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CDAF95")));
//        mAcBAr.setTitle(Html.fromHtml("<font color='#000000'> Người vs người </font>"));
//        mAcBAr.setHomeButtonEnabled(true);
//        mAcBAr.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.newgame:
                AlertDialog.Builder builder = new AlertDialog.Builder(Obj_HumVsHum.this);
                builder.setTitle("New game");
                builder.setMessage("Bạn có muốn tạo game mới không ?");
                builder.setIcon(R.mipmap.newgame);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newgameIntentSing = new Intent(Obj_HumVsHum.this, Obj_HumVsHum.class);
                    newgameIntentSing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newgameIntentSing.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(newgameIntentSing);
                }
            });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                break;

            case R.id.music:
                Toast.makeText(getBaseContext(), "Tắt âm thanh", Toast.LENGTH_SHORT).show();
                intentMusicBg=new Intent(Obj_HumVsHum.this, BackgroundSoundService.class);
                stopService(intentMusicBg);
                //startService(intentMusicBg);
                break;

            case R.id.home:
                Toast.makeText(getBaseContext(), "Về nhà chính", Toast.LENGTH_SHORT).show();
                homeIntent = new Intent(Obj_HumVsHum.this, MainActivity.class);
                startActivity(homeIntent);
                break;

            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    // Source code by LamNguyen
}
