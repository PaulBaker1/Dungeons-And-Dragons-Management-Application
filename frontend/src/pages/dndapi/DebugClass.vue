<template>
  <div class="debug-container">
    <h2>Debug: Class Data</h2>
    <button @click="fetchData">Reload Data</button>
    <pre>{{ jsonData }}</pre>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DnDService } from '@/services/dndApiService.js'

// A reactive variable to hold the stringified JSON data
const jsonData = ref('')

// Function to fetch the data for a specific class (for example, "bard")
const fetchData = async () => {
  try {
    // Adjust the argument 'bard' if you want to debug a different class
    const data = await DnDService.getClass('bard')
    // Format the JSON nicely
    jsonData.value = JSON.stringify(data, null, 2)
    console.log('Fetched class data:', data)
  } catch (err) {
    jsonData.value = 'Error: ' + err
    console.error('Failed to fetch class data:', err)
  }
}

// Automatically fetch data when the component mounts
onMounted(fetchData)
</script>

<style scoped>
.debug-container {
  background-color: #f0f0f0;
  color: #333;
  font-family: monospace;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 0.5rem;
}
button {
  margin-bottom: 1rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
}
pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  max-height: 80vh;
  overflow-y: auto;
}
</style>
