package com.dino.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dino.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel: ViewModel() {

  protected suspend fun <T> Flow<Resource<T>>.runFlow(stateVariable: MutableStateFlow<Resource<T>>) {
    stateVariable.value = Resource.Loading
    collect { result ->
      stateVariable.value = result
    }
  }

  protected suspend fun <T> Flow<Resource<T>>.runFlow(stateVariable: (Resource<T>) -> Unit) {
    stateVariable.invoke(Resource.Loading)
    collect { result ->
      stateVariable.invoke(result)
    }
  }

  protected suspend fun <T: Any> Flow<PagingData<T>>.runPagingFlow(stateVariable: MutableStateFlow<PagingData<T>>) {
    cachedIn(viewModelScope)
      .collect {
        stateVariable.value = (it)
      }
  }

}