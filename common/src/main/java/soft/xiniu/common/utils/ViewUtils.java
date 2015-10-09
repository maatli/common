package soft.xiniu.common.utils;

import android.text.Layout;
import android.widget.TextView;

/**
 * Created by libo on 15/9/20.
 */
public class ViewUtils {
    /**
     *  判断给定的TextView是否被截断显示
     * @param textView
     * @return
     */
    public static boolean checkTextViewEllipsed(TextView textView) {
        Layout l = textView.getLayout();
        if ( l != null){
            int lines = l.getLineCount();
            if ( lines > 0) {
                if ( l.getEllipsisCount(lines-1) > 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
