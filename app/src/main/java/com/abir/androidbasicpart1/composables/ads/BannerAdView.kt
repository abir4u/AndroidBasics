package com.abir.androidbasicpart1.composables.ads

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView(context: Context, adUnitId: String) {
    AndroidView(
        factory = { ctx ->
            AdView(ctx).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                val adRequest = AdRequest.Builder().build()
                loadAd(adRequest)
            }
        },
        update = { view ->
            // Refresh ad if needed
        }
    )
}