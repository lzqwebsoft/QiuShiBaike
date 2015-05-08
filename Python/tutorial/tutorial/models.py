# -*- coding:utf8 -*-
import mysql.connector
import os
from mysql.connector import errorcode

import settings

def db_connect():
    """
    使用settings.py中的配置，创建数据库连接
    返回数据库连接对象
    """
    try:
        cnx = mysql.connector.connect(**settings.DATABASE)
        return cnx
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Someting is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exists")
        else:
            print(err)
        exit(1)

def create_qiushi_table(cursor):
    new_qiushi_table = (
        "DROP TABLE IF EXISTS `qiushi`;"
        "CREATE TABLE `qiushi` ("
        "  `id` int(11) NOT NULL AUTO_INCREMENT,"
        "  `author` varchar(255) NULL,"
        "  `header` mediumblob NULL,"
        "  `content` text NOT NULL,"
        "  `thumb` mediumblob NULL,"
        "  `created_at` datetime NOT NULL,"
        "  PRIMARY KEY (`id`)"
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8")
    try:
        cursor.execute(new_qiushi_table)
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_TABLE_EXISTS_ERROR:
            print("table qiushi already exists.")
        else:
            print(err.msg)
    else:
        print('create table qiushi successfully.')
        cursor.close()

def insert_into_db(cursor, datas):
    insert_sql = ("INSERT INTO qiushi "
              "(author, header, content, thumb, created_at) "
              "VALUES (%(author)s, %(header)s, %(content)s, %(thumb)s, %(created_at)s)")
    header_file = settings.IMAGES_STORE + "/" + datas['header']
    thumb_file = settings.IMAGES_STORE + "/" + datas['thumb']
    header_blod = None
    thumb_blod = None
    if os.path.isfile(header_file):
        f = open(header_file, "rb")
        header_blod = f.read()
        f.close()
    if os.path.isfile(thumb_file):
        f = open(thumb_file, "rb")
        thumb_blod = f.read()
        f.close()
    datas['header'] = header_blod
    datas['thumb'] = thumb_blod
    cursor.execute(insert_sql, datas)
