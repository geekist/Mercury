package com.ytech.account.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.ytech.account.R
import com.ytech.account.UserManager
import com.ytech.account.databinding.FragmentLoginBinding
import com.ytech.account.model.LoginLayoutBean
import com.ytech.core.arouter.ARouterConstant
import com.ytech.model.User
import com.ytech.ui.base.SupportFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Route(path = ARouterConstant.ModuleAccount.FRAGMENT_LOGIN)
class LoginFragment : SupportFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var layoutData: LoginLayoutBean
    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentLoginBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_login,
            container,
            false)

        initData()
        initView()

        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun initData() {
        layoutData = LoginLayoutBean()
        fragmentLoginBinding.bean = layoutData
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun initView() {

        fragmentLoginBinding.close.setOnClickListener {
            //finish()
        }

        fragmentLoginBinding.featureName.setOnClickListener {
            layoutData.isLogin = !layoutData.isLogin
            initEditText()
        }

        fragmentLoginBinding.btnLogin.setOnClickListener {
            if (layoutData.isLogin) { //登录
                loginAction()
            } else { //注册
                registerAction()
            }
        }
    }

    private fun loginAction() {
        loginViewModel.login(getUserName(), getPassword()).observe(viewLifecycleOwner, Observer { user ->
            saveUserInfo(user)
        })
    }

    private fun saveUserInfo(user: com.ytech.model.User?) {
        UserManager.saveUser(user)
        //finish()
    }

    private fun registerAction() {
        loginViewModel.register(getUserName(), getPassword(), getSurePassword())
            .observe(viewLifecycleOwner, Observer { user ->
               // saveUserInfo(user)
            })
    }

    private fun getUserName(): String {
        return fragmentLoginBinding.userName.text.toString().trim()
    }

    private fun getPassword(): String {
        return fragmentLoginBinding.password.text.toString().trim()
    }

    private fun getSurePassword(): String {
        return fragmentLoginBinding.surePassword.text.toString().trim()
    }

    private fun initEditText() {
        fragmentLoginBinding.userName.text = null
        fragmentLoginBinding.password.text = null
        fragmentLoginBinding.surePassword.text = null
    }
}