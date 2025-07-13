package com.za.irecipe.ui.screens.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.za.irecipe.ui.model.BannerPage

@Composable
fun BannerPager(
    pages: List<BannerPage>,
    modifier: Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { pages.size }
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth()
        ) { pageIndex ->
            BannerWithImage(
                title = pages[pageIndex].title,
                text = pages[pageIndex].description,
                image = pages[pageIndex].image,
                isClickable = true,
                onClick = {
                    pages[pageIndex].onClick()
                },
                modifier = Modifier.padding(5.dp),
                showArrow = true
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            pages.forEachIndexed { index, _ ->
                val isSelected = pagerState.currentPage == index

                val animatedColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                    label = "DotColor"
                )

                val animatedWidth by animateDpAsState(
                    targetValue = if (isSelected) 20.dp else 10.dp,
                    label = "DotWidth"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(
                            color = animatedColor,
                            shape = CircleShape
                        )
                        .size(width = animatedWidth, height = 10.dp)
                )
            }
        }
    }
}