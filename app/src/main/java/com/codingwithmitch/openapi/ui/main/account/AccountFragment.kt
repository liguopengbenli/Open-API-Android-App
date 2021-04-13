package com.codingwithmitch.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.model.AccountProperties
import com.codingwithmitch.openapi.session.SessionManager
import com.codingwithmitch.openapi.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject

class AccountFragment : BaseAccountFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        change_password.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }
        logout_button.setOnClickListener {
            viewModel.logout()
        }
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(
            AccountStateEvent.GetAccountPropertiesEvent()
        )
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState->
            if(dataState != null){
                stateChangeListener.onDataStateChange(dataState)
                dataState?.let {
                    it.data?.let {
                            data ->
                        data.data?.let {
                                event ->
                            event.getContentIfNotHandled()?.let {
                                    viewState ->
                                viewState.accountProperties?.let {
                                        accountProperties ->
                                    Log.d(TAG, "AccountFragment DataState: ${accountProperties}")
                                    viewModel.setAccountPropertiesData(accountProperties)
                                }
                            }

                        }

                    }
                }
            }
        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            viewState->
            viewState?.let {
                it.accountProperties?.let {
                    Log.d(TAG, " AccountFragment, ViewState: $it")
                    setAccountDataFields(it)
                }
            }
        })
    }

    private fun setAccountDataFields(accountProperties: AccountProperties){
        email?.setText(accountProperties.email)
        username?.setText(accountProperties.username)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}