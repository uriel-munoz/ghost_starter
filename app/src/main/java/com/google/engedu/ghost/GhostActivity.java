package com.google.engedu.ghost;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends Activity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssetManager asset = getAssets();
        try{
            InputStream inputStream = asset.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        }
        catch(IOException e){
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();

        }
        Button restartButton = (Button) findViewById(R.id.restart);


        setContentView(R.layout.activity_ghost);
        onStart(null);

    }


        public void onClick(View v){

            Log.d("PRE",v.getId()+"");
            if(v.getId()==R.id.restart)
            Log.d("PRE2","Restart Clicked");
            switch(v.getId())
            {
                case R.id.challenge:
                    Log.d("PRECHALLENGE","YES");
                    challengeWord();
                    break;
                case R.id.restart:
                    Log.d("PRERESTART","YES");
                    onStart(v);
                    break;
            }
        }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        //if key not letter return super.onKeyUp()
        char pressedKey = (char) event.getUnicodeChar();
        Log.d("Char", "" + pressedKey);
        if(Character.isLetter(pressedKey))
        {
            //add letter to word fragment
            //if check if fragment is word
            //update game status label
            TextView fragment = (TextView)findViewById(R.id.ghostText);
            fragment.append(pressedKey+"");
            Log.d("GHOST WORD","("+ fragment.getText().toString()+")");
            Log.d("GHOST FOUND", "("+ fragment.getText().toString()+")"+dictionary.isWord(fragment.getText().toString()));
            if(dictionary.isWord(fragment.getText().toString()))
            {
                TextView status = (TextView)findViewById(R.id.gameStatus);
                status.setText("Game Over");
            }
            userTurn=false;
            computerTurn();
            return true;
        }
        else{
            return super.onKeyUp(keyCode,event);
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView word = (TextView) findViewById(R.id.ghostText);
        // Do computer turn stuff then make it the user's turn again
        String fragment = word.getText().toString();
        String temp = dictionary.getAnyWordStartingWith(fragment);
        Log.d("W:","("+temp+")");
        if(dictionary.isWord(fragment) ||  temp==null)
        {
            label.setText("Computer WINS");

        }
        else
        {
            word.setText(temp.substring(0,word.length()+1));
            userTurn = true;
            label.setText(USER_TURN);
        }

    }



    public void challengeWord()
    {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView word = (TextView) findViewById(R.id.ghostText);
        String fragment = word.getText().toString();
        String winner ="";
        Log.d("Test", "TEMP");
        String possible = dictionary.getAnyWordStartingWith(fragment);
        if(fragment.length()>4 && dictionary.isWord(fragment) || possible.equals(null))
        {
           winner="User WINS!!";
        }
        else
        {
            winner="Computer WINS!!";
            word.setText(possible);
        }
        label.setText(winner);



    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);

        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }


}
