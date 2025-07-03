<template>
  <div class="flex min-h-screen bg-slate-900 text-white">
    <!-- Sidebar Navigation -->
    <TheSidebar />

    <!-- Main Content Area -->
    <div class="flex flex-col flex-1">
      <!-- Header -->
      <header class="bg-slate-800 px-6 py-4 border-b border-slate-700 flex justify-between items-center">
        <h1 class="text-lg font-semibold">⚔️ Welcome Traveler!</h1>
        <div class="flex items-center">
          <span class="text-2xl mr-2">🧝🏼</span>
          <span class="mr-4">{{ username }}</span>
          <button @click="logout" class="btn-outline px-3 py-1">Logout</button>
        </div>
      </header>

      <!-- Main Slot Area -->
      <main class="flex-1 overflow-y-auto px-6 py-6 bg-slate-900">
        <slot />
      </main>

      <!-- Footer -->
      <FootBar />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import TheSidebar from '@/components/layout/TheSidebar.vue'
import FootBar from '@/components/layout/FootBar.vue'
import axios from 'axios'

const router = useRouter()
const creds = localStorage.getItem('basicCreds')
const username = computed(() => creds ? atob(creds).split(':')[0] : '')

function logout() {
  localStorage.removeItem('basicCreds')
  delete axios.defaults.headers.common.Authorization
  router.push('/login')
}
</script>
