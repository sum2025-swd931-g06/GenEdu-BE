package com.genedu.subscription.utils;

public final class Constants {
    public static final class ErrorCode {
        // Subscription Plan
        public static final String INVALID_SUBSCRIPTION_PLAN_NAME = "INVALID_SUBSCRIPTION_PLAN_NAME";
        public static final String INVALID_SUBSCRIPTION_PLAN_ID = "INVALID_SUBSCRIPTION_PLAN_ID";
        public static final String DUPLICATED_SUBSCRIPTION_PLAN_NAME = "DUPLICATED_SUBSCRIPTION_PLAN";
        public static final String INVALID_SUBSCRIPTION_PLAN_PRICE = "INVALID_SUBSCRIPTION_PLAN_PRICE";
        public static final String INVALID_SUBSCRIPTION_PLAN_DURATION = "INVALID_SUBSCRIPTION_PLAN_DURATION";
        public static final String SUBSCRIPTION_PLAN_NOT_FOUND = "SUBSCRIPTION_PLAN_NOT_FOUND";
        public static final String STRIPE_API_FAILED = "STRIPE_API_FAILED";

        // User Billing Account
        public static final String USER_BILLING_ACCOUNT_NOT_FOUND = "USER_BILLING_ACCOUNT_NOT_FOUND";
        public static final String USER_BILLING_ACCOUNT_CREATION_FAILED = "USER_BILLING_ACCOUNT_CREATION_FAILED";

        public static final String CREATE_FAILED = "CREATE_FAILED";
        public static final String UPDATE_FAILED = "UPDATE_FAILED";
        public static final String DELETE_FAILED = "DELETE_FAILED";
        public static final String UNAUTHENTICATED = "UNAUTHENTICATED";
    }
}
