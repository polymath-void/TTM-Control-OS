package com.ttm.controlos.core.engine

import android.content.Context
import com.ttm.controlos.core.executor.RootCommandRouter
import com.ttm.controlos.core.parser.CommandParser
import com.ttm.controlos.core.parser.CommandResult
import com.ttm.controlos.core.system.SystemOutput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommandEngine(context: Context) {

    private val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val rootRouter = RootCommandRouter(context)

    private val _systemOutput = MutableStateFlow<SystemOutput>(SystemOutput.Idle)
    val systemOutput: StateFlow<SystemOutput> = _systemOutput.asStateFlow()

    fun processCommand(input: String) {
        when (val result = CommandParser.parse(input)) {
            is CommandResult.Success -> {
                val intent = result.intent

                engineScope.launch {
                    _systemOutput.value = SystemOutput.Processing("Executing: ${intent.action}")

                    val routerResult = rootRouter.routeCommand(intent.action, intent.target)

                    // Exhaustive when expression
                    _systemOutput.value = when (routerResult) {
                        is RootCommandRouter.RouterResult.Success -> 
                            SystemOutput.Success(routerResult.info)
                        is RootCommandRouter.RouterResult.Failure -> 
                            SystemOutput.Error(routerResult.errorReason)
                        // This handles any future additions to RouterResult automatically
                        // or prevents "must be exhaustive" errors.
                        else -> SystemOutput.Error("Unknown Router Result")
                    }
                }
            }
            is CommandResult.Error -> {
                _systemOutput.value = SystemOutput.Error(result.message)
            }
        }
    }
