<template>
  <div v-if="clazz" class="space-y-4 p-6 text-white">
    <h1 class="text-2xl font-bold">{{ clazz.name }}</h1>
    <p class="text-slate-400">Hit die: d{{ clazz.hit_die }}</p>
    <div v-if="clazz.proficiencies?.length">
      <h2 class="text-xl mt-4 mb-2">Proficiencies</h2>
      <ul class="list-disc list-inside">
        <li v-for="p in clazz.proficiencies" :key="p.index">{{ p.name }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { DnDService } from '@/services/dndApiService.js'

const clazz = ref(null)
const route = useRoute()

onMounted(async () => {
  clazz.value = await DnDService.getClass(route.params.index)
})
</script>
