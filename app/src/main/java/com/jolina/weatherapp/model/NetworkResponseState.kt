package com.jolina.weatherapp.model

/**
 * This state is used between the repository and view model layer
 *
 * The [Success] parameter is typed as [Any] with the intention that the repository
 * would handle transforming response data from the api to a local model if there was data coming
 * from a local db.
 * This is error prone as [Success.data] needs to be casted to the same type of variable that was
 * emitted from the repository, and given more time I would try to find a way to make it
 * more type safe while keeping it generic.
 */

sealed class NetworkResponseState {
    data class Success(val data: Any): NetworkResponseState()
    object Error: NetworkResponseState()
}
