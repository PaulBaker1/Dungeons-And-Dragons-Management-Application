<template>
  <form @submit.prevent="emit('submit', form)" class="space-y-6">
    <!-- Basic Info -->
    <div class="grid md:grid-cols-2 gap-4">
      <div>
        <label class="label">Player Name</label>
        <input v-model="form.playerName" class="input" required />
      </div>
      <div>
        <label class="label">Character Name</label>
        <input v-model="form.characterName" class="input" required />
      </div>
      <div>
        <label class="label">Class & Level</label>
        <input v-model="form.classAndLevel" class="input" required />
      </div>
      <div>
        <label class="label">Faction</label>
        <select v-model="form.faction" class="input">
          <option value="FRIENDLY">Friendly</option>
          <option value="NEUTRAL">Neutral</option>
          <option value="HOSTILE">Hostile</option>
        </select>
      </div>
    </div>

    <!-- Flags -->
    <div class="grid grid-cols-2 gap-4">
      <label class="flex items-center gap-2">
        <input type="checkbox" v-model="form.inspirationUsed" /> Used Inspiration
      </label>
      <div>
        <label class="label">Failed Death Saves</label>
        <input type="number" v-model.number="form.deathSavesFailed" class="input" min="0" />
      </div>
    </div>

    <!-- Currency -->
    <div class="grid md:grid-cols-3 gap-4">
      <div>
        <label class="label">Gold Earned</label>
        <input type="number" v-model.number="form.goldEarned" class="input" min="0" />
      </div>
      <div>
        <label class="label">Downtime Gained</label>
        <input type="number" v-model.number="form.downtimeGained" class="input" min="0" />
      </div>
      <div>
        <label class="label">Renown</label>
        <input type="number" v-model.number="form.renown" class="input" min="0" />
      </div>
    </div>

    <!-- Magic Items -->
    <div>
      <label class="label">Magic Items Found (comma-separated)</label>
      <input v-model="magicItems" class="input" placeholder="Bag of Holding, Wand of Web" />
    </div>

    <!-- Notes -->
    <div>
      <label class="label">Custom Notes</label>
      <textarea v-model="form.customNotes" class="input" rows="4" />
    </div>

    <!-- Submit -->
    <div class="pt-4">
      <button type="submit" class="btn-primary">💾 Submit</button>
    </div>
  </form>
</template>

<script setup>
import { reactive, watch, computed } from 'vue'
const props = defineProps({
  modelValue: Object,
})
const emit = defineEmits(['update:modelValue', 'submit'])

const form = reactive({
  playerName: '',
  characterName: '',
  classAndLevel: '',
  faction: 'NEUTRAL',
  inspirationUsed: false,
  deathSavesFailed: 0,
  goldEarned: 0,
  downtimeGained: 0,
  renown: 0,
  magicItemsFound: [],
  customNotes: '',
})

const magicItems = computed({
  get: () => form.magicItemsFound.join(', '),
  set: (val) => {
    form.magicItemsFound = val.split(',').map(i => i.trim()).filter(Boolean)
  }
})

watch(() => props.modelValue, (val) => {
  if (val) Object.assign(form, val)
}, { immediate: true })
</script>

<style scoped>
.label {
  @apply text-sm mb-1 text-slate-400;
}
.input {
  @apply w-full bg-slate-800 text-white border border-slate-600 rounded px-3 py-2;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold px-6 py-2 rounded;
}
</style>
