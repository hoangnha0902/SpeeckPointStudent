package com.nhahv.speechrecognitionpoint.ui.classcreate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;

class ClassCreateViewModel : ViewModel() {
    val years: MutableLiveData<ArrayList<String>> by lazy { MutableLiveData<ArrayList<String>>() }
    private val array = arrayListOf(
            "2014 - 2015", "2015 - 2016", "2016 - 2017", "2017 - 2018", "2018 - 2019",
            "2019 - 2020", "2020 - 2021", "2021 - 2022", "2023 - 2024"
    )

    init {
        years.value = array
    }
}
