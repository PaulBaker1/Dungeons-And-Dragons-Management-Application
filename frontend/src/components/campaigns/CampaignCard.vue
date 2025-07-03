<template>
  <Card class="shadow-2xl border-round-xl">
    <!-- Banner -->
    <img
        :src="campaign.image || fallbackImage"
        alt="Campaign Banner"
        class="w-full h-36 object-cover rounded-t"
    />

    <!-- Top-right controls -->
    <div class="absolute top-2 right-2 flex gap-2 text-white text-sm">
      <i class="fas fa-thumbtack opacity-70 hover:opacity-100 cursor-pointer" title="Pin"></i>
      <i @click="confirmDelete" class="fas fa-trash opacity-70 hover:opacity-100 cursor-pointer" title="Delete"></i>
    </div>

    <div class="p-4 space-y-2">
      <div class="text-xs text-slate-400">Experience advancement</div>
      <h2 class="text-lg font-semibold truncate text-white" :title="campaign.name">{{ campaign.name }}</h2>
      <div class="text-sm text-slate-300">
        👥 {{ campaign.playerCount ?? 0 }} players &nbsp;
        🧠 {{ campaign.encounterCount ?? 0 }} encounters
      </div>

      <Button
          :label="'👁 View Campaign'"
          :to="`/campaigns/${campaign.id}`"
          class="p-button p-button-sm w-full"
      />

      <Button
          label="🗑️ Delete"
          @click="confirmDelete"
          class="p-button-danger p-button-sm w-full"
      />

      <div class="text-xs text-slate-500 pt-2">
        Created {{ formatDate(campaign.createdAt) }}
      </div>
    </div>
  </Card>
</template>

<script setup>
import Card from 'primevue/card';
import Button from 'primevue/button';
import { useCampaignStore } from '@/store/campaignStore';
import { useRouter } from 'vue-router';

const props = defineProps({ campaign: Object });
const fallbackImage = 'https://placehold.co/600x200/1e293b/ffffff?text=Campaign+Banner';
const router = useRouter();
const store = useCampaignStore();

function formatDate(iso) {
  if (!iso) return 'Unknown date';
  const date = new Date(iso);
  return date.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' }) +
      ' at ' + date.toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit' });
}

async function confirmDelete() {
  if (confirm(`⚠️ Are you sure you want to delete "${props.campaign.name}"? This cannot be undone.`)) {
    await store.deleteCampaign(props.campaign.id);
  }
}
</script>

<style scoped>
:deep(.p-card) {
  background-color: #1e293b;
  color: #f1f5f9;
  border-radius: 0.5rem;
  border: 1px solid #334155;
}

:deep(.p-button) {
  background-color: #334155;
  color: white;
  font-weight: 600;
}

:deep(.p-button-danger) {
  background-color: #991b1b;
  color: white;
  font-weight: 600;
}
</style>
