package com.example.naseer.finalg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class HighScore extends Activity
{

    Button mainMenu;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ListView lv;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        tv1 = (TextView) findViewById(R.id.textViewHighScores);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        tv1.setText(pref.getString("score", "Not available"));

        lv = (ListView) findViewById(R.id.listView);

        List<String> your_array_list = new ArrayList<String>();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);



        String tempt = tv1.getText().toString();

        tempt = tempt.substring(2);

        String input[] = tempt.split(",");

        int tempLength = input.length/2;
        String temp[][] = new String[tempLength][2];

        int k=0;
        for(int i = 0; i< tempLength; i++){
            for(int j = 0; j<2 ; j++){
                System.out.println(i + "   " + j + "   " + k);
                temp[i][j] = input[k];
                k++;

            }
        }

        String temp2;
        for(int i=0; i < temp.length-1; i++){

            for(int j=0; j < temp.length-i-1 ; j++){
                if(Integer.parseInt(temp[j][1]) < Integer.parseInt(temp[j+1][1])){
                    temp2=temp[j+1][1];
                    temp[j+1][1] = temp[j][1];
                    temp[j][1] = temp2;

                    temp2=temp[j+1][0];
                    temp[j+1][0] = temp[j][0];
                    temp[j][0] = temp2;
                }
            }
        }

        String[] sortedArray = new String[input.length];
        k=0;
        for(int i = 0; i< sortedArray.length/2; i++){
            for(int j = 0; j<2 ; j++){
                sortedArray[k] = temp[i][j];
                k++;
            }

        }
        int j=1;
        if(sortedArray.length>=2) {
            for (int i = 0; i < sortedArray.length; i = i + 2) {
                if (j == 21) break;
                your_array_list.add(sortedArray[i] + " " + sortedArray[i + 1]);
                j++;
            }
        }



        arrayAdapter.notifyDataSetChanged();
        tv1.setText("Scores:");
        mainMenu = (Button) findViewById(R.id.buttonMainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScore.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
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
