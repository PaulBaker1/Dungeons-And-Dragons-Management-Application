import axios from 'axios'

export default {
    async create(campaignId, payload) {
        const res = await axios.post(`/api/campaigns/${campaignId}/sessions`, payload)
        return res.data
    },

    async getByCampaignId(campaignId) {
        const res = await axios.get(`/api/campaigns/${campaignId}/sessions`)
        return res.data
    },

    async getById(campaignId, sessionId) {
        const res = await axios.get(`/api/campaigns/${campaignId}/sessions/${sessionId}`)
        return res.data
    },

    async update(campaignId, sessionId, payload) {
        await axios.put(`/api/campaigns/${campaignId}/sessions/${sessionId}`, payload)
    },

    async remove(campaignId, sessionId) {
        await axios.delete(`/api/campaigns/${campaignId}/sessions/${sessionId}`)
    }
}
