<template>
  <section class="grid md:grid-cols-2 gap-6 mt-12">
    <div class="bg-slate-800 p-4 rounded shadow">
      <div class="flex justify-between items-center mb-2">
        <h3 class="text-lg font-semibold">DM Notes (Private)</h3>
        <router-link :to="`/campaigns/${campaignId}/notes`" class="text-sm text-blue-400 hover:underline">Edit Notes</router-link>
      </div>
      <div v-html="privateNotes" class="prose prose-invert text-sm text-slate-300"></div>
    </div>

    <div class="bg-slate-800 p-4 rounded shadow">
      <div class="flex justify-between items-center mb-2">
        <h3 class="text-lg font-semibold">DM Notes (Public)</h3>
        <router-link :to="`/campaigns/${campaignId}/notes`" class="text-sm text-blue-400 hover:underline">Edit Notes</router-link>
      </div>
      <div v-html="publicNotes" class="prose prose-invert text-sm text-slate-300"></div>
    </div>
  </section>
</template>

<script setup>
defineProps({
  privateNotes: String,
  publicNotes: String,
  campaignId: [String, Number],
})
</script>
