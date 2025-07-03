<template>
  <div class="px-4 py-6 text-white bg-gray-900 min-h-screen space-y-8 max-w-3xl mx-auto">

    <!-- class Header -->
    <fieldset class="card-block">
      <legend class="legend-label">🧝‍♂️ Class Info</legend>
      <h1 class="text-4xl font-bold mb-2">{{ classData.name || 'No Class Name' }}</h1>
      <p class="text-lg text-slate-300">Hit Die: <strong>d{{ classData.hit_die || 'N/A' }}</strong></p>
    </fieldset>

    <!-- Bard Lore Block -->
    <fieldset class="card-block" v-if="classData.index === 'bard'">
      <legend class="legend-label">🎶 About the Bard</legend>
      <div class="text-slate-300 space-y-2">
        <p>
          Bards are the consummate performers, scholars, and adventurers. Masters of music and magic alike,
          they weave spells with rhythm and rhyme, inspiring allies and confounding foes.
        </p>
        <p>
          With access to a wide range of support and utility spells, bards can adapt to nearly any situation.
          Their Jack of All Trades feature allows them to contribute in all roles—be it combat, exploration, or diplomacy.
        </p>
        <p>
          In D&D 5e, the Bard class is famed for versatility and charisma-driven magic, drawing from the full list
          of spellcasting schools and gaining powerful subclass abilities such as College of Lore or Valor.
        </p>
      </div>
    </fieldset>

    <!-- Description -->
    <fieldset class="card-block" v-if="classData.desc?.length">
      <legend class="legend-label">📜 Description</legend>
      <div class="grid grid-cols-1 gap-2 text-slate-300 leading-relaxed">
        <p v-for="(line, i) in classData.desc" :key="i">{{ line }}</p>
      </div>
    </fieldset>

    <!-- Proficiencies -->
    <fieldset class="card-block" v-if="classData.proficiencies?.length">
      <legend class="legend-label">🎯 Proficiencies</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="prof in classData.proficiencies" :key="prof.index" class="chip">
          {{ prof.name }}
        </span>
      </div>
    </fieldset>

    <!-- Saving Throws -->
    <fieldset class="card-block" v-if="classData.saving_throws?.length">
      <legend class="legend-label">🛡️ Saving Throws</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="save in classData.saving_throws" :key="save.index" class="chip">
          {{ save.name }}
        </span>
      </div>
    </fieldset>

    <!-- Subclasses -->
    <fieldset class="card-block" v-if="classData.subclasses?.length">
      <legend class="legend-label">🧬 Subclasses</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="sub in classData.subclasses" :key="sub.index" class="chip">
          {{ sub.name }}
        </span>
      </div>
    </fieldset>

    <!-- Proficiency Choices -->
    <fieldset class="card-block" v-if="classData.proficiency_choices?.length">
      <legend class="legend-label">🧩 Proficiency Choices</legend>
      <div class="space-y-4 text-slate-300">
        <div v-for="(choice, i) in classData.proficiency_choices" :key="i">
          <p class="font-medium mb-1">
            {{ choice.desc || ('Choose ' + choice.choose + ' from:') }}
          </p>
          <div class="flex flex-wrap gap-2">
            <span
                v-for="(option, j) in (choice.from?.options || [])"
                :key="j"
                class="chip"
            >
              {{ option.item.name }}
            </span>
          </div>
        </div>
      </div>
    </fieldset>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { DnDService } from '@/services/dndApiService.js'

const classData = ref({})
const route = useRoute()

onMounted(async () => {
  try {
    const classIndex = route.params.index || 'bard'
    const data = await DnDService.getClass(classIndex)
    classData.value = data
  } catch (err) {
    console.error('Failed to fetch class data:', err)
    classData.value = { error: 'Failed to fetch class data' }
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
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
}
</style>
