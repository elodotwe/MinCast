package com.jacobarau.mincast.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jacobarau.mincast.R;
import com.jacobarau.mincast.model.PodcastModelFactory;

public class AddPodcastDialogFragment extends DialogFragment implements AddPodcastDialogView {
    private AddPodcastDialogPresenter presenter;
    private EditText url;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        presenter = new AddPodcastDialogPresenter(PodcastModelFactory.getPodcastModel(context.getApplicationContext()), this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.popup_add_podcast, null);

        url = view.findViewById(R.id.editPodcastURL);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.onAddPodcast(url.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddPodcastDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onShown();
    }

    @Override
    public void setURL(String url) {
        this.url.setText(url);
    }
}
