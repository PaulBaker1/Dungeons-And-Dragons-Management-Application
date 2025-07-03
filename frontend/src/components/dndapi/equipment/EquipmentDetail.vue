<template>
  <div class="px-4 py-8 bg-gray-900 text-white min-h-screen max-w-3xl mx-auto space-y-8">
    <router-link to="/equipment" class="text-indigo-400 hover:underline block mb-4">
      ← Back to All Equipment
    </router-link>

    <!-- Equipment Info -->
    <fieldset class="card-block">
      <legend class="legend-label flex items-center gap-2">
        <span class="text-yellow-400">🛡️</span>
        <span>Equipment Info</span>
      </legend>

      <h1 class="text-4xl font-extrabold mb-2">{{ equipment.name }}</h1>
      <p class="text-slate-300 text-lg mb-4">{{ equipment.equipment_category?.name || 'Uncategorized' }}</p>

      <div class="flex flex-wrap gap-2 text-sm text-slate-300">
        <span v-if="equipment.weapon_category" class="chip">Weapon Category: {{ equipment.weapon_category }}</span>
        <span v-if="equipment.weapon_range" class="chip">Weapon Range: {{ equipment.weapon_range }}</span>
        <span v-if="equipment.category_range" class="chip">Category Range: {{ equipment.category_range }}</span>
        <span v-if="equipment.weight" class="chip">Weight: {{ equipment.weight }} lbs</span>
        <span v-if="equipment.cost" class="chip">Cost: {{ equipment.cost.quantity }} {{ equipment.cost.unit }}</span>
      </div>
    </fieldset>

    <!-- Damage -->
    <fieldset class="card-block" v-if="equipment.damage">
      <legend class="legend-label flex items-center gap-2">
        <span class="text-red-400">💥</span>
        <span>Damage</span>
      </legend>
      <p class="text-slate-300">
        <strong>Dice:</strong> {{ equipment.damage.damage_dice }}<br>
        <strong>Type:</strong> {{ equipment.damage.damage_type?.name || 'N/A' }}
      </p>
    </fieldset>

    <!-- Range -->
    <fieldset class="card-block" v-if="equipment.range">
      <legend class="legend-label flex items-center gap-2">
        <span class="text-green-400">🏹</span>
        <span>Range</span>
      </legend>
      <p class="text-slate-300">
        <strong>Normal:</strong> {{ equipment.range.normal }} ft
      </p>
    </fieldset>

    <!-- Properties -->
    <fieldset class="card-block" v-if="equipment.properties?.length">
      <legend class="legend-label flex items-center gap-2">
        <span class="text-blue-400">⚙️</span>
        <span>Properties</span>
      </legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="prop in equipment.properties" :key="prop.index" class="chip">
          {{ prop.name }}
        </span>
      </div>
    </fieldset>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const equipment = ref({})
const route = useRoute()

onMounted(async () => {
  try {
    const { data } = await axios.get(`/api/dnd/equipment/${route.params.index}`)
    equipment.value = data
  } catch (err) {
    console.error('❌ Error fetching equipment:', err)
    equipment.value = { name: 'Error loading equipment' }
  }
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
  font-size: 1.1rem;
  padding: 0 0.5rem;
}
.chip {
  background-color: #475569;
  color: #f8fafc;
  font-size: 0.875rem;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-weight: 500;
}
</style>
