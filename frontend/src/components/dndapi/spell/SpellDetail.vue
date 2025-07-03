<template>
  <div class="px-6 py-10 bg-gray-900 text-white min-h-screen max-w-4xl mx-auto">
    <router-link to="/spells" class="text-indigo-400 hover:underline mb-6 block">← Back to All Spells</router-link>

    <fieldset class="card-block mb-8">
      <legend class="legend-label">📖 Spell Info</legend>
      <h1 class="text-4xl font-bold mb-2">{{ spell.name || 'Unnamed Spell' }}</h1>
      <p class="text-slate-400 text-lg mb-4">Level {{ spell.level }} • {{ spell.school?.name || 'Magic' }}</p>

      <div class="grid grid-cols-2 gap-4 text-sm text-slate-300">
        <div><strong>Casting Time:</strong> {{ spell.casting_time || 'N/A' }}</div>
        <div><strong>Range:</strong> {{ spell.range || 'N/A' }}</div>
        <div><strong>Duration:</strong> {{ spell.duration || 'N/A' }}</div>
        <div><strong>Components:</strong> {{ spell.components?.join(', ') || 'N/A' }}</div>
        <div v-if="spell.material" class="col-span-2">
          <strong>Material:</strong> {{ spell.material }}
        </div>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="spell.desc?.length">
      <legend class="legend-label">🪄 Description</legend>
      <p v-for="(line, i) in spell.desc" :key="i" class="text-slate-300 mb-2 leading-relaxed">
        {{ line }}
      </p>
    </fieldset>

    <fieldset class="card-block" v-if="spell.higher_level?.length">
      <legend class="legend-label">🔺 At Higher Levels</legend>
      <p v-for="(line, i) in spell.higher_level" :key="i" class="text-slate-300 mb-2 leading-relaxed">
        {{ line }}
      </p>
    </fieldset>

    <fieldset class="card-block" v-if="spell.classes?.length">
      <legend class="legend-label">🎓 Classes</legend>
      <div class="flex flex-wrap gap-2 mt-1">
        <span v-for="cls in spell.classes" :key="cls.index" class="chip">
          {{ cls.name }}
        </span>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="spell.subclasses?.length">
      <legend class="legend-label">🧬 Subclasses</legend>
      <div class="flex flex-wrap gap-2 mt-1">
        <span v-for="sub in spell.subclasses" :key="sub.index" class="chip">
          {{ sub.name }}
        </span>
      </div>
    </fieldset>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const spell = ref({})
const error = ref(null)

onMounted(async () => {
  try {
    const { data } = await axios.get(`/api/dnd/spells/${route.params.index}`)
    spell.value = data
  } catch (err) {
    console.error('Failed to load spell:', err)
    error.value = 'Failed to load spell data.'
  }
})
</script>

<style scoped>
.card-block {
  background-color: #1e293b;
  border: 1px solid #334155;
  border-radius: 0.75rem;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}
.legend-label {
  color: #e0f2fe;
  font-weight: 600;
  font-size: 1rem;
  padding: 0 0.5rem;
}
.chip {
  background-color: #6366f1;
  color: white;
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
}
</style>
