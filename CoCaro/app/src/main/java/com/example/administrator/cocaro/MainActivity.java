package com.example.administrator.cocaro;

        import android.app.Activity;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageButton pvsp, pvsc, replay, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        pvsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPvsP = new Intent(MainActivity.this, PvsPActivity.class);
                startActivity(intentPvsP);
            }
        });

        pvsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPvsC = new Intent(MainActivity.this, PvsCActivity.class);
                startActivity(intentPvsC);
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPvsC = new Intent(MainActivity.this, ReplayActivity.class);
                startActivity(intentPvsC);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    private void AnhXa() {
        pvsp    = (ImageButton) findViewById(R.id.pvsp);
        pvsc    = (ImageButton) findViewById(R.id.pvsc);
        replay  = (ImageButton) findViewById(R.id.replay);
        exit    = (ImageButton) findViewById(R.id.exit);
    }
}
