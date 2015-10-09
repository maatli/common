package com.nice.demo.stage;

import com.nice.demo.stage.bean.User;
import com.nice.model.BaseDataProvider;
import com.nice.model.DataProviderListener;

/**
 * Created by libo on 15/9/25.
 */
public class UserDataProviderListener extends DataProviderListener<User> {

    @Override
    public void onLocalDataLoaded(User user) {

    }

    @Override
    public void onRemoteDataLoaded(User user) {

    }

    @Override
    public void onRemoteError(String err) {

    }
}
