<template>
  <div class="max-w-6xl mx-auto py-10 px-6 text-white space-y-6">
    <h1 class="text-3xl font-bold">🧝 All Races</h1>

    <!-- Loading / Error -->
    <div v-if="loading" class="text-slate-400">Loading races...</div>
    <div v-else-if="error" class="text-red-500">Error: {{ error }}</div>

    <!-- Race Grid -->
    <div v-else-if="filteredRaces.length" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      <router-link
          v-for="race in filteredRaces"
          :key="race.index"
          :to="`/races/${race.index}`"
          class="block bg-slate-800 border border-slate-700 p-4 rounded hover:border-indigo-500 transition shadow"
      >
        <h2 class="text-xl font-semibold text-indigo-300">{{ race.name }}</h2>
        <p class="text-sm text-slate-300 mt-1">Speed: {{ race.speed }} ft</p>
        <p class="text-xs text-slate-400 mt-2">
          Ability Bonuses:
          <span
              v-for="b in race.ability_bonuses"
              :key="b.ability_score.index"
              class="mr-2"
          >
            +{{ b.bonus }} {{ b.ability_score.name }}
          </span>
        </p>
      </router-link>
    </div>

    <!-- No Results -->
    <div v-else class="text-slate-500 mt-10 text-center">
      ❌ No races match your search.
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'

const races = ref([])
const search = ref('')
const error = ref(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await axios.get('/api/dnd/races')
    races.value = Array.isArray(data) ? data : []
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
})

const filteredRaces = computed(() =>
    races.value.filter(race =>
        race.name.toLowerCase().includes(search.value.toLowerCase())
    )
)

// If adding pagination later, reset page on search change
watch(search, () => {
  // currentPage.value = 1
})
</script>

<style scoped>
.input {
  @apply bg-slate-900 border border-slate-600 text-white text-sm rounded px-3 py-2;
}
</style>
