create table user_account(
	account_id	int 		auto_increment primary key	comment '主键自增ID ',
	account_name	varchar(20)	not null unique comment '用户名',
	password	varchar(32)	not null comment '登录密码',
	status		enum('ENABLED','DISABLED') default 'ENABLED' comment '状态',
	trade_password	varchar(32) comment '交易密码',
	create_time	datetime	not null comment '创建时间',
	latest_login_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '上次登录时间',
	nick_name	varchar(20)		comment '昵称',
	user_photo	varchar(255)	comment '用户头像',
	max_login	int 	   default 2 not null	comment '最大登录人数',
	vip_level	int 	comment '会员等级'	
)engine=innodb default charset=utf8