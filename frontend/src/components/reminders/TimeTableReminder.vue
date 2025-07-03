<template>
  <div>
    <h2>Set Timetable Reminder</h2>

    <input type="text" v-model="reminderText" placeholder="Enter reminder text" />
    <input type="time" v-model="reminderTime" />
    <button @click="setReminderTime">Set Reminder</button>

    <div v-if="isReminderShown">
      <h3>Reminder!</h3>
      <p>{{ reminderText }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted } from 'vue';

const reminderText = ref('');
const reminderTime = ref('');
const isReminderShown = ref(false);

// Store the interval ID for cleanup
let reminderInterval = null;

const setReminderTime = () => {
  if (reminderText.value && reminderTime.value) {
    isReminderShown.value = false;
    const targetTime = new Date();
    const [hours, minutes] = reminderTime.value.split(':');
    targetTime.setHours(hours, minutes, 0, 0);  // Set reminder time

    // Calculate the difference between the target time and the current time
    const timeDifference = targetTime.getTime() - new Date().getTime();

    if (timeDifference < 0) {
      alert('Please set a future time for the reminder.');
      return;
    }

    // Set a timeout for the reminder
    setTimeout(() => {
      isReminderShown.value = true;
    }, timeDifference);
  } else {
    alert('Please fill in both reminder text and time.');
  }
};

// Clean up when the component is unmounted
onUnmounted(() => {
  clearInterval(reminderInterval);
});
</script>

<style scoped>
/* Styling for the reminder and time table */
</style>
