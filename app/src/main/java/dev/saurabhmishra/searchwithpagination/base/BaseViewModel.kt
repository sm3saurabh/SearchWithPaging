package dev.saurabhmishra.searchwithpagination.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {

  protected fun <T> StateFlow<T>.setValue(data: T) {
    require(this is MutableStateFlow<T>) { "setValue can only be called on a mutable StateFlow" }
    this.value = data
  }

}