package com.nhahv.speechrecognitionpoint.data.models

import java.util.*

class Folder : Comparable<Folder> {

    var pathParent: String? = null
    var name: String? = null
    var path: String? = null
    var numberItem: Int = 0
    private var pathChild: MutableList<String> = ArrayList()
    var isFolder: Boolean = false

    constructor() {}

    constructor(name: String, path: String, pathChild: List<String>, pathParent: String, isFolder: Boolean) {
        this.path = path
        this.pathChild.addAll(pathChild)
        this.pathParent = pathParent
        this.numberItem = pathChild.size
        this.isFolder = isFolder
        this.name = name

    }

    fun getPathChild(): List<String> {
        return pathChild
    }

    fun setPathChild(pathChild: MutableList<String>) {
        this.pathChild = pathChild
    }

    override fun compareTo(folder: Folder): Int {
        val numberThis = if (isFolder) 2 else -1
        val numberFolder = if (folder.isFolder) 1 else -1
        return numberFolder - numberThis
    }
}
