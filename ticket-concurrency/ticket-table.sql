DROP TABLE IF EXISTS `ticket`;
DROP TABLE IF EXISTS `reservation`;

CREATE TABLE `ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_reserved` tinyint(1) NOT NULL DEFAULT '0',
  `reservation_status` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE `reservation` (
  `id` bigint(20) NOT NULL,
  `ticket_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=git s10 DEFAULT CHARSET=utf8;