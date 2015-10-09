package soft.xiniu.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 字符串处理类
 */
public class StringUtils {
    private static final char[] numChars = {'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final String[] units = {"千", "百", "十", ""};// 个位
    private static final String[] bigUnits = {"万", "亿"};
    private static char numZero = '零';

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        return false;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean notEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 将字符串转换成字符串组数,按照指定的标记进行转换
     * @param value
     * @param tag
     * @return
     */
    public static String[] str2Arr(String value, String tag) {
        if (!isEmpty(value)) {
            return value.split(tag);
        }
        return null;
    }

    /**
     * 将一个字符串数组组合成一个以指定分割符分割的字符串
     * @param array
     * @param separator
     * @return
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, "", 0, array.length);
    }

    /**
     * 将一个字符串数组组合成一个以指定分割符分割的字符串
     * @param array
     * @param ch
     * @param separator
     * @return
     */
    public static String join(Object[] array, String ch, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, ch, 0, array.length);
    }

    /**
     * 将一个字符串数组的某一部分组合成一个以指定分割符分割的字符串
     * @param array
     * @param separator
     * @param ch
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String join(Object[] array, String separator, String ch, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        // 开始位置大于结束位置
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return "";
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(ch + array[i] + ch);
            }
        }
        return buf.toString();
    }

    /**
     * 将一个集合组合成以指定分割符分割的字符串
     */
    public static String join(Collection collection, String separator) {
        if (collection == null) {
            return "";
        }
        return join(collection.iterator(), separator);
    }

    /**
     * 返回字符串的实际字符数，Java采用默认Unicode编码的情况下
     * @param str
     * @return
     */
    public static int getCharCount(String str) {
        if (true == isEmpty(str)) {
            return 0;
        }

        return getCharCount(str, 0, str.length());
    }

    /**
     * 返回字符串的实际字符数，Java采用默认Unicode编码的情况下
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static int getCharCount(String str, int start, int end) {
        if (true == isEmpty(str)) {
            return 0;
        }

        return str.codePointCount(start, end);
    }

    /**
     * 返回从头开始固定个数字符的子字符串，以实际字符个数表示
     * @param str
     * @param charCount
     * @return
     */
    public static String getSubChars(String str, int charCount) {
        char[] cArr=str.toCharArray();

        int idx=0;
        for(int count=0;count<charCount;idx++, count++){
            if(Character.isHighSurrogate(cArr[idx]))idx++; //如果四字节, 多算一个char
        }

        return str.substring(0, idx);
    }

    /**
     * 将字符串进行UTF-8编码后的字符串
     *
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * 发生错误抛出UnsupportedEncodingException
     * @param str
     * @return
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 发生错误的话返回默认字符串defultReturn
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }

        return str;
    }

    public boolean isIp(String IP) {//判断是否是一个IP
        boolean b = false;
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255)
                if (Integer.parseInt(s[1]) < 255)
                    if (Integer.parseInt(s[2]) < 255)
                        if (Integer.parseInt(s[3]) < 255)
                            b = true;
        }
        return b;
    }

    /**
     * 根据迭代器，迭代的元素将组合成以指定分割符分割的字符串
     */
    public static String join(Iterator iterator, String separator) {

        // 空的迭代器，返回 null
        if (iterator == null) {
            return null;
        }
        // 空元素，返回 null
        if (!iterator.hasNext()) {
            return "";
        }

        Object first = iterator.next();
        // 只有一个元素
        if (!iterator.hasNext()) {
            if (first != null) {
                return first.toString();
            } else {
                return "";
            }
        }

        StringBuffer buf = new StringBuffer(256);
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }

        return buf.toString();
    }

    /**
     * 将集合元素转换成字符串："'qq','aa','cc'"
     * @param collection
     * @param separator
     * @return
     */
    public static String Conll2StringWithSingleGuotes(@SuppressWarnings("rawtypes") Collection collection, String separator) {
        if (collection == null) {
            return null;
        }
        Iterator iterator = collection.iterator();

        // 空的迭代器，返回 null
        if (iterator == null) {
            return null;
        }
        // 空元素，返回 null
        if (!iterator.hasNext()) {
            return "";
        }

        Object first = iterator.next();
        // 只有一个元素
        if (!iterator.hasNext()) {
            if (first != null) {
                return "'" + first.toString() + "'";
            } else {
                return "";
            }
        }

        StringBuffer buf = new StringBuffer(256);
        if (first != null) {
            buf.append("'" + first + "'");
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append("'" + obj + "'");
            }
        }

        return buf.toString();
    }

    public static String escapeUnicode(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescapeUnicode(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    private static final int HOURS_OF_DAY = 24;
    private static final int MINUTES_OF_HOUR = 60;
    private static final int SECONDS_OF_MINUTE = 60;

    private static long CalPassedDay(long totalSec) {
        long result = totalSec / (HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE);
        return result;
    }

    private static long CalPassedHour(long totalSec) {
        long result = (totalSec % (HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE)) / (MINUTES_OF_HOUR * SECONDS_OF_MINUTE);
        return result;
    }

    private static long CalPassedMin(long totalSec) {
        long result = ((totalSec % (HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE)) % (MINUTES_OF_HOUR * SECONDS_OF_MINUTE)) / SECONDS_OF_MINUTE;
        return result;
    }

    private static long CalPassedSec(long totalSec) {
        long result = ((totalSec % (HOURS_OF_DAY * MINUTES_OF_HOUR * SECONDS_OF_MINUTE)) % (MINUTES_OF_HOUR * SECONDS_OF_MINUTE)) % SECONDS_OF_MINUTE;
        return result;
    }

    /**
     * 根据传入的时间戳返回时间
     *
     * @return
     */
    public static String TimeStampToTime(final long creat_stmap, final long server_stamp) {
        String result = "";
        long passedTotalSeconds = server_stamp - creat_stmap;
        if (passedTotalSeconds <= 0) {
            result = "1秒钟前";
            return result;
        }
        long passedDay = CalPassedDay(passedTotalSeconds);
        long passedHour = CalPassedHour(passedTotalSeconds);
        long passedMin = CalPassedMin(passedTotalSeconds);
        long passedSec = CalPassedSec(passedTotalSeconds);
        if (passedDay > 0) {
            result = new Long(passedDay).toString() + "天前";
            return result;
        }
        if (passedHour > 0) {
            result = new Long(passedHour).toString() + "小时前";
            return result;
        }
        if (passedMin > 0) {
            result = new Long(passedMin).toString() + "分钟前";
            return result;
        }
        if (passedSec > 0) {
            result = new Long(passedSec).toString() + "秒前";
            return result;
        }
        return result;
    }

    /**
     * 对字符串进行MD5加密
     * @param s
     * @return
     */
    public static String md5(String s) {
        if (isEmpty(s)) {
            return "";
        }
        try {
            // Create MD5 Hash
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            // 把密文转换成十六进制的字符串形式
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return s;
    }

    public static String sha256(String orignal) {
        if(isEmpty(orignal)) {
            return orignal;
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(null != md) {
            byte[] oriBytes = orignal.getBytes();
            md.update(oriBytes);
            byte[] digestRes = md.digest();
            String digestStr = getDigestStr(digestRes);
            return digestStr;
        }

        return "";
    }

    private static String getDigestStr(byte[] origBytes) {
        String tempStr = null;
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < origBytes.length; i++) {
            tempStr = Integer.toHexString(origBytes[i] & 0xff);
            if (tempStr.length() == 1) {
                stb.append("0");
            }

            stb.append(tempStr);
        }

        return stb.toString();
    }

    /**
     * md5加密
     * @param bytes
     * @return
     */
    public static byte[] md5(byte[] bytes) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            byte messageDigest[] = digest.digest();
            return messageDigest;
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static byte[] hash256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] hash256(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }

    public static String removeEmoji(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        return string.trim().replaceAll("([\ue000-\ue5ff])", "");
    }

    /**
     * 一个字符串是否包含另一字符串
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean contains(String str1, String str2) {
        if (isEmpty(str1) && isEmpty(str2)) {
            return true;
        } else if (isEmpty(str1)) {
            return false;
        } else if (isEmpty(str2)) {
            return false;
        } else {
            return (str1).contains(str2) || (str2).contains((str1));
        }
    }

    /**
     * 一个字符串是否包含另一字符串，忽略大小写
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean containsIgnoreCase(String str1, String str2) {
        if (isEmpty(str1) && isEmpty(str2)) {
            return true;
        } else if (isEmpty(str1)) {
            return false;
        } else if (isEmpty(str2)) {
            return false;
        } else {
            str1 = str1.toLowerCase();
            str2 = str2.toLowerCase();
            return (str1).contains(str2) || (str2).contains((str1));
        }
    }

    /**
     * 格式化手机号码 186-8134-1780
     *
     * @param phoneNumber
     * @param split       指定的分隔符
     * @return
     */
    public static String formatPhoneNumber(CharSequence phoneNumber, String split) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); i++) {
            sb.append(phoneNumber.charAt(i));
            if (split.equals(String.valueOf(phoneNumber.charAt(i))) || i == phoneNumber.length() - 1) {
                continue;
            }

            if (i == 2) {
                sb.append(split);
            } else if ((i - 2) % 4 == 0) {
                sb.append(split);
            }
        }
        return sb.toString();
    }

    /**
     * 一个字符串是以另一字符串开头
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean startsWith(String str1, String str2) {
        if (isEmpty(str1) && isEmpty(str2)) {
            return true;
        } else if (isEmpty(str1)) {
            return false;
        } else if (isEmpty(str2)) {
            return false;
        } else {
            return str1.startsWith(str2) || str2.startsWith(str1);
        }
    }

    /**
     * 字符串替换，从头到尾查询一次，替换后的字符串不检查
     *
     * @param str    源字符串
     * @param oldStr 目标字符串
     * @param newStr 替换字符串
     * @return 替换后的字符串
     */
    public static String replaceAll(String str, String oldStr, String newStr) {
        int i = str.indexOf(oldStr);
        while (i != -1) {
            str = str.substring(0, i) + newStr + str.substring(i + oldStr.length());
            i = str.indexOf(oldStr, i + newStr.length());
        }
        return str;
    }

    /**
     * 将一位数字转换为一位中文数字,只支持0~10000;
     *
     * @return
     * @throws Exception
     */
    public static String numberKArab2CN(Integer num) throws Exception {
        if (num < 0 || num > 10000) {
            throw new Exception("数字超出支持范围");
        }

        char[] numChars = (num + "").toCharArray();

        String tempStr = "";

        int inc = units.length - numChars.length;

        for (int i = 0; i < numChars.length; i++) {
            if (numChars[i] != '0') {
                tempStr += numberCharArab2CN(numChars[i]) + units[i + inc];
            } else {
                tempStr += numberCharArab2CN(numChars[i]);
            }
        }

        // 将连续的零保留一个
        tempStr = tempStr.replaceAll(numZero + "+", numZero + "");

        // 去掉未位的零
        tempStr = tempStr.replaceAll(numZero + "$", "");

        return tempStr;

    }

    private static char numberCharArab2CN(char onlyArabNumber) {

        if (onlyArabNumber == '0') {
            return numZero;
        }

        if (onlyArabNumber > '0' && onlyArabNumber <= '9') {
            return numChars[onlyArabNumber - '0' - 1];
        }

        return onlyArabNumber;
    }

    /**
     * 将ascii编码的字符串转换为Unicode字符串
     * @param ascii
     * @return
     */
    public static String ASCII2Unicode(String ascii) {
        StringBuffer unicode = new StringBuffer(ascii);

        int code;
        for (int i = 0; i < ascii.length(); i++) {
            code = (int) ascii.charAt(i);

            if ((0xA1 <= code) && (code <= 0xFB))
                unicode.setCharAt(i, (char) (code + 0xD60));
        }

        return unicode.toString();
    }

    /**
     * 将Unicode编码转换为ascii编码
     * @param unicode
     * @return
     */
    public static String Unicode2ASCII(String unicode) {
        StringBuffer ascii = new StringBuffer(unicode);

        int code;
        for (int i = 0; i < unicode.length(); i++) {
            code = (int) unicode.charAt(i);

            if ((0xE01 <= code) && (code <= 0xE5B))
                ascii.setCharAt(i, (char) (code - 0xD60));
        }

        return ascii.toString();
    }


}
