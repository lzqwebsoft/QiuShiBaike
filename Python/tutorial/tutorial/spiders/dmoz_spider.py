# -*- coding: utf8 -*-

import scrapy

from tutorial.items import TutorialItem

class DmozSpider(scrapy.Spider):
    name = 'dmoz'  # 做为爬虫的名字，用于唯一标识
    allowed_domains = ['domz.org']
    # start_urls 作为爬虫开始起步收集信息的URL列表
    start_urls = [
        "http://www.dmoz.org/Computers/Programming/Languages/Python/Books/",
        "http://www.dmoz.org/Computers/Programming/Languages/Python/Resources/"
    ]

    def parse(self, response):
        for sel in response.xpath('//ul/li'):
            item = TutorialItem()
            item['title'] = sel.xpath('a/text()').extract()
            item['link'] = sel.xpath('a/@href').extract()
            item['desc'] = sel.xpath('text()').extract()
            yield item


