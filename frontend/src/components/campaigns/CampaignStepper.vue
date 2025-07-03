<!-- src/components/campaigns/CampaignStepper.vue -->
<template>
  <div class="flex items-center justify-between bg-slate-800 border border-slate-700 rounded-md px-6 py-3">
    <div v-for="(step, index) in steps" :key="step.label" class="flex items-center">
      <div class="flex items-center space-x-2">
        <div class="w-5 h-5 bg-blue-500 rounded-full flex items-center justify-center text-white text-xs">
          {{ index + 1 }}
        </div>
        <span class="text-sm text-slate-300">{{ step.label }}</span>
      </div>
      <div v-if="index < steps.length - 1" class="mx-4 text-slate-500">→</div>
    </div>
  </div>
</template>

<script setup>
const steps = [
  { label: 'Create campaign' },
  { label: 'Create players' },
  { label: 'Add players to campaign' },
  { label: 'Create encounter' }
]
</script>
