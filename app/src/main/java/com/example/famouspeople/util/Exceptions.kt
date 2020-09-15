package com.example.famouspeople.util

import java.io.IOException
import javax.inject.Inject

class ApiException @Inject constructor(message: String, val code: Int) : IOException(message)

class NoInternetException @Inject constructor(message: String) : IOException(message)