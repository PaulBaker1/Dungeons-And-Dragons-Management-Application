<template>
  <div class="max-w-6xl mx-auto py-10 px-6 text-white space-y-6">
    <!-- Header -->
    <h1 class="text-3xl font-bold">👾 Monsters</h1>

    <!-- Filters -->
    <div class="flex flex-wrap gap-4 bg-slate-800 p-4 rounded border border-slate-700">
      <input
          v-model="search"
          type="text"
          placeholder="Search by name..."
          class="input w-64"
      />
    </div>

    <!-- Loading / Error -->
    <div v-if="loading" class="text-slate-400">Loading monsters...</div>
    <div v-else-if="error" class="text-red-500">Error: {{ error }}</div>

    <!-- Monster Grid -->
    <div v-else-if="paginatedMonsters.length" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      <router-link
          v-for="monster in paginatedMonsters"
          :key="monster.index"
          :to="`/monsters/${monster.index}`"
          class="block bg-slate-800 border border-slate-700 p-4 rounded hover:border-indigo-500 transition shadow"
      >
        <h2 class="text-xl font-semibold text-indigo-300">
          {{ monster.name }}
        </h2>
        <p class="text-sm text-slate-300 mt-1">Type: {{ monster.type }}</p>
        <p class="text-sm text-slate-300">Size: {{ monster.size }}</p>
        <p class="text-xs text-slate-400 mt-2">HP: {{ monster.hit_points }}</p>
      </router-link>
    </div>

    <!-- No Results Message -->
    <div v-else class="text-slate-500 mt-10 text-center">
      ❌ No monsters match your search.
    </div>

    <!-- Pagination Controls -->
    <div v-if="totalPages > 1" class="flex justify-center mt-6 gap-4">
      <button
          @click="currentPage--"
          :disabled="currentPage === 1"
          class="btn-outline px-4 py-1"
      >
        ◀ Prev
      </button>
      <span class="text-slate-400 mt-1">Page {{ currentPage }} of {{ totalPages }}</span>
      <button
          @click="currentPage++"
          :disabled="currentPage === totalPages"
          class="btn-outline px-4 py-1"
      >
        Next ▶
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'

const monsters = ref([])
const search = ref('')
const error = ref(null)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = 9

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await axios.get('/api/dnd/monsters')
    monsters.value = Array.isArray(data) ? data : []
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
})

const filteredMonsters = computed(() =>
    monsters.value.filter(monster =>
        monster.name.toLowerCase().includes(search.value.toLowerCase())
    )
)

const totalPages = computed(() =>
    Math.ceil(filteredMonsters.value.length / pageSize)
)

const paginatedMonsters = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredMonsters.value.slice(start, start + pageSize)
})

watch(search, () => {
  currentPage.value = 1
})
</script>

<style scoped>
.input {
  @apply bg-slate-900 border border-slate-600 text-white text-sm rounded px-3 py-2;
}

.btn-outline {
  @apply border border-slate-500 text-white rounded hover:bg-slate-700 disabled:opacity-50;
}
</style>
