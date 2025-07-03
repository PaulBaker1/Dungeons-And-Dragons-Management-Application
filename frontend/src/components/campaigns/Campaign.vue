<template>
  <div class="flex flex-1">
    <!-- Main Campaigns Area -->
    <div class="flex-1 px-6 py-4 space-y-4">
      <!-- Breadcrumb -->
      <div class="text-sm text-slate-400 flex items-center space-x-2">
        <span class="text-slate-300">Content</span>
        <i class="fas fa-angle-right" />
        <span class="text-white font-medium">Campaigns</span>
      </div>

      <!-- Campaign Stepper -->
      <CampaignStepper />

      <!-- Instructional Banner -->
      <div class="bg-slate-800 border border-slate-700 rounded-md p-4 flex justify-between items-center">
        <div class="text-slate-300 text-sm">
          Create characters for the players in your party.
        </div>
        <router-link to="/players/create" class="btn-primary-green">
          ➕ Create Player
        </router-link>
      </div>

      <!-- Toolbar -->
      <div class="flex items-center justify-between flex-wrap gap-2">
        <span class="text-slate-400 text-sm">{{ filteredCampaigns.length }} campaigns</span>
        <div class="flex items-center gap-2">
          <router-link to="/campaigns/create" class="btn-primary-green">
            ➕ New
          </router-link>

          <!-- Filter -->
          <select v-model="filterStatus" class="input text-sm">
            <option value="">All</option>
            <option value="DRAFT">Draft</option>
            <option value="ACTIVE">Active</option>
            <option value="ARCHIVED">Archived</option>
          </select>

          <!-- Sort -->
          <select v-model="sortBy" class="input text-sm">
            <option value="title">Title</option>
            <option value="createdAt">Created</option>
          </select>
        </div>
      </div>

      <!-- Campaign Cards -->
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4">
        <CampaignCard
            v-for="c in sortedCampaigns"
            :key="c.id"
            :campaign="c"
        />
      </div>

      <!-- Empty state -->
      <div v-if="!campaigns.length" class="text-slate-500 text-sm italic mt-4">
        No campaigns available.
      </div>
    </div>

    <!-- Right Sidebar -->
    <div class="hidden xl:block w-80 border-l border-slate-700 p-4 bg-slate-900 space-y-6">
      <SidebarWidgets />
    </div>
  </div>
</template>

<script setup>
import { onMounted, computed, ref } from 'vue'
import { useCampaignStore } from '@/store/campaignStore'
import CampaignStepper from '@/components/campaigns/CampaignStepper.vue'
import CampaignCard from '@/components/campaigns/CampaignCard.vue'
import SidebarWidgets from '@/components/layout/SidebarWidgets.vue'

const store = useCampaignStore()
const filterStatus = ref('')
const sortBy = ref('title')

onMounted(() => {
  store.fetchCampaigns()
})

const campaigns = computed(() => store.campaigns)

const filteredCampaigns = computed(() => {
  return campaigns.value.filter(c => !filterStatus.value || c.status === filterStatus.value)
})

const sortedCampaigns = computed(() => {
  return [...filteredCampaigns.value].sort((a, b) => {
    if (sortBy.value === 'createdAt') {
      return new Date(b.createdAt) - new Date(a.createdAt)
    }
    return (a[sortBy.value] || '').localeCompare(b[sortBy.value] || '')
  })
})
</script>

<style scoped>
.input {
  @apply bg-slate-800 text-white border border-slate-600 rounded px-2 py-1;
}
.btn-primary-green {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded;
}
.btn-primary-green {
  @apply bg-blue-500 hover:bg-blue-600 text-white font-semibold px-4 py-2 rounded;
}
</style>
