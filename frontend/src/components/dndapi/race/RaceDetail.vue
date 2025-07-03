<template>
  <div class="px-4 py-6 text-white bg-gray-900 min-h-screen space-y-8 max-w-3xl mx-auto">
    <!-- Header -->
    <fieldset class="card-block">
      <legend class="legend-label">🧝 Race Info</legend>
      <h1 class="text-4xl font-bold mb-2">{{ race.name }}</h1>
      <p class="text-slate-300 text-lg">Speed: <strong>{{ race.speed }} ft</strong></p>
    </fieldset>

    <!-- Abilities -->
    <fieldset class="card-block">
      <legend class="legend-label">⚖️ Ability Bonuses</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="b in race.ability_bonuses" :key="b.ability_score.index" class="chip">
          +{{ b.bonus }} {{ b.ability_score.name }}
        </span>
      </div>
    </fieldset>

    <!-- Alignment / Age -->
    <fieldset class="card-block">
      <legend class="legend-label">📜 Background</legend>
      <p class="text-slate-300 mb-2"><strong>Alignment:</strong> {{ race.alignment }}</p>
      <p class="text-slate-300"><strong>Age:</strong> {{ race.age }}</p>
    </fieldset>

    <!-- Size -->
    <fieldset class="card-block">
      <legend class="legend-label">📏 Size</legend>
      <p class="text-slate-300 mb-1"><strong>Size:</strong> {{ race.size }}</p>
      <p class="text-slate-300">{{ race.size_description }}</p>
    </fieldset>

    <!-- Proficiencies -->
    <fieldset class="card-block" v-if="race.starting_proficiencies?.length">
      <legend class="legend-label">🎯 Starting Proficiencies</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="p in race.starting_proficiencies" :key="p.index" class="chip">
          {{ p.name }}
        </span>
      </div>
    </fieldset>

    <!-- Proficiency Choice -->
    <fieldset class="card-block" v-if="race.starting_proficiency_options">
      <legend class="legend-label">🧩 Proficiency Choice</legend>
      <p class="text-slate-300 mb-2">{{ race.starting_proficiency_options.desc }}</p>
      <div class="flex flex-wrap gap-2">
        <span
            v-for="opt in race.starting_proficiency_options.from.options"
            :key="opt.item.index"
            class="chip"
        >
          {{ opt.item.name }}
        </span>
      </div>
    </fieldset>

    <!-- Languages -->
    <fieldset class="card-block" v-if="race.languages?.length">
      <legend class="legend-label">🗣️ Languages</legend>
      <div class="flex flex-wrap gap-2 mb-2">
        <span v-for="lang in race.languages" :key="lang.index" class="chip">
          {{ lang.name }}
        </span>
      </div>
      <p class="text-slate-300">{{ race.language_desc }}</p>
    </fieldset>

    <!-- Traits -->
    <fieldset class="card-block" v-if="race.traits?.length">
      <legend class="legend-label">💎 Traits</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="t in race.traits" :key="t.index" class="chip">{{ t.name }}</span>
      </div>
    </fieldset>

    <!-- Subraces -->
    <fieldset class="card-block" v-if="race.subraces?.length">
      <legend class="legend-label">🧬 Subraces</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="s in race.subraces" :key="s.index" class="chip">{{ s.name }}</span>
      </div>
    </fieldset>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const race = ref({})
const route = useRoute()

onMounted(async () => {
  try {
    const index = route.params.index
    const { data } = await axios.get(`/api/dnd/races/${index}`)
    race.value = data
  } catch (err) {
    race.value = { name: 'Error loading race', error: err.message }
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
  background-color: #6366f1;
  color: white;
  font-size: 0.875rem;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
}
</style>
