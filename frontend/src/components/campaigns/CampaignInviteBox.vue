<template>
  <div class="bg-slate-800 rounded p-4 shadow mb-8">
    <p class="text-sm text-slate-400 mb-1">Campaign Invite Link</p>
    <div class="flex items-center justify-between">
      <input :value="link" readonly class="w-full bg-slate-700 px-3 py-2 rounded text-sm text-white font-mono mr-2" />
      <button @click="copy" class="btn-outline">Copy Link</button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({ code: String })
const link = computed(() => `https://yourapp.com/join/${props.code}`)

const copy = () => {
  navigator.clipboard.writeText(link.value)
  alert('Invite link copied!')
}
</script>
