package com.ytech.common.common

object Constants {

    const val JPUSH_SEQUENCE = 100101

    // 图片最多能选9张
    const val MAX_IMAGE_SELECT_SIZE = 9

    // 视频最多能够选1个
    const val MAX_VIDEO_SELECT_SIZE = 1

    // 网络请求成功
    const val REQUEST_SUCCESS = 200

    // 没有设置过登录密码需要跳转到设置密码页面
    const val NEED_SETTING_PWD = 40001

    // token失效
    const val TOKEN_INVALID = 422

    // 服务器出现错误
    const val SERVER_ERROR = 500

    const val EXTRA_SUPPORT_SWIPE_BACK = "extra_support_swipe_back"
    const val EXTRA_WEB_SHOW_LOCAL_TITLE_BAR = "extra_web_show_local_title_bar"
    const val EXTRA_WEB_SHOW_BACK_ICON = "extra_web_show_back_icon"
    const val EXTRA_WEB_SUPPORT_EVENT_BUS = "extra_web_support_event_bus"

    const val EXTRA_MESSAGE_ID = "message_id"
    const val EXTRA_MESSAGE_TYPE = "message_type"

    // 聊天消息类型
    const val CHAT_MESSAGE_TYPE = 5

    // 评价通知类型
    const val COMMENT_MESSAGE_TYPE = 7

    const val EXTRA_WEB_URL = "extra_web_url"

    // 选择图片返回items
    const val EXTRA_RESULT_SELECTED_ITEMS = "extra_result_selected_items"

    const val EXTRA_USERNAME = "extra_username"
    const val EXTRA_TASK_ITEM = "extra_task_item"
    const val EXTRA_IMAGE = "extra_image"
    const val EXTRA_IMAGES = "extra_images"
    const val EXTRA_POSITION = "extra_position"
    const val EXTRA_HEAD = "extra_head"
    const val EXTRA_CAN_CHANGE_HEAD = "extra_can_change_user_head"
    const val EXTRA_CROP_IMAGE_PATH = "extra_crop_image_path"
    const val EXTRA_LEAVE_INFO = "extra_leave_info"
    const val EXTRA_ID = "extra_id"
    const val EXTRA_MESSAGE_ITEM = "extra_chat_item"
    const val EXTRA_DAILY_EVALUATION = "extra_daily_evaluation"
    const val EXTRA_ASPECT_RATIO_X = "extra_aspect_ratio_x"
    const val EXTRA_ASPECT_RATIO_Y = "extra_aspect_ratio_y"
    const val EXTRA_GROWTH_HANDBOOK = "extra_growth_handbook"
    const val EXTRA_FRAGMENT_PATH = "extra_fragment_path"
    const val EXTRA_BUNDLE = "extra_bundle"
    const val EXTRA_URL = "extra_url"

    const val MAX_PWD_LENGTH = 20
    const val MIN_PWD_LENGTH = 6

    const val DEFAULT_PAGE_SIZE = 20

    const val APP_PREFS_KEY = "yuyateacher"
    const val APP_ENCRYPT_KEY = "YUYATEACHERYUYAY"

    // android注入到js的对象名称
    const val ANDROID_JS_OBJECT = "Android"

    // 时间相关
    const val YYYY_MM_DD = "yyyy-MM-dd"
    const val YYYY_MM = "yyyy-MM"
    const val YYYY_MM_CH = "yyyy年MM月"
    const val YY_MM_DD_HH_MM_SS = "yyMMddHHmmss"
    const val YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    const val YYYY_MM_DD_HH_MM_CHINESE = "yyyy年MM月dd日HH:mm"
    const val YYYY_MM_DD_HH_MM_SS_24HOUR = "yyyy-MM-dd HH:mm:ss"
    const val YYYY_MM_DD_HH_MM_SS_12HOUR = "yyyy-MM-dd hh:mm:ss"
    const val MM_DD = "MM.dd"
    const val M_M_D_D = "M月dd日"
    const val M_D = "M月d日"
    const val M_DD = "M.dd"
    const val HH_MM_ss_24HOUR = "HH:mm:ss"
    const val HH_MM_ss_12HOUR = "hh:mm:ss"
    const val HH_MM_24HOUR = "HH:mm"
    const val HH_MM_12HOUR = "hh:mm"

    object RequestCode {
        const val SETTING_PWD = 0
        const val PUBLISH_TASK = 1
        const val PHOTO_PICKER = 2
        const val VIDEO_PICKER = 3
        const val IMAGE_CROP = 4
        const val LOOK_HEAD = 5
        const val TAKE_PHOTO = 6
        const val ADD_GROWTH_HANDBOOK = 7
    }
}