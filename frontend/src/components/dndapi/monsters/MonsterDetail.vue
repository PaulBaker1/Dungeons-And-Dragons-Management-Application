<template>
  <div class="px-4 py-8 bg-gray-900 text-white min-h-screen max-w-3xl mx-auto space-y-8">
    <!-- Back Link -->
    <router-link to="/monsters" class="text-indigo-400 hover:underline block mb-4">
      ← Back to All Monsters
    </router-link>

    <!-- Monster Info -->
    <fieldset class="card-block">
      <legend class="legend-label flex items-center gap-2">
        <span class="text-purple-400">👾</span>
        <span>Monster Info</span>
      </legend>

      <h1 class="text-4xl font-extrabold mb-2 text-white">{{ monster.name || 'Unnamed Monster' }}</h1>

      <p class="text-slate-400 text-lg mb-4">
        {{ monster.size }} • {{ monster.type }}
        <span v-if="monster.challenge_rating !== undefined">• CR {{ monster.challenge_rating }}</span>
      </p>

      <div class="flex flex-wrap gap-2 text-sm text-slate-300">
        <span class="chip"><strong>HP:</strong> {{ monster.hit_points }}</span>
        <span class="chip"><strong>AC:</strong> {{ armorClass }}</span>
        <span class="chip"><strong>Speed:</strong> {{ speedString }}</span>
        <span class="chip"><strong>Alignment:</strong> {{ monster.alignment || 'Unknown' }}</span>
      </div>
    </fieldset>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const monster = ref({})
const route = useRoute()

onMounted(async () => {
  try {
    const { data } = await axios.get(`/api/dnd/monsters/${route.params.index}`)
    monster.value = data
  } catch (err) {
    console.error("🚨 Error fetching monster:", err)
    monster.value = { name: 'Error loading monster', error: err.message }
  }
})

const speedString = computed(() => {
  const speed = monster.value.speed || {}
  return Object.entries(speed)
      .map(([type, val]) => `${type}: ${val}`)
      .join(', ')
})

const armorClass = computed(() => {
  const ac = monster.value.armor_class
  if (typeof ac === 'number') return ac
  if (Array.isArray(ac)) return ac.map(a => a.value || a).join(', ')
  return 'Unknown'
})
</script>

<style scoped>
.card-block {
  background-color: #1e293b;
  border: 1px solid #334155;
  border-radius: 0.75rem;
  padding: 1.5rem;
}

.legend-label {
  color: #e0f2fe;
  font-weight: 600;
  font-size: 1rem;
  padding: 0 0.5rem;
}

.chip {
  background-color: #334155;
  color: #f1f5f9;
  font-size: 0.875rem;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-weight: 500;
}
</style>
