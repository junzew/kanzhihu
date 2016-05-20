package com.example.junze.kanzhihu.model;

import java.util.List;

/**
 * Created by junze on 16-05-13.
 */
public class ThemeListItem {
        /*
* {
"limit": 1000,
"subscribed": [ ],
"others": [
    {
        "color": 8307764,
        "thumbnail": "http://pic4.zhimg.com/2c38a96e84b5cc8331a901920a87ea71.jpg",
        "description": "内容由知乎用户推荐，海纳主题百万，趣味上天入地",
        "id": 12,
        "name": "用户推荐日报"
    },
    ...
]

thumbnail : 供显示的图片地址
description : 主题日报的介绍
id : 该主题日报的编号
name : 供显示的主题日报名称
}*/
        private String thumbnail;
        private String description;
        private int id;
        private String name;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
}
