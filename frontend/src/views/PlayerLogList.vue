<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">🎲 Player Logs</h1>
      <router-link
          :to="`/sessions/${sessionId}/logs/create`"
          class="btn-primary"
      >➕ Add Player Log</router-link>
    </div>

    <!-- Filter/Search -->
    <div class="flex items-center gap-4">
      <input
          v-model="filters.playerName"
          placeholder="Search by player name"
          class="input w-full max-w-sm"
      />
      <button @click="search" class="btn-outline">🔍 Search</button>
    </div>

    <!-- Table -->
    <div v-if="logs.length > 0" class="overflow-x-auto">
      <table class="w-full text-sm border border-slate-700">
        <thead class="bg-slate-800 text-slate-300">
        <tr>
          <th class="p-2 text-left">Player</th>
          <th class="p-2 text-left">Character</th>
          <th class="p-2 text-left">Class</th>
          <th class="p-2 text-left">Faction</th>
          <th class="p-2 text-left">Gold</th>
          <th class="p-2 text-left">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr
            v-for="log in logs"
            :key="log.id"
            class="border-t border-slate-700 hover:bg-slate-800"
        >
          <td class="p-2">{{ log.playerName }}</td>
          <td class="p-2">{{ log.characterName }}</td>
          <td class="p-2">{{ log.classAndLevel }}</td>
          <td class="p-2">{{ log.faction }}</td>
          <td class="p-2">{{ log.goldEarned }} gp</td>
          <td class="p-2 flex gap-2">
            <router-link :to="`/playerlogs/${log.id}/edit`" class="btn-outline text-xs">✏️ Edit</router-link>
            <button @click="confirmDelete(log.id)" class="btn-danger text-xs">🗑️ Delete</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div v-else class="text-slate-400 italic">No player logs yet.</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const sessionId = route.params.sessionId || null
const campaignId = route.params.campaignId || null

const logs = ref([])
const filters = ref({ playerName: '' })

const fetchLogs = async () => {
  try {
    const url = campaignId
        ? `/api/campaigns/${campaignId}/logs`
        : `/api/sessions/${sessionId}/logs`

    const res = await axios.get(url)
    logs.value = res.data
  } catch (err) {
    console.error('❌ Failed to fetch characters logs:', err)
  }
}

const search = async () => {
  if (!filters.value.playerName) return fetchLogs()

  try {
    const res = await axios.get(`/api/playerlogs/search`, {
      params: { playerName: filters.value.playerName }
    })
    logs.value = res.data
  } catch (err) {
    console.error('❌ Search failed:', err)
  }
}

const confirmDelete = async (id) => {
  if (confirm('Are you sure you want to delete this characters log?')) {
    try {
      await axios.delete(`/api/playerlogs/${id}`)
      await fetchLogs()
    } catch (err) {
      console.error('❌ Failed to delete:', err)
    }
  }
}

onMounted(fetchLogs)
</script>

<style scoped>
.input {
  @apply bg-slate-800 text-white px-3 py-2 border border-slate-600 rounded;
}
.btn-primary {
  @apply bg-blue-500 hover:bg-blue-600 text-white font-medium px-4 py-2 rounded;
}
.btn-outline {
  @apply border border-slate-600 text-white hover:bg-slate-700 px-3 py-1 rounded;
}
.btn-danger {
  @apply border border-red-500 text-red-400 hover:bg-red-500 hover:text-white px-3 py-1 rounded;
}
</style>
