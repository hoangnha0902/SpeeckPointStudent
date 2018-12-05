package com.nhahv.speechrecognitionpoint.ui.register

import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.Account
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.PASSWORD
import com.nhahv.speechrecognitionpoint.util.Constant.USER_NAME
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_ACCOUNTS
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
            val accounts = getAccounts()
            for (item in accounts) {
                if (userName.text.toString() == item.userName) {
                    toast("Tài khoản đã toàn tại")
                    return@setOnClickListener
                }
            }
            accounts.add(Account(userName.text.toString(), password.text.toString()))
            sharePrefs().put(PREF_ACCOUNTS, Gson().toJson(accounts))
            sharePrefs().put(USER_NAME, userName.text.toString())
            sharePrefs().put(PASSWORD, password.text.toString())
            sharePrefs().get(Constant.IS_LOGIN, true)
            navigateClearStack(R.id.action_registerFragment_to_managerFragment)
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

    private fun getAccounts(): ArrayList<Account> {
        val value = sharePrefs().get(PREF_ACCOUNTS, "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Account>>(value)
    }
}
