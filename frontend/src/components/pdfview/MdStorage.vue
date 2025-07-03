<template>
  <div class="md-viewer">
    <!-- File upload form -->
    <div class="upload-section">
      <h2>Upload Markdown File</h2>
      <input type="file" @change="handleFileUpload" accept=".md" />
      <button @click="uploadFile" :disabled="!selectedFile">Upload</button>
      <div v-if="uploadMessage" class="upload-message">{{ uploadMessage }}</div>
    </div>

    <!-- List of stored files -->
    <div class="files-list" v-if="files.length > 0">
      <h2>Available Files</h2>
      <ul>
        <li v-for="file in files" :key="file" @click="fetchFile(file)">
          {{ file }}
        </li>
      </ul>
    </div>

    <!-- Content display -->
    <div v-if="mdContent" v-html="mdContent" class="content"></div>
    <div class="controls" v-if="mdContent">
      <button @click="previousSection" :disabled="currentSection <= 0">Previous</button>
      <span>Section {{ currentSection + 1 }} of {{ totalSections }}</span>
      <button @click="nextSection" :disabled="currentSection >= totalSections - 1">Next</button>
    </div>
  </div>
</template>
