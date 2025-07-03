<template>
  <section class="home">
    <h2 class="text-2xl font-bold mb-4">Welcome to the Chat App 🧙</h2>
    <p class="mb-4">Select your role from the chat bubble and start talking!</p>

    <DemoDisplay />
  </section>
</template>

<script setup>
import DemoDisplay from '@/components/DemoDisplay.vue'
</script>

<style scoped>
.home {
  padding: 2rem;
}
</style>
