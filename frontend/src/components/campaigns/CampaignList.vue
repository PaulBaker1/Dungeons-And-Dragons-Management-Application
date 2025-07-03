<template>
  <div>
    <!-- Show login form if not logged in -->
    <Login
        v-if="!isLoggedIn"
        @login-success="onLoginSuccess"
    />

    <!-- Once logged in, show filters and campaign cards -->
    <div v-else class="max-w-6xl mx-auto py-10 px-6 text-white space-y-6">
      <div class="flex justify-between items-center">
        <h1 class="text-3xl font-bold">📘 Campaigns</h1>
        <button @click="$router.push('/campaigns/create')" class="btn-primary-green">
          ➕ New Campaign
        </button>
      </div>

      <!-- Filters -->
      <div class="flex flex-wrap gap-4 bg-slate-800 p-4 rounded border border-slate-700">
        <input
            v-model="search"
            placeholder="Search by title..."
            class="input w-64"
        />
        <select v-model="sort" class="input w-48">
          <option value="created">🕓 Sort by Created</option>
          <option value="title">🔤 Sort by Title</option>
          <option value="players">👥 Sort by Players</option>
        </select>
      </div>

      <!-- Campaign Cards -->
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        <div
            v-for="c in filteredCampaigns"
            :key="c.id"
            class="bg-slate-800 border border-slate-700 rounded p-4 shadow space-y-2"
        >
          <div class="text-lg font-semibold">{{ c.title }}</div>
          <!-- Show last session objective instead of date -->
          <div class="text-sm text-slate-400">
            Last session: {{ new Date().toISOString().split('T')[0] }}
          </div>
          <div class="text-xs">
            <span class="font-medium">Players:</span> {{ '🟢 Active' }}
          </div>
          <div class="flex gap-2 mt-2">
            <router-link
                :to="`/campaigns/${c.id}`"
                class="btn-outline text-sm px-2"
            >
              🔍 View
            </router-link>
            <router-link
                :to="`/campaigns/${c.id}/notes/public`"
                class="btn-outline text-sm px-2"
            >
              📝 Notes
            </router-link>
            <!-- Delete button added -->
            <button
                @click="deleteCampaign(c.id)"
                class="btn-danger text-sm px-2"
            >
              🗑️ Delete
            </button>
          </div>
        </div>
      </div>

      <div v-if="!filteredCampaigns.length" class="text-slate-500 text-sm italic">
        No campaigns found.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import Login from '@/components/auth/Login.vue'
import { authFetch } from '@/components/auth/api/httpAuth'
import { useCampaignStore } from '@/store/campaignStore.js'

const store = useCampaignStore()
const search = ref('')
const sort = ref('created')
const isLoggedIn = ref(Boolean(localStorage.getItem('basicCreds')))
const campaigns = computed(() => store.campaigns)

onMounted(() => {
  if (isLoggedIn.value) {
    store.fetchCampaigns()
  }
})

const filteredCampaigns = computed(() => {
  const query = search.value.trim().toLowerCase()
  let list = [...(campaigns.value || [])]

  if (query) {
    list = list.filter(c => c.title?.toLowerCase().includes(query))
  }

  return list.sort((a, b) => {
    switch (sort.value) {
      case 'title':
        return (a.title || '').localeCompare(b.title || '')
      case 'players':
        return (b.playerCount ?? 0) - (a.playerCount ?? 0)
      case 'created':
      default:
        return new Date(b.createdAt || 0) - new Date(a.createdAt || 0)
    }
  })
})

async function onLoginSuccess() {
  isLoggedIn.value = true
  await store.fetchCampaigns()
}

async function deleteCampaign(id) {
  if (confirm(`Are you sure you want to delete this campaign?`)) {
    try {
      await store.deleteCampaign(id)
      alert('Campaign deleted successfully!')
      store.fetchCampaigns()
    } catch (err) {
      console.error('Failed to delete campaign', err)
      alert('Failed to delete campaign.')
    }
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
.btn-primary-green {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded;
}
.btn-danger {
  @apply bg-red-600 hover:bg-red-700 text-white rounded;
}
</style>
---

## How to Log In

Depending on your preference, you can log in either via the browser’s built‑in Basic Auth prompt (for a quick demo) or through the custom Vue login form, **and you must ensure your campaign fetch calls carry the `Authorization` header**.

### A) Browser Prompt Method

1. **Run the backend**: `mvn spring-boot:run`.
2. **Visit the protected endpoint**: open `http://localhost:8080/api/hello` in your browser.
3. When the browser shows the Basic Auth dialog, enter one of the two demo credentials:
- **dm** / **admin123**
- **user** / **user123**
4. On success, you will see a plain‑text greeting like:
```text
Hello, dm! Roles: [ROLE_ADMIN]