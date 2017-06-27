# 弹力布局。
![image](https://github.com/wangdongyi/ElasticApplication/blob/master/app/src/main/assets/readme.gif)

<br/>可以中心，左侧，右侧收起展开布局。继承RelativeLayout。
# 自定义两个属性  
```Java
<br/>app:imageButtonBackground="@mipmap/ic_launcher"布局的背景
<br/>app:imageButtonGravity="center"收缩后的位置
<br/> getBtnImageView()可以获得点击按钮，可以自定义。
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.wdy.elastic.MainActivity">


    <com.wdy.elastic.library.WDYElasticLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="10dp"
        app:imageButtonGravity="center">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            android:orientation="vertical">

            <TextView
                android:id="@+id/ttt"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="这是一个测试布局"
                android:textColor="@android:color/background_dark" />

            <ImageView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:src="@mipmap/ic_launcher_round" />
        </LinearLayout>

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher_round" />
    </com.wdy.elastic.library.WDYElasticLayout>
</LinearLayout>
```
