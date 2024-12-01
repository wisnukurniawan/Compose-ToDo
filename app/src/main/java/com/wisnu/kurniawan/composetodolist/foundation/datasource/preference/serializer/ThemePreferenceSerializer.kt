package com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.UserThemePreference
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object ThemePreferenceSerializer : Serializer<UserThemePreference> {

    override val defaultValue: UserThemePreference = UserThemePreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserThemePreference {
        try {
            return UserThemePreference.parseFrom(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: UserThemePreference, output: OutputStream) = t.writeTo(output)

}
