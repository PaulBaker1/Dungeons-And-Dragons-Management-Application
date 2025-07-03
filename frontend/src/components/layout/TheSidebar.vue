<!-- src/components/layout/TheSidebar.vue -->
<template>
  <aside class="bg-slate-900 text-white w-64 min-h-screen flex flex-col border-r border-slate-800">
    <div class="text-center py-4 border-b border-slate-800 font-bold text-lg">
      🛡️ DnD Tool
    </div>

    <div class="flex-1 px-4 py-6 space-y-6 overflow-y-auto">
      <!-- DM Section -->
      <div>
        <h3 class="text-xs text-slate-500 font-semibold mb-2">DM CONTENT</h3>
        <SidebarLink to="/campaigns" icon="pi pi-flag" label="Campaigns" />
        <SidebarLink to="/pdf-view" icon="pi pi-book" label="Adventures" />
        <SidebarLink to="/characters" icon="pi pi-id-card" label="Characters" />
        <SidebarLink to="/dice" icon="pi pi-table" label="Dice" />
        <SidebarLink to="/board-paint" icon="pi pi-pen-to-square" label="Board" />
      </div>

      <div>
        <h3 class="text-xs text-slate-500 font-semibold mb-2">HANDBOOK</h3>
        <SidebarLink to="/classes" icon="pi pi-user" label="Classes" />
        <SidebarLink to="/races" icon="pi pi-eye-slash" label="Races" />
        <SidebarLink to="/spells" icon="pi pi-sparkles" label="Spells" />
        <SidebarLink to="/monsters" icon="pi pi-user-edit" label="Monsters" />
        <SidebarLink to="/equipment" icon="pi pi-book" label="Equipment" />
      </div>

      <div>
        <h3 class="text-xs text-slate-500 font-semibold mb-2">UTILS</h3>
        <SidebarLink to="/remainders" icon="pi pi-bell" label="Reminders" />
        <SidebarLink to="/image-gallery" icon="pi pi-image" label="Img Gallery" />
        <SidebarLink to="/midi-player" icon="pi pi-volume-up" label="MIDI" />
      </div>
    </div>
  </aside>
</template>

<script setup>
import SidebarLink from '@/components/navigation/SidebarLink.vue'
</script>
