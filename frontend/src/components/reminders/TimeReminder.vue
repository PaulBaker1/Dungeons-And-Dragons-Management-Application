<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <h1 class="text-3xl font-bold mb-6">⏰ Timer Reminder</h1>

    <!-- Timer Reminder Section -->
    <div class="card space-y-6">
      <legend class="section">🕒 Timer Reminder</legend>
      <div class="grid grid-cols-1 gap-6">
        <div>
          <label class="label">Reminder Text *</label>
          <input v-model="timerReminderText" class="input" required />
        </div>
        <div>
          <label class="label">Timer Duration (in seconds) *</label>
          <input type="number" v-model="timerDuration" class="input" required min="1" />
        </div>
        <button @click="startTimer" class="btn-primary">Start Timer</button>

        <div v-if="isTimerActive">
          <p class="mt-4">{{ timeRemaining }} seconds left</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useGlobalReminderStore } from '@/store/globalReminder';

// Global reminder store
const globalReminderStore = useGlobalReminderStore();

// Timer state
const timerReminderText = ref('');
const timerDuration = ref(0);
const timeRemaining = ref(0);
const isTimerActive = ref(false);
let timerInterval = null;

// Start timer function
const startTimer = () => {
  if (timerDuration.value > 0 && timerReminderText.value) {
    timeRemaining.value = timerDuration.value;
    isTimerActive.value = true;

    // Start the countdown
    timerInterval = setInterval(() => {
      timeRemaining.value -= 1;
      if (timeRemaining.value <= 0) {
        clearInterval(timerInterval);
        isTimerActive.value = false;

        // Trigger the global reminder notification
        globalReminderStore.showGlobalNotification(timerReminderText.value);
      }
    }, 1000);
  } else {
    alert('Please enter a reminder and a valid duration.');
  }
};
</script>

<style scoped>
.card {
  @apply bg-slate-800 p-6 rounded-lg border border-slate-600;
}
.input {
  @apply w-full bg-slate-700 p-2 border border-slate-600 rounded text-white;
}
.label {
  @apply block text-sm mb-2 text-slate-400;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
}
.section {
  @apply text-lg font-semibold text-slate-300 mb-4;
}
</style>
