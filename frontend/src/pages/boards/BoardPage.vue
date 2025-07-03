<template>
  <div class="board-page">
    <h1 class="text-xl font-semibold mb-4">Checkers Board</h1>
    <MapsBoard />
    <MapsPaint />
  </div>
</template>

<script setup>
import MapsBoard from '@/components/board/MapsBoard.vue'; // Ensure the correct path to MapsBoard.vue
</script>

<style scoped>
.board-page {
  padding: 20px;
  text-align: center;
}

h1 {
  margin-bottom: 20px;
}
</style>
