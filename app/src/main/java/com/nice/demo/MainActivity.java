package com.nice.demo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nice.debugutils.PopupButtonContainer;
import com.nice.demo.stage.UserDataProviderListener;
import com.nice.demo.stage.bean.User;
import com.nice.demo.stage.http.UserDataProvider;


public class MainActivity extends ActionBarActivity {

    private UserDataProviderListener listener = new UserDataProviderListener() {
        @Override
        public void onLocalDataLoaded(User user) {
        }

        @Override
        public void onRemoteDataLoaded(User user) {
        }

        @Override
        public void onRemoteError(String err) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new PopupButtonContainer(this).addButton("测试", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            UserDataProvider userDataProvider = new UserDataProvider();
                                            userDataProvider.setDataListener(listener);
                                            userDataProvider.loadUserInfo();
                                        }
                                    })
                                    .show();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
