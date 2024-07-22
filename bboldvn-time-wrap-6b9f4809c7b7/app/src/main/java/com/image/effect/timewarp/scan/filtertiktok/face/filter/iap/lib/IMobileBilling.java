package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib;

import android.content.Context;

import java.util.List;

public interface IMobileBilling {

    String getAdFreeSku();

    List<String> getFeatureSkus();

    boolean isProductConsumable(String sku);

    boolean isPurchasedFeature(Context context);

}