package software.sauce.easyledger.domain.data

data class DataState<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false,
){
    companion object{

        fun <T> success(
            data: T,
            loading: Boolean = false
        ): DataState<T>{
            return DataState(
                data = data,
                loading = loading
            )
        }

        fun <T> error(
            message: String,
        ): DataState<T>{
            return DataState(
                error = message
            )
        }

        fun <T> loading(): DataState<T> = DataState(loading = true)
    }
}