<template>
  <div class="min-h-screen bg-slate-900 text-white">
    <!-- General Container -->
    <div v-if="campaign" class="max-w-5xl mx-auto py-10 px-6 space-y-10">
      <!-- Back Button -->
      <router-link :to="`/campaigns`" class="btn-primary text-sm">
        ← Back to Campaign
      </router-link>

      <!-- Campaign Header -->
      <div>
        <div class="flex justify-between items-start">
          <h1 class="text-3xl font-bold mb-2">
            <span v-if="!isEditing">{{ campaign.title }}</span>
            <input
                v-else
                v-model="editedTitle"
                class="bg-slate-800 border border-slate-600 rounded px-3 py-1 text-white w-full"
                placeholder="Campaign Title"
            />
          </h1>
          <button @click="toggleEdit" class="btn-primary text-sm px-4 py-2">
            {{ isEditing ? '❌ Cancel' : '✏️ Edit' }}
          </button>
        </div>

        <!-- Status -->
        <div v-if="!isEditing" class="text-sm text-slate-400 mb-4">
          🟢 Status: <span class="text-white font-medium">{{ campaign.status }}</span>
        </div>
        <div v-else class="mb-4">
          <label class="label">Status</label>
          <select v-model="editedStatus" class="input">
            <option value="DRAFT">Draft</option>
            <option value="ACTIVE">Active</option>
            <option value="COMPLETED">Completed</option>
            <option value="ARCHIVED">Archived</option>
          </select>
        </div>

        <!-- Banner image + description -->
        <img
            class="rounded shadow-xl w-full mb-4"
            :src="isEditing ? editedBanner : bannerUrl"
            alt="Campaign Banner"
        />
        <div v-if="isEditing">
          <input
              v-model="editedBanner"
              placeholder="Banner image URL"
              class="bg-slate-800 border border-slate-600 rounded px-3 py-1 text-sm text-white w-full mb-2"
          />

          <TiptapEditor v-model="editedDescription" placeholder="Campaign description..." />
          <button @click="saveEdits" class="btn-primary mt-2">
            💾 Save Changes
          </button>
        </div>

        <!-- Add border around description -->
        <div v-else class="border border-slate-700 p-4 rounded-lg mb-4">
          <p class="text-slate-400 text-sm" v-html="campaign.description"></p>
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <!-- 📍 Key Locations Section -->
        <fieldset class="card">
          <legend class="section">📍 Key Locations</legend>
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-medium">Locations Management</h3>
            <button @click="toggleLocationEdit" class="btn-outline text-sm">
              {{ editingLocations ? 'Cancel' : 'Edit' }}
            </button>
          </div>

          <div class="grid grid-cols-1 gap-4">
            <div v-if="editingLocations">
              <div v-for="(loc, index) in tempLocations" :key="index" class="flex gap-2 items-center mb-2">
                <input v-model="tempLocations[index]" class="input flex-1" placeholder="Enter location" />
                <button @click.prevent="removeTempLocation(index)" class="text-red-400 hover:text-red-500">✕</button>
              </div>
              <div class="flex flex-col sm:flex-row gap-2">
                <button @click="addTempLocation" class="btn-outline">➕ Add Location</button>
                <button @click="saveLocations" class="btn-primary">💾 Save Locations</button>
              </div>
            </div>

            <div v-else>
              <ul class="list-disc pl-5 space-y-2">
                <li v-for="(location, index) in campaign.keyLocations" :key="index" class="text-slate-300">
                  {{ location }}
                </li>
                <p v-if="!campaign.keyLocations?.length" class="text-slate-400 italic">No locations added</p>
              </ul>
            </div>
          </div>
        </fieldset>

        <!-- 📂 Sections Section -->
        <fieldset class="card">
          <legend class="section">📂 Sections</legend>
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-medium">Sections Management</h3>
            <button @click="toggleSectionEdit" class="btn-outline text-sm">
              {{ editingSections ? 'Cancel' : 'Edit' }}
            </button>
          </div>

          <div class="grid grid-cols-1 gap-4">
            <div v-if="editingSections">
              <div v-for="(sec, index) in tempSections" :key="index" class="flex gap-2 items-center mb-2">
                <input v-model="tempSections[index]" class="input flex-1" placeholder="Section title" />
                <button @click.prevent="removeTempSection(index)" class="text-red-400 hover:text-red-500">✕</button>
              </div>
              <div class="flex flex-col sm:flex-row gap-2">
                <button @click="addTempSection" class="btn-outline">➕ Add Section</button>
                <button @click="saveSections" class="btn-primary">💾 Save Sections</button>
              </div>
            </div>

            <div v-else>
              <ul class="list-disc pl-5 space-y-2">
                <li v-for="(section, index) in campaign.sections" :key="index" class="text-slate-300">
                  {{ section }}
                </li>
                <p v-if="!campaign.sections?.length" class="text-slate-400 italic">No sections added</p>
              </ul>
            </div>
          </div>
        </fieldset>

        <!-- 🧪 Level Range Section -->
        <fieldset class="card">
          <legend class="section">🧪 Level Range</legend>

          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-medium text-slate-300">Level Configuration</h3>
            <button @click="toggleLevelEdit" class="btn-outline text-sm px-3 py-1">
              {{ editingLevels ? 'Cancel' : 'Edit' }}
            </button>
          </div>

          <div v-if="editingLevels" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="label">Minimum Level</label>
                <input type="number" v-model.number="tempMinLevel" class="input w-full" min="1" max="20" />
              </div>
              <div>
                <label class="label">Maximum Level</label>
                <input type="number" v-model.number="tempMaxLevel" class="input w-full" min="1" max="20" />
              </div>
            </div>
            <button @click="saveLevels" class="btn-primary w-full sm:w-auto">💾 Save Changes</button>
          </div>

          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="text-center">
              <p class="text-sm text-slate-400 mb-1">Minimum Level</p>
              <p class="text-white text-xl font-semibold">{{ campaign.minLevel || 1 }}</p>
            </div>
            <div class="text-center">
              <p class="text-sm text-slate-400 mb-1">Maximum Level</p>
              <p class="text-white text-xl font-semibold">{{ campaign.maxLevel || 20 }}</p>
            </div>
          </div>
        </fieldset>

        <!-- ⏱️ Timer Section -->
        <fieldset class="card">
          <legend class="section">⏱️ Timer</legend>

          <div class="flex justify-between items-center mb-4">
<!--            <h3 class="text-lg font-medium"></h3>-->
            <button @click="toggleTimer" class="btn-outline text-sm">
              {{ isRunning ? 'Stop Timer' : 'Start Timer' }}
            </button>
            <button @click="resetTimer" class="btn-outline text-sm ml-2" :disabled="!isRunning">Reset</button>
          </div>

          <div class="text-center">
            <p class="text-white text-3xl">{{ formattedTime }}</p>
          </div>
        </fieldset>
      </div>

      <!-- ▪️ Available Characters -->
      <div v-if="availableCharacters.length" class="mt-10">
        <h2 class="text-xl font-bold mb-4">Available Characters</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
              v-for="char in availableCharacters"
              :key="char.id"
              :class="[
              'border border-slate-700 rounded-xl p-4 shadow-lg flex flex-col justify-between',
              char.deactivated ? 'bg-gray-500' : 'bg-slate-800'
            ]"
          >
            <div class="space-y-2">
              <h3 class="text-lg font-semibold text-white truncate">
                {{ char.characterName }}
              </h3>
              <!-- Show player info only if active -->
              <div v-if="!char.deactivated">
                <p class="text-sm text-slate-400">{{ char.playerName }}</p>
                <div class="text-sm text-slate-400">{{ char.classAndLevel }}</div>
                <div class="text-xs text-slate-500 mt-1">
                  🎯 AC: {{ char.armorClass }} | ❤️ HP: {{ char.hitPoints }} | ⚡ Init: {{ char.initiative }}
                </div>
              </div>
              <div v-else class="text-sm text-slate-400">
                This character is deactivated.
              </div>
            </div>
            <div class="pt-4 flex flex-col sm:flex-row gap-2">
              <router-link
                  v-if="!char.deactivated"
                  :to="`/characters/${char.id}`"
                  class="btn-primary-blue text-sm w-full text-center py-1"
              >
                👀 View
              </router-link>
              <button
                  v-if="!char.deactivated"
                  class="btn-danger text-sm w-full py-1"
                  @click="deactivateCharacter(char.id)"
              >
                Deactivate
              </button>
              <div
                  v-else
                  class="w-full text-center text-xs text-slate-300 py-1"
              >
                Deactivated
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="text-slate-500 italic text-sm mt-4">
        No available characters to add.
      </div>

      <!-- ▪️ Quests Section -->
      <div>
        <h2 class="text-xl font-bold mb-4">🗺️ Quests</h2>
        <div class="flex justify-between items-center mb-2">
          <p class="text-slate-300 text-sm">
            Track your quests for this campaign.
          </p>
          <router-link
              :to="`/campaigns/${campaignId}/quests/create`"
              class="btn-primary text-sm"
          >
            ➕ Add Quest
          </router-link>
        </div>
        <div v-if="quests.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
              v-for="quest in quests"
              :key="quest.id"
              class="bg-slate-800 border border-slate-700 rounded p-4 shadow space-y-1"
          >
            <div class="text-lg font-semibold">{{ quest.title }}</div>
            <div class="text-sm text-slate-400 truncate">{{ quest.description }}</div>
            <div class="text-xs text-slate-500">
              🎯 Status: {{ quest.status }}
            </div>
            <div class="flex flex-col sm:flex-row gap-2 mt-2">
<!--              <router-link-->
<!--                  :to="`/campaigns/${campaignId}/quests/${quest.id}`"-->
<!--                  class="btn-outline text-sm"-->
<!--              >-->
<!--                🔍 View-->
<!--              </router-link>-->
              <router-link
                  :to="`/campaigns/${campaignId}/quests/${quest.id}/edit`"
                  class="btn-outline text-sm"
              >
                🔍 View
              </router-link>
              <button
                  @click="deleteQuest(quest.id)"
                  class="btn-danger text-sm"
              >
                🗑️ Delete
              </button>
            </div>
          </div>
        </div>
        <div v-else class="text-slate-500 italic text-sm">
          No quests added yet.
        </div>
      </div>

      <!-- ▪️ Sessions Section -->
      <div>
        <h2 class="text-xl font-bold mb-4">📅 Sessions</h2>
        <div class="space-y-4">
          <div class="flex justify-between items-center">
            <p class="text-slate-300 text-sm">
              Track your campaign sessions here.
            </p>
            <router-link
                :to="`/campaigns/${campaignId}/sessions/create`"
                class="btn-primary text-sm"
            >
              ➕ Add Session
            </router-link>
          </div>
          <div v-if="sessionStore.sessions.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div
                v-for="session in sessionStore.sessions"
                :key="session.id"
                class="bg-slate-800 border border-slate-700 rounded p-4 shadow space-y-1"
            >
              <div class="text-lg font-semibold">
                {{ session.title }}
              </div>
              <div class="text-sm text-slate-400">
                <!-- Display the objective instead of the date -->
                {{ session.objective }}
              </div>
              <div class="flex flex-col sm:flex-row gap-2 mt-2">
<!--                <router-link-->
<!--                    :to="`/campaigns/${campaignId}/sessions/${session.id}/view`"-->
<!--                    class="btn-outline text-sm"-->
<!--                >-->
<!--                  🔍 View-->
<!--                </router-link>-->
                <router-link
                    :to="`/campaigns/${campaignId}/sessions/${session.id}/edit`"
                    class="btn-outline text-sm"
                >
                  🔍 View
                </router-link>
                <button
                    @click="confirmDelete(session)"
                    class="btn-danger text-sm"
                >
                  🗑️ Delete
                </button>
              </div>
            </div>
          </div>
          <div v-else class="text-slate-500 italic text-sm">
            No sessions added yet.
          </div>
        </div>
      </div>

      <!-- ▪️ DM Notes Section -->
      <section class="grid md:grid-cols-2 gap-6 mt-12">
        <!-- Private Notes -->
        <div class="bg-slate-800 p-4 rounded shadow">
          <div class="flex justify-between items-center mb-2">
            <h3 class="text-lg font-semibold">DM Notes (Private)</h3>
            <router-link
                :to="`/campaigns/${campaignId}/notes/private`"
                class="btn-primary-blue text-sm"
            >
              Edit Private Notes
            </router-link>
          </div>
          <div class="text-sm text-slate-300">
            <div v-if="campaign.dmNotesPrivate" v-html="campaign.dmNotesPrivate"></div>
            <p v-else class="italic text-slate-400">No private notes yet.</p>
          </div>
        </div>

        <!-- Public Notes -->
        <div class="bg-slate-800 p-4 rounded shadow">
          <div class="flex justify-between items-center mb-2">
            <h3 class="text-lg font-semibold">DM Notes (Public)</h3>
            <router-link
                :to="`/campaigns/${campaignId}/notes/public`"
                class="btn-primary-blue text-sm"
            >
              Edit Public Notes
            </router-link>
          </div>
          <div class="text-sm text-slate-300">
            <div v-if="campaign.dmNotesPublic" v-html="campaign.dmNotesPublic"></div>
            <p v-else class="italic text-slate-400">No public notes yet.</p>
          </div>
        </div>
      </section>
    </div>

    <!-- Loading state -->
    <div v-else class="text-center py-20 text-slate-500">
      Loading campaign details...
    </div>
  </div>
</template>


<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useCampaignStore } from '@/store/campaignStore.js'
import { useSessionStore } from '@/store/sessionStore.js'
import CharacterService from '@/services/CharacterService.js'
import SessionService from '@/services/SessionService.js'
import QuestService from '@/services/QuestService.js'
import TiptapEditor from "@/components/TiptapEditor.vue";

const route = useRoute()
const campaignId = computed(() => route.params.id)
const storeCampaign = useCampaignStore()
const sessionStore = useSessionStore()

// Editable state
const isEditing = ref(false)
const editedTitle = ref('')
const editedDescription = ref('')
const editedBanner = ref('')
const editedStatus = ref('DRAFT')

// Tags
const editingTags = ref(false)
const tagInput = ref('')
const tempTags = ref([])

// Locations
const editingLocations = ref(false)
const tempLocations = ref([])

// Sections
const editingSections = ref(false)
const tempSections = ref([])

// Level Range
const editingLevels = ref(false)
const tempMinLevel = ref(1)
const tempMaxLevel = ref(20)

// Other data
const availableCharacters = ref([])
const quests = ref([])

const campaign = computed(() => storeCampaign.selectedCampaign)

// Timer state
const timer = ref(0)
let timerInterval = null; // Store interval ID
const isRunning = ref(false); // Timer running state

// Toggle timer state (Start/Stop)
function toggleTimer() {
  if (isRunning.value) {
    clearInterval(timerInterval); // Stop timer
  } else {
    startTimer(); // Start timer
  }
  isRunning.value = !isRunning.value; // Toggle the state
}

// Start timer
function startTimer() {
  timerInterval = setInterval(() => {
    timer.value++
  }, 1000)
}

// Reset timer
function resetTimer() {
  clearInterval(timerInterval); // Stop current timer
  timer.value = 0; // Reset the timer
  isRunning.value = false; // Stop running
}

// Format time as hh:mm:ss
const formattedTime = computed(() => {
  const hours = String(Math.floor(timer.value / 3600)).padStart(2, '0');
  const minutes = String(Math.floor((timer.value % 3600) / 60)).padStart(2, '0');
  const seconds = String(timer.value % 60).padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
});

watch(
    campaignId,
    async (id) => {
      if (id) {
        await storeCampaign.fetchCampaignById(id)
        await sessionStore.fetchByCampaign(id)
        await loadAvailableCharacters()
        quests.value = await QuestService.getQuestsByCampaign(id);

        tempTags.value = campaign.value.tags ? [...campaign.value.tags] : []
        tempLocations.value = campaign.value.keyLocations ? [...campaign.value.keyLocations] : []
        tempSections.value = campaign.value.sections ? [...campaign.value.sections] : []
        tempMinLevel.value = campaign.value.minLevel || 1
        tempMaxLevel.value = campaign.value.maxLevel || 20
      }
    },
    { immediate: true }
)

function toggleEdit() {
  if (!isEditing.value) {
    editedTitle.value = campaign.value.title
    editedDescription.value = campaign.value.description
    editedBanner.value = campaign.value.bannerImageUrl || ''
    editedStatus.value = campaign.value.status || 'DRAFT'
  }
  isEditing.value = !isEditing.value
}

async function saveEdits() {
  await storeCampaign.updateCampaign(campaign.value.id, {
    title: editedTitle.value,
    description: editedDescription.value,
    bannerImageUrl: editedBanner.value,
    status: editedStatus.value,
  })
  isEditing.value = false
}

// TAGS
function toggleTagEdit() {
  editingTags.value = !editingTags.value
  if (editingTags.value && campaign.value.tags) {
    tempTags.value = [...campaign.value.tags]
  }
}
function addTag() {
  const trimmed = tagInput.value.trim()
  if (trimmed && !tempTags.value.includes(trimmed)) {
    tempTags.value.push(trimmed)
    tagInput.value = ''
  }
}
function removeTag(index) {
  tempTags.value.splice(index, 1)
}
async function saveTags() {
  await storeCampaign.updateCampaign(campaign.value.id, {
    tags: tempTags.value
  })
  editingTags.value = false
}

// LOCATIONS
function toggleLocationEdit() {
  editingLocations.value = !editingLocations.value
  if (editingLocations.value && campaign.value.keyLocations) {
    tempLocations.value = [...campaign.value.keyLocations]
  }
}
function addTempLocation() {
  tempLocations.value.push('')
}
function removeTempLocation(index) {
  tempLocations.value.splice(index, 1)
}
async function saveLocations() {
  await storeCampaign.updateCampaign(campaign.value.id, {
    keyLocations: tempLocations.value
  })
  editingLocations.value = false
}

// SECTIONS
function toggleSectionEdit() {
  editingSections.value = !editingSections.value
  if (editingSections.value && campaign.value.sections) {
    tempSections.value = [...campaign.value.sections]
  }
}
function addTempSection() {
  tempSections.value.push('')
}
function removeTempSection(index) {
  tempSections.value.splice(index, 1)
}
async function saveSections() {
  await storeCampaign.updateCampaign(campaign.value.id, {
    sections: tempSections.value
  })
  editingSections.value = false
}

// LEVEL RANGE
function toggleLevelEdit() {
  if (!editingLevels.value) {
    tempMinLevel.value = campaign.value.minLevel || 1
    tempMaxLevel.value = campaign.value.maxLevel || 20
  }
  editingLevels.value = !editingLevels.value
}

async function saveLevels() {
  await storeCampaign.updateCampaign(campaign.value.id, {
    minLevel: tempMinLevel.value,
    maxLevel: tempMaxLevel.value,
    recommendedLevelRange: {
      minLevel: tempMinLevel.value,
      maxLevel: tempMaxLevel.value,
    },
  })
  editingLevels.value = false
}

// CHARACTERS
async function loadAvailableCharacters() {
  const characters = await CharacterService.getUnassigned()
  availableCharacters.value = characters.map(char => ({ ...char, deactivated: char.deactivated || false }))
}
async function deactivateCharacter(characterId) {
  try {
    availableCharacters.value = availableCharacters.value.map(char => {
      if (char.id === characterId) {
        return { ...char, deactivated: true }
      }
      return char
    })
    alert('Character has been deactivated.')
  } catch (err) {
    console.error(err)
    alert('Failed to deactivate character.')
  }
}

// QUESTS
async function deleteQuest(questId) {
  const confirmed = confirm(`Delete this quest?`)
  if (!confirmed) return
  try {
    await QuestService.remove(campaignId.value, questId);  // Pass campaignId and questId
    quests.value = quests.value.filter(q => q.id !== questId);
  } catch (err) {
    console.error('Failed to delete quest:', err);
    alert('Failed to delete quest.');
  }
}

// SESSIONS
async function confirmDelete(session) {
  const confirmed = confirm(`⚠️ Delete "${session.title}"?`)
  if (!confirmed) return
  await SessionService.remove(campaign.value.id, session.id)
  await sessionStore.fetchByCampaign(campaign.value.id)
}



const bannerUrl = computed(() =>
    campaign.value?.bannerImageUrl ||
    'https://placehold.co/1024x200/1e293b/ffffff?text=Campaign+Banner'
)
</script>

<style scoped>
/* Input and Label Styles */
.input {
  @apply w-full bg-slate-800 p-2 border border-slate-600 rounded text-white;
}
.label {
  @apply block text-sm mb-1 text-slate-400;
}
/* Card Wrapper */
.card {
  @apply border border-slate-700 rounded-lg p-4 bg-slate-900;
}
.section {
  @apply font-semibold text-slate-300 text-lg;
}
.section-round {
  @apply text-xl font-bold mb-2 text-slate-100;
}
/* Buttons */
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold rounded text-sm px-4 py-2 text-center;
}
.btn-primary-blue {
  @apply bg-blue-500 hover:bg-blue-600 text-white rounded text-sm px-4 py-2 text-center;
}
.btn-outline {
  @apply border border-slate-500 text-white rounded hover:bg-slate-700 w-full sm:w-auto text-center px-4 py-2;
}
.btn-danger {
  @apply bg-red-600 hover:bg-red-700 text-white rounded text-sm px-4 py-2 text-center;
}
</style>
