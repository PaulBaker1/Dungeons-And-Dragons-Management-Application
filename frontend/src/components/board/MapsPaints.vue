<template>
  <div class="main-container">
    <!-- Left Panel (Map Area with Drawing) -->
    <div class="map-container">
      <canvas
          ref="drawingCanvas"
          class="game-map"
          :width="canvasWidth"
          :height="canvasHeight"
          @mousedown="startDrawing"
          @mousemove="draw"
          @mouseup="stopDrawing"
          @mouseleave="stopDrawing"
          @dblclick="placeText"
      >
        Your browser does not support the HTML5 canvas.
      </canvas>
    </div>

    <!-- Right Panel (Color Picker, Shape Selector, and Text Controls) -->
    <div class="controls-container">
      <h3>DRAWING TOOLS</h3>

      <div class="color-picker">
        <label>Color:</label>
        <input type="color" v-model="color" />
      </div>

      <div class="brush-size">
        <label>Brush Size:</label>
        <input type="range" min="1" max="30" v-model="brushSize" />
      </div>

      <div class="shape-selector">
        <button @click="setDrawingShape('pen')">Pen</button>
        <button @click="setDrawingShape('rectangle')">Rectangle</button>
        <button @click="setDrawingShape('circle')">Circle</button>
        <button @click="setDrawingShape('text')">Text</button>
        <button @click="setDrawingShape('eraser')">Eraser</button>
      </div>

      <div class="actions">
        <button @click="clearDrawing">Clear</button>
        <button @click="saveDrawing">Save Drawing</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';

// Canvas settings based on the map size
const canvasWidth = 1080; // Width of canvas (1080px map size)
const canvasHeight = 764; // Height of canvas (764px map size)
const drawingCanvas = ref(null);

const color = ref('#000000'); // Default drawing color (black)
const brushSize = ref(5); // Default brush size
const drawingShape = ref('pen'); // Default shape (pen tool)
const isDrawing = ref(false); // To track if the user is currently drawing
const lastX = ref(0); // Last mouse X position
const lastY = ref(0); // Last mouse Y position
const drawnObjects = ref([]); // To store drawn objects (rectangles, circles, etc.)
const startX = ref(0); // Starting X position for shapes
const startY = ref(0); // Starting Y position for shapes

let context = null; // Canvas drawing context
const gridSize = 40; // Size of each square in the grid

// Set up the canvas context
onMounted(() => {
  context = drawingCanvas.value.getContext('2d');
  context.lineJoin = 'round';
  context.lineCap = 'round';
  context.strokeStyle = color.value;
  context.lineWidth = brushSize.value;

  // Set the canvas background to dark gray
  context.fillStyle = '#333'; // Dark background for the canvas
  context.fillRect(0, 0, canvasWidth, canvasHeight);

  // Draw the grid on the canvas when it's initialized
  drawGrid();
});

// Function to draw the grid
const drawGrid = () => {
  context.strokeStyle = '#555'; // Grid line color (lighter gray)
  context.lineWidth = 1;

  // Draw vertical lines
  for (let x = 0; x <= canvasWidth; x += gridSize) {
    context.moveTo(x, 0);
    context.lineTo(x, canvasHeight);
  }

  // Draw horizontal lines
  for (let y = 0; y <= canvasHeight; y += gridSize) {
    context.moveTo(0, y);
    context.lineTo(canvasWidth, y);
  }

  context.stroke(); // Draw grid
};

// Set drawing shape (pen, eraser, etc.)
const setDrawingShape = (shape) => {
  drawingShape.value = shape;
};

// Start drawing (mousedown)
const startDrawing = (event) => {
  const { offsetX, offsetY } = event;
  lastX.value = offsetX;
  lastY.value = offsetY;
  isDrawing.value = true;

  // Store the starting position for shapes (rectangles, circles)
  if (drawingShape.value === 'rectangle' || drawingShape.value === 'circle') {
    startX.value = offsetX;
    startY.value = offsetY;
  }
};

// Stop drawing (mouseup)
const stopDrawing = () => {
  isDrawing.value = false;
  context.beginPath(); // Reset drawing path
};

// Draw (pen/eraser) on mouse move
const draw = (event) => {
  if (!isDrawing.value) return;

  const { offsetX, offsetY } = event;

  if (drawingShape.value === 'pen') {
    context.lineWidth = brushSize.value; // Set brush size dynamically
    context.strokeStyle = color.value; // Set the current color
    context.beginPath();
    context.moveTo(lastX.value, lastY.value);
    context.lineTo(offsetX, offsetY);
    context.stroke();
    context.closePath();
  } else if (drawingShape.value === 'eraser') {
    context.lineWidth = brushSize.value * 2; // Make eraser larger than brush size
    context.strokeStyle = '#333'; // Erase with dark color (canvas background)
    context.beginPath();
    context.moveTo(lastX.value, lastY.value);
    context.lineTo(offsetX, offsetY);
    context.stroke();
    context.closePath();

    eraseObject(offsetX, offsetY); // Check if the eraser overlaps any object
  } else if (drawingShape.value === 'rectangle') {
    drawRectangle(startX.value, startY.value, offsetX, offsetY); // Draw rectangle
  } else if (drawingShape.value === 'circle') {
    drawCircle(startX.value, startY.value, offsetX, offsetY); // Draw circle
  }

  lastX.value = offsetX;
  lastY.value = offsetY;
};

// Draw rectangle on the canvas
const drawRectangle = (startX, startY, offsetX, offsetY) => {
  const width = offsetX - startX;
  const height = offsetY - startY;
  context.beginPath();
  context.rect(startX, startY, width, height);
  context.fillStyle = color.value;
  context.fill();
  context.stroke();
  context.closePath();
};

// Draw circle on the canvas
const drawCircle = (startX, startY, offsetX, offsetY) => {
  const radius = Math.sqrt(Math.pow(offsetX - startX, 2) + Math.pow(offsetY - startY, 2));
  context.beginPath();
  context.arc(startX, startY, radius, 0, 2 * Math.PI);
  context.fillStyle = color.value;
  context.fill();
  context.stroke();
  context.closePath();
};

// Place text on the canvas (on double-click)
const placeText = (event) => {
  const mouseX = event.offsetX;
  const mouseY = event.offsetY;
  const text = prompt('Enter text:');

  if (text) {
    drawText(mouseX, mouseY, text);
  }
};

// Draw text on the canvas
const drawText = (x, y, text) => {
  context.font = `${brushSize.value * 2}px Arial`;
  context.fillStyle = color.value;
  context.fillText(text, x, y);
};

// Erase the drawn object when using the eraser tool
const eraseObject = (x, y) => {
  drawnObjects.value.forEach((obj, index) => {
    if (obj.type === 'rectangle' && x > obj.x - obj.width / 2 && x < obj.x + obj.width / 2 && y > obj.y - obj.height / 2 && y < obj.y + obj.height / 2) {
      drawnObjects.value.splice(index, 1); // Remove the object from array
      clearDrawing(); // Clear the entire canvas and redraw
      redrawObjects(); // Redraw all objects except the erased one
    } else if (obj.type === 'circle' && Math.sqrt(Math.pow(x - obj.x, 2) + Math.pow(y - obj.y, 2)) < obj.radius) {
      drawnObjects.value.splice(index, 1); // Remove the object from array
      clearDrawing(); // Clear the entire canvas and redraw
      redrawObjects(); // Redraw all objects except the erased one
    }
  });
};

// Redraw all objects (except the erased ones)
const redrawObjects = () => {
  drawnObjects.value.forEach((obj) => {
    if (obj.type === 'rectangle') {
      drawRectangle(obj.x, obj.y);
    } else if (obj.type === 'circle') {
      drawCircle(obj.x, obj.y);
    } else if (obj.type === 'text') {
      drawText(obj.x, obj.y, obj.text);
    }
  });
};

// Clear the canvas
const clearDrawing = () => {
  context.clearRect(0, 0, canvasWidth, canvasHeight);
  drawGrid(); // Redraw the grid
};

// Save the drawing as an image
const saveDrawing = () => {
  const dataURL = drawingCanvas.value.toDataURL();
  const link = document.createElement('a');
  link.href = dataURL;
  link.download = 'drawing.png';
  link.click();
};
</script>

<style scoped>
/* Main container holding everything */
.main-container {
  display: flex;
  height: 100vh;
  background-color: #1e1e1e;
  color: white;
  flex-direction: column;
  padding: 10px;
}

/* Map area with canvas for drawing */
.map-container {
  flex: 1;
  position: relative;
  margin-bottom: 20px; /* Space for tokens below */
  background-color: #333;
}

.game-map {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1; /* Canvas layer above the map */
}

/* Controls Panel */
.controls-container {
  background-color: #222;
  padding: 10px;
  margin-bottom: 20px;
}

.controls-container h3 {
  margin-bottom: 10px;
}

.color-picker,
.brush-size,
.shape-selector,
.actions {
  margin-bottom: 10px;
}

input[type='color'],
input[type='range'] {
  width: 100%;
}

button {
  padding: 5px 10px;
  margin-right: 10px;
  background-color: #444;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #555;
}
</style>
