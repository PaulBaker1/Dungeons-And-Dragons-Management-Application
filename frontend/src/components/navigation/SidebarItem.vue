<template>
  <div class="sidebar-container">
    <div class="logo text-center py-4 border-b border-slate-800">
      <h1 class="text-xl font-bold text-white">🛡️ DnD Tool</h1>
    </div>

    <div class="sidebar-section">
      <h2 class="section-title">DM CONTENT</h2>
      <SidebarLink to="/campaigns" icon="pi pi-flag" label="Campaigns" />
      <SidebarLink to="/classes" icon="pi pi-user" label="Classes" />
      <SidebarLink to="/races" icon="pi pi-settings" label="Races" />
      <SidebarLink to="/quests" icon="pi pi-book" label="Quests" />
      <SidebarLink to="/npc" icon="pi pi-users" label="NPCs" />
      <SidebarLink to="/dice" icon="pi pi-table" label="Dice Roller" />
      <SidebarLink to="/spells" icon="pi pi-sparkles" label="Spells" />
      <SidebarLink to="/reminders" icon="pi pi-bell" label="Reminders" />
      <SidebarLink to="/items" icon="pi pi-briefcase" label="Items" />
      <SidebarLink to="/import" icon="pi pi-upload" label="Import" />
    </div>


    <div class="sidebar-section mt-6">
      <h2 class="section-title">PLAYER CONTENT</h2>
      <SidebarLink to="/characters" icon="pi pi-id-card" label="Characters" />
      <SidebarLink to="/followed" icon="pi pi-users" label="Followed Users" />
    </div>
  </div>
</template>

<style scoped>
.sidebar-container {
  @apply bg-slate-900 text-white h-screen w-64 flex flex-col shadow-lg border-r border-slate-800;
}
.sidebar-section {
  @apply px-4 py-4 space-y-2;
}
.section-title {
  @apply text-xs text-slate-500 font-semibold mb-2 tracking-wide;
}
</style>
<script setup lang="ts">
import SidebarLink from "@/components/navigation/SidebarLink.vue";
</script>