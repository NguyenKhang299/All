package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.billingclient.api.Purchase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class IapData {

    static boolean isPurchased(Context context, String sku) {
        if (TextUtils.isEmpty(sku)) {
            return false;
        }
        return !TextUtils.isEmpty(AppStorage.getPurchase(context, sku));
    }

    /**
     * Check to remove Deactivated SKUs
     * And Keep Update Activating SKUs
     */

    static void updatePurchasedData(Context context, List<Purchase> activePurchaseItems) {
        for (String localSku : MobileBilling.getAllSkus()) {
            if (TextUtils.isEmpty(localSku)) {
                continue;
            }

            Purchase foundPurchase = findPurchase(activePurchaseItems, localSku);

            if (foundPurchase != null) {
                savePurchasedData(context, localSku, foundPurchase);
            } else {
                removePurchasedData(context, localSku);
            }
        }
    }

    static void savePurchasedData(Context context, String sku, Purchase purchase) {
//        Logger.iap("Data. Save. " + purchase);
        if (purchase == null || TextUtils.isEmpty(sku)) {
            return;
        }

        AppStorage.savePurchase(context, sku, purchase.getOriginalJson());
    }

    static void removePurchasedData(Context context, String sku) {
//        Logger.iap("Data. Remove: " + sku);
        if (TextUtils.isEmpty(sku)) {
            return;
        }

        AppStorage.removePurchase(context, sku);
    }

    private static Purchase findPurchase(List<Purchase> purchases, String sku) {
        for (Purchase purchase : purchases) {
            try {
                if (TextUtils.equals(purchase.getProducts().get(0), sku)) { // TODO: Note .getProducts().get(0)
                    return purchase;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    static List<String> getPurchasedSkus(Context context) {
        List<String> purchasedSkus = new ArrayList<>();
        if (MobileBilling.isIntegrated()) {
            for (String localSku : MobileBilling.getAllSkus()) {
                if (TextUtils.isEmpty(localSku)) {
                    continue;
                }

                if (!TextUtils.isEmpty(AppStorage.getPurchase(context, localSku))) {
                    purchasedSkus.add(localSku);
                }
            }
        }
        return purchasedSkus;
    }

    protected static class AppStorage {
        private static final String IAB_SHARED_PREFIX = "pf_iap_";

        //

        private static SharedPreferences getSharedPreferences(Context context) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

        static void putString(Context context, String key, String value) {
            getSharedPreferences(context).edit().putString(key, value).commit();
        }

        static String getString(Context context, String key) {
            return getSharedPreferences(context).getString(key, "");
        }

        static void putStringSet(Context context, String key, Set<String> set) {
            getSharedPreferences(context).edit().putStringSet(key, set).commit();
        }

        static synchronized void putStringSetValue(Context context, String key, String value) {
            Set<String> stringSet = getStringSet(context, key);
            stringSet.add(value);

            deleteKey(context, key);
            putStringSet(context, key, stringSet);
        }

        static Set<String> getStringSet(Context context, String key) {
            return getSharedPreferences(context).getStringSet(key, new HashSet<String>());
        }

        static void putBoolean(Context context, String key, boolean value) {
            getSharedPreferences(context).edit().putBoolean(key, value).commit();
        }

        static boolean getBoolean(Context context, String key) {
            return getSharedPreferences(context).getBoolean(key, false);
        }

        static long getLong(Context context, String key) {
            return getSharedPreferences(context).getLong(key, 0L);
        }

        static void putLong(Context context, String key, long value) {
            getSharedPreferences(context).edit().putLong(key, value).commit();
        }

        static boolean containKey(Context context, String key) {
            return getSharedPreferences(context).contains(key);
        }

        private static void deleteKey(Context context, String key) {
            getSharedPreferences(context).edit().remove(key).commit();
        }

        // IAB SKU

        static void savePurchase(Context context, String sku, String purchaseJson) {
            String key = genSkuKey(sku);
            if (TextUtils.isEmpty(key)) {
                return;
            }
            if (TextUtils.isEmpty(purchaseJson)) {
                deleteKey(context, key);
            }

            putString(context, key, purchaseJson);
        }

        static void removePurchase(Context context, String sku) {
            String key = genSkuKey(sku);
            if (TextUtils.isEmpty(key)) {
                return;
            }

            deleteKey(context, key);
        }

        static String getPurchase(Context context, String sku) {
            String key = genSkuKey(sku);
            if (TextUtils.isEmpty(key)) {
                return "";
            }

            return getString(context, key);
        }

        private static String genSkuKey(String sku) {
            if (TextUtils.isEmpty(sku)) {
                return null;
            }
            return IAB_SHARED_PREFIX + sku;
        }

    }


}
