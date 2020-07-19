package com.twiagle.dispike.common.result;

public class CodeMsg {
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    private int code;
    private  String msg;
    private CodeMsg(int code, String msg) {
        this.code=code;
        this.msg = msg;
    }

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg USER_EXIST = new CodeMsg(500216, "用户已经存在，无需重复注册");
    public static CodeMsg REGISTER_SUCCESS = new CodeMsg(500217, "注册成功");
    public static CodeMsg REGISTER_FAIL = new CodeMsg(500218, "注册异常");
    public static CodeMsg FILL_REGISTER_INFO = new CodeMsg(500219, "请填写注册信息");
    public static CodeMsg WAIT_REGISTER_DONE = new CodeMsg(500220, "等待注册完成");

    //秒杀模块 5005XX
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");
    public static final CodeMsg ORDER_NOT_EXIST = new CodeMsg(500502, "订单不存在");

    public CodeMsg fillArgs(Object... args) {
        String message = String.format(msg, args);
        return new CodeMsg(code, message);
    }

}
