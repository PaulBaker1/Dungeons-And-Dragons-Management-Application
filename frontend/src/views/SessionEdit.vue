<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-10">
    <router-link :to="`/campaigns/${campaignId}`" class="btn-primary text-sm">
      ← Back to Campaign
    </router-link>

    <div v-if="session">
      <div class="flex justify-between items-center">
        <h1 class="text-3xl font-bold">
          {{ isEditing ? 'Edit Session' : 'Session Details' }}
        </h1>
        <div>
          <!-- Inline toggle still works -->
          <button v-if="!isEditing" @click="startEdit" class="btn-primary">
            Edit
          </button>
          <div v-else class="space-x-2">
            <button @click="cancelEdit" class="btn-outline">Cancel</button>
            <button @click="submit" class="btn-primary">Save</button>
          </div>
        </div>
      </div>

      <form @submit.prevent="submit" class="space-y-10">
        <fieldset class="card">
          <legend class="section">Metadata</legend>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="label">Title</label>
              <input
                  v-model="editable.title"
                  class="input"
                  :readonly="!isEditing"
                  required
              />
            </div>
            <div>
              <label class="label">Objective</label>
              <input
                  v-model="editable.objective"
                  class="input"
                  :readonly="!isEditing"
                  required
              />
            </div>
            <div>
              <label class="label">Date</label>
              <input
                  v-model="editable.session_date"
                  type="datetime-local"
                  class="input"
                  :readonly="!isEditing"
                  required
              />
            </div>
            <div>
              <label class="label">Status</label>
              <select
                  v-model="editable.status"
                  class="input"
                  :disabled="!isEditing"
              >
                <option>DRAFT</option>
                <option>ACTIVE</option>
                <option>COMPLETED</option>
                <option>ARCHIVED</option>
              </select>
            </div>
          </div>

          <div>
            <label class="label">Summary</label>
            <div v-if="!isEditing" class="prose" v-html="session.summary"></div>
            <TiptapEditor
                v-else
                v-model="editable.summary"
                :editable="true"
            />
          </div>
        </fieldset>
      </form>

      <section class="mt-10">
        <h2 class="text-2xl font-bold mb-4">Session Management</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- UPCOMING -->
          <div class="card">
            <h3 class="section mb-2">Upcoming Sessions</h3>
            <ul class="text-slate-400 space-y-1">
              <li v-if="upcomingSessions.length === 0">
                No upcoming sessions.
              </li>
              <li
                  v-for="ses in upcomingSessions"
                  :key="ses.id"
                  class="flex justify-between items-center"
              >
                <span>{{ formatDateDisplay(ses.session_date) }}</span>
                <button
                    @click="editSession(ses.id)"
                    class="btn-outline text-sm"
                >
                  Edit
                </button>
              </li>
            </ul>
            <div class="mt-4">
              <label class="label">Notes</label>
              <div
                  v-if="!isEditing"
                  class="prose"
                  v-html="session.upcomingNotes"
              ></div>
              <TiptapEditor
                  v-else
                  v-model="editable.upcomingNotes"
                  :editable="true"
              />
            </div>
          </div>

          <!-- PAST -->
          <div class="card">
            <h3 class="section mb-2">Past Sessions</h3>
            <ul class="text-slate-400 space-y-1 max-h-40 overflow-auto">
              <li v-if="pastSessions.length === 0">
                No past sessions yet.
              </li>
              <li
                  v-for="ses in pastSessions"
                  :key="ses.id"
                  class="flex justify-between items-center"
              >
                <span>{{ formatDateDisplay(ses.session_date) }}</span>
                <button
                    @click="editSession(ses.id)"
                    class="btn-outline text-sm"
                >
                  Edit
                </button>
              </li>
            </ul>
            <div class="mt-4">
              <label class="label">Notes</label>
              <div
                  v-if="!isEditing"
                  class="prose"
                  v-html="session.pastNotes"
              ></div>
              <TiptapEditor
                  v-else
                  v-model="editable.pastNotes"
                  :editable="true"
              />
            </div>
          </div>

          <!-- ATTENDANCE -->
          <div class="card col-span-2">
            <h3 class="section mb-2">Attendance & Notes</h3>
            <ul class="list-disc pl-5 text-slate-300 mb-4">
              <li v-for="p in session.players" :key="p.id">
                {{ p.name }} –
                <span
                    :class="p.present ? 'text-green-400' : 'text-red-500'"
                >
                  {{ p.present ? 'Present' : 'Absent' }}
                </span>
              </li>
            </ul>
            <div>
              <label class="label">Attendance Notes</label>
              <div
                  v-if="!isEditing"
                  class="prose"
                  v-html="session.attendanceNotes"
              ></div>
              <TiptapEditor
                  v-else
                  v-model="editable.attendanceNotes"
                  :editable="true"
              />
            </div>
          </div>
        </div>
      </section>
    </div>

    <div v-else class="text-center text-slate-400 italic pt-10">
      Loading session...
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useSessionStore } from '@/store/sessionStore.js'
import SessionService from '@/services/SessionService.js'
import TiptapEditor from '@/components/TiptapEditor.vue'

// router + route
const route = useRoute()
const router = useRouter()
const campaignId = computed(() => route.params.campaignId)
const sessionId = computed(() => route.params.id)

// state
const session = ref(null)
const isEditing = ref(false)
const editable = reactive({
  title: '',
  objective: '',
  session_date: '',
  status: '',
  summary: '',
  upcomingNotes: '',
  pastNotes: '',
  attendanceNotes: ''
})
const upcomingSessions = ref([])
const pastSessions = ref([])
const sessionStore = useSessionStore()

// helpers
function formatDateForInput(iso) {
  const d = new Date(iso)
  if (isNaN(d)) return ''
  const pad = n => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}
function formatInputToISO(str) {
  const d = new Date(str)
  return isNaN(d) ? null : d.toISOString()
}
function formatDateDisplay(iso) {
  return new Date(iso).toLocaleString()
}

// load one session into `session` + `editable`
async function fetchSession() {
  const data = await SessionService.getById(campaignId.value, sessionId.value)
  session.value = data
  // prepare editable
  const copy = { ...data, session_date: formatDateForInput(data.sessionDate || data.session_date) }
  Object.assign(editable, {
    title: copy.title,
    objective: copy.objective,
    session_date: copy.session_date,
    status: copy.status,
    summary: copy.summary || '',
    upcomingNotes: copy.upcomingNotes || '',
    pastNotes: copy.pastNotes || '',
    attendanceNotes: copy.attendanceNotes || ''
  })
}

// load list of sessions for sidebar
async function loadSessionLists() {
  await sessionStore.fetchByCampaign(campaignId.value)
  const list = sessionStore.sessions
  const now = Date.now()
  upcomingSessions.value = list.filter(s => new Date(s.session_date) >= now)
  pastSessions.value = list.filter(s => new Date(s.session_date) < now)
}

// toggle into edit
function startEdit() {
  isEditing.value = true
}
// cancel edit
function cancelEdit() {
  isEditing.value = false
}
// save
async function submit() {
  try {
    const payload = {
      title: editable.title,
      objective: editable.objective,
      summary: editable.summary,
      status: editable.status,
      sessionDate: formatInputToISO(editable.session_date),
      upcomingNotes: editable.upcomingNotes,
      pastNotes: editable.pastNotes,
      attendanceNotes: editable.attendanceNotes
    }
    await SessionService.update(campaignId.value, sessionId.value, payload)

    // sync
    Object.assign(session.value, payload)
    Object.assign(editable, {
      ...payload,
      session_date: formatDateForInput(payload.sessionDate)
    })

    isEditing.value = false
    loadSessionLists()
    alert('Session updated successfully')
  } catch {
    alert('Error saving session')
  }
}
// navigate to another session’s edit
function editSession(id) {
  router.push({ name: 'SessionEdit', params: { campaignId: campaignId.value, id } })
}

// initial data + auto‑edit on /edit route
onMounted(async () => {
  await fetchSession()
  await loadSessionLists()
  // if URL is the *edit* route, open editors immediately
  if (route.name === 'SessionEdit') {
    startEdit()
  }
})
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
  @apply bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded font-semibold;
}
.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}
.prose {
  @apply bg-slate-800 p-4 border border-slate-700 rounded text-white;
}
</style>
