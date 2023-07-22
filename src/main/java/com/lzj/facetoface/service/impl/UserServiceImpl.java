package com.lzj.facetoface.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzj.facetoface.common.ErrorCode;
import com.lzj.facetoface.constant.UserConstant;
import com.lzj.facetoface.exception.BusinessException;
import com.lzj.facetoface.model.domain.User;
import com.lzj.facetoface.model.vo.UserVO;
import com.lzj.facetoface.service.UserService;
import com.lzj.facetoface.mapper.UserMapper;
import com.lzj.facetoface.utils.AlgorithmUtils;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lzj.facetoface.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 14397
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2022-11-02 18:15:04
*
* 用户服务实现类
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值：混淆密码
     */
    private static final String SALT = "lzj";  //加入与密码混淆进行加密

    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //账户长度，不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        //密码和校验码长度，不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        //星球编号长度，大于5位
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        //星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号重复");
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
                return null;
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        //3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setProfile(originUser.getProfile());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签列表搜索用户（内存过滤）
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {
        // 1.判断是否为空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 内存查询过程：
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 1.查询所有用户
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        // 2.在内存中判断是否包含有要求的标签
        /*for (User user: userList) {
            // 字符串转JSON，引入一个库
            String tagsStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>(){}.getType());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }*/
        return userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            if (StringUtils.isBlank(tagsStr)) {
                return false;
            }
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>(){}.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 用户修改
     * @param user
     * @param loginUser
     * @return
     */
    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 补充校验，如果用户没有传任何要更新的值，就直接报错，不用执行 update 语句！！！
        // 如果是管理员，允许更新任意用户
        // 如果不是管理员，只允许更新当前（自己的）信息
        if (isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User)userObj;
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;    //简写优化
    }
    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole() == UserConstant.ADMIN_ROLE;    //简写优化
    }

    @Override
    public List<User> matchUsers(long num, User loginUser) {
        // 过滤掉标签为空的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","tags"); // 只查 id、tags字段
        queryWrapper.isNotNull("tags");
        List<User> userList = this.list(queryWrapper);
        String tags = loginUser.getTags();
        // 将当前用户的tags字符串，转化为 json
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User,Long>> list = new ArrayList<>();
        // 依次计算所有用户和当前用户的相似度
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            // 将搜索的用户的tags字符串，转化为 json
            String userTags = user.getTags();
            // 空标签或者是当前用户自己（剔除自己不查询）
            if(StringUtils.isBlank(userTags) || user.getId().equals(loginUser.getId())){
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken< List<String>>() {}.getType());
            // 计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(user,distance));
        }
        // 按编辑距离由小到大排序
        List<Pair<User,Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        List<Long> userIdList = topUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        // 1, 3, 2 变成
        // 1, 2, 3 了
        userQueryWrapper.in("id", userIdList);
        Map<Long, List<User>> userIdUserListMap = this.list()
                .stream()
                .map(user -> getSafetyUser(user))
                .collect(Collectors.groupingBy(User::getId));
        List<User> finalUserList = new ArrayList<>();
        for (Long userId: userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        /*for (User user: userVOList) {
            System.out.println("用户：" + user.getTags());
        }
        List<Long> longList = topUserPairList.stream().map(pair -> pair.getValue()).collect(Collectors.toList());
        for (Long score: longList) {
            System.out.println("用户相似度：" + score);
        }*/
        return finalUserList;
    }

    /**
     * 根据标签列表搜索用户（SQL 查询版）
     * @param tagNameList
     * @return
     */
    @Deprecated   // 过期的注解，表示此方法过期
    private List<User> searchUsersBySQL(List<String> tagNameList) {
        // 1.判断是否为空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.创建查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 3.拼接 and 查询
        // like '%Java%' and like '%Python%'
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags",tagName);
        }
        // 4.userMapper的查询语法进行查询，查询的是列表
        List<User> userList = userMapper.selectList(queryWrapper);
        // 5.将遍历得到的用户对象，进行脱敏
        // userList.forEach(this::getSafetyUser); // lamba表达式，如果调用的函数的参数刚好是每次遍历的对象，则可简化。
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }
}




