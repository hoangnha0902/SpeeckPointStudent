package com.nhahv.speechrecognitionpoint.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.Account
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.IS_LOGIN
import com.nhahv.speechrecognitionpoint.util.Constant.PASSWORD
import com.nhahv.speechrecognitionpoint.util.Constant.USER_NAME
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : androidx.fragment.app.Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isLogin = sharePrefs().get(IS_LOGIN, false)
        if (isLogin) {
            navigateClearStack(R.id.action_loginFragment_to_managerFragment)
        }
        val labelName = SharedPrefs.getInstance(requireContext()).get(USER_NAME, "")
        val labelPassword = SharedPrefs.getInstance(requireContext()).get(PASSWORD, "")

        if (!TextUtils.isEmpty(labelName)) {
            loginWithoutUser.visibility = View.GONE
            userName.setText(labelName)
        }
        btnRegister.setOnClickListener {
            navigate(btnRegister, R.id.action_loginFragment_to_registerFragment)
        }
        login.setOnClickListener {
            if (TextUtils.isEmpty(userName.text.toString())) {
                toast("Nhập tên tài khoản")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password.text.toString())) {
                toast("Nhập mật khẩu")
                return@setOnClickListener
            }
            if (userName.text.toString() != labelName || password.text.toString() != labelPassword) {
                toast("Tên tài khoản hoặc mật khẩu không đúng")
                return@setOnClickListener
            }

            val accounts = getAccounts()
            for (item in accounts) {
                if (userName.text.toString() == item.userName && password.text.toString() == item.password) {
                    sharePrefs().put(IS_LOGIN, true)
                    sharePrefs().put(USER_NAME, userName.text.toString())
                    sharePrefs().put(PASSWORD, password.text.toString())
                    navigateClearStack(login, R.id.action_loginFragment_to_managerFragment)
                }
            }
        }
        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                password.transformationMethod = null
            } else {
                password.transformationMethod = PasswordTransformationMethod()
            }
        }

        loginWithoutUser.setOnClickListener {
            navigateClearStack(loginWithoutUser, R.id.action_loginFragment_to_managerFragment)
        }
    }

    private fun getAccounts(): ArrayList<Account> {
        val value = sharePrefs().get(SharedPrefs.PREF_ACCOUNTS, "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Account>>(value)
    }
}
