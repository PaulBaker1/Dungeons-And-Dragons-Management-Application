// src/store/questStore.js
import { defineStore } from 'pinia'
import QuestService from '@/services/QuestService'

export const useQuestStore = defineStore('quest', {
    state: () => ({
        quests: []
    }),
    actions: {
        async fetchByCampaign(campaignId) {
            const res = await QuestService.getByCampaign(campaignId)
            this.quests = res
        }
    }
})
