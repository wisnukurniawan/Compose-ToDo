package com.wisnu.kurniawan.composetodolist.foundation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ExtraSmallRadius = 0.dp
val SmallRadius = 4.dp
val MediumRadius = 8.dp
val LargeRadius = 16.dp
val ExtraLargeRadius = 24.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(ExtraSmallRadius),
    small = RoundedCornerShape(SmallRadius),
    medium = RoundedCornerShape(MediumRadius),
    large = RoundedCornerShape(LargeRadius),
    extraLarge = RoundedCornerShape(ExtraLargeRadius)
)
