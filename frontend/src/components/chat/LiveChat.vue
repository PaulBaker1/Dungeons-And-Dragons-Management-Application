<script setup>
import { ref } from 'vue'
import { Chat } from '@chat-ui/vue3'

// your two roles
const roles = [
  {
    value: 'dm',
    label: 'Dungeon Master',
    avatar: 'https://i.imgur.com/VxVxM4N.png'
  },
  {
    value: 'player',
    label: 'KnightOfValor',
    avatar: 'https://i.imgur.com/uIgDDDd.png'
  }
]

const currentRole = ref('dm')
const isOpen     = ref(true)
const messages   = ref([
  {
    message: 'Welcome, adventurer!',
    type: 'chatbot',
    timestamp: new Date().toLocaleTimeString()
  }
])

function handleSend(text) {
  // push under the current role
  messages.value.push({
    message: text,
    type: currentRole.value === 'player' ? 'person' : 'chatbot',
    timestamp: new Date().toLocaleTimeString()
  })

  // simulate a reply
  setTimeout(() => {
    const isPlayer = currentRole.value === 'player'
    messages.value.push({
      message: isPlayer
          ? `DM says: Got "${text}"`
          : `Player replies: Roger that!`,
      type: isPlayer ? 'chatbot' : 'person',
      timestamp: new Date().toLocaleTimeString()
    })
  }, 800)
}

function closeWidget() {
  isOpen.value = false
}
</script>

<template>
  <div
      v-if="isOpen"
      class="fixed bottom-4 right-4 z-50 w-80 bg-white rounded-lg shadow-lg flex flex-col overflow-hidden"
  >
    <!-- HEADER -->
<!--    <div class="flex items-center justify-between px-4 py-2 border-b">-->
<!--      <div class="flex space-x-2">-->
<!--        <template v-for="r in roles" :key="r.value">-->
<!--          <img-->
<!--              :src="r.avatar"-->
<!--              :alt="r.label"-->
<!--              class="h-8 w-8 rounded-full cursor-pointer"-->
<!--              :class="{-->
<!--              'ring-2 ring-green-600': currentRole === r.value,-->
<!--              'opacity-70': currentRole !== r.value-->
<!--            }"-->
<!--              @click="currentRole = r.value"-->
<!--          />-->
<!--        </template>-->
<!--      </div>-->
<!--      <div class="flex items-center space-x-3">-->
<!--        <span class="h-2 w-2 bg-green-500 rounded-full" title="Online"></span>-->
<!--        <button-->
<!--            @click="closeWidget"-->
<!--            class="text-gray-500 hover:text-gray-700 text-xl leading-none"-->
<!--        >-->
<!--          &times;-->
<!--        </button>-->
<!--      </div>-->
<!--    </div>-->

    <!-- CHAT -->
    <div class="flex-1 overflow-auto bg-gray-50">
      <Chat
          :chat="messages"
          :onSend="handleSend"
          placeholder="Type your message here..."
      />
    </div>
  </div>
</template>

<style scoped>
/* force the inner chat area to scroll */
.fixed > .chat-ui {
  height: 300px;
}
</style>
