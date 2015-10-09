package soft.xiniu.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文件访问工具类
 */
public class FileUtils {

    // 文件扩展名分隔符
    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /* ===============================================文件读写========================================= */
    
    /**
     * 读取文件内容
     * @param file 文件对象
     * @param charsetName 文件字符编码
     * @return 如果文件不存在，返回null，否则返回文件内容
     * @throws 如果操作发生问题，抛出RuntimeException
     */
    public static String readFile(File file, String charsetName) {
        if (file == null || !file.isFile()) {
            return null;
        }
        
        StringBuilder fileContent = new StringBuilder("");
        BufferedReader reader = null;
        try {
            InputStreamReader is = null;
            // 传入charsetName为空的话，采用默认的编码方式
            if(false == StringUtils.isEmpty(charsetName)) {
                is = new InputStreamReader(new FileInputStream(file), charsetName);
            } else {
                is = new InputStreamReader(new FileInputStream(file));
            }

            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }

                fileContent.append(line);
            }
            reader.close();

            return fileContent.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * 读取文件内容
     * @param file 文件对象
     * @return 如果文件不存在，返回null，否则返回文件内容
     * @throws 如果操作发生问题，抛出RuntimeException
     */
    public static String readFile(File file) {
        return readFile(file, null);
    }
    
    /**
     * 向文件中写内容
     * @param file 文件对象
     * @param content 字符串内容
     * @param append 是否向文件尾部追加
     * @return 写入成功返回true，其他情况返回false
     * @throws 失败抛出运行时异常
     */
    public static boolean writeFile(File file, String content, boolean append) {
        if (file == null || !file.isFile()) {
            return false;
        }
        
        if (StringUtils.isEmpty(content)) {
            return false;
        }
        
        FileWriter fileWriter = null;
        try {
            makeDirs(file.getAbsolutePath());
            fileWriter = new FileWriter(file.getAbsolutePath(), append);
            fileWriter.write(content);
            fileWriter.close();
            
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * 向文件中写内容
     * @param file 文件对象
     * @param content 字符串内容
     * @return 写入成功返回true，其他情况返回false
     * @throws 失败抛出运行时异常
     */
    public static boolean writeFile(File file, String content) {
        return writeFile(file, content, false);
    }
    
    /**
     * 向文件中写内容
     * @param file 文件对象
     * @param contentList 字符串内容列表
     * @param append 是否是向文件追加内容
     * @return 写入成功返回true，其他情况返回false
     * @throws 失败抛出运行时异常
     */
    public static boolean writeFile(File file, List<String> contentList, boolean append) {
        if (file == null || !file.isFile()) {
            return false;
        }
        
        if ((contentList == null) || (contentList.size() <= 0)) {
            return false;
        }
        
        FileWriter fileWriter = null;
        try {
            makeDirs(file.getAbsolutePath());
            fileWriter = new FileWriter(file.getAbsolutePath(), append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * 向文件中写内容
     * @param file 文件对象
     * @param contentList 字符串内容列表
     * @return 写入成功返回true，其他情况返回false
     * @throws 失败抛出运行时异常
     */
    public static boolean writeFile(File file, List<String> contentList) {
        return writeFile(file, contentList, false);
    }
    
    /**
     * 向文件中写内容
     * @param file 文件对象
     * @param stream 输入流
     * @param append 是否写到文件末尾
     * @return 写入成功返回true，其他情况返回false
     * @throws 失败抛出运行时异常
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * 向文件中写内容
     * @param file 文件对象
     * @param stream 输入流
     * @return 写入成功返回true，其他情况返回false
     * @throws 失败抛出运行时异常
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }
    
    /* ===============================================文件操作========================================= */
    
    /**
     * 得到文件夹路径
     * 
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }
    
    /**
     * 根据文件路径创建文件夹，完整的路径
     * 
     * @param filePath
     * @return 创建成功或者文件夹已经存在返回true，其他情况返回false
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
    
    /**
     * 根据文件路径创建文件夹，完整的路径
     * 
     * @param filePath
     * @return 创建成功或者文件夹已经存在返回true，其他情况返回false
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }
    
    /**
     * 复制文件
     * @param sourceFilePath 源文件路径
     * @param destFilePath 目标文件路径
     * @return 复制成功返回true
     * @throws 失败抛出运行时异常
     */
    public static boolean copyFile(String destFilePath, String sourceFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        
        // TODO 没有判断给出的路径是文件还是文件夹
        return writeFile(StringUtils.isEmpty(destFilePath)?null:new File(destFilePath), inputStream);
    }
    
    // TODO 复制文件夹，删除文件夹
    
    /**
     * 获取不带扩展名的文件名
     * 
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     * 
     * @param filePath 文件路径
     * @return
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }
    
    /**
     * 获取文件名
     * 
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     * 
     * @param filePath 文件路径
     * @return
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param filePath 文件路径
     * @return
     */
    public static String getFileExtension(String filePath) {
        if ((filePath == null) || (filePath.trim().length() <= 0)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }
    
    /**
     * 判断文件是否存在
     * 
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if ((filePath == null) || (filePath.trim().length() <= 0)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }
    
    /**
     * 判断文件夹是否存在
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if ((directoryPath == null) || (directoryPath.trim().length() <= 0)) {
            return false;
        }

        File dire = new File(directoryPath);
        
        return (dire.exists() && dire.isDirectory());
    }
    
    /**
     * 删除文件
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if ((path == null) || (path.trim().length() <= 0)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        
        if (file.isFile()) {
            return file.delete();
        }
        
        if (!file.isDirectory()) {
            return false;
        }
        
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        
        return file.delete();
    }
    
    /**
     * 获取文件大小
     * 
     * @param path
     * @return 如果文件不存在返回-1
     */
    public static long getFileSize(String path) {
        if ((path == null) || (path.trim().length() <= 0)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
    
    /**
     * 获取文件夹文件列表
     * @param rootFolder
     * @param recursive
     * @return
     */
    public static List<File> getFileList(File rootFolder, boolean recursive) {
        return getFileList(rootFolder, "(.*)", recursive);
    }
    
    /**
     * 获取文件夹文件列表
     * @param rootFolder
     * @param recursive
     * @return
     */
    public static List<File> getFileList(String rootFolder, boolean recursive) {
        return getFileList(new File(rootFolder), "(.*)", recursive);
    }
    
    /**
     * 获取文件夹文件列表
     * @param rootFolder
     * @param regex
     * @param recursive
     * @return
     */
    public static List<File> getFileList(String rootFolder, String regex,boolean recursive) {
        return getFileList(new File(rootFolder), "(.*)", recursive);
    }
    
    /**
     * 获取文件夹文件列表
     * @param rootFolder
     * @param regex
     * @param recursive
     * @return
     */
    public static List<File> getFileList(File rootFolder, String regex, boolean recursive) {
        Pattern pattern = Pattern.compile(regex);
        
        return getFiles(rootFolder, pattern, recursive);
    }
    
    /**
     * 获取文件夹文件列表
     * @param rootFolder 根目录
     * @param regexPattern Pattern对象
     * @param recursive 是否递归
     * @return 返回文件列表
     */
    private static List<File> getFiles(File rootFolder, Pattern regexPattern, boolean recursive) {
        List<File> fileList = new ArrayList<File>();
        File[] files = rootFolder.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && recursive) {
                    fileList.addAll(getFiles(file, regexPattern, recursive));
                } else {
                    if (regexPattern.matcher(file.getAbsolutePath()).matches()) {
                        fileList.add(file);
                    }
                }
            }
        }
        
        return fileList;
    }
}
