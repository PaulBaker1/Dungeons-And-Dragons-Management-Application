<template>
  <div>
    <h2>Set Reminder Based on Time Zone</h2>

    <input type="text" v-model="reminderText" placeholder="Enter reminder text" />
    <input type="datetime-local" v-model="reminderDateTime" />
    <button @click="setReminderWithTimeZone">Set Reminder with Time Zone</button>

    <div v-if="isReminderShown">
      <h3>Reminder!</h3>
      <p>{{ reminderText }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const reminderText = ref('');
const reminderDateTime = ref('');
const isReminderShown = ref(false);

const setReminderWithTimeZone = () => {
  if (reminderText.value && reminderDateTime.value) {
    const reminderDate = new Date(reminderDateTime.value);

    // Convert to user's local time zone
    const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    console.log('User Local Time Zone:', userTimeZone);

    // Calculate the time difference from now to the reminder date
    const currentTime = new Date();
    const timeDifference = reminderDate.getTime() - currentTime.getTime();

    if (timeDifference < 0) {
      alert('Please set a future time for the reminder.');
      return;
    }

    // Set a timeout to trigger the reminder when the time comes
    setTimeout(() => {
      isReminderShown.value = true;
    }, timeDifference);
  } else {
    alert('Please fill in both reminder text and time.');
  }
};
</script>

<style scoped>
/* Styling for the time zone reminder */
</style>
