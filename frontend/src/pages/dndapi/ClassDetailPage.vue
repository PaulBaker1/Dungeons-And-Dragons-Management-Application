<template>
  <div class="px-6 py-4">
    <h2 class="text-2xl font-bold text-white mb-2">{{ classData.name }}</h2>
    <p class="text-slate-300">Hit Die: d{{ classData.hit_die }}</p>

    <!-- Description (if available) -->
    <Card v-if="classData.desc?.length" class="mt-4">
      <template #title>Description</template>
      <p v-for="line in classData.desc" :key="line" class="text-slate-300">{{ line }}</p>
    </Card>

    <!-- Proficiencies -->
    <Card v-if="classData.proficiencies?.length" class="mt-4">
      <template #title>Proficiencies</template>
      <ul class="list-disc ml-5 text-slate-300">
        <li v-for="prof in classData.proficiencies" :key="prof.index">{{ prof.name }}</li>
      </ul>
    </Card>

    <!-- Saving Throws -->
    <Card v-if="classData.saving_throws?.length" class="mt-4">
      <template #title>Saving Throws</template>
      <ul class="list-disc ml-5 text-slate-300">
        <li v-for="save in classData.saving_throws" :key="save.index">{{ save.name }}</li>
      </ul>
    </Card>

    <!-- Subclasses -->
    <Card v-if="classData.subclasses?.length" class="mt-4">
      <template #title>Subclasses</template>
      <ul class="list-disc ml-5 text-slate-300">
        <li v-for="sub in classData.subclasses" :key="sub.index">{{ sub.name }}</li>
      </ul>
    </Card>

    <!-- Proficiency Choices -->
    <Card v-if="classData.proficiency_choices?.length" class="mt-4">
      <template #title>Choose Proficiencies</template>
      <div v-for="(choice, i) in classData.proficiency_choices" :key="i" class="mb-3">
        <p class="text-slate-300">
          {{ choice.desc || ('Choose ' + choice.choose + ' from:') }}
        </p>
        <ul class="list-disc ml-6 text-slate-300">
          <li v-for="option in choice.from.options" :key="option.item.index">
            {{ option.item.name }}
          </li>
        </ul>
      </div>
    </Card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { DnDService } from '@/services/dndApiService.js'
import Card from 'primevue/card'

const classData = ref({})
const route = useRoute()

onMounted(async () => {
  try {
    classData.value = await DnDService.getClass(route.params.index)
    console.log('Fetched class data:', classData.value)
  } catch (err) {
    console.error('Failed to fetch class:', err)
  }
})
</script>

<style scoped>
:deep(.p-card) {
  background-color: #1e293b;
  color: #f1f5f9;
  border-radius: 0.5rem;
  border: 1px solid #334155;
}
:deep(.p-card-title) {
  font-weight: 600;
  font-size: 1.1rem;
}
</style>
