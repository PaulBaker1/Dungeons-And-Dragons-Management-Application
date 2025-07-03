<!-- src/components/campaigns/CampaignAddCard.vue -->
<template>
  <div
      @click="$router.push('/campaigns/create')"
      class="cursor-pointer bg-slate-800 hover:bg-slate-700 border border-dashed border-slate-600 rounded-md flex items-center justify-center h-64 transition"
  >
    <div class="text-center text-slate-400 space-y-2">
      <i class="fas fa-plus-circle text-3xl text-indigo-400" />
      <div class="text-sm font-medium">Create new campaign</div>
    </div>
  </div>
</template>
