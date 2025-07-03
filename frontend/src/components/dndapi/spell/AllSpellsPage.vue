<template>
  <div class="max-w-6xl mx-auto py-10 px-6 text-white space-y-6">
    <!-- Header -->
    <h1 class="text-3xl font-bold">🧙 Spells</h1>

    <!-- Filters -->
    <div class="flex flex-wrap gap-4 bg-slate-800 p-4 rounded border border-slate-700">
      <input
          v-model="search"
          type="text"
          placeholder="Search by name..."
          class="input w-64"
      />
      <select v-model="levelFilter" class="input w-40">
        <option value="">All Levels</option>
        <option value="0">Cantrip</option>
        <option v-for="n in 9" :key="n" :value="n">Level {{ n }}</option>
      </select>
    </div>

    <!-- Loading/Error -->
    <div v-if="loading" class="text-slate-400">Loading spells...</div>
    <div v-else-if="error" class="text-red-500">Error: {{ error }}</div>

    <!-- Spell Grid -->
    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      <router-link
          v-for="spell in paginatedSpells"
          :key="spell.index"
          :to="`/spells/${spell.index}`"
          class="block bg-slate-800 border border-slate-700 p-4 rounded hover:border-indigo-500 transition shadow"
      >
        <h2 class="text-xl font-semibold text-indigo-300">
          {{ spell.name }}
          <span class="text-sm text-slate-400"> (Level {{ spell.level }})</span>
        </h2>
        <p class="text-sm text-slate-300 mt-1 line-clamp-3">
          {{ spell.shortDescription || 'No description' }}
        </p>
        <p class="text-xs mt-2 text-slate-400">
          Casting Time: {{ spell.casting_time || 'N/A' }} • Duration: {{ spell.duration || 'N/A' }}
        </p>
      </router-link>
    </div>

    <!-- Pagination Simple -->
    <div v-if="totalPages > 1" class="flex justify-center mt-6 gap-4">
      <button
          @click="currentPage--"
          :disabled="currentPage === 1"
          class="btn-outline px-4 py-1"
      >
        ◀ Prev
      </button>
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

const spells = ref([])
const search = ref('')
const levelFilter = ref('')
const classFilter = ref('')
const loading = ref(false)
const error = ref(null)
const currentPage = ref(1)
const pageSize = 6

const availableClasses = ref([])

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await axios.get('/api/dnd/spells/summary')
    spells.value = data

    // extract distinct classes for filter
    const all = data.flatMap(spell => spell.classes || []).map(c => c.name)
    availableClasses.value = [...new Set(all)].sort()
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
})

const filteredSpells = computed(() =>
    spells.value.filter(spell => {
      const matchesName = spell.name.toLowerCase().includes(search.value.toLowerCase())
      const matchesLevel = levelFilter.value === '' || spell.level === +levelFilter.value
      const matchesClass =
          classFilter.value === '' ||
          (spell.classes?.some(cls => cls.name === classFilter.value) ?? false)

      return matchesName && matchesLevel && matchesClass
    })
)

const totalPages = computed(() => Math.ceil(filteredSpells.value.length / pageSize))

const paginatedSpells = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredSpells.value.slice(start, start + pageSize)
})

watch([search, levelFilter, classFilter], () => {
  currentPage.value = 1
})
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.input {
  @apply bg-slate-900 border border-slate-600 text-white text-sm rounded px-3 py-2;
}
.btn-outline {
  @apply border border-slate-500 text-white rounded hover:bg-slate-700 disabled:opacity-50;
}
</style>
