package com.example.famouspeople.features.peopleList.ui.modelClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewPerson(
    val adult: Boolean? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val networkKnownFor: List<ViewKnownFor>? = null,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val popularity: Double? = null,
    val profilePath: String? = null
) : Parcelable