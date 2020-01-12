package com.galihpw.weatherapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class WelcomeActivity extends Activity {

    private ConstraintLayout llBackgroundWelcome;
    private EditText etNama, etKodePos;
    private Button bSubmit;
    private String nama, kodePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        llBackgroundWelcome = findViewById(R.id.llBackgroundWelcome);
        etNama = findViewById(R.id.etNama);
        etKodePos = findViewById(R.id.etKodePos);
        bSubmit = findViewById(R.id.bSubmit);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 4){
            llBackgroundWelcome.setBackgroundResource(R.drawable.bg_malam);
        }else if(timeOfDay >= 4 && timeOfDay < 10){
            llBackgroundWelcome.setBackgroundResource(R.drawable.bg_pagi);
        }else if(timeOfDay >= 10 && timeOfDay < 14){
            llBackgroundWelcome.setBackgroundResource(R.drawable.bg_siang);
        }else if(timeOfDay >= 14 && timeOfDay < 18){
            llBackgroundWelcome.setBackgroundResource(R.drawable.bg_sore);
        }else if(timeOfDay >= 18 && timeOfDay < 24){
            llBackgroundWelcome.setBackgroundResource(R.drawable.bg_malam);
        }

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = etNama.getText().toString();
                kodePos = etKodePos.getText().toString();

                if(!nama.equals("") && !kodePos.equals("")) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("kodepos", kodePos);
                    startActivity(intent);
                    finish();
                }else{
                    if(nama.equals("")) {
                        Toast.makeText(WelcomeActivity.this, "Nama tidak boleh kosong ", Toast.LENGTH_SHORT).show();
                    }else if(kodePos.equals("")){
                        Toast.makeText(WelcomeActivity.this, "Kode Pos tidak boleh kosong ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
