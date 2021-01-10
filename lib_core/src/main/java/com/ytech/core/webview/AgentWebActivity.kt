package com.ytech.core.webview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.WebView
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.convenientbanner.utils.ScreenUtil
import com.gyf.immersionbar.ktx.immersionBar
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.ytech.common.common.getAppVersionName
import com.ytech.common.common.getStatusBarHeight
import com.ytech.common.common.loadColor
import com.ytech.common.log.L
import com.ytech.common.text.isNotEmptyStr
import com.ytech.common.view.hide
import com.ytech.core.R
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.constant.AppConstant
import com.ytech.ui.base.SupportActivity
import kotlinx.android.synthetic.main.activity_web_client.*
import kotlinx.android.synthetic.main.fragment_web_client.*

@Route(path = ARouterConstant.LibCore.ACTIVITY_AGENT_WEB)
class AgentWebActivity : SupportActivity() {

    @Autowired
    lateinit var title: String

    @Autowired
    lateinit var url: String

    companion object {
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, AgentWebActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }

        const val ANDROID_JS_OBJECT = "Android"
    }


    protected open var mAgentWeb: AgentWeb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ARouter.getInstance().inject(this)

        setContentView(R.layout.activity_agent_web)

        initView()

    }

    fun initView() {
        mTitleBar.setNavigationIcon(R.drawable.ic_title_back_black)
        mTitleBar.setTitle(title)
        mTitleBar.getToolBar().setNavigationOnClickListener {
            finish()
        }

        initImmersionBar()
        initAgentWeb()
    }

    private fun initImmersionBar() {
        immersionBar {
            //   fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
            //.statusBarColor(R.color.primaryColor)
            // transparentStatusBar()
            statusBarColor(R.color.white)
            // navigationBarColor(R.color.colorPrimary)
            statusBarDarkFont(true,0.2f)
        }
    }

     private fun initAgentWeb() {
        L.e("initAgentWeb")
        mAgentWeb = AgentWeb.with(this@AgentWebActivity)
            // 传入AgentWeb的父控件
            .setAgentWebParent(
                mContentLayout,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
            // 设置进度条颜色与高度 单位为dp。
            .useDefaultIndicator(this.loadColor(R.color.secondaryColor), 1)
            // 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
            .setWebViewClient(mWebViewClient)
            .setWebChromeClient(mWebChromeClient)
            // 严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            // 参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新，
            .setMainFrameErrorView(R.layout.web_layout_error, R.id.mBtnReload)
            .createAgentWeb()
            .ready()
            .go(url)
        mAgentWeb?.agentWebSettings?.webSettings?.let { webSettings ->
            // 这里需要设置为true，才能让Webivew支持<meta>标签的viewport属性
            webSettings.useWideViewPort = true
            webSettings.userAgentString =
                webSettings.userAgentString + " xmandroidapp##v${this.getAppVersionName()}"
        }
        mAgentWeb?.let {
            // 注入Android对象
//            it.jsInterfaceHolder.addJavaObject(
//                WebClientFragment.ANDROID_JS_OBJECT,
//                AndroidInterface(this@WebClientFragment, it)
//            )
        }
        L.e("加载的url : $url")
    }

    private val mWebViewClient by lazy {
        object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.let {
                    it.evaluateJavascript(
                        "javascript:getAndroidStatusBarHeight(${this@AgentWebActivity.getStatusBarHeight()},${
                            ScreenUtil.getScreenWidth(
                                this@AgentWebActivity
                            )
                        })"
                    ) { }
                }
            }
        }
    }

    private val mWebChromeClient by lazy {
        object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                if (title.isNotEmptyStr()) {
                    val newTitle = if (title.length > 15) "${title.substring(0, 15)}..." else title
                    //  mTitleBar?.setTitle(newTitle)
                }
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage ?: return super.onConsoleMessage(consoleMessage)
                L.e("onConsoleMessage = ${consoleMessage.message()}")
                return super.onConsoleMessage(consoleMessage)
            }
        }
    }

    /*
    override fun refresh(needScrollToTop: Boolean) {
        val urlLoader = mAgentWeb?.urlLoader ?: return
        L.e("重新加载 url = $mUrl")
        urlLoader.stopLoading()
        urlLoader.reload()
    }*/

    /*override fun receiveLoginSuccessEvent() {
        AccountManager.getInstance().getToken()?.let { token ->
            L.e("好好养WebFragment old url = $mUrl")
            mUrl = WebPages.appendParams(mUrl, "token" to token)
            L.e("好好养WebFragment 重新加载 url = $mUrl")
            mAgentWeb?.urlLoader?.loadUrl(mUrl)
        }
    }*/

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    /*
        override fun getTitleBar(): TitleBar? = mTitleBar

        override fun supportEventBus() = mSupportEventBus

        override fun supportSwipeBack() = mSupportSwipeBack
    */

//    override fun onBackPressedSupport() {
//        if (mAgentWeb?.back() == true) {
//            return
//        }
//        return super.onBackPressedSupport()
//    }


}