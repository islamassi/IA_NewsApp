package com.islamassi.latestnews.api

/**
 * generic class that contains data and status about a resource.
 */
sealed class Resource<T>(
   val data: T? = null,
   val message: String? = null
) {
   /**
    *  used for successfully loaded resources from database or network
    */
   class Success<T>(data: T) : Resource<T>(data)

   /**
    *  used for resources that is being loaded network. The resource may contains data stored in local database
    */
   class Loading<T>(data: T? = null) : Resource<T>(data)

   /**
    * Used for resources that failed to load
    */
   class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}