package com.example.newsfeed.domain.model

sealed class RefreshResponse {
    data object Success : RefreshResponse()
    data object NoNetworkConnection : RefreshResponse()
    data class Failure(val message: String) : RefreshResponse()
}
