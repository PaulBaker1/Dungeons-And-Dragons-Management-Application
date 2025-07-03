<template>
  <div class="max-w-6xl mx-auto py-10 px-6 text-white">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold">🗺️ Quest List</h1>
      <router-link :to="'/campaigns/' + campaignId + '/quests/create'" class="btn-primary-green">➕ New Quest</router-link>
    </div>

    <div class="flex flex-wrap gap-4 bg-slate-800 p-4 rounded border border-slate-700">
      <input
          v-model="searchQuery"
          placeholder="Search by title..."
          class="input w-64"
      />
      <select v-model="selectedStatus" class="input w-48">
        <option value="">All Statuses</option>
        <option value="Active">Active</option>
        <option value="Completed">Completed</option>
        <option value="Failed">Failed</option>
      </select>
    </div>

    <div v-if="quests.length > 0" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
      <div
          v-for="quest in filteredQuests"
          :key="quest.id"
          class="bg-slate-800 border border-slate-700 rounded p-4 shadow space-y-2"
      >
        <div class="text-lg font-semibold">{{ quest.title }}</div>
        <div class="text-sm text-slate-400">{{ quest.description }}</div>
        <div class="text-xs">
          <span class="font-medium">Status:</span>
          <span :class="statusBadge(quest.status)">{{ quest.status }}</span>
        </div>
        <div class="flex gap-2 mt-2">
          <!-- Update Edit link to include campaignId -->
          <router-link
              :to="`/campaigns/${campaignId}/quests/${quest.id}/edit`"
              class="btn-outline text-sm px-2"
          >
            ✏️ Edit
          </router-link>
          <button
              @click="deleteQuest(quest.id)"
              class="btn-danger text-sm px-2"
          >
            🗑️ Delete
          </button>
        </div>
      </div>
    </div>

    <div v-else class="text-slate-500 text-sm italic text-center pt-10">
      No quests available.
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import QuestService from '@/services/QuestService.js';

const quests = ref([]);
const searchQuery = ref('');
const selectedStatus = ref('');
const route = useRoute();
const campaignId = route.params.campaignId;  // Correctly access campaignId from the route

onMounted(async () => {
  if (campaignId) {
    try {
      quests.value = await QuestService.getQuestsByCampaign(campaignId);
    } catch (error) {
      console.error('Failed to fetch quests:', error);
    }
  } else {
    console.error('No campaign ID found in route');
  }
});

const filteredQuests = computed(() => {
  let result = quests.value;

  if (searchQuery.value.trim()) {
    result = result.filter((qs) =>
        qs.title.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
  }

  if (selectedStatus.value) {
    result = result.filter((qs) => qs.status === selectedStatus.value);
  }

  return result;
});

async function deleteQuest(questId) {
  if (confirm('Are you sure you want to delete this quest?')) {
    try {
      await QuestService.remove(campaignId.value, questId);  // Pass campaignId along with questId
      quests.value = quests.value.filter((q) => q.id !== questId);  // Remove quest from the list
    } catch (error) {
      console.error('Failed to delete quest:', error);
      alert('Failed to delete quest.');
    }
  }
}


function statusBadge(status) {
  return {
    Active: 'text-green-400',
    Completed: 'text-blue-400',
    Failed: 'text-red-400',
  }[status] || 'text-slate-400';
}
</script>

<style scoped>
.input {
  @apply bg-slate-900 border border-slate-600 text-white text-sm rounded px-3 py-2;
}
.btn-primary-green {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded;
}
.btn-outline {
  @apply border border-slate-500 text-white rounded hover:bg-slate-700;
}
.btn-danger {
  @apply bg-red-600 hover:bg-red-700 text-white rounded;
}
</style>
