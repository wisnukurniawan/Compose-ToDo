package com.wisnu.kurniawan.composetodolist.foundation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ExtraSmallRadius = 4.dp
val SmallRadius = 8.dp
val MediumRadius = 16.dp
val LargeRadius = 24.dp
val ExtraLargeRadius = 28.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(ExtraSmallRadius),
    small = RoundedCornerShape(SmallRadius),
    medium = RoundedCornerShape(MediumRadius),
    large = RoundedCornerShape(LargeRadius),
    extraLarge = RoundedCornerShape(ExtraLargeRadius)
)
