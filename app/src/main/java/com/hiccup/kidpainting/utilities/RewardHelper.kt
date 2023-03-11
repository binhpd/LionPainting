package com.hiccup.kidpainting.utilities

class RewardHelper {
    var rewardAccessCollection = -1
    fun setRewardCollection(reward: Int) {
        rewardAccessCollection = reward
    }

    companion object {
        private var reward: RewardHelper? = null
        val instance: RewardHelper
            get() {
                if (reward == null) {
                    reward = RewardHelper()
                }
                return reward!!
            }
    }
}