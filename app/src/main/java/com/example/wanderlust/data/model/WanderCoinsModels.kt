package com.example.wanderlust.data.model

data class WanderCoinsReward(
    val titleEn: String,
    val titleKh: String,
    val pointsCost: Int,
    val discountValueUsd: Double,
    val voucherCode: String,
)

data class WanderCoinsAccount(
    val balancePoints: Int = 450,
    val totalEarned: Int = 850,
    val rewardsList: List<WanderCoinsReward> = listOf(
        WanderCoinsReward("$2.00 Off Bus Tickets", "បញ្ចុះតម្លៃ $២.០០ សំបុត្រឡាន", 200, 2.0, "WANDER-2USD"),
        WanderCoinsReward("$5.00 Off Tour Packages", "បញ្ចុះតម្លៃ $៥.០០ កញ្ចប់ទេសចរណ៍", 450, 5.0, "WANDER-5USD"),
    ),
)

object SampleWanderCoins {
    val account = WanderCoinsAccount()
}
