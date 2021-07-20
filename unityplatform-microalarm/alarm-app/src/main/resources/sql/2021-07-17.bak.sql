create table alarm_config_level
(
    level_no         varchar(64)   not null comment '等级编码'
        primary key,
    alarm_level      int           null comment '告警等级(1/2/3)',
    level_illustrate varchar(255)  null comment '告警说明(重要/一般/告知)',
    alarm_way        varchar(255)  null comment '告警方式(闪烁/弹窗)',
    mark             varchar(1024) null comment '备注'
)
    comment '告警等级表';

create table alarm_config_monitor
(
    monitor_no       varchar(64)   not null comment '测点编码'
        primary key,
    monitor_name     varchar(256)  null comment '测点名称',
    rule_no          varchar(64)   null comment '规则编码',
    monitor_type     int           null comment '测点类型(0遥信数据/1遥测数据)',
    model_type       int           null comment '每个告警页面会有不同的模块',
    model_name       varchar(64)   null comment '模块名称',
    group_type       int           null comment '分组类型(按照模块进行分组)',
    group_name       varchar(64)   null comment '分组名称',
    create_time      datetime      null comment '创建时间',
    update_time      datetime      null comment '更新时间',
    value_type       int           null comment '0number1bool',
    monitor_describe varchar(1024) null comment '测点描述',
    engineering_unit varchar(64)   null comment '告警描述单位需要用到此字段(如果不为空就拼接上该信息)'
)
    comment '测点';

create table alarm_config_rule
(
    rule_no             varchar(64)  not null comment '规则编码'
        primary key,
    version             int          not null,
    alarm_rule_name     varchar(255) not null comment '告警策略名称',
    alarm_type          varchar(64)  null comment '告警类型(运行事项/操作记录/系统事件)',
    alarm_value         varchar(255) null comment '告警规则值',
    pre_alarm_one_value varchar(255) null comment '一级预警规则值',
    pre_alarm_two_value varchar(255) null comment '二级预警规则值',
    level_no_alarm      varchar(64)  null comment '告警等级编码',
    level_no_one        varchar(64)  null comment '一级预警等级编码',
    level_no_two        varchar(64)  null comment '二级预警等级编码',
    create_time         datetime     null comment '创建时间',
    update_time         datetime     null comment '更新时间',
    delete_mark         int          null comment '0未删除1已删除',
    rule_type           int          null comment '规则类型(0遥信数据/1遥测数据)',
    group_type          int          null comment '分组类型'
)
    comment '规则表';

create table alarm_produce_info
(
    version         int            not null,
    info_no         varchar(64)    not null comment '告警信息编码'
        primary key,
    monitor_no      varchar(64)    null comment '测点编码',
    group_type      int            null comment '页面分组类型(同alarm_config_monitor#group_type)',
    rule_no         varchar(64)    null comment '规则编码',
    event_time      datetime       null comment '测点产生数据时间',
    process_time    datetime       null comment '测点处理时间',
    monitor_num     decimal(12, 5) null comment '测点值',
    monitor_num_str varchar(64)    null comment '测点值字符串',
    create_time     datetime       null comment '创建时间',
    with_history    int            null comment '0 非历史 1历史',
    pre_info_no     varchar(64)    null comment '上一个状态编码',
    next_info_no    varchar(64)    null comment '下一条状态编码',
    has_confirm     int            null comment '是否已确认(0未确认1已确认)',
    confirm_person  varchar(64)    null comment '确认人',
    confirm_time    datetime       null comment '确认时间',
    has_restore     int            null comment '是否已恢复(即告警是否已解除,0未解除1已解除)',
    restore_time    datetime       null comment '恢复时间',
    alarm_date      datetime       null comment '告警产生日期(同createTime)',
    alarm_timestamp timestamp      null comment '告警产生时间戳',
    row_stamp       bigint         null comment '行号(唯一不重复)'
)
    comment '告警信息';

create table fs_basic_parameter
(
    id             int          not null
        primary key,
    number         varchar(255) not null comment '风机厂家编号',
    manufacturer   varchar(255) not null comment '风机厂家名称',
    model          varchar(255) not null comment '风机型号',
    hub_height     varchar(255) null comment '轮毂高度',
    in_wind_speed  varchar(255) null comment '切入风速',
    out_wind_speed varchar(255) null comment '切出风速',
    create_time    datetime     null comment '创建时间',
    update_time    datetime     null comment '修改时间'
)
    comment '风机参数';

create table mp_backen_to_golden
(
    id          int auto_increment
        primary key,
    backendId   int          null comment '映射关系中mysql数据库表中编号',
    goldenId    int          null comment '映射关系中golden数据库表中id',
    number      int          null comment '风机编号',
    description varchar(255) null comment '描述信息',
    tagName     varchar(255) null comment '标签点名称'
)
    collate = utf8mb4_general_ci;

create table wf_analyse_cdq
(
    id           int auto_increment
        primary key,
    newest       int            null comment '当日最新日期(0是， 1否) ',
    calc_date    datetime       null comment '计算日期',
    avg_rmse     decimal(12, 5) null comment '均方根误差',
    avg_mae      decimal(12, 5) null comment '平均绝对误差',
    biggest_diff decimal(12, 5) null comment '最大预测误差',
    about_r      decimal(12, 5) null comment '相关系数',
    r1_ratio     decimal(12, 5) null comment '准确率',
    r2_ratio     decimal(12, 5) null comment '合格率'
)
    comment '超短期功率分析';

create table wf_analyse_dq
(
    id           int auto_increment
        primary key,
    calc_date    datetime       null comment '计算日期',
    avg_rmse     decimal(12, 5) null comment '均方根误差',
    avg_mae      decimal(12, 5) null comment '平均绝对误差',
    biggest_diff decimal(12, 5) null comment '最大预测误差',
    about_r      decimal(12, 5) null comment '相关系数',
    r1_ratio     decimal(12, 5) null comment '准确率',
    r2_ratio     decimal(12, 5) null comment '合格率',
    constraint wf_analyse_dq_calc_date_uindex
        unique (calc_date)
)
    comment '短期功率分析';

create table wf_assess_change
(
    id                  int auto_increment
        primary key,
    org_id              varchar(64)    null,
    day_ref_id          int            null comment '关联wf_assess_day#id',
    calc_date           datetime       null comment '计算日期',
    dq_hiatus           int            null comment '短期预测功率漏报次数',
    dq_ratio            decimal(12, 5) null comment '短期预测功率准确率（%）',
    dq_electric         decimal(12, 5) null comment '短期预测功率准确率考核电量（MWh）',
    dq_pay              decimal(12, 5) null comment '短期预测功率准确率考核费用（元）',
    cdq_hiatus          int            null comment '超短期预测功率漏报次数',
    cdq_ratio           decimal(12, 5) null comment '超短期预测功率准确率（%）',
    cdq_electric        decimal(12, 5) null comment '短期预测功率准确率考核电量（MWh）',
    cdq_pay             decimal(12, 5) null comment '超短期预测功率准确率考核费用（元）',
    exec_person         varchar(255)   null comment '执行人',
    guardian            varchar(255)   null comment '监护人',
    fix_reason          text           null comment '修正原因',
    fix_time            datetime       null comment '修正时间',
    create_time         datetime       null comment '创建日期',
    newest              int            null comment '是否最新(0 是 1否)',
    pre_id              int            null comment '之前修改的记录',
    day_assess_electric decimal(12, 5) null comment '日考核电量'
);

create table wf_assess_day
(
    id                  int auto_increment
        primary key,
    org_id              varchar(255)   null,
    version             int            not null,
    calc_date           datetime       null,
    dq_hiatus           int            null comment '短期预测功率漏报次数',
    dq_ratio            decimal(12, 5) null comment '短期预测功率准确率（%）',
    dq_electric         decimal(12, 5) null comment '短期预测功率准确率考核电量（MWh）',
    dq_pay              decimal(12, 5) null comment '短期预测功率准确率考核费用（元）',
    cdq_hiatus          int            null comment '超短期预测功率漏报次数',
    cdq_ratio           decimal(12, 5) null comment '短期预测功率准确率（%）',
    cdq_electric        decimal(12, 5) null comment '短期预测功率准确率考核电量（MWh）',
    cdq_pay             decimal(12, 5) null comment '超短期预测功率准确率考核费用（元）',
    create_time         datetime       null,
    update_time         datetime       null,
    day_assess_electric decimal(12, 5) null comment '日考核电量',
    constraint wf_assess_day_calc_date_uindex
        unique (calc_date)
)
    comment '日考核结果';

create table wf_assess_month
(
    id                    int auto_increment
        primary key,
    org_id                varchar(255)   null,
    version               int            not null comment '版本',
    calc_date             datetime       null comment '计算日期',
    auto_electric         decimal(12, 5) null comment '自动核算考核电量（MWh）',
    auto_pay              decimal(12, 5) null comment '自动核算考核费用（元）',
    fnl_electric          decimal(12, 5) null comment '最终修改后考核电量（MWh）',
    fnl_pay               decimal(12, 5) null comment '最终修改后考核费用（元）',
    schedule_electric     decimal(12, 5) null comment '调度考核电量（MWh）',
    schedule_pay          decimal(12, 5) null comment '调度考核费用（元）',
    contrast_electric     decimal(12, 5) null comment '考核电量对比结果（MWh）',
    contrast_pay          decimal(12, 5) null comment '考核费用对比结果（元）',
    fnl_contrast_electric decimal(12, 5) null comment '最终修改后-考核电量对比结果（MWh）',
    fnl_contrast_pay      decimal(12, 5) null comment '最终修改后-考核费用对比结果（元）',
    create_time           datetime       null comment '创建时间',
    update_time           datetime       null comment '修改时间'
)
    comment '月考核结果';

create table wf_basic_mapping
(
    id             int auto_increment
        primary key,
    org_id         varchar(255) null comment '组织id',
    station_name   varchar(255) not null comment '风场名称',
    station_number varchar(255) not null comment '风场编码',
    create_time    datetime     null comment '创建时间',
    constraint wf_basic_mapping_station_number_uindex
        unique (station_number)
)
    comment '编码映射';

create table wf_basic_parse_result
(
    id             int auto_increment
        primary key,
    version        int           null comment '版本(乐观锁)',
    org_id         varchar(255)  null comment '组织id',
    file_name      varchar(4096) null comment '文件名称',
    file_type      varchar(64)   null comment 'dq|cdq|nwp(短期预测|超短期预测|气象预测)',
    success_mark   int           null comment '0成功1失败2未解析',
    fail_reason    text          null comment '解析失败原因',
    file_suffix    varchar(255)  null comment '文件后缀',
    file_prefix    varchar(255)  null comment '文件前缀',
    wind_farm_name varchar(255)  null comment '风场名称',
    data_gen_date  datetime      null,
    constraint wf_basic_parse_result_file_type_data_gen_date_uindex
        unique (file_type, data_gen_date)
)
    comment '文件解析结果';

create table wf_data_capacity
(
    id                         int auto_increment
        primary key,
    org_id                     varchar(255)   null,
    event_date_time            datetime       null comment '事件事件',
    power_calc_capacity        decimal(12, 5) null comment '短期/超短期功率计算时使用的容量',
    check_calc_capacity        decimal(12, 5) null comment '考核结果计算时使用的容量',
    wind_platform_gen_electric decimal(12, 5) null comment '风电场发电量',
    create_time                datetime       null comment '创建时间',
    status                     varchar(64)    null comment '(数据获取状态)00全场成功, 11 全部失败，01功率计算成功, 10考核结果计算成功',
    constraint wf_data_capacity_event_date_time_uindex
        unique (event_date_time)
)
    comment '容量/风电场发电量';

create table wf_data_cdq
(
    id               int auto_increment
        primary key,
    org_id           varchar(255)   null comment '组织id',
    order_num        varchar(64)    null comment '顺序号',
    station_number   varchar(255)   null comment '统一编码',
    body_time        int            null comment 'body中的日期,值为序号',
    event_date_time  datetime       null comment '事件时间',
    forecast_produce decimal(12, 5) null comment '上报出力值',
    create_time      datetime       null comment '创建时间',
    sample_ids       varchar(255)   null comment '本机编号的字符串，以’,’分割',
    sample_cap       decimal(12, 5) null comment '本机装机容量',
    cap              decimal(12, 5) null comment '风场额定装机容量',
    running_cap      decimal(12, 5) null comment '风场实时开机容量',
    header_date      datetime       null comment '头部日期,格式: [yyyy-MM-dd HH:mm]',
    constraint wf_data_cdq_header_date_event_date_time_uindex
        unique (header_date, event_date_time)
)
    comment '超短期功率预测';

create index index_name
    on wf_data_cdq (event_date_time);

create table wf_data_cf
(
    id              int auto_increment
        primary key,
    org_id          varchar(255)   null comment '组织id',
    order_num       varchar(64)    null comment '顺序号',
    station_number  varchar(255)   null comment '统一编码',
    body_time       int            null comment '数据体数据,数值表示',
    event_date_time datetime       null comment '事件时间,根据数据体时间计算得出',
    wind_speed      decimal(12, 5) null comment '风速',
    high_level      decimal(12, 5) null comment '高层',
    wind_direction  decimal(12, 5) null comment '风向',
    temperature     decimal(12, 5) null comment '温度',
    humidity        decimal(12, 5) null comment '湿度',
    pressure        decimal(12, 5) null comment '气压',
    create_time     datetime       null comment '创建时间',
    coordinates     varchar(4096)  null comment '天气预报数据点的经纬度坐标',
    turbine_high    decimal(12, 5) null comment '风场风机轮毂高度',
    header_date     datetime       null comment '头部时间',
    status          int            null comment '获取状态(0获取成功, 1获取失败)',
    fetch_time      datetime       null comment '获取数据的时间',
    fail_msg        text           null comment '失败原因(status为1, 则有此字段有数据说明)',
    calc_power      decimal(12, 5) null comment '功率计算',
    constraint wf_data_cf_event_date_time_uindex
        unique (event_date_time)
)
    comment '实测气象';

create table wf_data_dq
(
    id               int auto_increment
        primary key,
    org_id           varchar(255)   null comment '组织id',
    order_num        varchar(64)    null comment '顺序',
    station_number   varchar(255)   null comment '统一编码',
    body_time        int            null comment 'body中的日期,值为序号',
    event_date_time  datetime       null comment '事件生成事件',
    forecast_produce decimal(12, 5) null comment '上报出力值',
    forecast_check   decimal(12, 5) null comment '上报检修预测',
    create_time      datetime       null comment '创建时间',
    sample_ids       varchar(255)   null comment '本机编号的字符串，以’,’分割',
    sample_cap       decimal(12, 5) null comment '本机装机容量',
    cap              decimal(12, 5) null comment '风场额定装机容量',
    header_date      datetime       null comment '头部日期,格式: [yyyy-MM-dd HH:mm:ss]',
    constraint wf_data_dq_header_date_event_date_time_uindex
        unique (header_date, event_date_time)
)
    comment '短期功率预测';

create index index_name
    on wf_data_dq (event_date_time);

create table wf_data_nwp
(
    id              int auto_increment
        primary key,
    org_id          varchar(255)   null comment '组织id',
    order_num       varchar(64)    null comment '顺序号',
    station_number  varchar(255)   null comment '统一编码',
    body_time       int            null comment '数据体数据,数值表示',
    event_date_time datetime       null comment '事件时间,根据数据体时间计算得出',
    wind_speed      decimal(12, 5) null comment '风速',
    high_level      decimal(12, 5) null comment '高层',
    wind_direction  decimal(12, 5) null comment '风向',
    temperature     decimal(12, 5) null comment '温度',
    humidity        decimal(12, 5) null comment '湿度',
    pressure        decimal(12, 5) null comment '气压',
    create_time     datetime       null comment '创建时间',
    coordinates     varchar(4096)  null comment '天气预报数据点的经纬度坐标',
    turbine_high    decimal(12, 5) null comment '风场风机轮毂高度',
    header_date     datetime       null comment '头部时间',
    calc_power      decimal(12, 5) null comment '计算功率',
    constraint header_date_event_date_time_high_level_uindex
        unique (header_date, event_date_time, high_level)
)
    comment '数值天气预报';

create index index_name
    on wf_data_nwp (event_date_time);

create table wf_data_zr
(
    id               int auto_increment
        primary key,
    org_id           varchar(255)   null comment '组织id',
    order_num        int            null comment '顺序号',
    station_number   varchar(255)   null comment '统一编码',
    event_date_time  datetime       null comment '事件事件',
    actual_produce   decimal(12, 5) null comment '实际出力值',
    machine_capacity decimal(12, 5) null comment '开机容量',
    create_time      datetime       null comment '创建时间',
    sample_ids       varchar(1024)  null comment '样本机编号的字符串，以’,’分割',
    sample_cap       decimal(12, 5) null comment '样本机装机容量',
    cap              decimal(12, 5) null comment '风场额定装机容量',
    body_time        time           null comment '数据体中的时间信息',
    row_raw_data     text           null,
    status           int            null comment '获取状态(0获取成功, 1获取失败)',
    fetch_time       datetime       null comment '获取数据的时间',
    fail_msg         text           null comment '失败原因(status为1, 则有此字段有数据说明)',
    constraint wf_data_zr_event_date_time_uindex
        unique (event_date_time)
)
    comment '实际功率接收';

create table wf_time_base
(
    id            int auto_increment
        primary key,
    time_ratio    int          null comment ' (1/5/15单位min)',
    date_time     datetime     null,
    date_time_str varchar(255) null,
    constraint wf_time_base_time_ratio_date_time_uindex
        unique (time_ratio, date_time)
)
    comment '时间基准表';

