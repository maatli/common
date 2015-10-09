package soft.xiniu.common.utils;

import android.os.Environment;

public class SDUtils {
    /**
     * SD卡是否可用
     * @return
     */
    public static boolean isSdcardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    
    
}
