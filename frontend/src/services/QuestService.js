import axios from 'axios';

const API_URL = '/api/campaigns';  // Backend API URL for campaigns

const QuestService = {
    // Fetch all quests across campaigns (if required in the future)
    // This function can be removed if it's not used
    async getAll() {
        try {
            const response = await axios.get('/api/quests'); // Ensure backend supports this endpoint
            return response.data;
        } catch (error) {
            console.error('Error fetching all quests:', error);
            throw error;
        }
    },

    // Get all quests for a specific campaign
    async getQuestsByCampaign(campaignId) {
        try {
            const response = await axios.get(`${API_URL}/${campaignId}/quests`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching quests for campaign ${campaignId}:`, error);
            throw error;
        }
    },

    // Create a new quest within a specific campaign
    async createInCampaign(campaignId, quest) {
        try {
            const response = await axios.post(`${API_URL}/${campaignId}/quests`, quest, {
                headers: { 'Content-Type': 'application/json' }
            });
            return response.data;
        } catch (error) {
            console.error(`Error creating quest in campaign ${campaignId}:`, error);
            throw error;
        }
    },

    // Update an existing quest within a specific campaign
    async updateInCampaign(campaignId, questId, quest) {
        try {
            const response = await axios.put(`${API_URL}/${campaignId}/quests/${questId}`, quest, {
                headers: { 'Content-Type': 'application/json' }
            });
            return response.data;
        } catch (error) {
            console.error(`Error updating quest ${questId} in campaign ${campaignId}:`, error);
            throw error;
        }
    },

    // Get a specific quest by campaignId and questId
    async getQuestById(campaignId, questId) {
        try {
            const response = await axios.get(`${API_URL}/${campaignId}/quests/${questId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching quest ${questId} for campaign ${campaignId}:`, error);
            throw error;
        }
    },

    // Delete a quest from a specific campaign
    async remove(campaignId, questId) {
        try {
            const response = await axios.delete(`${API_URL}/${campaignId}/quests/${questId}`);
            return response.data;
        } catch (error) {
            console.error(`Error deleting quest ${questId} from campaign ${campaignId}:`, error);
            throw error;
        }
    }
};

export default QuestService;
