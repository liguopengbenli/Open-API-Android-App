package com.codingwithmitch.openapi.util

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class ErrorHandling{

    class NetworkErrors{

        companion object{

            private val TAG: String = "AppDebug"

            val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
            val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"

            val ERROR_SAVE_ACCOUNT_PROPERTIES = "Error saving account properties.\nTry restarting the app."
            val ERROR_SAVE_AUTH_TOKEN = "Error saving authentication token.\nTry restarting the app."

            val GENERIC_AUTH_ERROR = "Error"
            val PAGINATION_DONE_ERROR = "Invalid page."
            val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
            val ERROR_UNKNOWN = "Unknown error"


            fun isNetworkError(msg: String): Boolean{
                when{
                    msg.contains(UNABLE_TO_RESOLVE_HOST) -> return true
                    else-> return false
                }
            }

            fun parseDetailJsonResponse(rawJson: String?): String{
                Log.d(TAG, "parseDetailJsonResponse: ${rawJson}")
                try{
                    if(!rawJson.isNullOrBlank()){
                        if(rawJson.equals(ERROR_CHECK_NETWORK_CONNECTION)){
                            return PAGINATION_DONE_ERROR
                        }
                        return JSONObject(rawJson).get("detail") as String
                    }
                }catch (e: JSONException){
                    Log.e(TAG, "parseDetailJsonResponse: ${e.message}")
                }
                return ""
            }

            fun isPaginationDone(errorResponse: String?): Boolean{
                // if error response = '{"detail":"Invalid page."}' then pagination is finished
                return PAGINATION_DONE_ERROR.equals(parseDetailJsonResponse(errorResponse))
            }
        }
    }

}




















