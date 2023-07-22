USE facetoface;

-- 用户表
create table user
(
    id           bigint auto_increment comment 'id' primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '用户账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    profile      varchar(512)                       null comment '个人简介',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '状态 0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色  -0 普通角色  -1 管理员',
    planetCode   varchar(512)                       null comment '星球编号',
    tags         varchar(512)                       null comment '标签列表Json'
) comment '用户表';

alter table user add COLUMN tags varchar(1024) null comment '标签列表Json';

-- 队伍表
create table team
(
    id           bigint auto_increment comment 'id' primary key,
    name     	 varchar(256)                   not null comment '队伍名称',
    description  varchar(1024)                      null comment '描述',
    maxNum     	 int      default 1             not null comment '最大人数',
    expireTime   datetime              				null comment '过期时间',
    userId       bigint 								 comment '用户id（队长id）',
    status   	 int      default 0             not null comment '状态 0 - 公开，1 - 私有，2 - 加密',
    password 	 varchar(512)                   	null comment '队伍密码',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '队伍表';

-- 用户队伍关系表
create table user_team
(
    id           bigint auto_increment comment 'id'      primary key,
    userId       bigint 								 comment '用户id',
    teamId       bigint 								 comment '队伍id',
    joinTime     datetime    					    null comment '加入时间',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '用户队伍关系表';

-- 标签表
create table tag
(
    id         bigint auto_increment comment 'id' primary key,
    tagName    varchar(256)                       null comment '标签名称',
    userId     bigint                             null comment '用户id',
    parentId   bigint                             null comment '父标签id',
    isParent   tinyint                            null comment '0 - 不是父标签，1 - 是父标签',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    constraint unique_tagName unique (tagName)
) comment '标签表';

create index idx_userId on tag (userId);

INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (1, 'admin', 'admin', 'https://img0.baidu.com/it/u=1691000662,1326044609&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1689440400&t=87c4d5905e2b4e0249fa3c756f94feff', 0, '山不在高，有仙则灵', '6aa034476ac13b8d1999ba872307f5d5', '17866662222', '123@qq.com', 0, '2023-07-13 23:53:53', '2023-07-13 23:53:53', 0, 0, '10927', '["Java","c++","Python","男"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (2, 'user', 'user', 'https://img2.baidu.com/it/u=3963117194,3292498392&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '鲁莽者要学会思考，善思者要克服犹豫', '6aa034476ac13b8d1999ba872307f5d5', '13744441111', '456@qq.com', 0, '2023-07-14 00:16:45', '2023-07-14 00:16:45', 0, 0, '1', '["PHP","Go","Python","女"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (3, 'hooker', 'hooker', 'https://img0.baidu.com/it/u=2320742784,2018536583&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 1, '采菊东篱下，悠然见南山', '6aa034476ac13b8d1999ba872307f5d5', '16697210321', '789@qq.com', 0, '2023-07-14 00:17:50', '2023-07-14 00:17:50', 0, 0, '2', '["Java","c++","Python","男"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (4, 'famishment', 'famishment', 'https://img2.baidu.com/it/u=3746710106,888580210&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 1, '有志者事竟成', '6aa034476ac13b8d1999ba872307f5d5', '17532325555', '321@qq.com', 0, '2023-07-14 00:18:24', '2023-07-14 00:18:24', 0, 0, '3', '["Jscript","Vue","Redis","女"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (5, 'balabala', 'balabala', 'https://img1.baidu.com/it/u=33841558,3217758637&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', 1, '自己的事，自己上心', '6aa034476ac13b8d1999ba872307f5d5', '19876531432', '987@qq.com', 0, '2023-07-14 00:19:10', '2023-07-14 00:19:10', 0, 0, '4', '["Java","Redis","React","女"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (6, '假用户', 'faker', 'https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '千里之行，始于足下', '12345678', '18755667123', '555@qq.com', 0, '2023-07-14 00:20:23', '2023-07-14 00:20:23', 0, 0, '101', '["Java","大一","c++","男"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (7, '假用户', 'faker', 'https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '有朋自远方来，不亦说乎', '12345678', '18755667123', '555@qq.com', 0, '2023-07-14 00:20:23', '2023-07-14 00:20:23', 0, 0, '101', '["Python","大二","Redis","女"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (8, '假用户', 'faker', 'https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '一言之美，贵于千金', '12345678', '18755667123', '555@qq.com', 0, '2023-07-14 00:20:23', '2023-07-14 00:20:23', 0, 0, '101', '["Java","大三","Vue","男"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (9, '假用户', 'faker', 'https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '在水一方', '12345678', '18755667123', '555@qq.com', 0, '2023-07-14 00:20:23', '2023-07-14 00:20:23', 0, 0, '101', '["PHP","大四","React","女"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (10, '假用户', 'faker', 'https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '在水一方', '12345678', '18755667123', '555@qq.com', 0, '2023-07-14 00:20:23', '2023-07-14 00:20:23', 0, 0, '101', '["Java","大一","Jscript","男","编程"]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (11, '假用户', 'faker', 'https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '在水一方', '12345678', '18755667123', '555@qq.com', 0, '2023-07-14 00:20:23', '2023-07-14 00:20:23', 0, 0, '101', '[]');
INSERT INTO facetoface.user (id, username, userAccount, avatarUrl, gender, profile, userPassword, phone, email, userStatus, createTime, updateTime, isDelete, userRole, planetCode, tags) VALUES (12, '假用户', 'faker', 'https://img2.baidu.com/it/u=2327794482,132256549&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 0, '在水一方', '12345678', '18755667123', '555@qq.com', 0, '2023-07-14 00:20:23', '2023-07-14 00:20:23', 0, 0, '101', '[]');