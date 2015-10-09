package soft.xiniu.common.debugutils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by libo on 15/9/6.
 */
public class ButtonContainerView extends LinearLayout {

    public ButtonContainerView(Context context) {
        this(context, null);
    }

    public ButtonContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(HORIZONTAL);
    }

    public void addButton(String title, OnClickListener clickListener) {
        Button button = new Button(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        button.setText(title);
        button.setTextSize(18);
        button.setTextColor(0xFF000000);
        button.setOnClickListener(clickListener);

        this.addView(button, params);
    }
}
