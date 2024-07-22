package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap;

import android.content.Context;

import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib.IMobileBilling;

import java.util.List;

public class IapConfig implements IMobileBilling {
    public static String AD_FREE = "product_ad_free";

    @Override
    public String getAdFreeSku() {
        return AD_FREE;
    }

    @Override
    public List<String> getFeatureSkus() {
        return null;
    }

    @Override
    public boolean isProductConsumable(String sku) {
        return false;
    }

    @Override
    public boolean isPurchasedFeature(Context context) {
        return false;
    }
}
