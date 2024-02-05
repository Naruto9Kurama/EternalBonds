package com.creator.common.utils

import com.creator.common.Constants
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.banner.BannerView


object AdUtil {

    fun setBanner(bannerView: BannerView, bannerAdSize: BannerAdSize=BannerAdSize.BANNER_SIZE_360_57,l:Long=30){
        bannerView.adId = Constants.Ad.BANNER_ID
        bannerView.bannerAdSize = bannerAdSize
        bannerView.setBannerRefresh(l)
        val adParam = AdParam.Builder().build()
        bannerView.loadAd(adParam)
    }


}