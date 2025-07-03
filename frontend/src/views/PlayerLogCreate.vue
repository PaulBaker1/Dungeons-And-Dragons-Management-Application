<template>
  <div class="max-w-4xl mx-auto px-6 py-8 text-white space-y-8">
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">🧑‍⚖️ Create Player Log</h1>
      <router-link :to="`/campaigns/${campaignId}`" class="btn-outline">Back to Campaign</router-link>
    </div>

    <PlayerLogForm @submit="handleSubmit" />
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import PlayerLogForm from "@/views/PlayerLogForm.vue";

const route = useRoute()
const router = useRouter()
const campaignId = route.params.campaignId
const sessionId = route.params.sessionId

const handleSubmit = async (payload) => {
  try {
    await PlayerLogService.create({ ...payload, sessionId })
    router.push(`/campaigns/${campaignId}/sessions/${sessionId}`)
  } catch (err) {
    console.error('❌ Failed to save characters log:', err)
  }
}
</script>

<style scoped>
.btn-outline {
  @apply border border-slate-600 text-slate-300 hover:bg-slate-700 py-2 px-4 rounded text-sm;
}
</style>
