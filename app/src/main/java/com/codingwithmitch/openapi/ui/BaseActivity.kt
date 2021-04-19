package com.codingwithmitch.openapi.ui


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.util.Constants.Companion.PERMISSIONS_REQUEST_READ_STORAGE
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity(),
    DataStateChangeListener,
    UICommunicationListener
{

    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

    abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onUIMessageReived(uiMessage: UIMessage) {
            when(uiMessage.uiMessageType){
                is UiMessageType.AreYouSureDialog->{
                    areYouSureDialog(
                        uiMessage.message,
                        uiMessage.uiMessageType.callback
                    )
                }

                is UiMessageType.Toast -> {
                    displayToast(uiMessage.message)
                }

                is UiMessageType.Dialog -> {
                    displayInfoDialog(uiMessage.message)
                }

                is UiMessageType.None ->{
                    Log.i(TAG, "onUIMessageReceive: ${uiMessage.message}")
                }

            }
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        dataState?.let {
            GlobalScope.launch(Main){
                displayProgressBar(it.loading.isLoading)
                it.error?.let{
                    errorEvent->
                    handleStateError(errorEvent)
                }
                it.data?.let {
                    it.response?.let {
                        responseEvent->
                        handleStateResponse(responseEvent)
                    }
                }
            }
        }
    }

    private fun handleStateResponse(event: Event<Response>){
        event.getContentIfNotHandled()?.let{
            when(it.responseType){
                is ResponseType.Toast ->{
                    it.message?.let{message ->
                        displayToast(message)
                    }
                }

                is ResponseType.Dialog ->{
                    it.message?.let{ message ->
                        displaySuccessDialog(message)
                    }
                }

                is ResponseType.None -> {
                    Log.i(TAG, "handleStateResponse: ${it.message}")
                }
            }

        }
    }

    private fun handleStateError(errorEvent: Event<StateError>) {
            errorEvent.getContentIfNotHandled()?.let {
                when(it.response.responseType){
                    is ResponseType.Toast-> {
                        it.response.message?.let {
                            message-> displayToast(message)
                        }
                    }
                    is ResponseType.Dialog-> {
                        it.response.message?.let {
                            message-> displayErrorDialog(message)
                        }
                    }
                    is ResponseType.None-> {
                        Log.e(TAG, "handleStateError: ${it.response.message}")
                    }
                }
            }
    }

    abstract fun displayProgressBar(bool: Boolean)

    override fun hideSoftKeyboard() {
        if(currentFocus != null){
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun isStoragePermissionGranted(): Boolean {
        if(
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSIONS_REQUEST_READ_STORAGE
            )
            return false
        }else{
            return true
        }
    }


}
