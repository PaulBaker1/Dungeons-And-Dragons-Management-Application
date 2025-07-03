<template>
  <div class="p-6">
    <button @click="$router.back()" class="text-indigo-400 mb-4 hover:underline">← Back</button>

    <div v-if="loading">Loading...</div>
    <div v-else-if="error" class="text-red-500">{{ error }}</div>
    <div v-else-if="!campaign" class="text-slate-400">Campaign not found.</div>
    <div v-else class="text-white">
      <h1 class="text-3xl font-bold mb-2">{{ campaign.name }}</h1>
      <p>Status: {{ campaign.status }}</p>
      <p>Created: {{ campaign.createdAt }}</p>
      <p class="mt-2">{{ campaign.description }}</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCampaignStore } from '@/store/campaignStore.js'

const route = useRoute()
const store = useCampaignStore()
const { selectedCampaign: campaign, loading, error } = store

onMounted(() => {
  store.fetchCampaignById(route.params.id)
})
</script>
