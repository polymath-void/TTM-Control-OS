package com.ttm.controlos.core.parser

import com.ttm.controlos.core.intent.TTMIntent

sealed class CommandResult {

    data class Success(val intent: TTMIntent) : CommandResult()

    data class Error(val message: String) : CommandResult()
}
