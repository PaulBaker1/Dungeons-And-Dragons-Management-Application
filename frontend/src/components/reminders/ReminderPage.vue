<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <h1 class="text-3xl font-bold mb-6">⏰ Reminder System</h1>

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

    <!-- Time Table Reminder Section -->
    <div class="card space-y-6">
      <legend class="section">⏰ Time Table Reminder</legend>
      <div class="grid grid-cols-1 gap-6">
        <div>
          <label class="label">Reminder Text *</label>
          <input v-model="timeTableReminderText" class="input" required />
        </div>
        <div>
          <label class="label">Reminder Time *</label>
          <input type="time" v-model="timeTableReminderTime" class="input" required />
        </div>
        <button @click="setTimeTableReminder" class="btn-primary">Set Time Table Reminder</button>
      </div>
    </div>

    <!-- Time Zone Reminder Section -->
    <div class="card space-y-6">
      <legend class="section">🕰️ Time Zone Reminder</legend>
      <div class="grid grid-cols-1 gap-6">
        <div>
          <label class="label">Reminder Text *</label>
          <input v-model="timeZoneReminderText" class="input" required />
        </div>
        <div>
          <label class="label">Reminder Date and Time *</label>
          <input type="datetime-local" v-model="timeZoneReminderDateTime" class="input" required />
        </div>
        <button @click="setTimeZoneReminder" class="btn-primary">Set Time Zone Reminder</button>
      </div>
    </div>

    <!-- Global Reminder Notification -->
    <div v-if="isGlobalNotificationActive" class="fixed inset-0 flex items-center justify-center z-50">
      <div class="bg-teal p-4 rounded-lg text-center max-w-md mx-auto">
        <h2 class="text-xl text-white font-semibold mb-4">⏳ Reminder Time!</h2>
        <p class="text-lg text-white mb-6">{{ globalReminderText }}</p>
        <button @click="closeNotification" class="btn-primary">OK</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted } from 'vue';

// Timer Reminder
const timerReminderText = ref('');
const timerDuration = ref(0);
const timeRemaining = ref(0);
const isTimerActive = ref(false);
const isTimerReminderActive = ref(false);
let timerInterval = null;

// Time Table Reminder
const timeTableReminderText = ref('');
const timeTableReminderTime = ref('');
const isTimeTableReminderActive = ref(false);

// Time Zone Reminder
const timeZoneReminderText = ref('');
const timeZoneReminderDateTime = ref('');
const isTimeZoneReminderActive = ref(false);

// Global Notification
const isGlobalNotificationActive = ref(false);
const globalReminderText = ref('');

// Timer Reminder Logic
const startTimer = () => {
  if (timerDuration.value > 0 && timerReminderText.value) {
    timeRemaining.value = timerDuration.value;
    isTimerActive.value = true;
    isTimerReminderActive.value = false;

    timerInterval = setInterval(() => {
      timeRemaining.value -= 1;
      if (timeRemaining.value <= 0) {
        clearInterval(timerInterval);
        isTimerReminderActive.value = true;
        isTimerActive.value = false;
        showGlobalNotification(timerReminderText.value);
      }
    }, 1000);
  } else {
    alert('Please enter a reminder and a valid duration.');
  }
};

// Time Table Reminder Logic
const setTimeTableReminder = () => {
  if (timeTableReminderText.value && timeTableReminderTime.value) {
    const targetTime = new Date();
    const [hours, minutes] = timeTableReminderTime.value.split(':');
    targetTime.setHours(hours, minutes, 0, 0);

    const timeDifference = targetTime.getTime() - new Date().getTime();
    if (timeDifference < 0) {
      alert('Please set a future time for the reminder.');
      return;
    }

    setTimeout(() => {
      isTimeTableReminderActive.value = true;
      showGlobalNotification(timeTableReminderText.value);
    }, timeDifference);
  } else {
    alert('Please fill in both reminder text and time.');
  }
};

// Time Zone-Based Reminder Logic
const setTimeZoneReminder = () => {
  if (timeZoneReminderText.value && timeZoneReminderDateTime.value) {
    const reminderDate = new Date(timeZoneReminderDateTime.value);

    const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    console.log('User Local Time Zone:', userTimeZone);

    const currentTime = new Date();
    const timeDifference = reminderDate.getTime() - currentTime.getTime();

    if (timeDifference < 0) {
      alert('Please set a future time for the reminder.');
      return;
    }

    setTimeout(() => {
      isTimeZoneReminderActive.value = true;
      showGlobalNotification(timeZoneReminderText.value);
    }, timeDifference);
  } else {
    alert('Please fill in both reminder text and time.');
  }
};

// Show global reminder notification
const showGlobalNotification = (reminderText) => {
  globalReminderText.value = reminderText;
  isGlobalNotificationActive.value = true;
};

// Close the global notification
const closeNotification = () => {
  isGlobalNotificationActive.value = false;
};

// Cleanup on unmount
onUnmounted(() => {
  clearInterval(timerInterval);
});
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

/* Styling for the global notification modal */
.bg-teal {
  background-color: #334155;  /* Dark blue-gray background */
}
</style>
