// src/store/demoStore.js
import { defineStore } from 'pinia'
import axios from 'axios'

export const useDemoStore = defineStore('demo', {
    state: () => ({
        data: null,
        loading: false,
        error: null
    }),
    actions: {
        async fetchDemo() {
            this.loading = true
            this.error = null
            try {
                const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/demo`)
                this.data = response.data
            } catch (err) {
                this.error = err.message || 'Unknown error'
            } finally {
                this.loading = false
            }
        }
    }
})
