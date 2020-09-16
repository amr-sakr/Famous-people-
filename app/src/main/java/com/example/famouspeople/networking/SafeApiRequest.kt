package  com.example.famouspeople.networking


import com.example.famouspeople.util.ApiException
import com.example.famouspeople.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Result<T> {
        Timber.d("Iam in SafeApiRequest")
        return withContext(Dispatchers.IO) {
            return@withContext try {

                val response = call()
                return@withContext handleResult(response)

            } catch (exception: ApiException) {
                Timber.e("ApiException ${exception.message}")
                Result.Error(exception)
            } catch (exception: NoInternetException) {
                Timber.e("NoInternetException ${exception.message}")
                Result.Error(exception)
            } catch (exception: Exception) {
                Timber.e("Exception ${exception.message}")
                Result.Error(exception)
            }
        }
    }

    private fun <T> handleResult(response: Response<T>): Result<T> {
        return when (response.code()) {
            in 1..399 -> Result.Success(response.body()!!)
            401 -> Result.Error(ApiException(response.message(), response.code()))
            else -> Result.Error(Exception())
        }
    }
}