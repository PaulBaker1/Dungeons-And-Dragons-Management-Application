<template>
  <div class="px-6 py-6 max-w-4xl mx-auto text-white">
    <!-- Loading / Error States -->
    <div v-if="loading" class="text-slate-400">Loading...</div>
    <div v-else-if="!campaign">Campaign not found.</div>

    <!-- Main Content -->
    <div v-else>
      <!-- Title & Description -->
      <h1 class="text-2xl font-bold mb-4">📜 {{ campaign.title }}</h1>
      <p class="text-slate-300 mb-4">{{ campaign.description }}</p>

      <!-- Basic Info Grid -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <!-- Level Range -->
        <div>
          <h2 class="font-semibold text-lg">🎚 Level Range</h2>
          <p class="text-slate-400">Levels {{ campaign.recommendedLevelRange.minLevel }}–{{ campaign.recommendedLevelRange.maxLevel }}</p>
        </div>

        <!-- Tags -->
        <div>
          <h2 class="font-semibold text-lg">🏷 Tags</h2>
          <div class="flex flex-wrap gap-2">
            <span
                v-for="tag in campaign.tags"
                :key="tag"
                class="bg-indigo-600 px-2 py-1 text-sm rounded"
            >
              {{ tag }}
            </span>
          </div>
        </div>

        <!-- Key Locations -->
        <div>
          <h2 class="font-semibold text-lg">📍 Locations</h2>
          <ul class="list-disc pl-5 text-slate-400">
            <li v-for="loc in campaign.keyLocations" :key="loc">{{ loc }}</li>
          </ul>
        </div>

        <!-- Sections -->
        <div>
          <h2 class="font-semibold text-lg">📖 Sections</h2>
          <ul class="list-disc pl-5 text-slate-400">
            <li v-for="sec in campaign.sections" :key="sec">{{ sec }}</li>
          </ul>
        </div>
      </div>

      <!-- SESSION MANAGEMENT MODULE -->
      <section class="mt-10">
        <h2 class="text-xl font-bold mb-4">🗓 Session Management</h2>

        <!-- Grid wrapper -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Next Session / Scheduler -->
          <div class="bg-slate-800 p-4 rounded-lg border border-slate-700">
            <h3 class="font-semibold text-lg mb-2">⏰ Schedule Next Session</h3>
            <div class="space-y-3">
              <input type="date" v-model="newSession.date" class="w-full px-3 py-2 rounded bg-slate-900 border border-slate-700" />
              <input type="time" v-model="newSession.time" class="w-full px-3 py-2 rounded bg-slate-900 border border-slate-700" />
              <button @click="saveNextSession" class="btn-primary w-full">Save</button>
            </div>
          </div>

          <!-- Upcoming Sessions -->
          <div class="bg-slate-800 p-4 rounded-lg border border-slate-700">
            <h3 class="font-semibold text-lg mb-2">📅 Upcoming Sessions</h3>
            <ul class="text-slate-400 space-y-1">
              <li v-if="!upcomingSessions.length">No upcoming sessions.</li>
              <li v-for="ses in upcomingSessions" :key="ses.id" class="flex justify-between items-center">
                <span>{{ formatDate(ses.date) }} – {{ ses.time }}</span>
                <router-link :to="`/sessions/${ses.id}`" class="text-indigo-400 hover:underline text-sm">Details</router-link>
              </li>
            </ul>
          </div>

          <!-- Past Sessions / Logs -->
          <div class="bg-slate-800 p-4 rounded-lg border border-slate-700">
            <h3 class="font-semibold text-lg mb-2">📜 Past Sessions</h3>
            <ul class="text-slate-400 space-y-1 max-h-52 overflow-auto">
              <li v-if="!pastSessions.length">No past sessions yet.</li>
              <li v-for="ses in pastSessions" :key="ses.id" class="flex justify-between items-center">
                <span>{{ formatDate(ses.date) }}</span>
                <router-link :to="`/sessions/${ses.id}`" class="text-indigo-400 hover:underline text-sm">Log</router-link>
              </li>
            </ul>
          </div>

          <!-- Attendance & Notes -->
          <div class="bg-slate-800 p-4 rounded-lg border border-slate-700">
            <h3 class="font-semibold text-lg mb-2">📝 Attendance & Notes</h3>
            <p class="text-slate-400 mb-3">Quick snapshot of players and session notes.</p>
            <ul class="text-slate-300 space-y-1 list-disc list-inside">
              <li v-for="player in campaign.players" :key="player.id">
                {{ player.name }} – <span :class="player.present ? 'text-green-400' : 'text-red-500'">{{ player.present ? 'Present' : 'Absent' }}</span>
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- Navigation Buttons -->
      <div class="mt-8 flex justify-between">
        <router-link :to="`/campaigns`" class="btn-outline">← Back</router-link>
        <router-link :to="`/campaigns/${campaign.id}/sessions`" class="btn-primary">📅 All Sessions</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CampaignDetailsWithSession',
  data() {
    return {
      loading: false,
      campaign: null,          // fetched in mounted()
      upcomingSessions: [],
      pastSessions: [],
      newSession: { date: '', time: '' },
    };
  },
  methods: {
    // dummy – replace with real formatting lib
    formatDate(str) {
      return new Date(str).toLocaleDateString();
    },
    saveNextSession() {
      if (!this.newSession.date || !this.newSession.time) return;
      // push to upcomingSessions locally (mock)
      const id = Date.now();
      this.upcomingSessions.push({ id, ...this.newSession });
      this.newSession = { date: '', time: '' };
    },
    async fetchCampaign() {
      // mock async fetch – replace with real API
      this.loading = true;
      setTimeout(() => {
        this.campaign = {
          id: 1,
          title: 'The Lost Kingdom',
          description: 'Epic quest to reclaim the throne.',
          recommendedLevelRange: { minLevel: 5, maxLevel: 15 },
          tags: ['high‑fantasy', 'politics'],
          keyLocations: ['Ancient Ruins', 'Dark Forest', 'Royal Capital'],
          sections: ['Prologue', 'Main Quest', 'Side Stories'],
          players: [
            { id: 1, name: 'Alice', present: true },
            { id: 2, name: 'Bob',   present: false },
          ],
        };
        this.loading = false;
      }, 500);
    },
  },
  mounted() {
    this.fetchCampaign();
  },
};
</script>

<style scoped>
.btn-primary {
  @apply bg-indigo-600 hover:bg-indigo-500 text-white px-4 py-2 rounded;
}
.btn-outline {
  @apply border border-indigo-400 text-indigo-400 px-4 py-2 rounded hover:bg-indigo-600 hover:text-white transition;
}
</style>
