package com.lzj.facetoface.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.facetoface.common.BaseResponse;
import com.lzj.facetoface.common.ErrorCode;
import com.lzj.facetoface.common.ResultUtils;
import com.lzj.facetoface.exception.BusinessException;
import com.lzj.facetoface.model.domain.User;
import com.lzj.facetoface.model.request.UserLoginRequest;
import com.lzj.facetoface.model.request.UserRegisterRequest;
import com.lzj.facetoface.model.vo.UserVO;
import com.lzj.facetoface.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lzj.facetoface.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * @Auther: lzj
 * @Date: 2022/11/2-11-02-22:50
 * @description: com.lzj.facetoface.controller
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowCredentials = "true")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        //用户注册请求为空，则抛出自定义的错误码-参数错误
        if (userRegisterRequest == null) {
            //return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        //使用StringUtils第三方工具类中的判断是否为空的方法，如果为空，则返回自定义的返回工具类
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        return new BaseResponse<>(0,result,"ok");
        return ResultUtils.success(result);     //工具类.方法
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
//        return new BaseResponse<>(0,user,"ok");
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrent(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }

    // todo 推荐多个，未实现
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        // 如果有缓存，直接读缓存
        String redisKey = String.format("yupao:user:recommend:%s",loginUser.getId());
        Page<User> userPage = (Page<User>) redisTemplate.opsForValue().get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        /*List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());*/
        // 修改为分页，并放进redis缓存
        // 无缓存，查数据库
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 写缓存
        // 为什么用 try...catch ？
        // 答：哪怕缓存写入失败，也可以把数据库中查出来的值，返回给前端，对异常捕获一下，而不是交给全局异常处理器去抛（不然用户得到的信息是错误的）
        try {
            redisTemplate.opsForValue().set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS); // 30s,过期
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(userPage);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user,HttpServletRequest request) {
       // 1.校验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.校验权限
        User loginUser = userService.getLoginUser(request);
        // 3.触发更新
        int updateResult = userService.updateUser(user,loginUser);
        return ResultUtils.success(updateResult);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request){
        if(num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.matchUsers(num, loginUser));
    }
}
