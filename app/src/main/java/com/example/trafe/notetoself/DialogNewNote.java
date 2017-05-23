package com.example.trafe.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by TraFe on 11/16/2016.
 */

public class DialogNewNote extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_note, null);

        final EditText editTitle = (EditText) dialogView.findViewById(R.id.editTitle);
        final EditText editDescription = (EditText) dialogView.findViewById(R.id.editDescription);
        final CheckBox checkBoxIdea = (CheckBox) dialogView.findViewById(R.id.checkBoxIdea);
        final CheckBox checkBoxTodo = (CheckBox) dialogView.findViewById(R.id.checkBoxTodo);
        final CheckBox checkBoxImportant = (CheckBox) dialogView.findViewById(R.id.checkBoxImportant);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);

        builder.setView(dialogView).setMessage("Add a new note");
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Note newNote = new Note();
                newNote.setmTitle(editTitle.getText().toString());
                newNote.setmDescription(editDescription.getText().toString());
                newNote.setmIdea(checkBoxIdea.isChecked());
                newNote.setmTodo(checkBoxTodo.isChecked());
                newNote.setmImportant(checkBoxImportant.isChecked());

                MainActivity callingActivity = (MainActivity) getActivity();

                callingActivity.createNewNote(newNote);

                dismiss();
            }
        });

        return builder.create();
    }
}
