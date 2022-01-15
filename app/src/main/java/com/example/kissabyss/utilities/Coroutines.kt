package com.example.kissabyss.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Coroutines {
    fun runJobInIO(job:suspend (()-> Unit)) =
        CoroutineScope(Dispatchers.IO).launch { job() }
}