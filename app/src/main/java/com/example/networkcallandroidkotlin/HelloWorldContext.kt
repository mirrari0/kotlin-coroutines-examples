package com.example.networkcallandroidkotlin

import kotlin.coroutines.CoroutineContext

class HelloWorldContext: CoroutineContext {
    override fun <R> fold(initial: R, operation: (R, CoroutineContext.Element) -> R): R {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}