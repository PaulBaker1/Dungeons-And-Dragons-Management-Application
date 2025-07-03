<template>
  <div class="prep-item-manager">
    <h1>Prep Items Management</h1>

    <!-- Create Prep Item Form -->
    <section class="create-prep-item">
      <h2>Create Prep Item</h2>
      <form @submit.prevent="handleCreate">
        <div>
          <label>Title:</label>
          <input type="text" v-model="newItem.title" required />
        </div>
        <div>
          <label>Description:</label>
          <textarea v-model="newItem.description"></textarea>
        </div>
        <div>
          <label>Type:</label>
          <select v-model="newItem.type" required>
            <option value="" disabled>Select type</option>
            <option v-for="type in prepItemTypes" :key="type" :value="type">
              {{ type }}
            </option>
          </select>
        </div>
        <div>
          <label>Visible to Players:</label>
          <input type="checkbox" v-model="newItem.visibleToPlayers" />
        </div>
        <div>
          <label>Is Critical:</label>
          <input type="checkbox" v-model="newItem.isCritical" />
        </div>
        <div>
          <label>File URL:</label>
          <input type="text" v-model="newItem.fileUrl" />
        </div>
        <div>
          <label>Session ID:</label>
          <input type="number" v-model.number="newItem.sessionId" placeholder="Optional" />
        </div>
        <div>
          <label>Campaign ID:</label>
          <input type="number" v-model.number="newItem.campaignId" required />
        </div>
        <button type="submit">Create Prep Item</button>
      </form>
    </section>

    <!-- Fetch Prep Items Controls -->
    <section class="fetch-prep-items">
      <h2>Fetch Prep Items</h2>
      <div>
        <label>Session ID:</label>
        <input type="number" v-model.number="fetchSessionId" placeholder="Enter session ID" />
        <button @click="loadSessionPrepItems">Load Session Items</button>
      </div>
      <div>
        <label>Campaign ID (Backlog):</label>
        <input type="number" v-model.number="fetchCampaignId" placeholder="Enter campaign ID" />
        <button @click="loadCampaignBacklog">Load Campaign Backlog</button>
      </div>
    </section>

    <!-- Prep Items List -->
    <section class="prep-item-list">
      <h2>Prep Items</h2>
      <ul>
        <li v-for="item in prepItems" :key="item.id">
          <strong>{{ item.title }}</strong> - {{ item.description }}
          <div>
            Type: {{ item.type }}, Status: {{ item.status }}, Visible:
            {{ item.visibleToPlayers ? 'Yes' : 'No' }}
          </div>
          <div>
            <!-- Update Status -->
            <label>Update Status:</label>
            <select v-model="item.newStatus">
              <option disabled value="">Select new status</option>
              <option v-for="status in prepStatuses" :key="status" :value="status">
                {{ status }}
              </option>
            </select>
            <button @click="updateStatus(item)">Update</button>
          </div>
          <div>
            <button @click="toggleVisibility(item)">
              Toggle Visibility (Current: {{ item.visibleToPlayers ? 'Visible' : 'Hidden' }})
            </button>
            <button @click="deleteItem(item.id)">Delete</button>
          </div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const prepItemTypes = ['MAP', 'LORE', 'NPC', 'HANDOUT', 'PUZZLE', 'ENCOUNTER', 'SOUND', 'MISC'];
const prepStatuses = ['TODO', 'IN_PROGRESS', 'READY', 'USED'];

// Model for creating a new prep item
const newItem = ref({
  title: '',
  description: '',
  type: '',
  visibleToPlayers: false,
  isCritical: false,
  fileUrl: '',
  sessionId: null,
  campaignId: null,
});

// IDs for fetching items by session or campaign backlog
const fetchSessionId = ref(null);
const fetchCampaignId = ref(null);

// Array to hold fetched prep items
const prepItems = ref([]);

// Helper function for making API calls
async function apiFetch(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!response.ok) {
    throw new Error(await response.text());
  }
  return response.json();
}

// Create a new prep item
async function handleCreate() {
  try {
    const payload = {
      title: newItem.value.title,
      description: newItem.value.description,
      type: newItem.value.type,
      visibleToPlayers: newItem.value.visibleToPlayers,
      isCritical: newItem.value.isCritical,
      fileUrl: newItem.value.fileUrl,
      sessionId: newItem.value.sessionId,
      campaignId: newItem.value.campaignId,
    };
    const data = await apiFetch('/api/prepitems', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
    // Add the created item to the list
    prepItems.value.unshift(data);
    // Reset form
    newItem.value = {
      title: '',
      description: '',
      type: '',
      visibleToPlayers: false,
      isCritical: false,
      fileUrl: '',
      sessionId: null,
      campaignId: null,
    };
  } catch (err) {
    alert('Error creating prep item: ' + err.message);
  }
}

// Fetch prep items by session
async function loadSessionPrepItems() {
  if (!fetchSessionId.value) {
    alert('Please enter a session ID');
    return;
  }
  try {
    const data = await apiFetch(`/api/sessions/${fetchSessionId.value}/prepitems`);
    // Add a temporary field for updating status
    prepItems.value = data.map(item => ({ ...item, newStatus: '' }));
  } catch (err) {
    alert('Error fetching session prep items: ' + err.message);
  }
}

// Fetch backlog prep items by campaign
async function loadCampaignBacklog() {
  if (!fetchCampaignId.value) {
    alert('Please enter a campaign ID');
    return;
  }
  try {
    const data = await apiFetch(`/api/campaigns/${fetchCampaignId.value}/prepitems/backlog`);
    prepItems.value = data.map(item => ({ ...item, newStatus: '' }));
  } catch (err) {
    alert('Error fetching campaign backlog: ' + err.message);
  }
}

// Update the status of a prep item
async function updateStatus(item) {
  if (!item.newStatus) {
    alert('Please select a new status');
    return;
  }
  try {
    const payload = { status: item.newStatus };
    const updated = await apiFetch(`/api/prepitems/${item.id}/status`, {
      method: 'PATCH',
      body: JSON.stringify(payload),
    });
    // Update the item locally with the new data
    Object.assign(item, updated);
    item.newStatus = '';
  } catch (err) {
    alert('Error updating status: ' + err.message);
  }
}

// Toggle the visibility flag for a prep item
async function toggleVisibility(item) {
  try {
    const payload = { visibleToPlayers: !item.visibleToPlayers };
    const updated = await apiFetch(`/api/prepitems/${item.id}/visibility`, {
      method: 'PATCH',
      body: JSON.stringify(payload),
    });
    Object.assign(item, updated);
  } catch (err) {
    alert('Error toggling visibility: ' + err.message);
  }
}

// Delete a prep item
async function deleteItem(id) {
  if (!confirm('Are you sure you want to delete this prep item?')) return;
  try {
    await apiFetch(`/api/prepitems/${id}`, { method: 'DELETE' });
    prepItems.value = prepItems.value.filter(item => item.id !== id);
  } catch (err) {
    alert('Error deleting prep item: ' + err.message);
  }
}
</script>

<style scoped>
.prep-item-manager {
  padding: 1rem;
}

.prep-item-manager h1,
.prep-item-manager h2 {
  margin-bottom: 0.5rem;
}

.create-prep-item form div,
.fetch-prep-items div {
  margin-bottom: 0.75rem;
}

.create-prep-item label,
.fetch-prep-items label {
  display: block;
  margin-bottom: 0.25rem;
}

.create-prep-item input,
.create-prep-item textarea,
.create-prep-item select,
.fetch-prep-items input {
  width: 100%;
  padding: 0.5rem;
  box-sizing: border-box;
}

.prep-item-list ul {
  list-style: none;
  padding: 0;
}

.prep-item-list li {
  border: 1px solid #ccc;
  padding: 0.75rem;
  margin-bottom: 0.5rem;
}
</style>
