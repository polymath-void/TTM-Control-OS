package com.ttm.controlos.core.engine

import android.content.Context
import com.ttm.controlos.core.executor.Executor
import com.ttm.controlos.core.parser.CommandParser
import com.ttm.controlos.core.parser.CommandResult
import com.ttm.controlos.core.security.PermissionRouter
import com.ttm.controlos.core.security.PermissionHandler

object CommandEngine {

    fun handle(
        context: Context,
        input: String,
        onError: (String) -> Unit = {}
    ) {

        when (val result = CommandParser.parse(input)) {

            is CommandResult.Success -> {

                val intent = result.intent

                val permission = PermissionRouter.requiredPermission(intent)

                PermissionHandler.request(context, permission)

                Executor.execute(context, intent)
            }

            is CommandResult.Error -> {
                onError(result.message)
            }
        }
    }
}
