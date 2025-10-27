package com.example.victor_ai.data.network

data class ModelUsage(
    val account_id: String,
    val model_name: String,
    val provider: String,
    val input_tokens_used: Int,
    val output_tokens_used: Int,
    val input_token_price: Float,
    val output_token_price: Float,
    val account_balance: Float,
)
