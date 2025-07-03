<template>
  <div class="min-h-screen bg-slate-900 text-white">
    <div class="max-w-3xl mx-auto py-10 px-4">
      <div class="flex justify-between items-center mb-6">
        <router-link :to="`/campaigns/${campaignId}`" class="btn-outline">
          ← Back
        </router-link>
        <h1 class="text-2xl font-bold">
          {{ isPrivate ? 'Edit Private Notes' : 'Edit Public Notes' }}
        </h1>
        <button @click="cancelEdit" class="btn-outline">
          Cancel
        </button>
      </div>

      <form @submit.prevent="saveNotes" class="space-y-6">
        <TiptapEditor
            v-model="notes"
            placeholder="Enter your notes here..."
            class="input min-h-[300px]"
        />

        <div class="flex justify-end space-x-4">
          <button type="button" @click="cancelEdit" class="btn-outline">
            Cancel
          </button>
          <button type="submit" class="btn-primary">
            💾 Save
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCampaignStore } from '@/store/campaignStore.js'
import TiptapEditor from '@/components/TiptapEditor.vue'

const route = useRoute()
const router = useRouter()
const store = useCampaignStore()

const campaignId = computed(() => route.params.id)
// route.params.type should be 'private' or 'public'
const noteType = computed(() => route.params.type)
const isPrivate = computed(() => noteType.value === 'private')

const notes = ref('')
const campaign = computed(() => store.selectedCampaign)

onMounted(async () => {
  await store.fetchCampaignById(campaignId.value)
  // initialize editor content based on type
  notes.value = isPrivate.value
      ? (campaign.value.dmNotesPrivate || '')
      : (campaign.value.dmNotesPublic || '')
})

function cancelEdit() {
  router.push(`/campaigns/${campaignId.value}`)
}

async function saveNotes() {
  // prepare payload
  const payload = {}
  if (isPrivate.value) payload.dmNotesPrivate = notes.value
  else payload.dmNotesPublic = notes.value

  // update and navigate back
  await store.updateCampaignNotes(campaignId.value, payload)
  router.push(`/campaigns/${campaignId.value}`)
}
</script>

<style scoped>
.input {
  @apply w-full bg-slate-800 p-4 border border-slate-600 rounded text-white;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded;
}
.btn-outline {
  @apply bg-transparent border border-slate-600 text-white px-4 py-2 rounded hover:bg-slate-800;
}
</style>
