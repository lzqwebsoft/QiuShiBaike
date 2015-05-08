# -*- coding:utf8 -*-
# Scrapy settings for tutorial project
#
# For simplicity, this file contains only the most important settings by
# default. All the other settings are documented here:
#
#     http://doc.scrapy.org/en/latest/topics/settings.html
#

BOT_NAME = 'tutorial'

SPIDER_MODULES = ['tutorial.spiders']
NEWSPIDER_MODULE = 'tutorial.spiders'

# Crawl responsibly by identifying yourself (and your website) on the user-agent
USER_AGENT = 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0'

ITEM_PIPELINES = {
     'tutorial.pipelines.QiuShiThumbPipeline': 200,
     'tutorial.pipelines.QiuShiHeaderPipeline': 100,
     'tutorial.pipelines.QiuShiPipeline': 300}
IMAGES_STORE = '/root/Images/QiuShiBaiKe'

DATABASE = {
     'host' : 'localhost',
     'port' : 3306,
     'user' : 'root',
     'password' : '123456',
     'database' : 'scrape',
     'raise_on_warnings': True,
}
