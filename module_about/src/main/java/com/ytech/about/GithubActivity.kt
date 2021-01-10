package com.ytech.about

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ktx.immersionBar
import com.ytech.about.adapter.GitHubListAdapter
import com.ytech.common.device.dp2px
import com.ytech.core.arouter.ARouterConstant
import com.ytech.model.about.GitItem
import com.ytech.model.apply.ProjectItemSub
import com.ytech.ui.base.SupportActivity
import com.ytech.ui.view.RecyclerViewDivider
import kotlinx.android.synthetic.main.activity_github.*

@Route(path= ARouterConstant.ModuleAbout.ACTIVITY_GITHUB)
class GithubActivity : SupportActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        initActionBar()
        initImmersionBar()
        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home->finish()
        }
        return true
        // return super.onOptionsItemSelected(item)
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar ?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_title_back_black)
        }
    }

    private fun initImmersionBar() {
        immersionBar {
            //   fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
            //.statusBarColor(R.color.primaryColor)
             transparentStatusBar()
           // statusBarColor(R.color.white)
            // navigationBarColor(R.color.colorPrimary)
            //statusBarDarkFont(true,0.2f)
        }
    }

    private fun initView(){
        recycleView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val adapter = GitHubListAdapter(prepareListData())
        recycleView.adapter = adapter

        val divider = RecyclerViewDivider(this,
            LinearLayoutManager.HORIZONTAL,
            dp2px(this, 1.0F),
            this.resources.getColor(R.color.divider))
        recycleView.addItemDecoration(divider)
    }

    private fun prepareListData() : List<GitItem> {
        val listGit = mutableListOf<GitItem>()
        listGit.add(GitItem("ARouter","https://github.com/alibaba/ARouter"))
        listGit.add(GitItem("Fragmentation","https://github.com/YoKeyword/Fragmentation"))
        listGit.add(GitItem("EventBus","https://github.com/greenrobot/EventBus"))
        listGit.add(GitItem("Retrofit","https://github.com/square/retrofit"))
        listGit.add(GitItem("Gson",""))
        listGit.add(GitItem("Glide","https://github.com/bumptech/glide"))
        listGit.add(GitItem("PermissionX","https://github.com/guolindev/PermissionX"))
        listGit.add(GitItem("FlycoTabLayout","https://github.com/H07000223/FlycoTabLayout"))
        listGit.add(GitItem("SmartRefreshLayout","https://github.com/scwang90/SmartRefreshLayout"))
        listGit.add(GitItem("Xpopup","https://github.com/li-xiaojun/XPopup"))
        listGit.add(GitItem("agentweb","https://github.com/Justson/AgentWeb"))
        listGit.add(GitItem("ImmersionBar","https://github.com/gyf-dev/ImmersionBar"))
        listGit.add(GitItem("Banner","https://github.com/youth5201314/banner"))
        listGit.add(GitItem("BackgroundLibrary","https://github.com/JavaNoober/BackgroundLibrary"))
        listGit.add(GitItem("Autosize","https://github.com/JessYanCoding/AndroidAutoSize"))
        listGit.add(GitItem("AndroidPdfViewer","https://github.com/barteksc/AndroidPdfViewer"))
        listGit.add(GitItem("CircleImageView","https://github.com/hdodenhof/CircleImageView"))
        listGit.add(GitItem("MMkv","https://github.com/Tencent/MMKV"))

        return listGit
    }
 }