package ir.nabzi.aroundme.model

data class ServerResponse<T>(val meta : Meta , val response : T)
data class Meta(val code : Int , val requestId : String)