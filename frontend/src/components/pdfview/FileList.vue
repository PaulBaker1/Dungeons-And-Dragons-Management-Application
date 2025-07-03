<template>
  <div class="file-list">
    <h2>Available Markdown Files</h2>
    <div class="file-item" v-for="file in files" :key="file.filename">
      <div class="cover" @click="viewFile(file.filename)">
        <img :src="file.coverImageUrl" alt="Cover Image" />
      </div>
      <div class="file-name" @click="viewFile(file.filename)">
        <h3>{{ file.filename }}</h3>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const files = ref([]);
    const router = useRouter();

    // Fetch the list of files with cover images
    const fetchFiles = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/files/list');
        files.value = response.data;
      } catch (error) {
        console.error('Error fetching file list:', error);
      }
    };

    // Navigate to the PDFView route (md file viewer) when a file is clicked
    const viewFile = (filename) => {
      router.push({ name: 'PDFView', params: { filename } });
    };

    // Fetch files when the component is mounted
    onMounted(() => {
      fetchFiles();
    });

    return {
      files,
      viewFile
    };
  }
};
</script>

<style scoped>
.file-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.file-item {
  width: 200px;
  cursor: pointer;
  text-align: center;
}

.cover img {
  width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.file-name h3 {
  margin-top: 10px;
  font-size: 16px;
  color: #333;
}
</style>
