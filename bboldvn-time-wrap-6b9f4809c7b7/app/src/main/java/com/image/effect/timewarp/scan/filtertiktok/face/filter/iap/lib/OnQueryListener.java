package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib;

import com.android.billingclient.api.SkuDetails;

import java.util.HashMap;

public interface OnQueryListener {
    void onSkuQueryResult(HashMap<String, SkuDetails> skuDetailsMap);

    void onSkuQueryFailed();
}