package com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileBilling {

    private static final String EXCEPTION_INIT = "MobileBilling must be initialized by calling MobileBilling.init(<? implements IMobileBilling>.class); on Application.";
    private static final String EXCEPTION_BASE64 = "BASE_64_ENCODED_PUBLIC_KEY is invalid: ";
    private static final String EXCEPTION_NULL_LISTENER = "Listener must not be null";

    private static IMobileBilling iMobileBilling;
    private static boolean isTestMode = false;
    private static boolean isIntegrated = false;

    // Initial

    public static void init(Class<? extends IMobileBilling> mobileBillingClazz) {
        Logger.iap("Initializing...");
        try {
            iMobileBilling = mobileBillingClazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(EXCEPTION_INIT);
        }
        isIntegrated = true;
    }

    public static void queryPurchases(Context context) {
        MobileBillingImpl.queryPurchases(context);
    }

    public static void enableTest(boolean testMode) {
        isTestMode = testMode;
    }

    public static void enableLog(boolean log) {
    }

    // Private

    static boolean isIntegrated() {
        return isIntegrated;
    }

    static boolean isTestMode() {
        return isTestMode;
    }

    static String getAdFreeSku() {
        return iMobileBilling.getAdFreeSku();
    }

    private static List<String> getFeatureSkus() {
        return iMobileBilling.getFeatureSkus();
    }

    static List<String> getAllSkus() {
        ArrayList<String> allSku = new ArrayList<>();
        if (!TextUtils.isEmpty(getAdFreeSku())) {
            allSku.add(getAdFreeSku());
        }
        if (getFeatureSkus() != null) {
            allSku.addAll(getFeatureSkus());
        }
        return allSku;
    }

    static boolean isConsumable(String sku) {
        return iMobileBilling.isProductConsumable(sku);
    }

    // Validate

    private static void assertImplements() {
        if (iMobileBilling == null) {
            throw new RuntimeException(EXCEPTION_INIT);
        }
    }

    private static void assertNull(Object listener) {
        if (listener == null) {
            throw new RuntimeException(EXCEPTION_NULL_LISTENER);
        }
    }

    /**
     * BILLING
     */

    public static boolean isPurchasedSku(Context context, String sku) {
        assertImplements();
        return IapData.isPurchased(context, sku);
    }

    public static boolean isPurchasedFeature(Context context) {
        assertImplements();
        return iMobileBilling.isPurchasedFeature(context);
    }

    public static void querySkuSubsDetails(final Context context, final List<String> skuList, final OnQueryListener listener) {
        assertImplements();
        assertNull(listener);
        MobileBillingImpl.querySkuDetails(context, skuList, BillingClient.SkuType.SUBS, listener);
    }

    public static void querySkuInAppDetails(final Context context, final List<String> skuList, final OnQueryListener listener) {
        assertImplements();
        assertNull(listener);
        MobileBillingImpl.querySkuDetails(context, skuList, BillingClient.SkuType.INAPP, listener);
    }

    public static void purchase(final android.app.Activity activity, final SkuDetails buySkuDetail, final OnPurchaseListener onPurchaseListener) {
        assertImplements();
        assertNull(onPurchaseListener);
        if (isTestMode()) {
            MobileBillingImpl.purchaseTest(activity, buySkuDetail, onPurchaseListener);
            return;
        }

        MobileBillingImpl.purchase(activity, buySkuDetail, onPurchaseListener);
    }

    public static void purchaseSkuInApp(final android.app.Activity activity, final String sku, final OnPurchaseListener onPurchaseListener) {
        assertImplements();
        assertNull(onPurchaseListener);
        if (isTestMode()) {
            MobileBillingImpl.purchaseTest(activity, sku, onPurchaseListener);
            return;
        }

        MobileBillingImpl.purchaseSku(activity, sku, BillingClient.SkuType.INAPP, onPurchaseListener);
    }

    public static void purchaseSkuSubs(final android.app.Activity activity, final String sku, final OnPurchaseListener onPurchaseListener) {
        assertImplements();
        assertNull(onPurchaseListener);
        if (isTestMode()) {
            MobileBillingImpl.purchaseTest(activity, sku, onPurchaseListener);
            return;
        }

        MobileBillingImpl.purchaseSku(activity, sku, BillingClient.SkuType.SUBS, onPurchaseListener);
    }

    public static boolean isDeviceSupported(Context context) {
        return MobileBilling.isSupportBilling(context);
    }

    /**
     * UTILS
     */

    private static Map<String, String> PERIOD_MAPPING = new HashMap<String, String>() {{
        put("W", "week");
        put("M", "month");
        put("Y", "year");
    }};

    public static String getPricePerPeriod(SkuDetails skuDetails) {
        if (skuDetails == null) {
            return "";
        }

        if (TextUtils.equals(skuDetails.getType(), BillingClient.SkuType.INAPP)) {
            return skuDetails.getPrice();
        }

        try {
            String subscriptionPeriod = skuDetails.getSubscriptionPeriod();
            int period = Integer.parseInt(subscriptionPeriod.substring(1, subscriptionPeriod.length() - 1));
            String unitKey = subscriptionPeriod.substring(subscriptionPeriod.length() - 1);
            String unit = PERIOD_MAPPING.get(unitKey) + (period > 1 ? "s" : "");
            return String.format("%s/%s%s", skuDetails.getPrice(), (period > 1 ? period + " " : ""), unit);
        } catch (Exception ignored) {
        }
        return skuDetails.getPrice();
    }

    public static int calcSavingPercent(String skuTotal, HashMap<String, SkuDetails> skuDetailsMap) {
        try {
            long totalPrice = skuDetailsMap.get(skuTotal).getPriceAmountMicros();
            long otherPrice = 0;
            for (String sku : skuDetailsMap.keySet()) {
                if (!sku.equals(skuTotal)) {
                    otherPrice += skuDetailsMap.get(sku).getPriceAmountMicros();
                }

            }

            return 100 - (int) (100 * totalPrice / otherPrice);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int calcSavingPercentPerYear(long monthlyPrice, long yearlyPrice) {
        if (monthlyPrice == 0) {
            return 0;
        }

        long totalYearly = 12 * monthlyPrice;
        return 100 - (int) (100 * yearlyPrice / totalYearly);
    }

    /**
     * https://developer.android.com/reference/com/android/billingclient/api/SkuDetails#getpriceamountmicros
     *
     * @param yearlySkuDetails
     * @return
     */
    @Deprecated
    public static String calcPriceMonthly(SkuDetails yearlySkuDetails) {
        Currency currency = Currency.getInstance(yearlySkuDetails.getPriceCurrencyCode());

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(currency);

        long priceAmount = yearlySkuDetails.getPriceAmountMicros() / 1_000_000 / 12;

        return format.format(priceAmount);
    }

    /**
     * Check device is supported In App Billing
     *
     * @param context
     * @return
     */

    static boolean isSupportBilling(Context context) {
        try {
            Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            return !context.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }


}
