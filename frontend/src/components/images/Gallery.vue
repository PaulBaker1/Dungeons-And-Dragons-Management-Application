<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8 bg-slate-900 rounded-lg shadow-xl">
    <h2 class="text-4xl font-bold mb-6 text-center text-silver">🖼️ Image Gallery</h2>

    <!-- Image Upload Section -->
    <div class="flex justify-between items-center mb-6">
      <div class="flex-1">
        <input type="file" @change="handleFileUpload" accept="image/*" class="input" />
      </div>
      <button @click="addImage" :disabled="!imageData" class="btn-primary ml-4 text-xl">
        <span class="mr-2">📤</span>Add Image
      </button>
    </div>

    <!-- Separator -->
    <hr class="border-slate-600 mb-6" />

    <!-- Gallery Grid -->
    <div class="gallery-grid grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-6 mb-6">
      <div
          v-for="(image, index) in images"
          :key="index"
          class="relative group cursor-pointer"
          @click="openFullImage(image)"
      >
        <div class="text-center card">
          <img :src="image" alt="Gallery Image" class="object-cover w-full h-32 rounded-md shadow-lg group-hover:opacity-80" />
        </div>
        <button
            @click.stop="removeImage(index)"
            class="absolute top-2 right-2 bg-red-600 text-white p-2 rounded-full text-xl opacity-0 group-hover:opacity-100 transition-opacity"
        >
          🗑️
        </button>
      </div>
    </div>

    <!-- Separator -->
    <hr class="border-slate-600 mb-6" />

    <!-- Modal for viewing larger image -->
    <div v-if="isModalOpen" class="fixed inset-0 flex justify-center items-center z-50">
      <div class="bg-white p-4 rounded-lg max-w-lg relative">
        <img :src="fullImage" alt="Full Image" class="w-full h-auto rounded-md" />
        <button @click="closeModal" class="absolute top-2 right-2 text-3xl text-gray-700">
          ❌
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      imageData: null, // Holds the base64 image data
      uploadMessage: '', // Upload message
      images: [], // Array to hold all images
      isModalOpen: false, // To track modal state
      fullImage: null, // Holds the image URL for the modal
    };
  },
  mounted() {
    this.loadImages(); // Load saved images when component mounts
  },
  methods: {
    // Handle file input change
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onloadend = () => {
          this.imageData = reader.result; // Store base64 image
        };
        reader.readAsDataURL(file);
      } else {
        this.uploadMessage = 'Please upload a valid image file.';
      }
    },

    // Add image to gallery and store in localStorage
    addImage() {
      if (this.imageData) {
        this.images.push(this.imageData); // Add image to array
        localStorage.setItem('images', JSON.stringify(this.images)); // Save to localStorage
        this.imageData = null; // Clear the current imageData
        this.uploadMessage = 'Image added successfully!';
      }
    },

    // Remove image from gallery and update localStorage
    removeImage(index) {
      this.images.splice(index, 1); // Remove the image from the array
      localStorage.setItem('images', JSON.stringify(this.images)); // Update localStorage
    },

    // Open modal to view full image
    openFullImage(image) {
      this.fullImage = image;
      this.isModalOpen = true;
    },

    // Close the modal
    closeModal() {
      this.isModalOpen = false;
      this.fullImage = null;
    },

    // Load images from localStorage
    loadImages() {
      const storedImages = JSON.parse(localStorage.getItem('images'));
      if (storedImages) {
        this.images = storedImages; // Load saved images
      }
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
}

.card {
  @apply border border-slate-700 rounded-lg p-4 bg-slate-900;
}

.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
  padding: 8px 12px; /* Match previous button size */
}

.btn-primary:disabled {
  @apply bg-gray-400 cursor-not-allowed;
}

.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}

.gallery-grid button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 10px;
  background-color: #e53e3e;
  color: white;
  border-radius: 50%;
  transition: transform 0.3s ease;
  font-size: 24px; /* Make the button icon bigger */
}

.gallery-grid button:hover {
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

/* Card styling for images */
.gallery-grid div {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 2px;
  background-color: #2d3748;
  border-radius: 8px;
  cursor: pointer;
  transition: opacity 0.3s ease;
}

.gallery-grid img {
  object-fit: cover;
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.gallery-grid div:hover {
  opacity: 0.8;
}

/* Modal Styles */
.fixed {
  z-index: 50;
}
.bg-white {
  background-color: white;
}
</style>
