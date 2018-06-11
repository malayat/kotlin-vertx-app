package extensions

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.squareup.moshi.Moshi
import model.SunAdapter

inline fun <reified T : Any> moshiCustomDeserializerOf() = object : ResponseDeserializable<T> {
    override fun deserialize(content: String): T? =
        Moshi.Builder()
            .add(SunAdapter())
            //.add(KotlinJsonAdapterFactory())
            .build()
            .adapter(T::class.java)
            .fromJson(content)
}