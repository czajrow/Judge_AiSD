package tools;

import exceptions.InfoFileReadFailException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class InfoReader {

    public static final String INDEX = "index";
    public static final String ALIAS = "alias";
    public static final String EXECUTE_LINE = "executeLine";

    public static HashMap<String, String> readInfo(File directory) throws InfoFileReadFailException {

        String infoPath = directory.getPath() + File.separator + "info.txt";
        HashMap<String, String> infoMap = new HashMap<>();

        try (BufferedReader br =
                     new BufferedReader(new FileReader(new File(infoPath)))) {
            infoMap.put(INDEX, br.readLine());
            infoMap.put(ALIAS, br.readLine());
            infoMap.put(EXECUTE_LINE, br.readLine());
        } catch (IOException e) {
            throw new InfoFileReadFailException("Couldn't read info file in\"" + directory.getName() + "\" directory.");
        }
        if (infoMap.containsValue(null)) {
            throw new InfoFileReadFailException("Couldn't read info file in\"" + directory.getName() + "\" directory.");
        }
        return infoMap;
    }

}
