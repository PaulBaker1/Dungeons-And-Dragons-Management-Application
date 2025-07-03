import axios from 'axios'

export default {
    async getAll() {
        const res = await axios.get('/api/characters')
        return res.data
    },

    async remove(id) {
        await axios.delete(`/api/characters/${id}`)
    },

    async create(characterData) {
        const res = await axios.post('/api/characters/create', characterData)
        return res.data
    },

    getUnassigned: () => axios.get('/api/characters/unassigned').then(res => res.data),

    assignToCampaign: (campaignId, characterId) =>
        axios.post(`/api/campaigns/${campaignId}/characters/${characterId}`),

    // ✅ Add this function to fix your error:
    removeFromCampaign: (characterId) =>
        axios.delete(`/api/characters/${characterId}/campaign`)
}
