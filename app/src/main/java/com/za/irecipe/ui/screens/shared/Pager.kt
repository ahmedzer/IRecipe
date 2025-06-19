package com.za.irecipe.ui.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
        ) {

            BannerWithImage(
                title = pages[it].title,
                text = pages[it].description,
                image = pages[it].image,
                isClickable = true,
                onClick = {
                    pages[it].onClick()
                },
                modifier = Modifier.padding(5.dp),
                showArrow = true
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            pages.forEachIndexed { index, bannerPage ->
                Box(
                    modifier = Modifier
                        .background(
                            color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.LightGray,
                            shape = CircleShape
                        )
                        .size(10.dp)
                )
                if (index != pages.size - 1) {
                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
}