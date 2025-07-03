// src/store/campaignStore.js
import { defineStore } from 'pinia'
import CampaignService from '../services/CampaignService'

export const useCampaignStore = defineStore('campaigns', {
    state: () => ({
        campaigns: [],
        selectedCampaign: null,
        loading: false,
        error: null
    }),

    actions: {
        async fetchCampaigns(tag = null) {
            this.loading = true
            console.groupCollapsed('%c[CampaignStore] fetchCampaigns()', 'color:#93c5fd')
            console.log('→ tag filter:', tag)

            try {
                const res = await CampaignService.getAll(0, 20, tag)
                console.log('✅ Raw response:', res)

                this.campaigns = Array.isArray(res) ? res : res.data || []
                console.log('📦 Loaded campaigns:', this.campaigns)

                this.error = null
            } catch (err) {
                this.error = err.message
                console.error('❌ fetchCampaigns() failed:', err)
            } finally {
                this.loading = false
                console.groupEnd()
            }
        },

        async fetchCampaignById(id) {
            this.loading = true
            console.groupCollapsed(`[CampaignStore] fetchCampaignById(${id})`)

            try {
                const data = await CampaignService.getById(id)
                console.log('🎯 Loaded campaign:', data)

                this.selectedCampaign = data || null
                this.error = null
            } catch (err) {
                this.error = err.message
                console.error(`❌ fetchCampaignById(${id}) failed:`, err)
            } finally {
                this.loading = false
                console.groupEnd()
            }
        },

        async updateCampaignNotes(id, { dmNotesPrivate, dmNotesPublic }) {
            this.loading = true
            console.groupCollapsed(`[CampaignStore] updateCampaignNotes(${id})`)
            try {
                const payload = { dmNotesPrivate, dmNotesPublic }
                console.log('✍️ Updating with payload:', payload)

                await CampaignService.update(id, { dmNotesPrivate, dmNotesPublic })
                await this.fetchCampaignById(id)

                console.log('✅ Notes updated successfully.')
                this.error = null
            } catch (err) {
                this.error = err.message
                console.error(`❌ updateCampaignNotes(${id}) failed:`, err)
            } finally {
                this.loading = false
                console.groupEnd()
            }
        },

        async updateCampaign(id, payload) {
            this.loading = true
            console.groupCollapsed(`[CampaignStore] updateCampaign(${id})`)
            console.log('✍️ Payload:', payload)

            try {
                await CampaignService.update(id, payload)
                await this.fetchCampaignById(id)
                console.log('✅ Campaign updated.')
                this.error = null
            } catch (err) {
                this.error = err.message
                console.error(`❌ updateCampaign(${id}) failed:`, err)
            } finally {
                this.loading = false
                console.groupEnd()
            }
        },

        async deleteCampaign(id) {
            this.loading = true
            console.groupCollapsed(`[CampaignStore] deleteCampaign(${id})`)

            try {
                await CampaignService.delete(id)
                this.campaigns = this.campaigns.filter(c => c.id !== id)
                console.log('🗑️ Deleted campaign:', id)
                this.error = null
            } catch (err) {
                this.error = err.message
                console.error(`❌ deleteCampaign(${id}) failed:`, err)
            } finally {
                this.loading = false
                console.groupEnd()
            }
        }
    }
})
