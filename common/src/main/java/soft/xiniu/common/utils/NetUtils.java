package soft.xiniu.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
    /**
     * 网络是否可用
     * @param applicationContext
     * @return
     */
    public static boolean isNetworkAvailable(Context applicationContext) {
        ConnectivityManager connectivity = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
}
