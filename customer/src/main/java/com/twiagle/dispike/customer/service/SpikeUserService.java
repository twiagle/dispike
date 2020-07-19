package com.twiagle.dispike.customer.service;

import com.twiagle.dispike.common.entities.SpikeUser;
import com.twiagle.dispike.common.exception.GlobalException;
import com.twiagle.dispike.common.result.CodeMsg;
import com.twiagle.dispike.common.util.MD5Util;
import com.twiagle.dispike.common.util.UUIDUtil;
import com.twiagle.dispike.common.vo.LoginVo;
import com.twiagle.dispike.customer.controller.RegisterVo;
import com.twiagle.dispike.customer.dao.SpikeUserDao;
import com.twiagle.dispike.customer.redis.DLockApi;
import com.twiagle.dispike.customer.redis.RedisService;
import com.twiagle.dispike.customer.redis.SpikeUserPrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class SpikeUserService {
    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    SpikeUserDao spikeUserDao;
    @Autowired
    RedisService redisService;

    @Autowired
    private DLockApi dLock;

    public SpikeUser getById(long id) {
        SpikeUser user = redisService.get(SpikeUserPrefix.getById, "" + id, SpikeUser.class);
        if (user != null) return user;
        user = spikeUserDao.getByID(id);
        if (user != null) redisService.set(SpikeUserPrefix.getById, "" + id, user);
        return user;
    }

    public SpikeUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token))
            return null;

        SpikeUser spikeUser = redisService.get(SpikeUserPrefix.token, token, SpikeUser.class);
        if (spikeUser != null)
            reviseCookie(response, COOKIE_NAME_TOKEN, token, SpikeUserPrefix.token.expireSeconds());//update cookie expire
        return spikeUser;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {

        SpikeUser spikeUser = getById(Long.parseLong(loginVo.getPhoneNumber()));
        if (null == spikeUser) throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        String calculatedPassword = MD5Util.formPassToDBPass(loginVo.getPassword(), spikeUser.getSalt());
        if (!spikeUser.getPassword().equals(calculatedPassword)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        redisService.set(SpikeUserPrefix.token, token, spikeUser);
        reviseCookie(response, COOKIE_NAME_TOKEN, token, SpikeUserPrefix.token.expireSeconds());
        return token;
    }

    private void reviseCookie(HttpServletResponse response, String key, String value, int expiredSeconds) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public boolean updatePassword(String token, long id, String formPass) {//更新数据，如果数据使用缓存，一定要更新缓存
        //DB取user
        SpikeUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //先更新数据库
        SpikeUser toBeUpdate = new SpikeUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        spikeUserDao.update(toBeUpdate);
        //再处理缓存
        redisService.delete(SpikeUserPrefix.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SpikeUserPrefix.token, token, user);//更新修改的user信息，同时保持用户登录状态，使用同一token
        return true;
    }

    public CodeMsg register(RegisterVo userModel) {

        // 加锁
        String uniqueValue = UUIDUtil.uuid() + "-" + Thread.currentThread().getId();
        String lockKey = "redis-lock" + userModel.getPhone();
        boolean lock = dLock.lock(lockKey, uniqueValue, 60 * 1000);
        if (!lock)
            return CodeMsg.WAIT_REGISTER_DONE;
        log.debug("注册接口加锁成功");

        // 检查用户是否注册
        SpikeUser spikeUser = getById(userModel.getPhone());
        // 用户已经注册
        if (spikeUser != null) {
            dLock.unlock(lockKey, uniqueValue);
            return CodeMsg.USER_EXIST;
        }

        // 生成skuser对象
        SpikeUser newUser = new SpikeUser();

        newUser.setId(userModel.getPhone());
        newUser.setNickname(userModel.getNickname());
        newUser.setHead(userModel.getHead());

        newUser.setSalt(MD5Util.salt);

        String dbPass = MD5Util.formPassToDBPass(userModel.getPassword(), MD5Util.salt);
        newUser.setPassword(dbPass);

        // 写入数据库
        long id = spikeUserDao.insertUser(newUser);

        boolean unlock = dLock.unlock(lockKey, uniqueValue);
        if (!unlock)
            return CodeMsg.REGISTER_FAIL;
        log.debug("注册接口解锁成功");

        // 用户注册成功
        if (id > 0)
            return CodeMsg.SUCCESS;

        // 用户注册失败
        return CodeMsg.REGISTER_FAIL;
    }
}



























