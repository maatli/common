package soft.xiniu.common.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * LOG工具类
 *
 */
public class LogUtils {
    private final static String TAG = "Log4Android";

    private String tag = TAG;
    /* 日志前缀 */
    private String msgPrefix = "XINIU==**  ";
    /* 日志后缀 */
    private String msgPostfix = "";
    /* 保存日志的文件对象 */
    private File saveFile = null;
    /* 是否缓存日志内容 */
    private boolean cacheLog = false;
    /* 缓存的目标对象 */
    private ByteArrayOutputStream bos = null;
    private BufferedWriter writer = null;

    private static final boolean DEBUG_MODE = true;

    /* 是否为debug模式 */
    private boolean isDebug = DEBUG_MODE;

    public LogUtils closeDebug() {
        this.isDebug = false;

        return this;
    }

    public LogUtils openDebug() {
        this.isDebug = true;

        return this;
    }

    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 设置日志标签
     *
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 设置是否缓存打印消息
     *
     * @param cachLog
     */
    public void setCachLog(boolean cachLog) {
        this.cacheLog = cachLog;
    }

    /**
     * 设置日志消息前缀
     *
     * @param msgPrefix
     */
    public void setMsgPrefix(String msgPrefix) {
        this.msgPrefix = msgPrefix;
    }

    public String getMsgPrefix() {
        return msgPrefix;
    }

    /**
     * 设置日志消息后缀
     *
     * @param msgPostfix
     */
    public void setMsgPostfix(String msgPostfix) {
        this.msgPostfix = msgPostfix;
    }

    public String getMsgPostfix() {
        return msgPostfix;
    }

    /**
     * 设置保存日志消息的文件对象
     *
     * @param saveFile
     */
    public void saveLog(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * 获取保存日志消息的文件对象
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * 构建一个日志对象
     */
    public LogUtils() {
        isDebug = DEBUG_MODE;
    }

    /**
     * 构建一个日志对象
     *
     * @param tag
     *            日志标签
     */
    public LogUtils(String tag) {
        this();
        setTag(tag);

        isDebug = true;
    }


    public LogUtils(String tag, String msgPrefix) {
        this(tag);

        this.msgPrefix = msgPrefix+"\n";
    }

    public LogUtils(Object clazz) {
        this(clazz.getClass().getSimpleName());
    }

    /**
     * 构建一个日志对象
     *
     * @param tag
     *            日志标签
     * @param saveFile
     *            保存日志的文件对象
     */
    public LogUtils(String tag, File saveFile) {
        this(saveFile);

        setTag(tag);
    }

    /**
     * 构建一个日志对象
     *
     * @param saveFile
     *            保存日志的文件对象
     */
    public LogUtils(File saveFile) {
        this();
        if (saveFile == null) {
            return;
        }
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        // 传入的文件对象是一个目录，则自动在此目录下创建一个日志文件
        if (saveFile.isDirectory()) {
            String newFilePath = "auto_create_" + System.currentTimeMillis()
                    + ".log";
            printLog("Warning: saveFile is a directory, path = ‘"
                    + saveFile.getAbsolutePath() + "’. create a "
                    + "new file ‘" + newFilePath + "’");
            saveFile = new File(saveFile, newFilePath);
        }

        this.saveFile = saveFile;
    }

    /**
     * 构建一个日志对象
     *
     * @param tag
     *            日志标签
     * @param msgPrefix
     *            消息前缀
     * @param msgPostfix
     *            消息后缀
     */
    public LogUtils(String tag, String msgPrefix, String msgPostfix,
                       File saveFile) {
        this(saveFile);

        this.tag = tag;
        this.msgPostfix = msgPostfix;
        this.msgPrefix = msgPrefix;
    }

    public void i(Object log) {
        printLog(msgPrefix + (log == null ? "null" : log.toString())
                + msgPostfix, null, LOG_LEVEL.LOG_INFO);
    }

    public void r(Object error, Throwable tr) {
        printLog(msgPrefix + (error != null ? error.toString() : "null")+ msgPostfix, tr, LOG_LEVEL.LOG_ERROR);
    }

    public void e(Throwable tr) {
        r(tr != null ? tr.getMessage() : "null" + "", tr);
    }

    public void e(String msg, Throwable tr) {
        r(msg, tr);
    }

    public void d(Object debug) {
        printLog(msgPrefix + (debug != null ? debug.toString() : "null") + msgPostfix, null,
                LOG_LEVEL.LOG_DEBUG);
    }

    public void w(Object warn) {
        printLog(msgPrefix + (warn != null ? warn.toString() : "null") + msgPostfix, null,
                LOG_LEVEL.LOG_WARNING);
    }

    /**
     * 打印日志
     *
     * @param log
     *            日志内容
     * @param tr
     *            异常,只对 LOG_ERROR 级别的消息其作用，其它类型的消息，传入 null 即可
     * @param level
     *            日志级别
     */
    public void printLog(String log, Throwable tr, LOG_LEVEL level) {
        boolean saveLog = (saveFile != null);
        StringBuilder stringBuilder = null;
        if (saveLog || cacheLog) { // cacheLog 表示缓存日志内容
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM.dd HH:mm:ss");
            stringBuilder = new StringBuilder("=====================")
                    .append('\n').append(dateFormat.format(new Date()))
                    .append('\n').append(level.name() + "/" + tag + "\t")
                    .append(log).append('\n');

			/*
			 * 缓存日志需要缓存所有日志级别的日志内容
			 */
            if (cacheLog && level != LOG_LEVEL.LOG_ERROR) {
                stringBuilder.append(log);
            }
        }

        switch (level) {
            case LOG_DEBUG:
                if (isDebug)
                    Log.d(tag, log);
                if (saveLog) {
                    stringBuilder.append(log);
                }
                break;
            case LOG_ERROR:
                if (tr == null) {
                    Log.e(tag, log);
                    break;
                }
                if (saveLog || cacheLog) {
                    formatErrorStack(stringBuilder, tr);
                }
                if (isDebug)
                    Log.e(tag, log, tr);
                break;
            case LOG_INFO:
                if (isDebug)
                    Log.i(tag, log);
                break;
            case LOG_VERBOSE:
                if (isDebug)
                    Log.v(tag, log);
                break;
            case LOG_WARNING:
                if (isDebug)
                    Log.w(tag, log);
                if (saveLog) {
                    stringBuilder.append(log);
                }
                break;
            default:
                break;
        }

        if (saveLog || cacheLog) {
            stringBuilder.append("====================\n");
            if (saveLog) {
                saveLog(stringBuilder);
            }
            if (cacheLog) {
                if (bos == null) {
                    bos = new ByteArrayOutputStream();
                }
                try {
                    bos.write(stringBuilder.toString().getBytes());
                } catch (IOException e) {
                    r("cache log failed", e);
                }
            }
        }

    }

    /**
     * 保存日志内容，并自动输出附加信息，例如日志级别、标签、时间等等。
     * @param log 日志内容
     * @param tag 日志
     * @param level
     * @param saveFile
     */
    public static void saveLog(String log, String tag, LOG_LEVEL level, File saveFile) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat(
//				"yyyy-MM.dd HH:mm:ss.SSS");
//		StringBuilder stringBuilder = new StringBuilder("=====================")
//				.append('\n').append(dateFormat.format(new Date()))
//				.append('\n').append(level.name() + "/" + tag + "\t")
//				.append(log).append('\n');

        writeToFile(saveFile, log);
    }

    /**
     * 传入一个异常对象，保存异常内容到文件.自动输出附加信息，例如日志级别、标签、时间等等。
     *
     * @param tr 日志内容
     * @param tag 日志
     * @param saveFile 文件对象
     */
    public static void saveLog(Throwable tr, String tag, File saveFile) {
        StringBuilder stringBuilder = new StringBuilder();
        if(tr != null) {
            formatErrorStack(stringBuilder, tr);
        }
        saveLog(stringBuilder.toString(), tag, LOG_LEVEL.LOG_ERROR, saveFile);
    }

    /**
     * 保存异常内容到文件。自动输出附加信息，例如日志级别、标签、时间等等。
     *
     * @param sb 附带的日志内容
     * @param tr 异常对象，将读取详细的异常内容，保存到文件
     * @param tag 日志
     * @param saveFile 文件对象
     */
    public static void saveLog(StringBuilder sb, Throwable tr, String tag, File saveFile) {
        if(tr != null) {
            formatErrorStack(sb, tr);
        }
        saveLog(sb.toString(), tag, LOG_LEVEL.LOG_ERROR, saveFile);
    }

    /**
     * 写入日志内容到文件
     * @param saveFile 文件对象
     * @param log 日志内容
     */
    public static void writeToFile(File saveFile, CharSequence log) {
        Writer writer = null;
        try {
            writer = new FileWriter(saveFile, true);
            writer.append(log); // 不是覆盖
            writer.flush();
        } catch (Exception e) {
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
                writer = null;
            }
        }
    }

    public byte[] getCacheLogs() {
        if (bos == null) {
            return "NULL".getBytes();
        } else {
            return bos.toByteArray();
        }
    }

    /**
     * 关闭，释放资源
     */
    public void close() {
        try {
            if (bos != null) {
                bos.close();
            }
            saveFile = null;
            cacheLog = false;
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
        }
    }

    public String getTag() {
        return tag;
    }

    public static void formatErrorStack(Appendable err, Throwable tr) {
        formatErrorStack(err, "", tr);
    }

    public static void formatErrorStack(Appendable err, String indent, Throwable tr) {
        try {
            final Writer result = new StringWriter(512);
            final PrintWriter printWriter = new PrintWriter(result);
            Throwable cause = tr;
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            final String stacktraceAsString = result.toString();
            printWriter.close();
            err.append(stacktraceAsString);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    /**
     * 保存日志到日志文件
     *
     * @param log
     */
    private void saveLog(CharSequence log) {
        if (saveFile == null || !saveFile.exists()) {
            return;
        }
        try {
            if (writer == null) {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(saveFile)));
            }
            writer.append(log); // 不是覆盖
            writer.flush();
        } catch (Exception e) {
        }
    }

    /**
     * 打印日志，使用 INFO 日志级别，默认日志标签为 “Log4AndroidUtils”
     *
     * @param log
     *            日志内容
     */
    public static void printLog(String log) {
        if (DEBUG_MODE)
            Log.i(TAG, "XINIU==** " + log);
    }

    /**
     * 打印日志，使用指定日志级别
     *
     * @param tag
     *            日志标签
     * @param log
     *            日志内容
     */
    public static void printLog(String tag, String log) {
        if (DEBUG_MODE)
            Log.i(tag, log);
    }

    public enum LOG_LEVEL {
        LOG_INFO, LOG_DEBUG, LOG_ERROR, LOG_WARNING, LOG_VERBOSE
    }

    private static LogUtils testInstance = null;
    public static void test_momo(Object log) {
        if(testInstance == null) {
            testInstance = new LogUtils("xiniu");
        }
        testInstance.i(log);
    }

    public static LogUtils getInstance() {
        if(testInstance == null) {
            testInstance = new LogUtils("xiniu");
        }

        return testInstance;
    }

    public void longInfo(String str) {
        if(str.length() > 4000) {
            i(str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            i(str);
    }
}
