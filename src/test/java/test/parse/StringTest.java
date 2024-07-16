package test.parse;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Xavier
 * @date 2024/7/12 09:16
 */
public class StringTest {
    public static void main(String[] args) {
        String name = "com.buz.dc.core.service.group.GroupServiceImpl:kickOutGroup(com.buz.dc.core.dto.request.group.KickOutGroupParams)";
        String s = StringUtils.substringBetween(name, ".", "(");
        String s1 = StringUtils.substringAfterLast(s, ".");
        System.out.println(s);
        System.out.println(s1.replace(":","-")+".md");
        System.out.println();
    }
}
