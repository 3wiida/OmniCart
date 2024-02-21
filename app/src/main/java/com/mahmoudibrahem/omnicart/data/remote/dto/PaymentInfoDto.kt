package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.PaymentInfo

data class PaymentInfoDto(
    val amount: Double,
    val customer: String,
    val ephemeralKey: String,
    val paymentIntent: String
) {
    fun toPaymentInfo(): PaymentInfo {
        return PaymentInfo(
            customerID = customer,
            ephemeralKey = ephemeralKey,
            clientSecret=paymentIntent,
            amount = amount
        )
    }
}