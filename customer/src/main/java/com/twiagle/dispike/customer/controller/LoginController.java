package com.twiagle.dispike.customer.controller;

import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.exception.GlobalException;
import com.twiagle.dispike.common.result.CodeMsg;
import com.twiagle.dispike.common.result.Result;
import com.twiagle.dispike.common.vo.LoginVo;
import com.twiagle.dispike.customer.service.SpikeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    SpikeUserService spikeUserService;


    @RequestMapping("login/to_login")
    public String toLogin() {
        return "login";
    }


    @RequestMapping("login/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());

        spikeUserService.login(response, loginVo);
        return Result.success(true);
    }

    @RequestMapping(value = "doRegister", method = RequestMethod.GET)
    public String doRegister() {
        log.info("doRegister()");
        return "register";
    }


    /**
     * 注册接口
     *
     * @param registerVo
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Result<CodeMsg> register(RegisterVo registerVo) {
        log.info("RegisterVo = " + registerVo);

        if (registerVo == null) {
            throw new GlobalException(CodeMsg.FILL_REGISTER_INFO);
        }

        CodeMsg codeMsg = spikeUserService.register(registerVo);

        return Result.success(codeMsg);
    }


    /**
     * rpc
     */
    @ResponseBody
    @GetMapping("/login/{userId}")
    public SpikeUser getById(@PathVariable long userId) {
        return spikeUserService.getById(userId);
    }



}
