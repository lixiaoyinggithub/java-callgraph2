package visual.entity;

/**
 * @author Xavier
 * @date 2024/7/3 16:52
 */
public class Callee {
    private String className;
    private String methodName;
    private String fullName;
    private String callType;
    private String returnValType;
    private String paramTypeInfo;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getReturnValType() {
        return returnValType;
    }

    public void setReturnValType(String returnValType) {
        this.returnValType = returnValType;
    }

    public String getParamTypeInfo() {
        return paramTypeInfo;
    }

    public void setParamTypeInfo(String paramTypeInfo) {
        this.paramTypeInfo = paramTypeInfo;
    }
}
