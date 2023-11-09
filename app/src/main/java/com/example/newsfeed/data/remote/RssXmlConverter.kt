package com.example.newsfeed.data.remote

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RssFeed(
    @field:Element(name = "channel")
    var channel: Channel? = null
)

@Root(name = "channel", strict = false)
data class Channel(
    @field:ElementList(inline = true, entry = "item")
    var items: List<Item>? = null
)

@Root(name = "item", strict = false)
data class Item(
    @field:Element(name = "title")
    var title: String? = null,

    @field:ElementList(inline = true, entry = "link")
    var links: List<String>? = null,

    @field:Element(name = "pubDate")
    var pubDate: String? = null,

    @field:Namespace(reference = "http://search.yahoo.com/mrss/")
    @field:ElementList(inline = true, entry = "content", required = false)
    var imageUrlFromContent: List<ImageUrlFromContent>? = null,

    @field:Element(name = "enclosure", required = false)
    var imageUrlFromEnclosure: ImageUrlFromEnclosure? = null
)

@Root(name = "enclosure", strict = false)
class ImageUrlFromEnclosure {
    @field:Attribute(name = "url")
    var imageUrl: String? = null
}

@Root(name = "content", strict = false)
class ImageUrlFromContent {
    @field:Attribute(name = "url")
    var imageUrl: String? = null
}
