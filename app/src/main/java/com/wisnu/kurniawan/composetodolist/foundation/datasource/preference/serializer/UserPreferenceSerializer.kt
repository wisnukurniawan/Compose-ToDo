package com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserPreference
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object UserPreferenceSerializer : Serializer<UserPreference> {

    override val defaultValue: UserPreference = UserPreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreference {
        try {
            return UserPreference.parseFrom(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: UserPreference, output: OutputStream) = t.writeTo(output)

}
