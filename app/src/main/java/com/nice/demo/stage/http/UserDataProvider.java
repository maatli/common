package com.nice.demo.stage.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nice.demo.stage.bean.User;
import com.nice.model.BaseDataProvider;
import com.nice.model.DataProviderListener;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by libo on 15/9/25.
 */
public class UserDataProvider extends BaseDataProvider<User> {
    public UserDataProvider() {
        super();
    }

    public UserDataProvider(DataProviderListener listener) {
        super(listener);
    }

    public void loadUserInfo() {
        AsyncHttpClient httpClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("tag_id", "358206");
        params.put("tag_type", "undefined");
        params.put("tag_name", "润滑油");

        String url = "http://api.oneniceapp.com/tag/detailv2?sign=c0fdd77235f1e070105e6b4fbe282939&token=IUJ4rOrlkLUvJssjF_xkPXUavXJE2H_0&did=e21ea72d7b53c1475110d8316bf1b71d&osn=android&osv=4.3&appv=3.5.0&net=0-0-wifi&longitude=116.45362032574373&latitude=39.95380102383901&ch=abc36083&lp=1&ver=1&ts=1443182466294&im=864545028919230&sw=540&sh=960&la=zh-CN&seid=ad53242e02559346f92a294632879579&lm=&dt=YuLong+Coolpad+8729&dvi=753f3f2a559523f9ea6dba752c8c7e0c089700bb70c19a8bc122c8fe1e57559f";
        httpClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if(mDataListener != null) {
                    mDataListener.onLocalDataLoaded(new Object());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
