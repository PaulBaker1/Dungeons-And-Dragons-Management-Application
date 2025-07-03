<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-10 bg-gray-800">
    <router-link :to="`/campaigns/${campaignId}`" class="btn-primary text-sm">
      ← Back to Quest List
    </router-link>

    <div v-if="isCreating || quest">
      <div class="flex justify-between items-center">
        <h1 class="text-3xl font-bold">
          {{ isEditing ? '✏️ Edit Quest' : '📖 Quest Details' }}
        </h1>
        <div>
          <button v-if="!isEditing" @click="startEdit" class="btn-primary">✏️ Edit</button>
          <div v-else class="space-x-2">
            <button @click="cancelEdit" class="btn-outline">← Cancel</button>
            <button @click="submit" class="btn-primary">💾 Save</button>
          </div>
        </div>
      </div>

      <!-- Form for Editing Quest -->
      <form @submit.prevent="submit" class="space-y-10">
        <fieldset class="card">
          <legend class="section">🧾 Quest Information</legend>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="label">Title</label>
              <input v-model="editable.title" class="input" :readonly="!isEditing" required />
            </div>
            <div>
              <label class="label">Status</label>
              <select v-model="editable.status" class="input" :disabled="!isEditing">
                <option>Planned</option>
                <option>Active</option>
                <option>Completed</option>
                <option>Failed</option>
              </select>
              <p class="mt-2 text-sm text-slate-300">Current Status: {{ editable.status }}</p>
            </div>
            <div>
              <label class="label">Quest Giver</label>
              <input v-model="editable.questGiver" class="input" :readonly="!isEditing" />
            </div>
            <div>
              <label class="label">Reward</label>
              <input v-model="editable.reward" class="input" :readonly="!isEditing" />
            </div>

            <!-- Moved Description Section below the Title -->
            <div>
              <label class="label">Description</label>
              <TiptapEditor v-model="editable.description" :readonly="!isEditing" />
            </div>

            <!-- Moved Notes Section below the Reward -->
            <div>
              <label class="label">Notes</label>
              <TiptapEditor v-model="editable.notes" :readonly="!isEditing" />
            </div>

            <div>
              <label class="label">Priority</label>
              <select v-model="editable.priority" class="input" :disabled="!isEditing">
                <option>Low</option>
                <option>Medium</option>
                <option>High</option>
              </select>
              <p class="mt-2 text-sm text-slate-300">Current Priority: {{ editable.priority }}</p>
            </div>
            <div>
              <label class="label">Visibility</label>
              <input v-model="editable.visibility" type="checkbox" :disabled="!isEditing" />
              <span>Visible to players</span>
            </div>
            <div>
              <label class="label">Tags</label>
              <input v-model="editable.tags" class="input" :readonly="!isEditing" placeholder="Comma-separated tags" />
            </div>
          </div>
        </fieldset>

        <!-- Last Modified Info -->
        <div v-if="editable.lastModified" class="text-sm text-slate-400">
          <p>Last modified: {{ formatDate(editable.lastModified) }}</p>
        </div>
      </form>
    </div>

    <div v-else class="text-center text-slate-400 italic pt-10">
      Loading quest...
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, watch, computed, onMounted} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import QuestService from '@/services/QuestService'
import TiptapEditor from "@/components/TiptapEditor.vue";


const route = useRoute()
const router = useRouter()
const questId = computed(() => route.params.id)
const campaignId = computed(() => route.params.campaignId)

const quest = ref(null)
const editable = reactive({
  title: '',
  description: '',
  status: '',  // Default status
  questGiver: '',
  reward: '',
  notes: '',
  priority: 'Low',  // Default priority
  visibility: true,
  tags: '',
  lastModified: null
})
const isEditing = ref(false)

const isCreating = computed(() => !questId.value)

onMounted(async () => {
  if (isCreating.value) {
    quest.value = { ...editable }
    isEditing.value = true
  }
})


watch(questId, async (id) => {
  if (!id) return                       // skip when creating
  try {
    const data = await QuestService.getQuestById(campaignId.value, id)
    quest.value = data
    Object.assign(editable, JSON.parse(JSON.stringify(data)))
  } catch (err) {
    console.error('❌ Failed to fetch quest:', err)
  }
}, { immediate: true })

function startEdit() {
  Object.assign(editable, JSON.parse(JSON.stringify(quest.value)))
  isEditing.value = true
}

function cancelEdit() {
  Object.assign(editable, JSON.parse(JSON.stringify(quest.value)))
  isEditing.value = false
}

async function submit() {
  try {
    editable.priority = editable.priority.toUpperCase();
    editable.status = editable.status.toUpperCase(); // Ensure this status is being set before submission

    if (typeof editable.tags === 'string') {
      editable.tags = editable.tags.split(',').map(tag => tag.trim());
    } else if (!Array.isArray(editable.tags)) {
      editable.tags = [];
    }

    let savedQuest;
    if (questId.value) {
      savedQuest = await QuestService.updateInCampaign(campaignId.value, questId.value, editable);
      alert('✅ Quest updated successfully!');
    } else {
      savedQuest = await QuestService.createInCampaign(campaignId.value, editable);
      alert('✅ New quest created successfully!');
    }

    // Force status to remain the same even if backend response doesn't reflect it
    savedQuest.status = editable.status;  // Force update of status in savedQuest

    // Set status correctly after submission
    editable.status = savedQuest.status || 'Planned';
    Object.assign(quest.value, savedQuest);
    Object.assign(editable, savedQuest);
  } catch (err) {
    console.error('❌ Failed to save quest:', err);
    alert('Something went wrong while saving.');
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '—'
  const d = new Date(dateStr)
  return isNaN(d.getTime()) ? 'Invalid Date' : d.toLocaleString()
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
.bg-gray-800 {
  background-color: #2d3748; /* Dark gray background */
}
</style>
