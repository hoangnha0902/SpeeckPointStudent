package com.nhahv.speechrecognitionpoint.ui.pointInput

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.TypePoint
import com.nhahv.speechrecognitionpoint.ui.main.MainFragment
import com.nhahv.speechrecognitionpoint.util.CommonUtils
import kotlinx.android.synthetic.main.point_input_fragment.*

class PointInputFragment : androidx.fragment.app.DialogFragment() {

    companion object {
        fun newInstance(label: String, typePoint: TypePoint, position: Int) = PointInputFragment().apply {
            arguments = Bundle().apply {
                putString("label", label)
                putSerializable("typePoint", typePoint)
                putInt("position", position)
            }
        }
    }

    var point: String = ""
    private var listener: ((Double, TypePoint, Int?) -> Unit)? = null
    var typePointValue: TypePoint = TypePoint.MOUTH
    var position: Int? = 0
    private lateinit var viewModel: PointInputViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.point_input_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PointInputViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        labelDialog.text = arguments?.getString("label")
        typePointValue = arguments?.getSerializable("typePoint") as TypePoint
        position = arguments?.getInt("position")
        delete.setOnClickListener {
            point = ""
            pointShow.text = ""
        }

        close.setOnClickListener {
            dismiss()
        }

        done.setOnClickListener {
            listener?.invoke(point.toDouble(), typePointValue, position)
            dismiss()
        }

        number0.setOnClickListener {
            if (point.toInt() == 1) {
                point += "0"
            }
            pointShow.text = point
        }

        number1.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "1"
            point = setPointShow(point)
        }
        number2.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "2"
            point = setPointShow(point)
        }
        number3.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "3"
            point = setPointShow(point)
        }
        number4.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "4"
            point = setPointShow(point)
        }
        number5.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "5"
            point = setPointShow(point)
        }
        number6.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "6"
            point = setPointShow(point)
        }
        number7.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "7"
            point = setPointShow(point)
        }
        number8.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "8"
            point = setPointShow(point)
        }
        number9.setOnClickListener {
            if (point.isNotEmpty() && point.toDouble() == 10.0) return@setOnClickListener
            point += "9"
            point = setPointShow(point)
        }
    }

    private fun setPointShow(value: String): String {
        if (value.length == 1) {
            pointShow.text = value
            return value
        }
        val temp = CommonUtils.round(value.toDouble(), 1).toString()
        pointShow.text = temp
        return temp
    }

    fun setOnDismissListener(onListener: ((Double, TypePoint, Int?) -> Unit)?) {
        listener = onListener
    }
}
