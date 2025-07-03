<template>
  <div class="space-y-6">
    <!-- Share initiative -->
    <section>
      <h3 class="text-slate-400 text-xs uppercase mb-2">Share initiative</h3>
      <input
          value="https://shieldmaiden.app/initiative"
          readonly
          class="w-full px-3 py-2 bg-slate-800 text-slate-300 rounded border border-slate-600"
      />
      <button class="mt-2 text-xs text-blue-400 hover:underline">Copy Link</button>
    </section>

    <!-- Subscription -->
    <section class="text-sm">
      <h3 class="text-slate-400 text-xs uppercase mb-2">Subscription</h3>
      <ul class="space-y-1 text-slate-300 text-xs">
        <li>❌ Character sync</li>
        <li>❌ Avatar crop & upload</li>
        <li>❌ Background effects</li>
        <li>❌ Import content</li>
      </ul>
      <button class="mt-2 bg-red-500 hover:bg-red-600 text-white w-full rounded py-1">
        Subscribe
      </button>
    </section>

    <!-- Community -->
    <section>
      <h3 class="text-slate-400 text-xs uppercase mb-2">Community</h3>
      <a
          href="https://discord.gg/fhmKBM7"
          target="_blank"
          class="block text-blue-400 hover:underline text-sm"
      >
        Join us on Discord →
      </a>
    </section>
  </div>
</template>
