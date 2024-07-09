package visual.utils;

import cn.hutool.json.JSONUtil;
import visual.entity.Callee;
import visual.entity.Caller;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Xavier
 * @date 2024/7/3 17:19
 */
public class VisualStringTools {


    public static Caller parseCaller(String info) {
        String[] split = info.split(":");
        if (split.length != 2) {
            System.err.println("parseCaller error:" + info);
            return null;
        }
        Caller caller = new Caller();
        caller.setClassName(split[0]);
        caller.setFullName(info);
        caller.setMethodName(split[1]);
        return caller;
    }

    public static Callee parseCallee(String[] parts) {

        String method = parts[2];
        String returnType = parts[parts.length - 3];

        String[] split = method.split(":");
        if (split.length != 2) {
            System.err.println("parseCallee error:" + method);
            return null;
        }
        String invokeType = StringUtils.substringBetween(split[0], "(", ")");
        // 去除前面的括号
        String withoutTypeStr = method.replace("(" + invokeType + ")", "");

        // 抽取后面的参数
        String paramTypeInfo = StringUtils.substringBetween(withoutTypeStr, "(", ")");

        String[] withoutTypeParts = withoutTypeStr.split(":");
        if (withoutTypeParts.length != 2) {
            return null;
        }
        Callee callee = new Callee();
        callee.setClassName(withoutTypeParts[0]);
        callee.setFullName(withoutTypeStr);
        callee.setMethodName(withoutTypeParts[1]);
        callee.setCallType(invokeType);
        callee.setReturnValType(returnType);
        callee.setParamTypeInfo(paramTypeInfo);
        return callee;
    }

    public static String replaceBracket(String info) {
        return StringUtils.substringBefore(info, "(").replace(":<init>", ":init");
    }

    public static void main(String[] args) {
        String callerStr = "com.buz.dc.core.manager.popwindow.filter.FilterCondition:setComparisonOperator(java.lang.String)";
        String calleeStr = "(_ITF)com.buz.dc.core.manager.popwindow.filter.RegisterDayFilter:setComparisonOperator(java.lang.String)";
        Caller caller = parseCaller(callerStr);
//        Callee callee = parseCallee(calleeStr);

        System.out.println(JSONUtil.toJsonStr(caller));
//        System.out.println(JSONUtil.toJsonStr(callee));

//        System.out.println("noBracket:" + replaceBracket(callee.getFullName()));

        String line = "30327\tcom.buz.dc.core.infrastructure.redis.CommonRedisHandle:updateResendEmailCode(java.lang.String,com.buz.dc.core.entity.bo.login.EmailCodeParams)\t(INT)fm.lizhi.common.datastore.redis.client.RedisClient:setex(java.lang.String,int,java.lang.String)\t71\tvoid\tf\tjava.lang.String\t2\t-\n";
        String[] split = line.split("\t");

        System.out.println("part.len="+split.length);
        System.out.println("part.6="+split[6]);
        System.out.println("part.7="+split[7]);


    }

}
