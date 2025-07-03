<template>
  <div class="min-h-screen flex flex-col bg-surface-100 text-gray-900">
    <!-- Always render the GlobalReminder component -->
    <GlobalReminder />

    <!-- Main Layout Container -->
    <AppLayout>
      <router-view />
    </AppLayout>
    <LiveChat />
  </div>
</template>

<script setup>
import { useGlobalReminderStore } from '@/store/globalReminder.js';
import GlobalReminder from '@/components/reminders/GlobalReminder.vue'; // Adjust path if needed
import AppLayout from '@/layouts/AppLayout.vue'; // Adjust path if needed
import LiveChat from '@/components/chat/LiveChat.vue'

// Global reminder store will be accessible from here
const globalReminderStore = useGlobalReminderStore();
</script>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  font-family: 'Segoe UI', sans-serif;
  background: var(--surface-ground);
}

#app {
  width: 100%;
}

header {
  background-color: var(--surface-section);
  color: var(--text-color);
  padding: 1rem;
}

main {
  padding: 0;
  flex-grow: 1; /* Ensure main content grows to take up available space */
}
</style>
