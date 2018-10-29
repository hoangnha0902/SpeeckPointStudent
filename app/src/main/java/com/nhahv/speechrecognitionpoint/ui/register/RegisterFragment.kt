package com.nhahv.speechrecognitionpoint.ui.register

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        register.setOnClickListener {
            if (TextUtils.isEmpty(userName.text.toString())) {
                toast("Nhập tên tài khoản")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password.text.toString()) || TextUtils.isEmpty(rePassword.text.toString()) || password.text.toString() != rePassword.text.toString()) {
                toast("Nhập đúng mật khẩu")
                return@setOnClickListener
            }
            SharedPrefs.getInstance(requireContext()).put(USER_NAME, userName.text.toString())
            SharedPrefs.getInstance(requireContext()).put(PASSWORD, password.text.toString())
            register.findNavController().navigate(R.id.action_register_to_class, null, NavOptions.Builder().setClearTask(true).build())

        }
        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                password.transformationMethod = null
                rePassword.transformationMethod = null
            } else {
                password.transformationMethod = PasswordTransformationMethod()
                rePassword.transformationMethod = PasswordTransformationMethod()
            }
        }
    }

}
