<template>
  <div class="max-w-5xl mx-auto p-6 space-y-6 text-white">
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">🧙 Players in Campaign</h1>
      <router-link :to="`/campaigns/${campaignId}`" class="btn-outline">← Back</router-link>
    </div>

    <!-- 🔍 Search -->
    <input v-model="search" class="input w-full" placeholder="Search by player name..." />

    <!-- 📋 Player Logs -->
    <div class="grid gap-4">
      <div
          v-for="log in filteredLogs"
          :key="log.id"
          class="border border-slate-700 p-4 rounded bg-slate-800 space-y-2"
      >
        <div class="text-lg font-semibold">{{ log.characterName }} ({{ log.playerName }})</div>
        <div class="text-sm text-slate-400">{{ log.classAndLevel }} | Faction: {{ log.faction }}</div>
        <div class="text-sm">💰 Gold: {{ log.goldEarned }} | 🕒 Downtime: {{ log.downtimeGained }} | 🎖️ Renown: {{ log.renown }}</div>
        <div class="text-sm">💀 Death Saves Failed: {{ log.deathSavesFailed }} | 🌟 Used Inspiration: {{ log.inspirationUsed ? 'Yes' : 'No' }}</div>
        <div class="text-xs text-slate-300 italic">🧙 Magic Items: {{ log.magicItemsFound?.join(', ') || 'None' }}</div>
        <div class="text-xs text-slate-400 italic">📝 {{ log.customNotes || 'No notes.' }}</div>
      </div>
    </div>

    <div v-if="!filteredLogs.length" class="text-slate-500 italic">No players found.</div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import PlayerLogService from '@/services/PlayerLogService.js'

const route = useRoute()
const campaignId = route.params.campaignId

const logs = ref([])
const search = ref('')

onMounted(async () => {
  logs.value = await PlayerLogService.getByCampaign(campaignId)
})

const filteredLogs = computed(() =>
    logs.value.filter((log) =>
        log.playerName.toLowerCase().includes(search.value.toLowerCase())
    )
)
</script>

<style scoped>
.input {
  @apply bg-slate-900 text-white border border-slate-600 rounded px-3 py-2;
}
.btn-outline {
  @apply border border-slate-600 text-slate-300 hover:bg-slate-700 px-4 py-2 rounded text-sm;
}
</style>
