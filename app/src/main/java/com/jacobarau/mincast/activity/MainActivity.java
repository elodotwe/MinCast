package com.jacobarau.mincast.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jacobarau.mincast.R;
import com.jacobarau.mincast.db.PodcastDatabaseFactory;
import com.jacobarau.mincast.model.PodcastModel;
import com.jacobarau.mincast.subscription.Subscription;

import java.util.List;

public class MainActivity extends Activity implements MainView {
    private final String TAG = "MainActivity";
    private MainPresenter presenter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_podcast) {
            presenter.onAddPodcastSelected();
        }
//        if (id == R.id.action_add_podcast) {
//
//            return true
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, new PodcastModel(PodcastDatabaseFactory.getPodcastDatabase(getApplicationContext())));
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
        ListView listView = findViewById(R.id.podcasts_listview);
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
