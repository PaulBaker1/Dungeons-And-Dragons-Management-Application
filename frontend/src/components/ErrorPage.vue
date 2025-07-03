<template>
  <div class="max-w-6xl mx-auto py-16 px-6 text-white space-y-8">
    <div class="text-center space-y-4">
      <h1 class="text-6xl font-extrabold text-green-400">404</h1>
      <p class="text-2xl text-slate-300">Oops — you’ve wandered off the map!</p>
    </div>

    <blockquote class="bg-slate-800 border border-slate-700 px-6 py-4 rounded text-center text-lg italic text-slate-400 max-w-3xl mx-auto">
      “{{ quote }}”
    </blockquote>

    <div class="text-center">
      <router-link to="/" class="btn-primary-green inline-block">
        🧭 Return to Safety Zone
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const quotes = [
  'When in doubt, roll for initiative.',
  'Never split the party.',
  'May your d20 land on a natural 20.',
  'Beware the gelatinous cube.',
  'I cast fireball at my feet... again.',
  'Not all who wander are lost, some are just looking for treasure.',
  'Always check for traps—twice.',
  'That’s not a bug, that’s a feature… in the monster manual.',
  'You can’t outrun a beholder.',
  'There’s no problem a good long rest can’t fix.'
]

const quote = ref('')

onMounted(() => {
  quote.value = quotes[Math.floor(Math.random() * quotes.length)]
})
</script>

<style scoped>
.btn-primary-green {
  @apply bg-green-500 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded;
}
</style>
