<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <h2 class="text-3xl font-bold mb-6">MIDI Player</h2>

    <!-- MIDI File Upload Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <input type="file" @change="handleFileUpload" accept=".midi,.mid" />
        <button @click="addMidi" :disabled="!midiData" class="btn-primary ml-4">Add MIDI</button>
      </div>
      <div v-if="uploadMessage" class="upload-message mt-4 text-green-500">{{ uploadMessage }}</div>
    </div>

    <!-- MIDI File List -->
    <div class="midi-file-list grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-6">
      <div
          v-for="(midi, index) in midiFiles"
          :key="index"
          class="relative group cursor-pointer"
          @click="playMidi(midi)"
      >
        <div class="text-center bg-slate-700 p-4 rounded-md shadow-lg group-hover:opacity-80">
          <p class="text-white">{{ midi.name }}</p>
        </div>
        <button
            @click.stop="removeMidi(index)"
            class="absolute top-2 right-2 bg-red-500 text-white p-1 rounded-full text-sm opacity-0 group-hover:opacity-100 transition-opacity"
        >
          🗑️
        </button>
      </div>
    </div>

    <!-- MIDI Player Controls -->
    <div v-if="currentMidi">
      <h3 class="text-2xl mb-4">Now Playing: {{ currentMidi.name }}</h3>
      <div>
        <button @click="playMidi(currentMidi)" class="btn-primary">Play</button>
        <button @click="stopMidi" class="btn-outline ml-4">Stop</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      midiData: null, // Holds the raw MIDI file data (as a Blob URL)
      uploadMessage: '', // Upload message
      midiFiles: [], // Array to hold all uploaded MIDI files
      currentMidi: null, // Currently playing MIDI file
    };
  },
  mounted() {
    this.loadMidiFiles(); // Load saved MIDI files from localStorage when component mounts
  },
  methods: {
    // Handle file input change
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file && (file.type === "audio/midi" || file.name.endsWith('.midi') || file.name.endsWith('.mid'))) {
        const reader = new FileReader();
        reader.onloadend = () => {
          this.midiData = reader.result; // Store raw MIDI file data (as URL)
        };
        reader.readAsDataURL(file); // Read file as a URL
      } else {
        this.uploadMessage = 'Please upload a valid MIDI file.';
      }
    },

    // Add MIDI file to gallery and store in localStorage
    addMidi() {
      if (this.midiData) {
        const midi = {
          name: "Untitled MIDI", // Add file name here or prompt the user for a name
          data: this.midiData,   // Store the data URL of the file
        };
        this.midiFiles.push(midi); // Add MIDI file to array
        localStorage.setItem('midiFiles', JSON.stringify(this.midiFiles)); // Save to localStorage
        this.midiData = null; // Clear the current midiData
        this.uploadMessage = 'MIDI file added successfully!';
      }
    },

    // Remove MIDI file from gallery and update localStorage
    removeMidi(index) {
      this.midiFiles.splice(index, 1); // Remove the MIDI file from array
      localStorage.setItem('midiFiles', JSON.stringify(this.midiFiles)); // Update localStorage
    },

    // Load MIDI files from localStorage
    loadMidiFiles() {
      const storedMidiFiles = JSON.parse(localStorage.getItem('midiFiles'));
      if (storedMidiFiles) {
        this.midiFiles = storedMidiFiles; // Load saved MIDI files
      }
    },

    // Play MIDI file using MIDI.js
    playMidi(midi) {
      this.currentMidi = midi;
      MIDI.loadPlugin({
        soundfontUrl: "./soundfonts/",
        instrument: "acoustic_grand_piano",
        onsuccess: () => {
          MIDI.Player.loadFile(midi.data, () => {
            MIDI.Player.start();
          });
        },
      });
    },

    // Stop MIDI playback
    stopMidi() {
      MIDI.Player.stop();
      this.currentMidi = null;
    },
  },
};
</script>

<style scoped>
.midi-file-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1.5rem;
}

.midi-file-list div {
  text-align: center;
  padding: 10px;
  background-color: #1f2937;
  border-radius: 8px;
  cursor: pointer;
  transition: opacity 0.3s ease;
}

.midi-file-list div:hover {
  opacity: 0.8;
}

.upload-message {
  margin-top: 10px;
  color: green;
}

.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
}

.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}

.btn-primary, .btn-outline {
  cursor: pointer;
}

.midi-file-list button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px;
  background-color: red;
  color: white;
  border-radius: 50%;
}
</style>
