package com.wisnu.kurniawan.composetodolist.features.localized.base.ui

import android.content.Context
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wisnu.kurniawan.composetodolist.foundation.di.languageDatastore
import com.wisnu.kurniawan.composetodolist.foundation.extension.toLanguage
import com.wisnu.kurniawan.composetodolist.foundation.localization.LocalizationUtil
import com.wisnu.kurniawan.composetodolist.model.Language
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.util.*

@AndroidEntryPoint
abstract class LocalizedActivity : AppCompatActivity() {

    private val localizedViewModel: LocalizedViewModel by viewModels()
    private var currentLocale: Locale? = null

    init {
//        initListenLanguage()
    }

//    private fun initListenLanguage() {
//        lifecycleScope.launchWhenCreated {
//            localizedViewModel.effect.collect {
//                when (it) {
//                    is LocalizedEffect.ApplyLanguage -> {
//                        currentLocale = Locale(it.language.lang)
//                        LocalizationUtil.applyLanguageContext(this@LocalizedActivity, this@LocalizedActivity.getLocale())
//                        applyLanguage()
//                    }
//                }
//            }
//        }
//    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocalizationUtil.applyLanguageContext(context, context.getLocale())
    }

    override fun getBaseContext(): Context {
        val context = super.getBaseContext()
        return LocalizationUtil.applyLanguageContext(context, context.getLocale())
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationUtil.applyLanguageContext(newBase, newBase.getLocale()))
    }

    private fun Context.getLocale(): Locale {
        if (currentLocale == null) {
            runBlocking {
                this@getLocale.languageDatastore.data
                    .take(1)
                    .map { it.toLanguage() }
                    .catch { emit(Language.ENGLISH) }
                    .collect { currentLocale = Locale(it.lang) }
            }
        }

        return currentLocale!!
    }

}
