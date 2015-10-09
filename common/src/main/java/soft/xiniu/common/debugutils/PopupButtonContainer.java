package soft.xiniu.common.debugutils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by libo on 15/9/6.
 */
public class PopupButtonContainer {
    public static final boolean isDebug = true;

    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams wmParams = null;
    private Context mContext;

    private ButtonContainerView mContainerView;

    public PopupButtonContainer(Context context) {
        mContext = context;

        if (mContext == null) {
            return;
        }

        if (true == isDebug) {
            initFloatImage();
            createFloatView();
        }
    }

    public void show() {

    }

    private void initFloatImage() {
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        wmParams = new WindowManager.LayoutParams();

        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置Window flag
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 100;
        wmParams.y = 200;

        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void createFloatView() {
        mContainerView = new ButtonContainerView(mContext);

        // 调整悬浮窗口
        wmParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        // 显示myFloatView图像
        mWindowManager.addView(mContainerView, wmParams);
    }

    public PopupButtonContainer addButton(String title, View.OnClickListener onClickListener) {
        if (mContext != null && mContainerView != null) {
            mContainerView.addButton(title, onClickListener);
        }

        return this;
    }
}
