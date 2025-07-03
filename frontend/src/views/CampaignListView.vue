<template>
  <Login v-if="!loggedIn" @login-success="fetchCampaigns" />
  <div v-else>
    <!-- Filters & Campaign Cards as before -->
  </div>
</template>
<script>
import { ref, onMounted } from 'vue'
import Login from '@/components/auth/Login.vue'
import axios from 'axios'

export default {
  components: { Login },
  setup() {
    const loggedIn = ref(!!localStorage.getItem('basicCreds'))
    const campaigns = ref([])
    async function fetchCampaigns() {
      loggedIn.value = true
      const res = await axios.get('http://localhost:8080/api/campaigns')
      campaigns.value = res.data
    }
    onMounted(() => { if (loggedIn.value) fetchCampaigns() })
    return { loggedIn, campaigns, fetchCampaigns }
  }
}
</script>