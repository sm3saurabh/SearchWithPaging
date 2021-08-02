package dev.saurabhmishra.searchwithpagination.utils

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
  val mainThread: CoroutineContext
  val ioThread: CoroutineContext
  val default: CoroutineContext
}

class DefaultCoroutineContextProvider: CoroutineContextProvider {

  override val mainThread: CoroutineContext = Dispatchers.Main

  override val ioThread: CoroutineContext = Dispatchers.IO

  override val default: CoroutineContext = Dispatchers.Default

}