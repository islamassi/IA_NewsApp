

package com.islamassi.latestnews.repo

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.islamassi.latestnews.api.ApiResponse
import com.islamassi.latestnews.api.Resource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * A generic class that can provide a resource backed by both the database and the network.
 *
 * @param ResultType class type for the expected body that will be used in the app
 * @param RequestType class type of the body of the request
*/
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        val dbSource = loadFromDb()
        result.addSource(dbSource) { resultType ->
            result.removeSource(dbSource)
            // check if we should fetch data from network or load locale data
            if (shouldFetch(resultType)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { rT ->
                    setValue(Resource.Success(rT))
                }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { resultType ->
            setValue(Resource.Loading(resultType))
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response!!.isSuccessful) {
                // save network success body to locale database
                processResponse(response).let {
                    Observable.fromCallable { saveCallResult(it!!) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }
                // we specially request a new live data,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.
                result.addSource(loadFromDb()) { resultType ->
                    setValue(Resource.Success(resultType))
                }

            } else {
                onFetchFailed()
                result.addSource(dbSource) { resultType ->
                    setValue(Resource.Error(
                        response.errorMessage?:"",
                        resultType
                    ))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    @WorkerThread
    private fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    protected open fun onFetchFailed() {}

    /**
     * save network response to locale database
     */
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    /**
     * @return Weather to fetch this call from network or just from local database
     */
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    /**
     * loading data from locale database
     */
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    /**
     * create a network call
     */
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    /**
     * get the LiveData representation
     */
    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }
}