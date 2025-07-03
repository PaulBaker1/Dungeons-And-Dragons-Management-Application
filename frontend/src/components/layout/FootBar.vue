<template>
  <transition name="fade">
    <footer id="footerbar" class="text-slate-400 text-xs">
      <div class="pt-4 pb-6 px-4 max-w-7xl mx-auto">

        <!-- Logo & Description -->
        <div class="mb-6">
          <img src="/logo.svg" alt="DnD Tool" class="h-8 mb-1" />
          <p class="leading-relaxed">
            ©2025 DnD Tool. Icons by <a href="https://game-icons.net" target="_blank" class="underline hover:text-white">Delapouite, Lorc & Skull</a>.<br />
            Content released under the <a href="https://opencontent.com/" target="_blank" class="underline hover:text-white">OGL 1.0a</a>.
          </p>
        </div>

        <!-- Footer Columns -->
        <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
          <FooterColumn title="Quick Links" :items="[
            { label: 'Compendium', href: 'https://5e.tools/' },
            { label: 'Docs', href: 'https://vuejs.org/' },
            { label: 'Support', href: 'https://discord.gg/' },
            { label: 'Feedback', href: 'https://github.com/' }
          ]" />

          <FooterColumn title="DM Content" :items="[
            { label: 'Campaigns', href: 'https://www.dndbeyond.com/campaigns' },
            { label: 'Players', href: 'https://www.dndbeyond.com/characters' },
            { label: 'NPCs', href: 'https://www.dndbeyond.com/npc-generator' },
            { label: 'Items', href: 'https://5e.tools/items.html' }
          ]" />

          <FooterColumn title="Player Content" :items="[
            { label: 'Characters', href: 'https://www.dndbeyond.com/characters' },
            { label: 'Following', href: 'https://www.dndbeyond.com/followed-users' },
            { label: 'Account', href: 'https://www.dndbeyond.com/account' }
          ]" />

          <FooterColumn title="Follow Us" :items="[
            { label: 'Discord', href: 'https://discord.gg/' },
            { label: 'Patreon', href: 'https://patreon.com/' },
            { label: 'Twitter', href: 'https://twitter.com/' },
            { label: 'GitHub', href: 'https://github.com/' }
          ]" />
        </div>
      </div>
    </footer>
  </transition>
</template>

<script setup>
import FooterColumn from '@/components/navigation/FooterColumn.vue'
</script>

<style scoped>
#footerbar {
  background-color: #1e293b; /* slate-800 */
  border-top: 1px solid #334155; /* slate-700 */
  font-size: 0.75rem; /* text-xs */
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(12px);
}
</style>
