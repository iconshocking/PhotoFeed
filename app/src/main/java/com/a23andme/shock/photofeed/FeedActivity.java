package com.a23andme.shock.photofeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.a23andme.shock.photofeed.Network.ApiRequester;
import com.a23andme.shock.photofeed.Network.NetworkApi;
import com.a23andme.shock.photofeed.Network.NetworkService;
import com.a23andme.shock.photofeed.Network.Response;

import java.util.List;

public class FeedActivity extends AppCompatActivity implements PhotoView {
    public static final int LOGIN_INTENT_REQUEST_CODE = 101;
    public static final String LOGIN_INTENT_AUTH_TOKEN_EXTRA = "authToken";
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

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String authToken = data.getStringExtra(LOGIN_INTENT_AUTH_TOKEN_EXTRA);
                if (!TextUtils.isEmpty(authToken)) {
                    NetworkService.createApiService(NetworkApi.class, authToken);
                    ApiRequester apiRequester = NetworkService.getInstance();
                    presenter = new PhotoPresenter(this, apiRequester);
                    adapter = new FeedAdapter(presenter);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
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
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void likeClicked(Response.Photo photo) {

    }

    @Override
    public void displayPhotoData(List<Response.Photo> photos) {
        adapter.addFeedItems(photos);
    }
}
