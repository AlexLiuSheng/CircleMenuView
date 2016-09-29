# CircleMenuView
CircleMenuView that have many custom functions

之前公司项目需求的一个自定义圆形菜单，现封装并开源出来，供大家学习，一个可以定制的原型菜单,直接上图:

![](https://github.com/AlexLiuSheng/CircleMenuView/blob/master/img/Screenshot_2016-09-29-10-26-15-762.png)
![](https://github.com/AlexLiuSheng/CircleMenuView/blob/master/img/Screenshot_2016-09-29-13-12-57-427_com.allenliu.c.png)
![](https://github.com/AlexLiuSheng/CircleMenuView/blob/master/img/Screenshot_2016-09-29-13-15-10-260_com.allenliu.c.png)
![](https://github.com/AlexLiuSheng/CircleMenuView/blob/master/img/Screenshot_2016-09-29-15-36-22-379_com.allenliu.c.png)
##如何使用
###导入

gradle 

    compile 'com.allenliu:CircleMenuView:1.0.0'(审核中...)

###使用
####xml使用
        <com.allenliu.circlemenuview.CircleMenuView
        android:id="@+id/view"
        android:layout_centerHorizontal="true"
        android:layout_width="400dp"
        android:padding="20dp"
        app:center_text="@string/app_name"  //中心显示文字
        app:center_icon="@mipmap/ic_launcher" //中心的图标
        app:menu_text="@array/text"   //周围盘块的数据源 数组
        app:menu_icon="@array/icon"   //周围的图标数据源 数组 注意文字和图标数组大小应该一致，并且数组大小最好为偶数
        app:menu_text_size="12sp"     
        app:menu_text_color="#f2f"
        app:stroke_color="@color/colorAccent"  //扫边的颜色 有默认值
        app:gap_color="@color/colorPrimary"    //缝隙颜色  有默认值
        app:stroke_width="2dp"                 //扫边宽度 有默认值
        app:menu_item_background="#f7f7"       //盘块背景色 有默认值
        app:gap_size="10dp"                   //缝隙大小  有默认值
        app:inside_cirle_radius="75dp"       //内圆半径 不填默认是外圆的三分之一
        android:layout_height="500dp" />    //宽高如果不一致 取小的
        
###代码直接实例化
         new CircleMenuView(this)
                .setWidthAndHeight(300, 300)
                .setCenterText()
                .setCenterIcon()
                .setGapColor()
                .setGapSize()
                .setMenuIcons()
                .setMenuTexts()
                .setMenuTextColor()
                .setMenuTextSize()
                .setMenuItemBackground()
                .setInsideCircleRadius()
                .setStrokeColor()
                .setStrokeWidth()
                .setOnClickListener();//设置每个盘块点击事件
                
##License
        Copyright 2015 Google, Inc.

        Licensed to the Apache Software Foundation (ASF) under one or more contributor
        license agreements. See the NOTICE file distributed with this work for
        additional information regarding copyright ownership. The ASF licenses this
        file to you under the Apache License, Version 2.0 (the "License"); you may not
        use this file except in compliance with the License. You may obtain a copy of
        the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
        WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
        License for the specific language governing permissions and limitations under
        the License.
