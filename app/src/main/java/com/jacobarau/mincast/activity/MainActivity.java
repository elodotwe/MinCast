package com.jacobarau.mincast.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jacobarau.mincast.R;
import com.jacobarau.mincast.model.PodcastModelFactory;
import com.jacobarau.mincast.subscription.Subscription;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements MainView {
    private MainPresenter presenter;
    private ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.add_podcast:
                presenter.onAddPodcastSelected();
                break;
            case R.id.update_podcasts:
                presenter.onUpdatePodcastsSelected();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, PodcastModelFactory.getPodcastModel(getApplicationContext()));
        listView = findViewById(R.id.podcasts_listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.main_activity_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.unsubscribe:
                        SparseBooleanArray positions = listView.getCheckedItemPositions();
                        List<Subscription> toDelete = new ArrayList<>(positions.size());
                        for (int i = 0; i < positions.size(); i++) {
                            if (positions.valueAt(i)) {
                                toDelete.add((Subscription)listView.getItemAtPosition(positions.keyAt(i)));
                            }
                        }
                        presenter.onUnsubscribe(toDelete);
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onSubscriptionListChanged(final List<Subscription> subscriptions) {
        //TODO: maybe don't reinstantiate the adapter every time--notify of changes via an existing one instead?
        //TODO: what will this do to the UX when the user adds/deletes a podcast?
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return subscriptions.size();
            }

            @Override
            public Object getItem(int position) {
                return subscriptions.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.podcasts_listview_item, parent, false);
                }
                TextView title = convertView.findViewById(R.id.podcast_title);
                title.setText(subscriptions.get(position).getTitle());
                return convertView;
            }
        });
    }

    public void showAddPodcastDialog() {
        AddPodcastDialogFragment fragment = new AddPodcastDialogFragment();
        fragment.show(getFragmentManager(), "addPodcast");
    }
}
