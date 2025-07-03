<template>
  <div class="md-viewer">
    <input type="file" @change="handleFileUpload" accept=".md" />
    <div v-if="mdContent" v-html="mdContent" class="content"></div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { marked } from 'marked';

export default {
  setup() {
    const mdContent = ref(null); // Holds the current content of the uploaded markdown file

    // Load content from localStorage if it exists
    onMounted(() => {
      const storedContent = localStorage.getItem('mdContent');
      if (storedContent) {
        mdContent.value = storedContent; // Set the content from localStorage
      }
    });

    // Handle file upload
    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      if (file && (file.type === 'text/markdown' || file.name.endsWith('.md'))) {
        const fileReader = new FileReader();
        fileReader.onload = () => {
          const htmlContent = marked(fileReader.result); // Convert markdown to HTML
          mdContent.value = htmlContent;
          localStorage.setItem('mdContent', htmlContent); // Store content in localStorage
        };
        fileReader.readAsText(file);
      } else {
        alert('Please upload a valid markdown file.');
      }
    };

    return {
      mdContent,
      handleFileUpload,
    };
  },
};
</script>

<style scoped>
.md-viewer {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
  font-family: 'Arial', sans-serif;
}

.content {
  max-width: 800px;
  padding: 20px;
  background-color: #e3daca; /* Lighter background */
  border-radius: 10px;
  overflow-y: auto;
  color: #333; /* Darker text for readability */
}

/* Markdown-specific styles */
:deep(.content) h1,
:deep(.content) h2,
:deep(.content) h3 {
  font-family: 'Georgia', serif;
  color: #4D0700; /* Darker red for headings */
  border-bottom: 3px solid #9b4f96; /* Subtle underline matching the theme */
  padding-bottom: 8px;
  margin-bottom: 12px;
  text-transform: uppercase;
}

:deep(.content) strong {
  color: #340000; /* Muted dark red */
  font-weight: bold;
}

:deep(.content) em, :deep(.content) i {
  font-style: italic;
  color: #6b8c8e; /* Muted teal for italic text */
}

:deep(.content) ul {
  list-style-type: disc;
  margin-left: 20px;
  font-size: 16px;
}

:deep(.content) ul li {
  margin: 10px 0;
  padding-left: 5px;
}
</style>
