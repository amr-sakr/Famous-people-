package  com.example.famouspeople.networking



import com.example.famouspeople.util.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        val response = call.invoke()
        Timber.i("code: ${response.code()}")
        if (response.isSuccessful) {
            Timber.d( "response is -> ${response.body().toString()}")
            Timber.d( "response raw is -> ${response.raw()}")
            Timber.i(response.body().toString())

            return response.body()!!
        } else {
            val error = response.errorBody()?.toString()
            Timber.e(error.toString())
            val message = StringBuilder()
            error?.let {
                try {
                    // "message" is the error key from the api
                    message.append(JSONObject(it).getString("message"))
                    message.append(JSONObject(it).getString("error"))

                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error: $message")

            throw ApiException(message.toString(), response.code())
        }

    }
}