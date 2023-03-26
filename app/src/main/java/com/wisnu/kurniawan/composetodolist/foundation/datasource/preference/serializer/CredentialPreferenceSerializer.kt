package com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.model.CredentialPreference
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object CredentialPreferenceSerializer : Serializer<CredentialPreference> {

    override val defaultValue: CredentialPreference = CredentialPreference(token = "")

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): CredentialPreference {
        try {
            return CredentialPreference.ADAPTER.decode(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: CredentialPreference, output: OutputStream) {
        CredentialPreference.ADAPTER.encode(output, t)
    }

}
