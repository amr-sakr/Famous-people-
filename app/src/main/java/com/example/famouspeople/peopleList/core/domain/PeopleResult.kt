package com.example.famouspeople.peopleList.core.domain


data class PeopleResult (
    val adult: Boolean? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val networkKnownFor: List<KnownFor>? = null,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val popularity: Double? = null,
    val profilePath: String? = null
)