package com.example.naseer.finalg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

public class OptionsMenu extends Activity
{
    Button save, mainMenu;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText pname;
    String name="";
    public static boolean soundSetting = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        editor = pref.edit();

        pname = (EditText)findViewById(R.id.editText);

        name = pref.getString("playername", "Player 1");
        if(name.length()>0){
            pname.setText(name);
        }

        ToggleButton sound = (ToggleButton)findViewById(R.id.toggleButtonSound);

        sound.setChecked(soundSetting);

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked) {
                    soundSetting = true;
                }else {
                    soundSetting = false;
                }

            }
        });

        save = (Button) findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("playername", ""+pname.getText());
                editor.commit();
                Intent intent = new Intent(OptionsMenu.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });



        mainMenu = (Button) findViewById(R.id.buttonMainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something in response to button click
                Intent intent = new Intent(OptionsMenu.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
