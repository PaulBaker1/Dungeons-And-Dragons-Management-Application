<!-- GlobalReminder.vue -->
<template>
  <div v-if="isGlobalNotificationActive" class="fixed inset-0 flex items-center justify-center z-50">
    <div class="bg-teal p-4 rounded-lg text-center max-w-md mx-auto">
      <h2 class="text-xl text-white font-semibold mb-4">⏳ Reminder Time!</h2>
      <p class="text-lg text-white mb-6">{{ globalReminderText }}</p>
      <button @click="closeNotification" class="btn-primary">OK</button>
    </div>
  </div>
</template>

<script setup>
import { useGlobalReminderStore } from '@/store/globalReminder';

const globalReminderStore = useGlobalReminderStore();
const { isGlobalNotificationActive, globalReminderText, closeNotification } = globalReminderStore;

</script>

<style scoped>
/* Styling for the global reminder modal */
.fixed {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.bg-teal {
  background-color: #334155;  /* Dark blue-gray background */
}

.btn-primary {
  background-color: #38b2ac;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
}
</style>
