<template>
  <div class="max-w-3xl mx-auto p-6 text-white space-y-6">
    <h1 class="text-2xl font-bold">✏️ Edit Player Log</h1>

    <form @submit.prevent="submit">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="label">Player Name</label>
          <input v-model="log.playerName" class="input" required />
        </div>

        <div>
          <label class="label">Character Name</label>
          <input v-model="log.characterName" class="input" required />
        </div>

        <div>
          <label class="label">Class & Level</label>
          <input v-model="log.classAndLevel" class="input" required />
        </div>

        <div>
          <label class="label">Faction</label>
          <select v-model="log.faction" class="input">
            <option value="FRIENDLY">Friendly</option>
            <option value="NEUTRAL">Neutral</option>
            <option value="HOSTILE">Hostile</option>
          </select>
        </div>

        <div>
          <label class="label">Inspiration Used</label>
          <input type="checkbox" v-model="log.inspirationUsed" class="mr-2" />
        </div>

        <div>
          <label class="label">Death Saves Failed</label>
          <input type="number" v-model.number="log.deathSavesFailed" class="input" />
        </div>

        <div>
          <label class="label">Gold Earned</label>
          <input type="number" v-model.number="log.goldEarned" class="input" step="0.01" />
        </div>

        <div>
          <label class="label">Downtime Gained</label>
          <input type="number" v-model.number="log.downtimeGained" class="input" />
        </div>

        <div>
          <label class="label">Renown</label>
          <input type="number" v-model.number="log.renown" class="input" />
        </div>
      </div>

      <div>
        <label class="label">Magic Items Found (comma separated)</label>
        <input v-model="magicItemsText" class="input" />
      </div>

      <div>
        <label class="label">Custom Notes</label>
        <textarea v-model="log.customNotes" rows="4" class="input"></textarea>
      </div>

      <div class="flex justify-between mt-6">
        <router-link :to="`/sessions/${log.sessionId}/logs`" class="btn-outline">← Back</router-link>
        <button type="submit" class="btn-primary">💾 Save</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PlayerLogService from "@/services/PlayerLogService.js";


const route = useRoute()
const router = useRouter()
const logId = route.params.id

const log = ref({})
const magicItemsText = ref('')

onMounted(async () => {
  const data = await PlayerLogService.getById(logId)
  log.value = data
  magicItemsText.value = data.magicItemsFound?.join(', ') || ''
})

const submit = async () => {
  const payload = {
    ...log.value,
    magicItemsFound: magicItemsText.value.split(',').map(i => i.trim()).filter(Boolean)
  }
  await PlayerLogService.update(logId, payload)
  await router.push(`/sessions/${log.value.sessionId}/logs`)
}
</script>

<style scoped>
.input {
  @apply w-full bg-slate-800 border border-slate-600 p-2 rounded;
}
.label {
  @apply block text-sm text-slate-400 mb-1;
}
.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded;
}
</style>
