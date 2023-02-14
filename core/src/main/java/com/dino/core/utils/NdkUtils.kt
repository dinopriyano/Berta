package com.dino.core.utils

object NdkUtils {

  init {
    System.loadLibrary("native-lib")
  }

  private external fun adrNativeValues(): Map<String, String>

  private fun getValueFromJNI(key: String): String {
    return adrNativeValues()[key]
      ?: throw IllegalStateException("Key was not found, did you forget to add/remove it in native-lib.cpp?")
  }

  fun getBaseUrl(): String = getValueFromJNI("BASE_API_URL")
  fun getApiKey(): String = getValueFromJNI("API_KEY")

}