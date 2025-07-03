<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <!-- Header Section with increased bottom margin -->
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-3xl font-bold">🛠 Create a New Campaign</h1>
    </div>

    <router-link :to="`/campaigns`" class="btn-primary text-sm">
      ← Back to Campaign Menu
    </router-link>

    <form @submit.prevent="submitForm" class="space-y-8">
      <!-- 🎯 Basic Info -->
      <fieldset class="card space-y-6">
        <legend class="section">🎯 Basic Info</legend>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="label">Title</label>
            <input
                v-model="form.title"
                class="input"
                placeholder="Enter campaign title"
                required
            />
          </div>
          <div>
            <label class="label">Status</label>
            <select v-model="form.status" class="input">
              <option value="DRAFT">Draft</option>
              <option value="ACTIVE">Active</option>
              <option value="COMPLETED">Complete</option>
              <option value="ARCHIVED">Archive</option>
            </select>
          </div>
        </div>

        <div>
          <label class="label">Description</label>
          <textarea
              v-model="form.description"
              class="input"
              rows="3"
              placeholder="What's this campaign about?"
          />
        </div>
      </fieldset>

      <!-- 🧪 Level Range -->
      <fieldset class="card space-y-4">
        <legend class="section">🧪 Level Range</legend>
        <div class="grid grid-cols-2 gap-6">
          <div>
            <label class="label">Min Level</label>
            <input
                type="number"
                v-model="form.minLevel"
                class="input"
                min="1"
                max="20"
            />
          </div>
          <div>
            <label class="label">Max Level</label>
            <input
                type="number"
                v-model="form.maxLevel"
                class="input"
                min="1"
                max="20"
            />
          </div>
        </div>
      </fieldset>

      <!-- 🏷️ Tags -->
      <fieldset class="card space-y-4">
        <legend class="section">🏷️ Tags</legend>
        <input
            v-model="tagInput"
            @keydown.enter.prevent="addTag"
            class="input"
            placeholder="Press Enter to add tag"
        />
        <div class="flex flex-wrap mt-2 gap-2">
          <span
              v-for="(tag, index) in form.tags"
              :key="index"
              class="bg-indigo-500 text-sm px-2 py-1 rounded"
          >
            {{ tag }} <button @click.prevent="removeTag(index)" class="ml-1 text-xs">✕</button>
          </span>
        </div>
      </fieldset>

      <!-- 📍 Key Locations -->
      <fieldset class="card space-y-4">
        <legend class="section">📍 Key Locations</legend>
        <div
            v-for="(loc, index) in form.locations"
            :key="index"
            class="flex gap-2 mb-2"
        >
          <input v-model="form.locations[index]" class="input flex-1" />
          <button @click.prevent="removeLocation(index)" class="text-red-400">✕</button>
        </div>
        <button @click.prevent="addLocation" class="btn-outline">
          + Add Location
        </button>
      </fieldset>

      <!-- 📂 Sections -->
      <fieldset class="card space-y-4">
        <legend class="section">📂 Sections</legend>
        <div
            v-for="(sec, index) in form.sections"
            :key="index"
            class="flex gap-2 mb-2"
        >
          <input v-model="form.sections[index]" class="input flex-1" />
          <button @click.prevent="removeSection(index)" class="text-red-400">✕</button>
        </div>
        <button @click.prevent="addSection" class="btn-outline">
          + Add Section
        </button>
      </fieldset>

      <!-- ⚙️ Settings -->
      <fieldset class="card space-y-4">
        <legend class="section">⚙️ Settings</legend>
        <div class="flex items-center gap-6">
          <label class="flex items-center gap-2">
            <input type="checkbox" v-model="form.visibleToPlayers" />
            <span class="text-sm">Visible to Players</span>
          </label>
          <label class="flex items-center gap-2">
            <input type="checkbox" v-model="form.pinned" />
            <span class="text-sm">Pinned</span>
          </label>
        </div>
      </fieldset>

      <button type="submit" class="btn-primary w-full">
        🚀 Create Campaign
      </button>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useCampaignStore } from '@/store/campaignStore'

const router = useRouter()
const store = useCampaignStore()
const tagInput = ref('')

const form = reactive({
  title: '',
  description: '',
  minLevel: 1,
  maxLevel: 20,
  tags: [],
  sections: [],
  locations: [],
  status: 'DRAFT',
  visibleToPlayers: false,
  // You can add adventureCode and involvedFactions if needed:
  adventureCode: '',
  involvedFactions: []  // e.g., an empty array if not using this now
})

function addTag() {
  const trimmed = tagInput.value.trim()
  if (trimmed && !form.tags.includes(trimmed)) {
    form.tags.push(trimmed)
    tagInput.value = ''
  }
}
function removeTag(index) { form.tags.splice(index, 1) }
function addLocation() { form.locations.push('') }
function removeLocation(index) { form.locations.splice(index, 1) }
function addSection() { form.sections.push('') }
function removeSection(index) { form.sections.splice(index, 1) }

async function submitForm() {
  try {
    const payload = {
      title: form.title,
      description: form.description,
      status: form.status,
      minLevel: form.minLevel,
      maxLevel: form.maxLevel,
      adventureCode: form.adventureCode || "",
      tags: form.tags, // array of strings
      involvedFactions: form.involvedFactions || [],
      visibleToPlayers: form.visibleToPlayers,
      sections: form.sections, // array of strings
      keyLocations: form.locations, // array of strings
      archived: false,
      levelRange: {
        minLevel: form.minLevel,
        maxLevel: form.maxLevel
      }
    };

    console.log('Payload being sent:', JSON.stringify(payload, null, 2));
    await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/campaigns/create`, payload)
    await store.fetchCampaigns()
    router.push('/campaigns')
  } catch (err) {
    console.error('Error creating campaign:', err)
    if (err.response && err.response.data) {
      console.error('Server error details:', err.response.data)
      alert(`Failed to create campaign: ${err.response.data.message || ''}`)
    } else {
      alert('Failed to create campaign.')
    }
  }
}
</script>

<style scoped>
.input {
  @apply w-full bg-slate-800 p-2 border border-slate-600 rounded text-white;
}
.label {
  @apply block text-sm mb-1 text-slate-400;
}
.card {
  @apply border border-slate-700 rounded-lg p-4 bg-slate-900;
}
.section {
  @apply font-semibold text-slate-300 mb-2 text-lg;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
}
.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}
</style>
