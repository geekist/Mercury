# Mercury

一款基于[wanandroid.com](https://www.wanandroid.com/blog/show/2)开放API接口的android app 应用。 kotlin语言开发，采用了组件化开发、单activity+多fragement、MVVM架构的Retrofit+coroutine+LiveData+Databinding编程思想，对数据层、传输层、应用层进行了封装，可作为快速开发android应用的框架。

## 架构介绍

### 1、组件化应用结构

![组件化应用结构](https://img-blog.csdnimg.cn/2021010921180844.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3lhbmd3dTAwNw==,size_16,color_FFFFFF,t_70#pic_center)


采用组件化结构体系，将功能组件抽取出来，置于架构最底层，作为工具类组件；将业务模块解耦，各个业务模块可以独立开发调试，组件间路由通过ARouter实现；app层只包含最简单的UI或者完全是一个壳工程（根据项目情况而定）。可以使项目结构清晰、各个模块之间完全解耦，提高开发效率。

本项目的组件层次介绍：

**功能组件层：**

提供开发app的基础功能，这些基本功能独立于业务组件，可以被业务组件调用。

* lib_common：提供和业务逻辑无关的基本的功能，比如打印日志、IO操作、网络/设备检测、数据加解密等。

* lib_ui：提供UI相关的基本功能，比如自定义view，颜色、字体及其他资源。

* lib_model：提供应用所需的原子数据结构，因为在databinding的lib_ui中需要绑定数据，因此原子数据防止功能组件层。

* lib_core：提供应用相关的功能组件，比如网络封装、路由封装、数据总线定义等。


**业务组件层：**

实现项目的各个业务逻辑模块，业务组件之间相互独立，互不引用，组件间的路由由ARouter实现。

**App层**

负责管理各个业务组件，和打包apk，只有最简单的业务功能（主页面）或者没有具体的业务功能（壳工程），只负责编译打包工程。


### 2、基于Kotlin协程的Retrofit + coroutine + LiveData + ViewModel的MVVM架构

![架构](https://img-blog.csdnimg.cn/20210109214622569.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3lhbmd3dTAwNw==,size_16,color_FFFFFF,t_70#pic_center)

使用observable的LiveData和databinding的框架实现数据变化的实时双向控制。


### 3、单activity多fragment
采用single activity mutiple fragments降低多Activity的资源消耗，解决activity间的切换效率问题。

## 项目采用的开源框架库
* [ARouter实现组件间的路由以及依赖注入](https://github.com/alibaba/ARouter)


* [Fragmentation实现多fragments的装载和栈管理](https://github.com/YoKeyword/Fragmentation)


* [EventBus实现事件总线](https://github.com/greenrobot/EventBus)


* [Retrofit + OKHttp实现网络传输](https://github.com/square/retrofit)

* [gson实现json数据的转换]()

* [Glide实现图片加载处理](https://github.com/bumptech/glide)

* [PermissionX实现运行时权限处理](https://github.com/guolindev/PermissionX)

## 项目采用的开源UI控件及其他控件

* [FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout)

* [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

* [Xpopup]( https://github.com/li-xiaojun/XPopup)

* [agentweb](https://github.com/Justson/AgentWeb)

* [ImmersionBar](https://github.com/gyf-dev/ImmersionBar)

* [banner](https://github.com/youth5201314/banner)

* [backgroundLibrary](https://github.com/JavaNoober/BackgroundLibrary)

* [autosize](https://github.com/JessYanCoding/AndroidAutoSize)

* [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer)

* [circleImageView](https://github.com/hdodenhof/CircleImageView)

* [mmkv](https://github.com/Tencent/MMKV)