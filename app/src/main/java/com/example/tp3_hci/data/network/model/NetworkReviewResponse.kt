package com.example.tp3_hci.data.network.model

import com.example.tp3_hci.data.model.ReviewResponse
import com.google.gson.annotations.SerializedName
import java.util.*

class NetworkReviewResponse (
    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("date"   ) var date   : Date?    = null,
    @SerializedName("score"  ) var score  : Int?    = null,
    @SerializedName("review" ) var review : String? = null
) {
    fun asModel() : ReviewResponse {
        return ReviewResponse(
            id = id,
            date = date,
            score = score,
            review = review,
        )
    }
}