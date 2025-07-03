import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useGlobalReminderStore = defineStore('globalReminder', () => {
    // Reactive state
    const isGlobalNotificationActive = ref(false);
    const globalReminderText = ref('');

    // Internal IDs for scheduled tasks
    let timeoutId = null;
    let intervalId = null;

    // Show notification immediately
    function showGlobalNotification(reminderText) {
        clearScheduled();
        globalReminderText.value = reminderText;
        isGlobalNotificationActive.value = true;
    }

    // Close the notification
    function closeNotification() {
        isGlobalNotificationActive.value = false;
        globalReminderText.value = '';
    }

    // Clear any pending timeouts/intervals
    function clearScheduled() {
        if (timeoutId !== null) {
            clearTimeout(timeoutId);
            timeoutId = null;
        }
        if (intervalId !== null) {
            clearInterval(intervalId);
            intervalId = null;
        }
    }

    // Schedule a one-off timer (in seconds)
    function scheduleTimer(reminderText, seconds) {
        clearScheduled();
        timeoutId = setTimeout(() => {
            showGlobalNotification(reminderText);
        }, seconds * 1000);
    }

    // Schedule at clock time (HH:MM), today or tomorrow
    function scheduleAtTime(reminderText, hhmm) {
        clearScheduled();
        const [hour, minute] = hhmm.split(':').map(Number);
        const now = new Date();
        const target = new Date(now);
        target.setHours(hour, minute, 0, 0);
        if (target <= now) target.setDate(target.getDate() + 1);
        const ms = target.getTime() - now.getTime();
        timeoutId = setTimeout(() => {
            showGlobalNotification(reminderText);
        }, ms);
    }

    // Schedule at absolute ISO datetime string
    function scheduleAtDatetime(reminderText, isoString) {
        clearScheduled();
        const target = new Date(isoString);
        const now = new Date();
        const ms = target.getTime() - now.getTime();
        if (ms <= 0) {
            throw new Error('Cannot schedule a reminder in the past');
        }
        timeoutId = setTimeout(() => {
            showGlobalNotification(reminderText);
        }, ms);
    }

    // Optionally: schedule recurring reminder (e.g., every X seconds)
    function scheduleRecurring(reminderText, seconds) {
        clearScheduled();
        intervalId = setInterval(() => {
            showGlobalNotification(reminderText);
        }, seconds * 1000);
    }

    return {
        isGlobalNotificationActive,
        globalReminderText,
        showGlobalNotification,
        closeNotification,
        scheduleTimer,
        scheduleAtTime,
        scheduleAtDatetime,
        scheduleRecurring,
        clearScheduled,
    };
});
