package com.example.trafe.notetoself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    private NoteAdapter mNoteAdapter;
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;
    Animation mAnimFlash;
    Animation mFadeIn;

    public void createNewNote(Note n) {
        mNoteAdapter.addNote(n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNoteAdapter = new NoteAdapter();
        ListView listNote = (ListView) findViewById(R.id.listView);
        listNote.setAdapter(mNoteAdapter);

        //Handle clicks on the ListView
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int whichItem, long id) {

				/*
                    Create  a temporary Note
					Which is a reference to the Note
					that has just been clicked
				*/
                Note tempNote = mNoteAdapter.getItem(whichItem);

                // Create a new dialog window
                DialogShowNote dialog = new DialogShowNote();

                // Send in a reference to the note to be shown
                dialog.sendNoteSelected(tempNote);

                // Show the dialog window with the note in it
                dialog.show(getFragmentManager(), "");

            }
        });//End of listNote Block for clicks
    }//End of onCreate

    @Override
    protected void onResume() {
        super.onResume();

        mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);
        mSound = mPrefs.getBoolean("sound", true);
        mAnimOption = mPrefs.getInt("anim option", SettingsActivity.FAST);

        mAnimFlash = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flash);
        mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        //Set the rate of flash based on settings
        if(mAnimOption == SettingsActivity.FAST){

            mAnimFlash.setDuration(100);
            Log.i("anim = ", "" + mAnimOption);
        }else if(mAnimOption == SettingsActivity.SLOW){
            Log.i("anim = ", "" + mAnimOption);
            mAnimFlash.setDuration(1000);
        }

        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add) {
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(), "");
            return true;
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mNoteAdapter.saveNotes();
    }

    public class NoteAdapter extends BaseAdapter {
        List<Note> noteList = new ArrayList<Note>();
        private JSONSerializer mSerializer;

        public NoteAdapter() {
            mSerializer = new JSONSerializer("String", MainActivity.this.getApplicationContext());
            try {
                noteList = mSerializer.load();
            } catch (Exception e) {
                noteList = new ArrayList<Note>();
                Log.e("Error loading notes: ", "", e);
            }
        }

        public void saveNotes() {
            try {
                mSerializer.save(noteList);
            } catch (Exception e) {
                Log.e("Error Saving Notes", "", e);
            }
        }

        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Note getItem(int whichItem) {
            return noteList.get(whichItem);
        }


        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup) {
            if (view == null) {
                //First create a LayoutInflater
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Now instantiate view using inflater.inflate
                //using the listitem layout
                view = inflater.inflate(R.layout.listitem, viewGroup, false);
                //The false parameter is neccessary
                //because of the way that we want to use listitem
            }//End if

            //Grab a reference to all our TextView and ImageView widgets
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            ImageView ivImportant = (ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView ivTodo = (ImageView) view.findViewById(R.id.imageViewTodo);
            ImageView ivIdea = (ImageView) view.findViewById(R.id.imageViewIdea);

            //Hide any ImageView widgets that are not relevant
            Note tempNote = noteList.get(whichItem);

            if (tempNote.ismImportant() && mAnimOption != SettingsActivity.NONE){
                view.setAnimation(mAnimFlash);
            }else{
                view.setAnimation(mFadeIn);
            }

            if (!tempNote.ismImportant()) {
                ivImportant.setVisibility(View.GONE);
            }

            if (!tempNote.ismTodo()) {
                ivTodo.setVisibility(View.GONE);
            }

            if (!tempNote.ismIdea()) {
                ivIdea.setVisibility(View.GONE);
            }
            //Add this text to the heading and description
            txtTitle.setText(tempNote.getmTitle());
            txtDescription.setText((tempNote.getmDescription()));

            return view;
        }

        public void addNote(Note n) {
            noteList.add(n);
            notifyDataSetChanged();
        }
    }
}
