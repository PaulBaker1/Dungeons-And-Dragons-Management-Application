<!-- src/components/navigation/SidebarLink.vue -->
<template>
  <router-link
      :to="to"
      class="flex items-center gap-3 py-2 px-3 rounded hover:bg-slate-700 transition text-sm font-medium"
      active-class="bg-slate-700"
  >
    <i :class="icon" class="text-base text-slate-400 w-5 pi"></i>
    <span>{{ label }}</span>
  </router-link>
</template>

<script setup>
defineProps({
  to: String,
  label: String,
  icon: String
})
</script>
