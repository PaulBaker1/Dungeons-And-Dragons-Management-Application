<template>
  <div class="max-w-6xl mx-auto py-10 px-6 text-white space-y-6">
    <div class="flex justify-between items-center">
      <h1 class="text-3xl font-bold">🧝 Characters</h1>
      <router-link
          :to="`/characters/create`"
          class="btn-primary-green"
      >➕ New Character</router-link>
    </div>

    <!-- Filters -->
    <div class="flex flex-wrap gap-4 bg-slate-800 p-4 rounded border border-slate-700">
      <input v-model="searchQuery" placeholder="Search by name..." class="input w-64" />
      <select v-model="selectedFaction" class="input w-48">
        <option value="">All Factions</option>
        <option value="FRIENDLY">Friendly</option>
        <option value="NEUTRAL">Neutral</option>
        <option value="HOSTILE">Hostile</option>
      </select>
      <select v-model="sortOption" class="input w-48">
        <option value="name">Sort by Name</option>
        <option value="level">Sort by Level</option>
      </select>
    </div>

    <!-- Character Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
      <div
          v-for="character in filteredCharacters"
          :key="character.id"
          class="bg-slate-800 border border-slate-700 rounded p-4 shadow space-y-2"
      >
        <div class="text-lg font-semibold">{{ character.characterName }}</div>
        <div class="text-sm text-slate-400">{{ character.classAndLevel }}</div>
        <div class="text-xs">
          <span class="font-medium">Player:</span> {{ character.playerName }}
        </div>
        <div class="text-xs">
          <span class="font-medium">Faction:</span>
          <span :class="factionBadge(character.faction)">{{ character.faction }}</span>
        </div>
        <div class="flex gap-2 mt-2">
          <router-link
              :to="`/characters/${character.id}`"
              class="btn-outline text-sm px-2"
          >🔍 View</router-link>
          <button @click="deleteCharacter(character.id)" class="btn-danger text-sm px-2">
            🗑️ Delete
          </button>
        </div>
      </div>
    </div>

    <div v-if="!characters.length" class="text-slate-500 text-sm italic">
      No characters created yet.
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import CharacterService from '@/services/CharacterService.js'

const characters = ref([])
const searchQuery = ref('')
const selectedFaction = ref('')
const sortOption = ref('name')

onMounted(async () => {
  characters.value = await CharacterService.getAll()
})

const filteredCharacters = computed(() => {
  let result = characters.value

  if (searchQuery.value.trim()) {
    const q = searchQuery.value.toLowerCase()
    result = result.filter(c => c.characterName.toLowerCase().includes(q))
  }

  if (selectedFaction.value) {
    result = result.filter(c => c.faction === selectedFaction.value)
  }

  if (sortOption.value === 'name') {
    result = result.sort((a, b) => a.characterName.localeCompare(b.characterName))
  } else if (sortOption.value === 'level') {
    result = result.sort((a, b) => parseInt(b.classAndLevel.match(/\d+/)) - parseInt(a.classAndLevel.match(/\d+/)))
  }

  return result
})

function factionBadge(faction) {
  return {
    FRIENDLY: 'text-green-400',
    NEUTRAL: 'text-yellow-400',
    HOSTILE: 'text-red-400'
  }[faction] || 'text-slate-400'
}

async function deleteCharacter(id) {
  if (confirm('Are you sure you want to delete this character?')) {
    await CharacterService.remove(id)
    characters.value = characters.value.filter(c => c.id !== id)
  }
}
</script>

<style scoped>
.input {
  @apply bg-slate-900 border border-slate-600 text-white text-sm rounded px-3 py-2;
}
.btn-outline {
  @apply border border-slate-500 text-white rounded hover:bg-slate-700;
}
.btn-danger {
  @apply bg-red-600 hover:bg-red-700 text-white rounded;
}
.btn-primary-green {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded;
}
</style>
