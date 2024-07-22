package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib;

import android.app.Activity;
import android.content.Context;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.SkuType;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.billingclient.api.BillingClient.BillingResponseCode.OK;

class BillingManager implements PurchasesUpdatedListener {

    static final String TAG = "BillingManager. ";

    private static BillingManager _instance;
    private BillingClient _billingClient;
    private boolean _isServiceConnected;

    private PurchaseCallback _purchaseCallback = null;

    private BillingManager(Context context) {
        _billingClient = BillingClient
                .newBuilder(context.getApplicationContext())
                .enablePendingPurchases()
                .setListener(this)
                .build();
    }

    static BillingManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new BillingManager(context);
        }
        return _instance;
    }

    ///
    ///     CONNECT BEFORE DOING TASK
    ///     connect if needed
    ///

    private void connect(final ConnectionCallback callback) {
        if (_isServiceConnected) {
            callback.onReady();
        } else {
            _billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    Logger.iap(TAG + "Connection. SetupFinished. Code: " + billingResult.getResponseCode());
                    if (billingResult.getResponseCode() == OK) {
                        _isServiceConnected = true;
                        callback.onReady();
                    } else {
                        callback.onError(billingResult.getResponseCode());
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    Logger.iap(TAG + "Connection. Disconnected");
                    _isServiceConnected = false;
                }
            });
        }
    }

    ///
    ///     QUERY PURCHASED ITEMS on app open
    ///     <p>
    ///     queryPurchases vs queryPurchaseHistoryAsync
    ///     Generally, we should use queryPurchases(String skuType), which does not returns expired items. queryPurchaseHistoryAsync returns enabled and disabled items, as you see the documentation like following.
    ///     <p>
    ///     - queryPurchases
    ///     Get purchases details for all the items bought within your app. This method uses a cache of Google Play Store app without initiating a network request.
    ///     That gives you all the current active (non-consumed, non-cancelled, non-expired) purchases for each SKU.
    ///     <p>
    ///     - queryPurchaseHistoryAsync
    ///     Returns the most recent purchase made by the user for each SKU, even if that purchase is expired, canceled, or consumed.
    ///     queryPurchaseHistoryAsync won't do what you need because it will only give you a list of the most recent purchases for each SKU. They may have expired, been cancelled or been consumed, and there's no way to tell.
    ///     Therefore this response can't be used to tell what purchases to apply in your app.
    ///     <p>
    ///     [https://stackoverflow.com/a/54766769/2909384]
    ///     [https://stackoverflow.com/a/49162500/2909384]
    ///

    void queryPurchases(final QueryPurchasesCallback callback) {
        connect(new ConnectionCallback() {
            @Override
            public void onReady() {
                List<Purchase> activePurchases = new ArrayList<>();

                // Query Active Items [SUB]
                _billingClient.queryPurchasesAsync(
                        QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                        (subsPurchasesResult, subPurchases) -> {
                            if (subsPurchasesResult.getResponseCode() == OK) {
                                activePurchases.addAll(subPurchases);
                            }

                            // Query Active Items [INAPP]
                            _billingClient.queryPurchasesAsync(
                                    QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                                    (inappPurchasesResult, inappPurchases) -> {
                                        if (inappPurchasesResult.getResponseCode() == OK) {
                                            activePurchases.addAll(inappPurchases);
                                        }

                                        // Finished.
                                        callback.onFinish(activePurchases);
                                    }
                            );
                        }
                );
            }

            @Override
            public void onError(int code) {
                // Do nothing
            }
        });
    }

    ///
    ///     QUERY SKU DETAILS
    ///

    void querySkuDetails(final List<String> skuList, final @SkuType String skuType, final QuerySkuDetailsCallback callback) {
        connect(new ConnectionCallback() {
            @Override
            public void onReady() {
                SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList)
                        .setType(skuType)
                        .build();

                _billingClient.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == OK && skuDetailsList != null) {
                            HashMap<String, SkuDetails> skuDetailsMap = new HashMap<>();
                            for (SkuDetails skuDetails : skuDetailsList) {
                                skuDetailsMap.put(skuDetails.getSku(), skuDetails);
                            }
                            callback.onFinish(skuDetailsMap);
                        } else {
                            callback.onError(billingResult.getResponseCode());
                        }
                    }
                });
            }

            @Override
            public void onError(int code) {
                callback.onError(code);
            }
        });
    }

    ///
    ///     PURCHASE
    ///

    void purchase(final Activity activity, final SkuDetails buySkuDetail, final PurchaseCallback callback) {
        connect(new ConnectionCallback() {
            @Override
            public void onReady() {
                // Set global callback
                _purchaseCallback = callback;

                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(buySkuDetail)
                        .build();

                _billingClient.launchBillingFlow(activity, flowParams);
            }

            @Override
            public void onError(int code) {
                callback.onError(code);
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchaseList) {
        if (_purchaseCallback == null) {
            return;
        }

        if (billingResult.getResponseCode() == OK) {
            if (purchaseList != null && !purchaseList.isEmpty()) {
                _purchaseCallback.onPurchased(purchaseList);
            } else {
                _purchaseCallback.onError(-8);
            }
        } else {
            _purchaseCallback.onError(billingResult.getResponseCode());
        }

        // Remove global callback
        _purchaseCallback = null;
    }

    ///
    ///     CONSUME
    ///     for Consumable product
    ///

    void consumePurchase(final Purchase purchase) {
        Logger.iap(TAG + "Consume. " + purchase);
        connect(new ConnectionCallback() {
            @Override
            public void onReady() {
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

                _billingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
                    @Override
                    public void onConsumeResponse(BillingResult billingResult, String outToken) {
                        Logger.iap(TAG + "Consume. Response: " + billingResult.getResponseCode() + ":" + billingResult.getDebugMessage());
                    }
                });
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    ///
    ///     ACKNOWLEDGE
    ///     for Non-Consumable product
    ///

    void acknowledgePurchase(final Purchase purchase) {
        Logger.iap(TAG + "Acknowledge. " + purchase);
        connect(new ConnectionCallback() {
            @Override
            public void onReady() {
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();
                    _billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                        @Override
                        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                            Logger.iap(TAG + "Acknowledge. Response: " + billingResult.getResponseCode() + ":" + billingResult.getDebugMessage());
                        }
                    });
                }
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    ///
    ///     CALLBACK
    //

    interface ConnectionCallback {
        void onReady();

        void onError(int code);
    }

    interface QueryPurchasesCallback {
        void onFinish(List<Purchase> activePurchases);
    }

    interface QuerySkuDetailsCallback {
        void onFinish(HashMap<String, SkuDetails> skuDetailsMap);

        void onError(int code);
    }

    interface PurchaseCallback {
        void onPurchased(List<Purchase> purchaseList);

        void onError(int code);
    }

}