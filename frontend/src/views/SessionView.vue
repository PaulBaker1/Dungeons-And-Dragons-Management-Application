<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-10">
    <router-link :to="`/campaigns/${campaignId}`" class="btn-primary text-sm">
      ← Back to Campaign Menu
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
        <!-- Metadata Fieldset -->
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

        <!-- Important Data Fieldset -->
        <fieldset class="card">
          <legend class="section">Important Data</legend>
          <div v-if="editable.keyItems && editable.keyItems.length">
            <div
                v-for="(item, index) in editable.keyItems"
                :key="index"
                class="border border-slate-600 p-4 rounded mb-4"
            >
              <div>
                <label class="label">Item Name</label>
                <input v-model="item.itemName" class="input" :readonly="!isEditing" required />
              </div>
              <div>
                <label class="label">Description</label>
                <input v-model="item.description" class="input" :readonly="!isEditing" />
              </div>
              <div>
                <label class="label">Acquired</label>
                <input v-model="item.acquired" class="input" :readonly="!isEditing" />
              </div>
              <div>
                <label class="label">Location Found</label>
                <input v-model="item.locationFound" class="input" :readonly="!isEditing" />
              </div>
              <div>
                <label class="label">Used In</label>
                <input v-model="item.usedIn" class="input" :readonly="!isEditing" />
              </div>
              <button
                  v-if="isEditing"
                  @click.prevent="removeKeyItem(index)"
                  type="button"
                  class="btn-outline mt-2"
              >
                Remove
              </button>
            </div>
          </div>
          <div v-if="isEditing">
            <button @click.prevent="addKeyItem" type="button" class="btn-primary">
              + Add New Important Data
            </button>
          </div>
        </fieldset>
      </form>
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

const route = useRoute()
const router = useRouter()
const campaignId = computed(() => route.params.campaignId)
const sessionId = computed(() => route.params.id)

const session = ref(null)
const editable = reactive({})
const isEditing = ref(false)

// Fetch session details and initialize keyItems if missing
watch([campaignId, sessionId], async ([cid, sid]) => {
  if (!cid || !sid) return
  try {
    const data = await SessionService.getById(cid, sid)
    session.value = data
    // Ensure keyItems exists
    if (!data.keyItems) data.keyItems = []
    Object.assign(editable, JSON.parse(JSON.stringify(data)))
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

function addKeyItem() {
  if (!editable.keyItems) {
    editable.keyItems = []
  }
  editable.keyItems.push({
    itemName: '',
    description: '',
    acquired: '',
    locationFound: '',
    usedIn: ''
  })
}

function removeKeyItem(index) {
  editable.keyItems.splice(index, 1)
}

function sanitizeSessionForUpdate(session) {
  const {
    title,
    objective,
    summary,
    status,
    sessionDate,
    // other fields...
    keyItems = []
  } = session

  let isoSessionDate = "";
  // Ensure sessionDate is an array and then create a valid Date
  if (Array.isArray(sessionDate)) {
    const dateObj = new Date(...sessionDate)
    if (!isNaN(dateObj.getTime())) {
      isoSessionDate = dateObj.toISOString()
    } else {
      console.error("Invalid date array:", sessionDate)
    }
  } else {
    // If sessionDate is already a valid date string, use it directly
    isoSessionDate = new Date(sessionDate).toISOString()
  }

  return {
    title,
    objective,
    summary,
    status,
    sessionDate: isoSessionDate,
    // other fields mapped...
    keyItems: keyItems
        .filter(item => item.itemName || item.description || item.acquired || item.locationFound || item.usedIn)
        .map(({ itemName, description, acquired, locationFound, usedIn }) => ({
          itemName,
          description,
          acquired,
          locationFound,
          usedIn
        }))
  }
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

function formatDate(dateStr) {
  if (!dateStr) return '—'
  const d = new Date(dateStr)
  return isNaN(d.getTime()) ? 'Invalid Date' : d.toLocaleString()
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
</style>
