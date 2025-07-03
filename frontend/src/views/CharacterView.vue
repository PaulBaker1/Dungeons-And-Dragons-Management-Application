<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <div class="flex justify-between items-center">
      <h1 class="text-3xl font-bold mb-6">🦉 Character Sheet</h1>
      <button class="btn-primary" @click="$emit('edit')">✏️ Edit</button>
    </div>

    <div v-if="character">
      <fieldset class="card space-y-6">
        <legend class="section">🙍 Basic Info</legend>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Info label="Player Name" :value="character.playerName" />
          <Info label="Character Name" :value="character.characterName" />
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Info label="Class" :value="character.class" />
          <Info label="Level" :value="character.level" />
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="label">Avatar</label>
            <div class="mt-2 w-32 h-32 border border-slate-700 bg-slate-800 rounded overflow-hidden">
              <img :src="character.avatar || placeholderAvatar" class="object-cover w-full h-full" alt="" />
            </div>
          </div>
          <Info label="Control Player" :value="character.controlPlayer || '—'" />
        </div>

        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <Info v-for="(label, key) in abilityLabels" :key="key" :label="label" :value="character[key]" />
        </div>

        <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
          <Info label="Speed" :value="character.speed" />
          <Info label="Initiative" :value="character.initiative" />
          <Info label="Hit Points" :value="character.hitPoints" />
        </div>

        <div>
          <label class="label">Skills</label>
          <div class="grid grid-cols-2 md:grid-cols-3 gap-2">
            <div
                v-for="skill in character.skills"
                :key="skill.name"
                class="text-sm text-slate-300"
            >
              <span v-if="skill.proficient">✔️ {{ skill.name }}</span>
            </div>
          </div>
        </div>
      </fieldset>
    </div>

    <div v-else class="text-slate-400 text-sm italic text-center pt-10">
      Loading character...
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  character: Object,
  placeholderAvatar: String
})
const emit = defineEmits(['edit'])

const abilityLabels = {
  strength: '💪 Strength',
  dexterity: '🤸 Dexterity',
  constitution: '🧍 Constitution',
  intelligence: '🧠 Intelligence',
  wisdom: '🦉 Wisdom',
  charisma: '🗣 Charisma'
}
</script>

<script>
export default {
  components: {
    Info: {
      props: ['label', 'value'],
      template: `
        <div>
          <p class="text-sm text-slate-400 mb-1">{{ label }}</p>
          <p class="text-white font-medium">{{ value || '—' }}</p>
        </div>
      `
    }
  }
}
</script>

<style scoped>
.label {
  @apply block text-sm mb-1 text-slate-400;
}
.section {
  @apply text-xl font-bold mb-2 text-slate-100;
}
.card {
  @apply border border-slate-700 rounded-lg p-4 bg-slate-900;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
}
</style>
