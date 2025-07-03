<template>
  <header class="flex justify-end items-center p-4 bg-slate-900 text-white">
    <img src="https://placehold.co/32x32" alt="User" class="w-8 h-8 rounded-full mr-2">
    <span class="mr-4">{{ username }}</span>
    <button @click="logout" class="btn-outline px-3 py-1">Logout</button>
  </header>
</template>

<script setup>
  import { computed } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from 'axios'

  const router = useRouter()

  // Decode Basic Auth creds to get username
  const creds = localStorage.getItem('basicCreds')
  const username = computed(() => creds ? atob(creds).split(':')[0] : '')

  function logout() {
    localStorage.removeItem('basicCreds')
    delete axios.defaults.headers.common.Authorization
    router.push('/login')
  }
</script>