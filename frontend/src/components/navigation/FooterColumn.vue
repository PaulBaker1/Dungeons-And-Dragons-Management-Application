<template>
  <div>
    <h4 class="text-white font-semibold mb-1 text-sm">{{ title }}</h4>
    <ul class="space-y-1">
      <li v-for="(item, i) in items" :key="i">
        <a
            :href="item.href"
            class="hover:underline hover:text-white transition"
            target="_blank" rel="noopener"
        >
          {{ item.label }}
        </a>
      </li>
    </ul>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  items: Array
})
</script>
