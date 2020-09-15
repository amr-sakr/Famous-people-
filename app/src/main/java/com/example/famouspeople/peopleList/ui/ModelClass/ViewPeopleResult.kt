package com.example.famouspeople.peopleList.ui.ModelClass

import com.example.famouspeople.peopleList.core.domain.KnownFor

data class ViewPeopleResult(
    val adult: Boolean? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val networkKnownFor: List<KnownFor>? = null,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val popularity: Double? = null,
    val profilePath: String? = null
)