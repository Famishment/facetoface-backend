package com.lzj.facetoface.service;

import com.lzj.facetoface.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.facetoface.model.domain.User;
import com.lzj.facetoface.model.dto.TeamQuery;
import com.lzj.facetoface.model.request.TeamJoinRequest;
import com.lzj.facetoface.model.request.TeamQuitRequest;
import com.lzj.facetoface.model.request.TeamUpdateRequest;
import com.lzj.facetoface.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 14397
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2023-07-14 09:45:58
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除（解散）队伍
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long id, User loginUser);
}
