package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.SkuType;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.Purchase.PurchaseState;
import com.android.billingclient.api.SkuDetails;
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.android.billingclient.api.BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED;
import static com.android.billingclient.api.BillingClient.BillingResponseCode.USER_CANCELED;

class MobileBillingImpl {

    // BillingResponseCode {
    //     int SERVICE_TIMEOUT = -3;
    //     int FEATURE_NOT_SUPPORTED = -2;
    //     int SERVICE_DISCONNECTED = -1;
    //     int OK = 0;
    //     int USER_CANCELED = 1;
    //     int SERVICE_UNAVAILABLE = 2;
    //     int BILLING_UNAVAILABLE = 3;
    //     int ITEM_UNAVAILABLE = 4;
    //     int DEVELOPER_ERROR = 5;
    //     int ERROR = 6;
    //     int ITEM_ALREADY_OWNED = 7;
    //     int ITEM_NOT_OWNED = 8;
    // }

    // PurchaseState {
    //     int UNSPECIFIED_STATE = 0;
    //     int PURCHASED = 1;
    //     int PENDING = 2;
    // }

    private static final String MSG_CANNOT_FIND_PRODUCT = "Cannot find the product. Code ";
    private static final String MSG_CANNOT_PROCESS = "Cannot process your purchase. Code ";
    private static final String MSG_ITEM_OWNED = "You have already purchased this item. Please restart the app. Code ";
    private static final String MSG_CANCELED = "You have cancelled the purchase. Code ";
    private static final String MSG_CANNOT_VERIFY = "Cannot verify your purchase. Code ";
    private static final String MSG_PENDING = "You purchase is pending. Code ";
    private static final String MSG_SUCCEEDED = "Payment Successful";

    private static String BASE_64_ENCODED_PUBLIC_KEY = Const.IABBase64;

    ///
    ///     QUERY ACTIVE PURCHASES
    ///

    static void queryPurchases(final Context context) {
        BillingManager.getInstance(context).queryPurchases(activePurchases -> {
            Logger.iap("QueryPurchases. [" + activePurchases.size() + "]: " + activePurchases);

            // Remove Pending | Consume | Acknowledge if needed
            Iterator<Purchase> iterator = activePurchases.iterator();
            while (iterator.hasNext()) {
                Purchase purchase = iterator.next();
                if (purchase.getPurchaseState() != PurchaseState.PURCHASED) {
                    iterator.remove();
                } else if (MobileBilling.isConsumable(purchase.getProducts().get(0))) { // TODO: Note .getProducts().get(0)
                    iterator.remove();
                    BillingManager.getInstance(context).consumePurchase(purchase);
                } else if (!purchase.isAcknowledged()) {
                    BillingManager.getInstance(context).acknowledgePurchase(purchase);
                }
            }

            // Update Local Data
            Logger.iap("UpdatePurchasedData. [" + activePurchases.size() + "]: " + activePurchases);
            IapData.updatePurchasedData(context, activePurchases);

            // TODO S2S Check
            /*for (final Purchase purchaseItem : activePurchases) {
                IapVerifier.verifyPurchaseApi(context, purchaseItem, new IapVerifier.OnPurchaseVerifyListener() {
                    @Override
                    public void onPurchaseVerified(boolean isValid) {
                        if (!isValid) {
                            IapData.removePurchasedData(context, purchaseItem.getSku());
                        }
                    }
                });
            }*/
        });
    }

    ///
    ///     QUERY SKU DETAILS
    ///

    static void querySkuDetails(final Context context, final List<String> skuList, final @SkuType String skuType, final OnQueryListener listener) {
        BillingManager.getInstance(context).querySkuDetails(skuList, skuType, new BillingManager.QuerySkuDetailsCallback() {
            @Override
            public void onFinish(HashMap<String, SkuDetails> skuDetailsMap) {
                listener.onSkuQueryResult(skuDetailsMap);
            }

            @Override
            public void onError(int code) {
                listener.onSkuQueryFailed();
            }
        });
    }

    ///
    ///     PURCHASE A PRODUCT
    ///

    static void purchaseSku(final Activity activity, final String sku, @BillingClient.SkuType String skuType, final OnPurchaseListener onPurchaseListener) {
        BillingManager.getInstance(activity).querySkuDetails(Collections.singletonList(sku), skuType, new BillingManager.QuerySkuDetailsCallback() {
            @Override
            public void onFinish(HashMap<String, SkuDetails> skuDetailsMap) {
                if (!skuDetailsMap.containsKey(sku)) {
                    onError(-4);
                    return;
                }

                SkuDetails skuDetails = skuDetailsMap.get(sku);
                purchase(activity, skuDetails, onPurchaseListener);
            }

            @Override
            public void onError(int code) {
                if (onPurchaseListener != null) {
                    onPurchaseListener.onPurchaseFailed(MSG_CANNOT_FIND_PRODUCT + code, code);
                }
            }
        });
    }

    static void purchase(final android.app.Activity activity, final SkuDetails buySkuDetail, final OnPurchaseListener onPurchaseListener) {
        BillingManager.getInstance(activity).purchase(activity, buySkuDetail, new BillingManager.PurchaseCallback() {
            @Override
            public void onPurchased(List<Purchase> purchasedList) {
                handlePurchase(activity, onPurchaseListener, purchasedList);
            }

            @Override
            public void onError(int code) {
                String errorMsg = "";
                if (code == ITEM_ALREADY_OWNED) {
                    errorMsg = MSG_ITEM_OWNED + code;
                } else if (code == USER_CANCELED) {
                    errorMsg = MSG_CANCELED + code;
                } else {
                    errorMsg = MSG_CANNOT_PROCESS + code;
                }

                onPurchaseListener.onPurchaseFailed(errorMsg, code);
                Logger.iap("PurchaseFailed. " + errorMsg);
            }
        });
    }

    private static void handlePurchase(final android.app.Activity activity, final OnPurchaseListener onPurchaseListener, List<Purchase> purchasedList) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        showDialog(activity, progressDialog);

        for (final Purchase purchase : purchasedList) {
            int purchaseState = purchase.getPurchaseState();
            if (purchaseState == PurchaseState.PURCHASED) {
                // Client verify
                final String sku = purchase.getProducts().get(0); // TODO: Note .getProducts().get(0)
                if (!IapSecurity.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, purchase.getOriginalJson(), purchase.getSignature())) {
                    onPurchaseListener.onPurchaseFailed(MSG_CANNOT_VERIFY + 103, 103);
                    Logger.iap("PurchaseFailed. " + MSG_CANNOT_VERIFY + 103);
                    continue;
                }

                // Purchased
                if (MobileBilling.isConsumable(sku)) {
                    // Don't save local data. Just consume
                    BillingManager.getInstance(activity).consumePurchase(purchase);
                } else {
                    // Save Local data & Acknowledge
                    IapData.savePurchasedData(activity, sku, purchase);
                    BillingManager.getInstance(activity).acknowledgePurchase(purchase);
                }

                // TODO S2S verify
//                IapVerifier.verifyPurchaseApi(activity, purchase, new IapVerifier.OnPurchaseVerifyListener() {
//                    @Override
//                    public void onPurchaseVerified(boolean isValid) {
//                        if (isValid) {
                            onPurchaseListener.onPurchaseSucceeded(purchase);
                            Logger.iap("PurchaseSucceeded. " + purchase);
                            Toast.makeText(activity, MSG_SUCCEEDED, Toast.LENGTH_LONG).show();
//                        } else {
//                            IapData.removePurchasedData(activity, sku);
//                            onPurchaseListener.onPurchaseFailed(MSG_CANNOT_VERIFY + 104, 104);
//                            Logger.iap("PurchaseFailed. " + MSG_CANNOT_VERIFY + 104);
//                        }
//                    }
//                });
            } else if (purchaseState == PurchaseState.PENDING) {
                onPurchaseListener.onPurchaseFailed(MSG_PENDING + purchaseState, 102);
            } else {
                onPurchaseListener.onPurchaseFailed(MSG_CANNOT_VERIFY + 100, 100);
            }
        }

        dismissDialog(activity, progressDialog);
    }

    private static void showDialog(Activity activity, final ProgressDialog progressDialog) {
        progressDialog.setTitle("Loading…");
        progressDialog.setCanceledOnTouchOutside(false);
        activity.runOnUiThread(() -> {
            try {
                progressDialog.show();
            } catch (Exception ignored) {
            }
        });
    }

    private static void dismissDialog(Activity activity, final ProgressDialog progressDialog) {
        activity.runOnUiThread(() -> {
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception ignored) {
            }
        });
    }

    ///
    ///     TEST MODE
    ///

    static void purchaseTest(final android.app.Activity activity, final SkuDetails buySkuDetail, final OnPurchaseListener onPurchaseListener) {
        purchaseTest(activity, buySkuDetail.getSku(), onPurchaseListener);
    }

    static void purchaseTest(final android.app.Activity activity, final String sku, final OnPurchaseListener onPurchaseListener) {
        new AlertDialog.Builder(activity)
                .setTitle("IN-APP BILLING - TEST MODE")
                .setMessage("Test mode always success. \nClick OK to continue... \n")
                .setPositiveButton("OK", (dialog, which) -> {
                    IapData.AppStorage.savePurchase(activity, sku, "{}");

                    onPurchaseListener.onPurchaseSucceeded(null);
                    Toast.makeText(activity, MSG_SUCCEEDED, Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> onPurchaseListener.onPurchaseFailed(MSG_CANCELED + USER_CANCELED, USER_CANCELED))
                .show();
    }

}