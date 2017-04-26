# -*- coding:utf8 -*-
# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import scrapy
from tutorial.items import QiuShiItem
from scrapy.contrib.pipeline.images import ImagesPipeline
from scrapy.exceptions import DropItem
from tutorial.models import db_connect, create_qiushi_table, insert_into_db 

class TutorialPipeline(object):
    def process_item(self, item, spider):
        return item

# 百科附件图片处理管道
class QiuShiThumbPipeline(ImagesPipeline):
    
     def get_media_requests(self, item, info):
         for thumb in item['thumb']:
             yield scrapy.Request('http:' + thumb)

     def item_completed(self, results, item, info):
         image_paths = [x['path'] for status, x in results if status]
         if not image_paths:
            item['thumb'] = ''
         item['thumb'] = image_paths
         return item

# 百科用户头像处理管道
class QiuShiHeaderPipeline(ImagesPipeline):
    
     def get_media_requests(self, item, info):
         for header in item['header']:
             yield scrapy.Request('http:' + header)

     def item_completed(self, results, item, info):
         image_paths = [x['path'] for status, x in results if status]
         if not image_paths:
            item['header'] = ''
         item['header'] = image_paths
         return item

# 百科内容存储在数据库与文件中管道
class QiuShiPipeline(object):
    def __init__(self):
        self.f = open("QiuShiBaike.txt", 'w+')
        self.connect = db_connect()
        cursor = self.connect.cursor()
        create_qiushi_table(cursor)

    def process_item(self, item, spider):
        # 将获取的内容写入文件
        header =  "" 
        if type(item['header']) == list and len(item['header']) > 0 :
            header = item['header'][0]
        thumb = ""
        if type(item['thumb']) == list and len(item['thumb']) > 0:
            thumb = item['thumb'][0]
        author = ""
        if item['author']:
            author = item['author']
        content = ""
        if item['content']:
            content = "<br />".join([x.strip() for x in item['content'] if x.strip()])
        self.f.write((author + "\r\n" + header + "\r\n" + content + "\r\n" + thumb + "\r\n" + item['created_at']  + "\r\n").encode('UTF-8'))
        self.f.write("="*30+"\r\n")
        # 保存数据库数据
        datas = {
            "header": header,
            "author": author,
            "content": content,
            "created_at": item['created_at'],
            "thumb": thumb
        }
        try:
            cursor = self.connect.cursor()
            insert_into_db(cursor, datas)
            self.connect.commit()
        except:
            self.connect.rollback()
            raise
        finally:
            cursor.close()
            
        return item
