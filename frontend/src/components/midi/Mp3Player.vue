<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8 bg-slate-900 rounded-lg shadow-xl">
    <h2 class="text-4xl font-bold mb-6 text-center text-silver">MP3 Player</h2>

    <!-- MP3 File Upload Section -->
    <div class="flex justify-between items-center mb-6">
      <div class="flex-1">
        <input type="file" @change="handleFileUpload" accept=".mp3" class="input" />
      </div>
      <button @click="addMp3" :disabled="!mp3Data" class="btn-primary ml-4">Add MP3</button>
    </div>

    <!-- Separator -->
    <hr class="border-slate-600 mb-6" />

    <!-- MP3 File List -->
    <div class="mp3-file-list grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-6 mb-6">
      <div
          v-for="(mp3, index) in mp3Files"
          :key="index"
          class="relative group cursor-pointer"
          @click="playMp3(mp3)"
      >
        <div class="text-center card flex items-center justify-center p-4">
          <p class="text-white font-medium text-ellipsis overflow-hidden whitespace-nowrap" :title="mp3.name">{{ mp3.name }}</p>
        </div>
        <button
            @click.stop="removeMp3(index)"
            class="absolute top-2 right-2 bg-red-600 text-white p-1 rounded-full text-sm opacity-0 group-hover:opacity-100 transition-opacity"
        >
          🗑️
        </button>
      </div>
    </div>

    <!-- Separator -->
    <hr class="border-slate-600 mb-6" />

    <!-- MP3 Player Controls -->
    <div v-if="currentMp3">
      <h3 class="text-2xl mb-4 text-center text-silver">Now Playing: {{ currentMp3.name }}</h3>
      <AudioPlayer
          ref="audioPlayer"
          :option="{
          src: currentMp3.data,
          title: currentMp3.name,
          coverImage: 'https://placehold.co/150x150?text=Cover', // Default cover image
        }"
      />

      <!-- Previous and Next Track Buttons with Emojis -->
      <div class="flex justify-center mt-6 space-x-6">
        <button @click="skipToPreviousTrack" class="text-4xl">
          ◀️
        </button>
        <button @click="skipToNextTrack" class="text-4xl">
          ▶️
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import AudioPlayer from 'vue3-audio-player';
import 'vue3-audio-player/dist/style.css';

export default {
  components: {
    AudioPlayer,
  },
  data() {
    return {
      mp3Data: null, // Holds the raw MP3 file data (as a URL)
      uploadMessage: '', // Upload message
      mp3Files: [], // Array to hold all uploaded MP3 files
      currentMp3: null, // Currently playing MP3 file
      mp3Name: '', // Holds the name of the uploaded MP3 file
    };
  },
  mounted() {
    this.loadMp3Files(); // Load saved MP3 files from localStorage when component mounts
  },
  methods: {
    // Handle file input change
    handleFileUpload(event) {
      const file = event.target.files[0];

      // Check if file is an MP3 file by extension (ignores MIME type)
      if (file && file.name.endsWith('.mp3')) {
        const reader = new FileReader();
        reader.onloadend = () => {
          this.mp3Data = reader.result; // Store raw MP3 file data as URL
        };
        reader.readAsDataURL(file); // Read file as a URL
        this.mp3Name = file.name.replace('.mp3', ''); // Set file name (removing ".mp3" extension)
      } else {
        this.uploadMessage = 'Please upload a valid MP3 file.';
      }
    },

    // Add MP3 file to gallery and store in localStorage
    addMp3() {
      if (this.mp3Data) {
        const mp3 = {
          name: this.mp3Name || "Untitled MP3", // Use the custom name if provided, otherwise default to "Untitled MP3"
          data: this.mp3Data,   // Store the data URL of the file
        };
        this.mp3Files.push(mp3); // Add MP3 file to array
        localStorage.setItem('mp3Files', JSON.stringify(this.mp3Files)); // Save to localStorage
        this.mp3Data = null; // Clear the current mp3Data
        this.uploadMessage = 'MP3 file added successfully!';
        this.mp3Name = ''; // Clear the MP3 name after adding it
      }
    },

    // Remove MP3 file from gallery and update localStorage
    removeMp3(index) {
      this.mp3Files.splice(index, 1); // Remove the MP3 file from array
      localStorage.setItem('mp3Files', JSON.stringify(this.mp3Files)); // Update localStorage
    },

    // Load MP3 files from localStorage
    loadMp3Files() {
      const storedMp3Files = JSON.parse(localStorage.getItem('mp3Files'));
      if (storedMp3Files) {
        this.mp3Files = storedMp3Files; // Load saved MP3 files
      }
    },

    // Play MP3 file using vue3-audio-player
    playMp3(mp3) {
      this.currentMp3 = mp3;
    },

    // Skip to previous track
    skipToPreviousTrack() {
      const currentIndex = this.mp3Files.findIndex(mp3 => mp3 === this.currentMp3);
      const previousIndex = (currentIndex - 1 + this.mp3Files.length) % this.mp3Files.length; // Loop back to last track if at the beginning
      this.currentMp3 = this.mp3Files[previousIndex]; // Set current track to previous
    },

    // Skip to next track
    skipToNextTrack() {
      const currentIndex = this.mp3Files.findIndex(mp3 => mp3 === this.currentMp3);
      const nextIndex = (currentIndex + 1) % this.mp3Files.length; // Loop back to first track if at the end
      this.currentMp3 = this.mp3Files[nextIndex]; // Set current track to next
    },
  },
};
</script>

<style scoped>
/* Add general styling */
.input {
  @apply w-full bg-slate-800 p-2 border border-slate-600 rounded text-white;
}

.upload-message {
  margin-top: 10px;
  color: green;
  font-weight: bold;
}

.card {
  @apply border border-slate-700 rounded-lg p-4 bg-slate-900;
}

.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
  /* Make the button the same size as previous ones */
  padding: 8px 12px; /* More consistent size with previous buttons */
}

.btn-primary:disabled {
  @apply bg-gray-400 cursor-not-allowed;
}

.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}

.mp3-file-list button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px;
  background-color: #e53e3e;
  color: white;
  border-radius: 50%;
  transition: transform 0.3s ease;
}

.mp3-file-list button:hover {
  transform: scale(1.2);
}

/* Separator Styling */
hr {
  border-top: 1px solid #4A5568; /* Darker border color */
  margin-top: 1.5rem;
  margin-bottom: 1.5rem;
}

/* Silver color for text */
.text-silver {
  color: #B8B8B8;
}

/* Flex box adjustments for MP3 file name */
.mp3-file-list div {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 10px;
  background-color: #2d3748;
  border-radius: 8px;
  cursor: pointer;
  transition: opacity 0.3s ease;
}

.mp3-file-list div p {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  max-width: 100%;
}
</style>
