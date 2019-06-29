CREATE TABLE `miaosha_user`
(
    `id`              bigint(20)   NOT NULL COMMENT '用户ID,手机号码',
    `nickname`        varchar(255) NOT NULL,
    `password`        varchar(32)  DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt)',
    `salt`            varchar(10)  DEFAULT NULL,
    `head`            varchar(128) DEFAULT NULL COMMENT '头像,云储存的ID',
    `register_date`   datetime     DEFAULT NULL COMMENT '注册时间',
    `last_login_date` datetime     DEFAULT NULL COMMENT '上次登录时间',
    `login_count`     int(11)      DEFAULT '0' COMMENT '登录次数',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `goods` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
                         `goods_name` varchar(16) DEFAULT NULL COMMENT '商品名称',
                         `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
                         `goods_img` varchar(64) DEFAULT NULL COMMENT '商品图片',
                         `goods_detail` longtext COMMENT '商品详细介绍',
                         `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
                         `goods_stock` int(11) DEFAULT '0' COMMENT '商品库存',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
INSERT INTO `goods` VALUES ('1', 'iphoneX', 'Apple iPhone X 64G 银色', '/img/iphonex.png', 'Aphone (A1865)', '5000.00', '100');
INSERT INTO `goods` VALUES ('2', '华为Meta 9', '华为Meta 9', '/img/meta9.png', 'Meta 9', '3200.00', '50');

CREATE TABLE `miaosha_goods` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `goods_id` bigint(20) DEFAULT NULL,
                                 `miaosha_price` decimal(10,2) DEFAULT '0.00',
                                 `stock_count` int(11) DEFAULT NULL,
                                 `start_date` datetime DEFAULT NULL,
                                 `end_date` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
INSERT INTO `miaosha_goods` VALUES ('1', '1', '0.01', '9', '2019-05-28 11:10:12', '2019-05-31 15:50:59');

CREATE TABLE `order_info` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `user_id` bigint(20) DEFAULT NULL,
                              `goods_id` bigint(20) DEFAULT NULL,
                              `delivery_addr_id` bigint(20) DEFAULT NULL,
                              `goods_name` varchar(16) DEFAULT NULL,
                              `goods_count` int(11) DEFAULT '0',
                              `goods_price` decimal(10,2) DEFAULT '0.00',
                              `order_channel` tinyint(4) DEFAULT '0',
                              `status` tinyint(4) DEFAULT '0',
                              `create_date` datetime DEFAULT NULL,
                              `pay_date` datetime DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `miaosha_order` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `user_id` bigint(20) DEFAULT NULL,
                                 `order_id` bigint(20) DEFAULT NULL,
                                 `goods_id` bigint(20) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `u_uid_gid` (`user_id`,`goods_id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

