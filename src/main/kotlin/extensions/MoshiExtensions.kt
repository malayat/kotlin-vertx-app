package extensions

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.squareup.moshi.Moshi
import model.SunSetAdapter

inline fun <reified T : Any> sunSetDeserializerOf() = object : ResponseDeserializable<T> {
    override fun deserialize(content: String): T? =
        Moshi.Builder()
            .add(SunSetAdapter())
            .build()
            .adapter(T::class.java)
            .fromJson(content)
}


inline fun <reified T : Any> defaultDeserializerOf() = object : ResponseDeserializable<T> {
    override fun deserialize(content: String): T? =
        Moshi.Builder()
            .build()
            .adapter(T::class.java)
            .fromJson(content)
}