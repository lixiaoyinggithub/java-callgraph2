package visual.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier
 * @date 2024/7/10 16:08
 */
public class FileTools {

    public static void writeToFile(String data, String path) {
        try {
            FileWriter fileWriter = new FileWriter(path, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.append(data);
            bufferedWriter.append("\n"); // 换行

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createFileName(String methodName) {
        String classMethod = StringUtils.substringAfterLast(StringUtils.substringBetween(methodName, ".", "("), ".");
        return classMethod.replace(":", "-") + ".md";
    }

}
