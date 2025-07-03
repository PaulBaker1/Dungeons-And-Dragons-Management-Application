<template>
  <div class="max-w-3xl mx-auto py-10 text-white px-6">
    <div class="mb-6">
      <router-link
          to="/campaigns"
          class="inline-flex items-center gap-2 bg-slate-800 hover:bg-slate-700 text-white font-semibold px-4 py-2 rounded border border-slate-600 shadow"
      >
        ← Back to Campaigns
      </router-link>
    </div>

    <h1 class="text-2xl font-bold mb-6">🛠️ Create a New Session</h1>

    <form @submit.prevent="submitSession" class="space-y-6">
      <!-- Title -->
      <div>
        <label class="label">Title</label>
        <input v-model="title" class="input" required />
      </div>

      <!-- Objective -->
      <div>
        <label class="label">Objective</label>
        <input v-model="objective" class="input" required />
      </div>

      <!-- Date -->
      <div>
        <label class="label">Session Date</label>
        <input v-model="sessionDate" type="datetime-local" class="input" required />
      </div>

      <!-- Summary -->
      <div>
        <label class="label">Summary</label>
        <TiptapEditor v-model="summary" />
      </div>

      <!-- Status -->
      <div>
        <label class="label">Status</label>
        <select v-model="status" class="input" required>
          <option value="DRAFT">Draft</option>
          <option value="ACTIVE">Active</option>
          <option value="COMPLETED">Completed</option>
          <option value="ARCHIVED">Archived</option>
        </select>
      </div>

      <!-- Submit -->
      <div class="flex justify-end pt-4">
        <button type="submit" class="btn-primary">💾 Save Session</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SessionService from '@/services/SessionService'
import TiptapEditor from '@/components/TiptapEditor.vue'

const route = useRoute()
const router = useRouter()

const campaignId = computed(() => route.params.campaignId)

const title = ref('')
const objective = ref('')
const sessionDate = ref('')
const summary = ref('')
const status = ref('DRAFT')

const submitSession = async () => {
  const payload = {
    title: title.value,
    objective: objective.value,
    sessionDate: sessionDate.value,
    summary: summary.value,
    status: status.value,
    agendaItems: [],
    encounters: [],
    notes: [],
    decisions: [],
    keyItems: [],
  }

  const created = await SessionService.create(campaignId.value, payload)
  await router.push({ name: 'SessionView', params: { campaignId: campaignId.value, id: created.id } })
}

</script>

<style scoped>
.input {
  @apply w-full bg-slate-800 p-2 border border-slate-600 rounded text-white;
}
.label {
  @apply block text-sm mb-1 text-slate-400;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
}
</style>
