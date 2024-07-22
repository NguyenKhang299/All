package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib;

import com.android.billingclient.api.Purchase;

public interface OnPurchaseListener {
    void onPurchaseSucceeded(Purchase purchase);

    void onPurchaseFailed(String error, int code);
}