<!-- src/components/DemoDisplay.vue -->
<template>
  <div class="demo-display">
    <div v-if="loading" class="loading">Loading...</div>
    <div v-else-if="error" class="error">❌ {{ error }}</div>
    <div v-else class="data">
      <pre>{{ data }}</pre>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useDemoStore } from '../store/demoStore'

const demoStore = useDemoStore()

onMounted(() => {
  demoStore.fetchDemo()
})

const { data, loading, error } = demoStore
</script>

<style scoped>
.loading {
  font-weight: bold;
  color: blue;
}
.error {
  color: red;
}
.data {
  background: #f9f9f9;
  padding: 1rem;
  border-radius: 8px;
}
</style>
