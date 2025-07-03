import axios from 'axios'

export default {
    async getByCampaign(campaignId) {
        const res = await axios.get(`/api/campaigns/${campaignId}/logs`)
        return res.data
    },

    async create(payload) {
        const res = await axios.post(`/api/playerlogs`, payload)
        return res.data
    },

    async search(playerName) {
        const res = await axios.get(`/api/playerlogs/search`, { params: { playerName } })
        return res.data
    },

    async update(id, payload) {
        const res = await axios.patch(`/api/playerlogs/${id}`, payload)
        return res.data
    }
}
