package com.nhahv.speechrecognitionpoint.ui.groupExam


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.GroupExam
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.fragment_group_exam_create.*

class GroupExamCreateFragment : DialogFragment() {

    private val yearAdapter: ArrayAdapter<String> by lazy { ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, Constant.arrayYearGroupExam) }
    var idExamObject: String? = null
    private var listener: ((GroupExam) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_group_exam_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spYearGroupExam.adapter = yearAdapter
        spYearGroupExam.setSelection(4)


        cancel.setOnClickListener { dismiss() }
        createGroupExam.setOnClickListener {
            if (TextUtils.isEmpty(edtIdGroupExam.text)) {
                toast("Mã nhóm thi không được để trống")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(edtNameGroupExam.text)) {
                toast("Mã nhóm thi không được để trống")
                return@setOnClickListener
            }

            getGroupExamLists().forEach { groupExam: GroupExam ->
                if (groupExam.idGroupExam.trim().toLowerCase() == edtIdGroupExam.text.toString().trim().toLowerCase()) {
                    toast("Mã nhóm thi đã tồn tại")
                    return@setOnClickListener
                }
            }
            val groupExam = GroupExam(edtIdGroupExam.text.toString().trim(),
                    edtNameGroupExam.text.toString().trim(),
                    idExamObject ?: "",
                    spYearGroupExam.selectedItem.toString())
            listener?.invoke(groupExam)
            dismiss()
        }
    }


    fun listenerCallback(l: ((GroupExam) -> Unit)?) {
        listener = l
    }

    private fun getGroupExamLists(): ArrayList<GroupExam> {
        val value = sharePrefs().get(prefGroupExam(idExamObject), "")
        if (TextUtils.isEmpty(value)) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<GroupExam>>(value)
    }

    companion object {
        fun newInstance() = GroupExamCreateFragment()
    }

}
