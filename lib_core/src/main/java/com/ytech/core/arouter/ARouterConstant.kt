package com.ytech.core.arouter

object ARouterConstant {
    const val MAIN_FRAGMENT = "/app/MainFragment"

    const val MAIN_ACTIVITY = "/app/MainActivity"

    object ModuleAbout {
        const val FRAGMENT_ABOUT = "/about/AboutFragment"
        const val ACTIVITY_ABOUT = "/about/AboutMeActivity"
    }

    object ModuleHome {
        const val FRAGMENT_HOME = "/home/HomeFragment"
    }

    object ModuleAccount {
        const val FRAGMENT_LOGIN = "/account/LoginFragment"
        const val FRAGMENT_REGISTER = "/account/RegisterFragment"
        const val FRAGMENT_RENEW_PASSWORD = "/account/RenewPasswordFragment"
    }

    object ModuleKnowledge {
        const val FRAGMENT_KNOWLEDGE = "/knowledge/KnowledgeFragment"
        const val FRAGMENT_KNOWLEDGE_DETAIL = "/knowledge/KnowledgeDetailFragment"
    }

    object ModuleApply {
        const val FRAGMENT_APPLY = "/apply/ApplyFragment"
    }

    object LibCore {
        const val FRAGMENT_WEB_CLIENT = "/lib/core/WebClientFragment"
        const val ACTIVITY_WEB_CLIENT = "/lib/core/WebClientActivity"
    }

    object WebView {
        const val WEB_CLIENT_ACTIVITY_PROVIDER = "/lib/core/WebClientProvider"
        const val WEB_VIEW_PROVIDER_PATH = "/webView/webView_provider"

    }

    /**
     * 消息模块
     */
    object MsgModule {
        const val MESSAGE_FRAGMENT = "/msg/MessageFragment"

        const val ADDRESS_BOOK_FRAGMENT = "/msg/AddressBookFragment"

        /**
         * 聊天页面
         */
        const val CHAT_FRAGMENT = "/chat/ChatFragment"

        /**
         * 公告页面
         */
        const val BULLETIN_FRAGMENT = "/bulletin/BulletinFragment"

        const val BULLETIN_DETAIL_FRAGMENT = "/bulletin/BulletinDetailFragment"

        /**
         * 通知页面
         */
        const val NOTICE_FRAGMENT = "/notice/NoticeFragment"
    }

    /**
     * 任务模块
     */
    object TaskModule {
        const val TASK_FRAGMENT = "/task/TaskFragment"

        const val TASK_DETAIL_FRAGMENT = "/task/TaskDetailFragment"

        const val PUBLISH_TASK_FRAGMENT = "/task/publish/PublishTaskFragment"
    }

    /**
     * 家长课堂模块
     */
    object ClassroomModule {
        const val CLASSROOM_FRAGMENT = "/classroom/ClassroomFragment"
    }

    /**
     * 学生模块
     */
    object StuModule {
        const val STUDENT_FRAGMENT = "/stu/StudentFragment"

        const val STUDENT_INFO_FRAGMENT = "/stu/StudentInfoFragment"

        const val BASE_INFO_FRAGMENT = "/stu/BaseInfoFragment"

        const val SETTING_FRAGMENT = "/setting/AccountSettingFragment"

        /**
         * 成长手册页面
         */
        const val GROWTH_HANDBOOK_FRAGMENT = "/growth/GrowthHandbookFragment"

        const val GROWTH_HANDBOOK_DETAIL_FRAGMENT = "/growth/GrowthHandbookDetailFragment"

        const val ADD_GROWTH_HANDBOOK_FRAGMENT = "/growth/AddGrowthHandbookFragment"

        const val DAILY_EVALUATION_FRAGMENT = "/Evaluation/DailyEvaluationFragment"

        const val DAILY_EVALUATION_DETAIL_FRAGMENT = "/Evaluation/DailyEvaluationDetailFragment"

        /**
         * 考勤页面
         */
        const val ATTENDANCE_FRAGMENT = "/attendance/AttendanceFragment"

        const val TIME_CARD_STATISTICS_DETAIL_FRAGMENT = "/attendance/TimeCardStatisticsDetailFragment"

        /**
         * 请假记录页面
         */
        const val LEAVE_RECORD_FRAGMENT = "/leave/LeaveRecordFragment"

        /**
         * 请假详情页面
         */
        const val LEAVE_DETAIL_FRAGMENT = "/leave/LeaveDetailFragment"

        const val HEALTH_STATUS_FRAGMENT = "/student/HealthStatusFragment"

        const val VIDEO_PLAYER_FRAGMENT = "/video/VideoPlayerFragment"
    }

    object AccountBiz {
        /**
         * 登录界面
         */
        const val ACCOUNT_LOGIN_FRAGMENT = "/account/LoginFragment"

        /**
         * 设置密码页面
         */
        const val SETTING_PWD_FRAGMENT = "/account/SettingPwdFragment"

        /**
         * 图片浏览页面
         */
        const val IMAGE_VIEWER_FRAGMENT = "/account/common/ImageViewerFragment"

        /**
         * 用户头像查看页面
         */
        const val HEAD_VIEWER_FRAGMENT = "/account/head/HeadViewerFragment"

        /**
         * 头像裁剪页面
         */
        const val IMAGE_CROP_FRAGMENT = "/account/crop/ImageCropFragment"
    }

    object WebBiz {
        const val YU_YA_WEB_FRAGMENT = "/web/YuYaWebFragment"
    }

    object Service {
        const val MAIN_FRAGMENT_TAG_PROVIDER = "/app/MainFragmentTagService"
        const val MESSAGE_FRAGMENT_TAG_PROVIDER = "/msg/MessageFragmentTagProvider"
        const val TASK_FRAGMENT_TAG_PROVIDER = "/task/TaskFragmentTagProvider"
        const val CLASSROOM_FRAGMENT_TAG_PROVIDER = "/classroom/ClassroomFragmentTagProvider"
        const val STUDENT_FRAGMENT_TAG_PROVIDER = "/student/StudentFragmentTagProvider"

        const val ACCOUNT_API_SERVICE = "/account/AccountApi"
        const val TASK_API_SERVICE = "/task/TaskApi"
        const val MESSAGE_API_SERVICE = "/message/MessageApi"
    }
}