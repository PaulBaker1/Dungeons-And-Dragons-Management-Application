<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-10">
    <router-link :to="`/campaigns/${campaignId}`" class="btn-primary text-sm">
      ← Back to Campaign
    </router-link>

    <div v-if="session">
      <div class="flex justify-between items-center">
        <h1 class="text-3xl font-bold">
          {{ isEditing ? '✏️ Edit Session' : '📖 Session Details' }}
        </h1>
        <div>
          <button v-if="!isEditing" @click="startEdit" class="btn-primary">✏️ Edit</button>
          <div v-else class="space-x-2">
            <button @click="cancelEdit" class="btn-outline">← Cancel</button>
            <button @click="submit" class="btn-primary">💾 Save</button>
          </div>
        </div>
      </div>

      <form @submit.prevent="submit" class="space-y-10">
        <fieldset class="card">
          <legend class="section">🧾 Metadata</legend>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="label">Title</label>
              <input v-model="editable.title" class="input" :readonly="!isEditing" required />
            </div>
            <div>
              <label class="label">Objective</label>
              <input v-model="editable.objective" class="input" :readonly="!isEditing" required />
            </div>
            <div>
              <label class="label">Date</label>
              <input v-model="editable.sessionDate" type="datetime-local" class="input" :readonly="!isEditing" required />
            </div>
            <div>
              <label class="label">Status</label>
              <select v-model="editable.status" class="input" :disabled="!isEditing">
                <option>DRAFT</option>
                <option>ACTIVE</option>
                <option>COMPLETED</option>
                <option>ARCHIVED</option>
              </select>
            </div>
          </div>
          <div>
            <label class="label">Summary</label>
            <TiptapEditor v-model="editable.summary" :editable="isEditing" />
          </div>
        </fieldset>
      </form>

      <!-- Prep Items Section -->
      <section class="session-prep-items mt-8">
        <h2 class="text-2xl font-bold mb-4">Session Prep Items</h2>
        <!-- Pass the session ID to the PrepItemList component -->
        <PrepItemList :sessionId="session.id" />
      </section>
    </div>

    <div v-else class="text-center text-slate-400 italic pt-10">
      Loading session...
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SessionService from '@/services/SessionService'
import TiptapEditor from '@/components/TiptapEditor.vue'
import PrepItemList from '@/components/PrepItemList.vue' // The component handling prep items

const route = useRoute()
const router = useRouter()
const campaignId = computed(() => route.params.campaignId)
const sessionId = computed(() => route.params.id)

const session = ref(null)
const editable = reactive({})
const isEditing = ref(false)

watch([campaignId, sessionId], async ([cid, sid]) => {
  if (!cid || !sid) return
  try {
    const data = await SessionService.getById(cid, sid)
    session.value = data
    Object.assign(editable, JSON.parse(JSON.stringify(data))) // deep copy to enable editing
  } catch (err) {
    console.error('❌ Failed to fetch session:', err)
  }
}, { immediate: true })

function startEdit() {
  Object.assign(editable, JSON.parse(JSON.stringify(session.value)))
  isEditing.value = true
}

function cancelEdit() {
  isEditing.value = false
}

async function submit() {
  try {
    const cleaned = sanitizeSessionForUpdate(editable)
    await SessionService.update(campaignId.value, sessionId.value, cleaned)
    session.value = JSON.parse(JSON.stringify(editable))
    isEditing.value = false
    alert('✅ Session updated successfully!')
  } catch (err) {
    console.error('❌ Failed to update session:', err)
    alert('Something went wrong while saving.')
  }
}

function sanitizeSessionForUpdate(session) {
  const {
    title,
    objective,
    summary,
    status,
    sessionDate,
    agendaItems = [],
    encounters = [],
    notes = [],
    decisions = [],
    keyItems = []
  } = session

  return {
    title,
    objective,
    summary,
    status,
    sessionDate,
    agendaItems: agendaItems.map(({ id, content, dmOnly, order }) => ({ id, content, dmOnly, order })),
    encounters: encounters.map(({ id, name, location, estimatedDifficulty, outcome }) => ({ id, name, location, estimatedDifficulty, outcome })),
    notes: notes.map(({ id, type, content }) => ({ id, type, content })),
    decisions: decisions.map(({ id, decisionPoint, choiceMade, consequences }) => ({ id, decisionPoint, choiceMade, consequences })),
    keyItems: keyItems.map(({ id, itemName, description, acquired, locationFound, usedIn }) => ({ id, itemName, description, acquired, locationFound, usedIn }))
  }
}
</script>

<style scoped>
.input {
  @apply w-full bg-slate-800 p-2 border border-slate-600 rounded text-white disabled:opacity-50;
}
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
.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}
.session-prep-items {
  @apply mt-8;
}
</style>
