<script setup>
import { ref } from 'vue';
import { useGlobalReminderStore } from '@/store/globalReminder';

const globalReminderStore = useGlobalReminderStore();

const timerReminderText = ref('');
const timerDuration = ref(0);
const timeRemaining = ref(0);
const isTimerActive = ref(false);
let timerInterval = null;

const startTimer = () => {
  if (timerDuration.value > 0 && timerReminderText.value) {
    timeRemaining.value = timerDuration.value;
    isTimerActive.value = true;

    timerInterval = setInterval(() => {
      timeRemaining.value -= 1;
      if (timeRemaining.value <= 0) {
        clearInterval(timerInterval);
        isTimerActive.value = false;

        // Trigger the global reminder notification via the store
        globalReminderStore.showGlobalNotification(timerReminderText.value);
      }
    }, 1000);
  } else {
    alert('Please enter a reminder and a valid duration.');
  }
};
</script>
