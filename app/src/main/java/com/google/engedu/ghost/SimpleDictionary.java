package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            //Log.d("WORD",word);
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix == null)
        {
            Random rand = new Random();
            int index =rand.nextInt(words.size()) % words.size();

            return words.get(index);
        }

        String temp = searchWord(prefix,0,words.size());

        Log.d("PREFOUND","("+temp+")");
        return temp;
    }

    public String searchWord(String prefix,int start, int end)
    {
        int middle = (start+end)/2;
        if(end<start)
        {
            return null;
        }
        Log.d("PREFIX",prefix);


        int choice= words.get(middle).compareTo(prefix);

        Log.d("PREWORD",words.get(middle) + ""+choice);
        Log.d("PRETEST",words.get(middle).startsWith(prefix) +"");
        if(words.get(middle).startsWith(prefix))
        {
            Log.d("PREFOUND","YAY");
            return words.get(middle);
        }
        else if(choice<0)
        {
            return searchWord(prefix,middle+1,end);
        }
        else if(choice>0)
        {
            return searchWord(prefix,start,middle-1);
        }
        else
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
