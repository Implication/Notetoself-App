package com.example.trafe.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by TraFe on 11/16/2016.
 */

public class DialogShowNote extends DialogFragment {
    private Note mNote;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_show_note, null);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);

        txtTitle.setText(mNote.getmTitle());
        txtDescription.setText(mNote.getmDescription());

        ImageView ivImportant = (ImageView) dialogView.findViewById(R.id.imageViewImportant);
        ImageView ivTodo = (ImageView) dialogView.findViewById(R.id.imageViewTodo);
        ImageView ivIdea = (ImageView) dialogView.findViewById(R.id.imageViewIdea);

        if(!mNote.ismImportant()) {
            ivImportant.setVisibility(View.GONE);
        }
        if (!mNote.ismTodo()){
            ivTodo.setVisibility(View.GONE);
        }
        if(!mNote.ismIdea()){
            ivIdea.setVisibility(View.GONE);
        }

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);

        builder.setView(dialogView).setMessage("Your Note");

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    public void sendNoteSelected(Note noteSelected) {
        mNote = noteSelected;
    }
}
