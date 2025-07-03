import axios from 'axios';

const API = `${import.meta.env.VITE_API_BASE_URL}/api/campaigns`;

export default {
    async getAll(page = 0, size = 20, tag = null) {
        const params = { page, size }
        if (tag) params.tag = tag

        const res = await axios.get(API, { params })

        console.log('[API] Campaigns response:', res.data)
        return res.data // ✅ FIXED — not res.data.data
    },

    async getById(id) {
        const res = await axios.get(`${API}/${id}`);
        return res.data;
    },

    async update(id, payload) {
        return axios.put(`${API}/${id}`, payload)
    },

    async delete(id) {
        const res = await axios.delete(`${API}/${id}`);
    }
}
