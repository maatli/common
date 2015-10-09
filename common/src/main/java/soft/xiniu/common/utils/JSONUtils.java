package soft.xiniu.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    
    /**
     * 判断给定的字符串是否是有效的JSON字符串
     * @param str
     * @return
     */
	private boolean isValidJson(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}

		try {
			new JSONObject(str);			
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
}
