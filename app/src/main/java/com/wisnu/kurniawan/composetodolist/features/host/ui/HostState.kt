package com.wisnu.kurniawan.composetodolist.features.host.ui

import com.wisnu.kurniawan.composetodolist.model.Theme
import javax.annotation.concurrent.Immutable

@Immutable
data class HostState(val theme: Theme = Theme.SYSTEM)
