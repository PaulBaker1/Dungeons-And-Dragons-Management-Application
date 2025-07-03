// src/store/sessionStore.js

import { defineStore } from 'pinia'
import SessionService from '@/services/SessionService'

export const useSessionStore = defineStore('sessions', {
    state: () => ({
        sessions: [],
        loading: false,
        error: null
    }),

    actions: {
        async fetchByCampaign(campaignId) {
            this.loading = true
            try {
                this.sessions = await SessionService.getByCampaignId(campaignId)
                this.error = null
            } catch (err) {
                this.error = err.message
                console.error('❌ fetchByCampaign failed:', err)
            } finally {
                this.loading = false
            }
        }
    }
})
