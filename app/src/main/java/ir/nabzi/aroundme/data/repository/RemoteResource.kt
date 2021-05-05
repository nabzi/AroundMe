package ir.nabzi.aroundme.data.repository

import ir.nabzi.aroundme.data.model.Resource
import ir.nabzi.aroundme.data.model.Status

/**
*   RemoteResource<ResultType> is an abstract class that can be used for resources that can be fetched
 *   from remote server or database.
 *   If parameter "shouldFetch" is set to true, we always fetch data from server and update database,
 *   and it can be set to "data == nullOrEmpty()" if we want to load data from remote server if
 *   data is not present in database yet.
* */
abstract class RemoteResource<ResultType> {
    suspend fun get(shouldFetch : Boolean): Resource<ResultType>{
        return if (shouldFetch) {
            val resource = pullFromServer()

            if (resource.status == Status.ERROR) {
                Resource.error<ResultType>(
                    resource.message ?: "error loading from server", getFromDB()
                )
            } else {
                resource.data?.let {
                    updateDB(it)
                }
                Resource.success(getFromDB())
            }

        } else {
            Resource.success(getFromDB())
        }
    }

    abstract suspend fun updateDB(result : ResultType)

    abstract fun getFromDB(): ResultType

    abstract suspend fun pullFromServer(): Resource<ResultType>
}