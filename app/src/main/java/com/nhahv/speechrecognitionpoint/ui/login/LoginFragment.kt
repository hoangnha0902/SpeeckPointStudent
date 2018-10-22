package com.nhahv.speechrecognitionpoint.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.util.Constant.PASSWORD
import com.nhahv.speechrecognitionpoint.util.Constant.USER_NAME
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.toast
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRegister.setOnClickListener {
            btnRegister.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
            val labelName = SharedPrefs.getInstance(requireContext()).get(USER_NAME, "")
            val labelPassword = SharedPrefs.getInstance(requireContext()).get(PASSWORD, "")
            if (userName.text.toString() != labelName || password.text.toString() != labelPassword) {
                toast("Tên tài khoản hoặc mật khẩu không đúng")
                return@setOnClickListener
            }
            login.findNavController().navigate(R.id.action_loginFragment_to_classStudentFragment, null, NavOptions.Builder().setClearTask(true).build())
        }
        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                password.transformationMethod = null
            } else {
                password.transformationMethod = PasswordTransformationMethod()
            }
        }

        loginWithoutUser.setOnClickListener {
            loginWithoutUser.findNavController().navigate(R.id.action_loginFragment_to_classStudentFragment, null, NavOptions.Builder().setClearTask(true).build())
        }
    }
}
