<template>
  <div class="p-6 bg-gray-900 min-h-screen text-white">
    <h1 class="text-3xl font-bold mb-4 capitalize">📖 {{ route.params.index }} Spells</h1>

    <input
        v-model="search"
        type="text"
        placeholder="Search spells..."
        class="w-full px-4 py-2 mb-6 rounded bg-gray-800 text-white"
    />

    <div v-if="loading" class="text-slate-400">Loading spells...</div>
    <div v-else-if="error" class="text-red-500">Error: {{ error }}</div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div
          v-for="spell in filteredSpells"
          :key="spell.name"
          class="bg-slate-800 border border-slate-700 p-4 rounded"
      >
        <h2 class="text-xl font-semibold text-indigo-300">
          {{ spell.name }}
          <span class="text-sm text-slate-400"> (Level {{ spell.level }})</span>
        </h2>
        <p class="text-sm text-slate-300 mt-1 line-clamp-3">
          {{ spell.desc?.[0] || 'No description' }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const spells = ref([])
const search = ref('')
const loading = ref(false)
const error = ref(null)

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`/api/dnd/classes/${route.params.index}/spells`)
    spells.value = data.results
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
})

const filteredSpells = computed(() =>
    spells.value.filter(spell =>
        spell.name.toLowerCase().includes(search.value.toLowerCase())
    )
)
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
