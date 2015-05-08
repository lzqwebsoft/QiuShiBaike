# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

from scrapy.item import Item, Field

class TutorialItem(Item):
    title = Field()
    link = Field()
    desc = Field()

class QiuShiItem(Item):
    header = Field()
    author = Field()
    content = Field()
    thumb = Field()
    created_at = Field()
