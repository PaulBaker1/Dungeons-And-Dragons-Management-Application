import axios from 'axios'

// Base URL used for all API calls – our Vite proxy redirects /api to our backend.
const baseUrl = '/api/dnd'

export const DnDService = {
    // Fetch individual entries by index.
    getSpell: (index) => axios.get(`${baseUrl}/spells/${index}`).then(r => r.data),
    getMonster: (index) => axios.get(`${baseUrl}/monsters/${index}`).then(r => r.data),
    getBackground: (index) => axios.get(`${baseUrl}/backgrounds/${index}`).then(r => r.data),
    getRace: (index) => axios.get(`${baseUrl}/races/${index}`).then(r => r.data),
    getClass: (index) => axios.get(`${baseUrl}/classes/${index}`).then(r => r.data),
    getEquipment: (index) => axios.get(`${baseUrl}/equipment/${index}`).then(r => r.data),
    getLanguage: (index) => axios.get(`${baseUrl}/languages/${index}`).then(r => r.data),
    getProficiency: (index) => axios.get(`${baseUrl}/proficiencies/${index}`).then(r => r.data),
    getMagicItem: (index) => axios.get(`${baseUrl}/magic-items/${index}`).then(r => r.data),

    // Fetch full lists – note the type parameter controls which list is returned.
    getList: (type) => axios.get(`${baseUrl}/list/${type}`).then(r => r.data),

    // Fetch all SRD data (if you need to load everything at once).
    getAllSrdData: () => axios.get(`${baseUrl}/fetch-all`).then(r => r.data),
}
