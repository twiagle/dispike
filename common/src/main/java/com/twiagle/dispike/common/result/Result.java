package com.twiagle.dispike.common.result;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * error, corresponding frontend ajax data.code!=0
     */
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }
    private Result(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }
    /**
     * success, frontend ajax data.code==0
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }
    private Result(T data) {
        this.data = data;
    }

}
