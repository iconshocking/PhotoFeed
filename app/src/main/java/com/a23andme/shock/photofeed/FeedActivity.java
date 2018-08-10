package com.a23andme.shock.photofeed;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.a23andme.shock.photofeed.Network.Response;

import java.util.List;

import static com.a23andme.shock.photofeed.LoginLogoutActivity.IS_LOGOUT_EXTRA;

public class FeedActivity extends AppCompatActivity implements PhotoView, SharedPreferencesWrapper {
    public static final int LOGIN_INTENT_REQUEST_CODE = 101;
    public static final String AUTH_TOKEN_EXTRA = "authToken";
    public static final int FEED_COLUMN_COUNT = 2;

    private PhotoPresenter presenter;
    private FeedAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.photos_feed);
        recyclerView.setLayoutManager(new GridLayoutManager(
                this, FEED_COLUMN_COUNT, LinearLayoutManager.VERTICAL, false));

        presenter = new PhotoPresenter(this, this);

        adapter = new FeedAdapter(presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                presenter.clearAuthToken();
                displayLogin(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void displayLogin(boolean logoutFirst) {
        Intent intent = new Intent(this, LoginLogoutActivity.class);
        intent.putExtra(IS_LOGOUT_EXTRA, logoutFirst);
        startActivityForResult(intent, LOGIN_INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String authToken = data.getStringExtra(AUTH_TOKEN_EXTRA);
                presenter.newAuthTokenReceived(authToken);
            }
        }
    }

    @Override
    public void likeChangedForPhoto(Response.Photo photo, boolean newLikedValue) {

    }

    @Override
    public void displayPhotosData(List<Response.Photo> photos) {
        adapter.addFeedItems(photos);
    }

    @Override
    public String getStringValueForKey(String key) {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(key, null);
    }

    @Override
    public void setStringValueForKey(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(key, value).apply();
    }
}
