<template>
  <Card class="shadow-2xl border-round-xl relative overflow-hidden">
    <!-- class Banner -->
    <img
        :src="image || fallbackImage"
        :alt="`${dndClass.name} banner`"
        class="w-full h-36 object-cover rounded-t"
    />

    <!-- Top-right slot for future icons -->
    <div class="absolute top-2 right-2 flex gap-2 text-white text-sm">
      <i class="fas fa-dice-d20 opacity-60" title="D&D Class"></i>
    </div>

    <div class="p-4 space-y-2">
      <!-- Meta -->
      <div class="text-xs text-slate-400">Hit Die: <span class="font-medium text-slate-200">d{{ dndClass.hit_die }}</span></div>

      <!-- Name -->
      <h2 class="text-lg font-semibold truncate text-white" :title="dndClass.name">{{ dndClass.name }}</h2>

      <!-- Description -->
      <p class="text-sm text-slate-300 line-clamp-3">
        {{ dndClass.description || 'No description available for this class.' }}
      </p>

      <!-- View Button -->
      <Button
          :label="'🧙 View class'"
          :to="`/classes/${dndClass.index}`"
          class="p-button p-button-sm w-full"
      />
    </div>
  </Card>
</template>

<script setup>
import Card from 'primevue/card';
import Button from 'primevue/button';
import { computed } from 'vue';

const props = defineProps({
  dndClass: { type: Object, required: true }
});

const fallbackImage = 'https://placehold.co/600x200/1e293b/ffffff?text=D%26D+Class';

const image = computed(() =>
    props.dndClass.image || `https://source.unsplash.com/random/600x200/?fantasy,${props.dndClass.name}`
);
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
</style>
