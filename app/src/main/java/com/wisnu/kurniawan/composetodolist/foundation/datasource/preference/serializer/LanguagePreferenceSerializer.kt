package com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserLanguagePreference
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object LanguagePreferenceSerializer : Serializer<UserLanguagePreference> {

    override val defaultValue: UserLanguagePreference = UserLanguagePreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserLanguagePreference {
        try {
            return UserLanguagePreference.parseFrom(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: UserLanguagePreference, output: OutputStream) = t.writeTo(output)

}
