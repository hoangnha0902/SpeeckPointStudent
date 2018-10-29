package com.nhahv.speechrecognitionpoint.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.util.Constant.IS_LOGIN
import com.nhahv.speechrecognitionpoint.util.Constant.PASSWORD
import com.nhahv.speechrecognitionpoint.util.Constant.USER_NAME
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.navigate
import com.nhahv.speechrecognitionpoint.util.navigateClearStack
import com.nhahv.speechrecognitionpoint.util.toast
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

        val isLogin = SharedPrefs.getInstance(requireContext()).get(IS_LOGIN, false)
        if (isLogin) {
            login.findNavController().navigate(R.id.classStudentFragment)
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
            SharedPrefs.getInstance(requireContext()).put(IS_LOGIN, true)
            navigateClearStack(login, R.id.action_loginFragment_to_classStudentFragment)
        }
        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                password.transformationMethod = null
            } else {
                password.transformationMethod = PasswordTransformationMethod()
            }
        }

        loginWithoutUser.setOnClickListener {
            navigateClearStack(loginWithoutUser, R.id.action_loginFragment_to_classStudentFragment)
        }
    }
}
