package soft.xiniu.common.utils;

import android.os.Build;

public class DroidUtils {
    /**
     * 是否是4.0或者以后的系统
     * 
     * @return
     */
    public static boolean isIcsVsersion() {
        return Build.VERSION.SDK_INT >= 14;
    }

    public static boolean isJBVsersion() {
        return Build.VERSION.SDK_INT >= 16;
    }
    
    
}
