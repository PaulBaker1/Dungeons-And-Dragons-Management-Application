<template>
  <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DnDService } from '@/services/dndApiService.js'
import { useRouter } from 'vue-router'

const classList = ref([])
const router = useRouter()

const fetchClasses = async () => {
  const data = await DnDService.getList('classes')
  classList.value = data.results
}

const goToDetail = (index) => router.push(`/classes/${index}`)

onMounted(fetchClasses)
</script>
