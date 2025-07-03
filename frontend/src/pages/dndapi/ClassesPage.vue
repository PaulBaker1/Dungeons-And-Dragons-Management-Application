<template>
  <div class="p-4">
    <h1 class="text-3xl font-bold text-white mb-4">🏹 Classes</h1>
    <div v-if="loading" class="text-white">Loading...</div>
    <div v-else class="grid grid-cols-2 md:grid-cols-3 gap-4">
      <!-- Loop over the classList array -->
      <div
          v-for="cls in classList"
          :key="cls.index"
          class="bg-slate-800 hover:bg-slate-700 transition rounded-xl p-4 shadow-md text-white cursor-pointer"
          @click="goToDetail(cls.index)"
      >
        <div class="text-lg font-semibold">{{ cls.name }}</div>
        <div class="text-xs text-slate-400">Class Index: {{ cls.index }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { DnDService } from '@/services/dndApiService.js'

// Reactive variables to store class list and loading state.
const classList = ref([])
const loading = ref(true)
const router = useRouter()

// Fetch the list of classes using our DnD API service.
const fetchClasses = async () => {
  try {
    const data = await DnDService.getList('classes')
    // The API returns an object with a 'results' array.
    classList.value = data.results
  } catch (error) {
    console.error('Error fetching class list:', error)
  } finally {
    loading.value = false
  }
}

// Navigate to the detail page for the selected class.
const goToDetail = (index) => {
  router.push(`/classes/${index}`)
}

onMounted(fetchClasses)
</script>

<style scoped>
/* Add any additional styling as needed */
</style>
