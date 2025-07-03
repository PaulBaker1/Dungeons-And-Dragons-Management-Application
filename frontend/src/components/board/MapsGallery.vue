<template>
  <div class="gallery-container">
    <h1>Maps Gallery</h1>

    <!-- Add new map -->
    <div class="add-map">
      <input type="file" @change="uploadMap" />
    </div>

    <!-- Display maps as thumbnails -->
    <div class="maps-list">
      <div
          v-for="(map, index) in maps"
          :key="index"
          class="map-item"
          @click="selectMap(map)"
      >
        <img :src="map" alt="Map Thumbnail" class="map-thumbnail" />
        <button @click.stop="removeMap(index)">Remove</button>
      </div>
    </div>

    <!-- Select a map to draw on -->
    <div v-if="selectedMap" class="selected-map">
      <h2>Selected Map</h2>
      <img :src="selectedMap" alt="Selected Map" class="selected-map-img" />
      <router-link to="/map-paint">Go to Drawing Page</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useBoardStore } from '@/store/boardStore'; // Pinia store
import { useRouter } from 'vue-router'; // Vue Router for navigation

const boardStore = useBoardStore(); // Access the Pinia store
const router = useRouter(); // Vue Router instance

// Store maps in the gallery (replace with your actual data or store)
const maps = ref([]);
const selectedMap = ref(null);

// Function to upload a new map
const uploadMap = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      maps.value.push(reader.result); // Add the new map to the gallery
    };
    reader.readAsDataURL(file);
  }
};

// Function to select a map and store it in the boardStore
const selectMap = (map) => {
  selectedMap.value = map;
  boardStore.setMapImage(map); // Save the selected map to the Pinia store
  router.push({ path: '/map-paint' }); // Navigate to map-paint page
};

// Function to remove a map from the gallery
const removeMap = (index) => {
  maps.value.splice(index, 1); // Remove the map from the gallery
};
</script>

<style scoped>
.gallery-container {
  padding: 20px;
  text-align: center;
}

.add-map {
  margin-bottom: 20px;
}

.maps-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.map-item {
  position: relative;
}

.map-thumbnail {
  width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px;
  background-color: red;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 50%;
}

button:hover {
  background-color: darkred;
}

.selected-map {
  margin-top: 20px;
}

.selected-map-img {
  width: 100%;
  max-width: 600px;
  height: auto;
}
</style>
