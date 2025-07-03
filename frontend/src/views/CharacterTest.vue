<template>
  <div class="p-6 text-white bg-slate-900 min-h-screen space-y-4">
    <h1 class="text-2xl font-bold">🧪 Character Test View</h1>

    <div v-if="character && character.characterName">
      <p><strong>Player:</strong> {{ character.playerName }}</p>
      <p><strong>Character Name:</strong> {{ character.characterName }}</p>
      <p><strong>Class:</strong> {{ character.class }}</p>
      <p><strong>Level:</strong> {{ character.level }}</p>
      <p><strong>Strength:</strong> {{ character.strength }}</p>
      <p><strong>Dexterity:</strong> {{ character.dexterity }}</p>
      <p><strong>Constitution:</strong> {{ character.constitution }}</p>

      <p><strong>Raw object:</strong></p>
      <pre class="text-xs bg-slate-800 p-4 rounded max-w-xl overflow-x-auto">
{{ character }}
      </pre>
    </div>

    <div v-else>
      <p>Loading character...</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const character = reactive({
  id: null,
  playerName: '',
  characterName: '',
  class: '',
  level: null,
  strength: null,
  dexterity: null,
  constitution: null,
})

async function loadCharacter() {
  const id = route.params.id
  if (!id) return

  try {
    const res = await fetch(`/api/characters/${id}`)
    const data = await res.json()

    const [characterClass, levelStr] = (data.classAndLevel || '').split(' ')
    const level = parseInt(levelStr || '1')

    Object.assign(character, {
      id: data.id,
      playerName: data.playerName,
      characterName: data.characterName,
      class: characterClass === 'null' ? '' : characterClass,
      level: isNaN(level) ? 1 : level,
      strength: data.strength,
      dexterity: data.dexterity,
      constitution: data.constitution,
    })
  } catch (err) {
    console.error('Failed to load character:', err)
  }
}

onMounted(loadCharacter)
</script>

<style scoped>
body {
  background-color: #0f172a;
}
</style>
