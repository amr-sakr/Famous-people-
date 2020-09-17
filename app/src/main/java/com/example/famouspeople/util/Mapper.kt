package com.example.famouspeople.util

import com.example.famouspeople.features.peopleList.core.domain.Person
import com.example.famouspeople.networking.entities.peopleList.NetworkPerson
import com.example.famouspeople.features.peopleList.ui.modelClass.ViewPerson
import com.example.famouspeople.features.personDetails.core.domain.Profile
import com.example.famouspeople.features.personDetails.ui.modelClass.ViewProfile
import com.example.famouspeople.networking.entities.profileImages.NetworkProfile

fun NetworkPerson.toPeopleResult(): Person {
    return Person(
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        popularity = popularity,
        profilePath = profilePath
    )
}


fun Person.toViewPeopleResult(): ViewPerson {
    return ViewPerson(
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        popularity = popularity,
        profilePath = profilePath
    )
}


fun NetworkProfile.toProfile(): Profile? {
    return Profile(
        filePath = filePath
    )

}

fun Profile.toViewProfile(): ViewProfile {

    return ViewProfile(
        filePath = filePath
    )
}