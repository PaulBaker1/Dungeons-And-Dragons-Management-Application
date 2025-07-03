<template>
  <div class="px-4 py-6 text-white bg-gray-900 min-h-screen space-y-8 max-w-3xl mx-auto">

    <!-- class Header -->
    <fieldset class="card-block">
      <legend class="legend-label">🧝‍♂️ Class Info</legend>
      <h1 class="text-4xl font-bold mb-2">{{ classData.name || 'No Class Name' }}</h1>
      <p class="text-lg text-slate-300">Hit Die: <strong>d{{ classData.hit_die || 'N/A' }}</strong></p>
    </fieldset>

    <!-- class Lore Block -->
    <fieldset class="card-block" v-if="classData.index === 'barbarian'">
      <legend class="legend-label">🪓 About the Barbarian</legend>
      <div class="text-slate-300 space-y-2">
        <p>Barbarians are fierce warriors of primitive background who can enter a battle rage, becoming unstoppable avatars of fury and strength.</p>
        <p>With unmatched physical might and resilience, they shrug off damage and cut down enemies with brutal ferocity.</p>
        <p>They excel in frontline combat and thrive without heavy armor, instead relying on their Rage and high Constitution.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'bard'">
      <legend class="legend-label">🎶 About the Bard</legend>
      <div class="text-slate-300 space-y-2">
        <p>Bards are versatile spellcasters, storytellers, and adventurers who wield music and magic to charm, inspire, and confound.</p>
        <p>They thrive on creativity, adaptability, and support, and can pick up abilities from other classes through Magical Secrets.</p>
        <p>Ideal for players who enjoy flexible gameplay and utility in both social and combat situations.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'cleric'">
      <legend class="legend-label">🙏 About the Cleric</legend>
      <div class="text-slate-300 space-y-2">
        <p>Clerics are divine spellcasters who draw power from a deity to heal allies, smite foes, and turn the tide of battle with divine intervention.</p>
        <p>They can choose from a variety of domains, each granting powerful thematic spells and features.</p>
        <p>Highly customizable and versatile, clerics can act as frontline tanks, support casters, or powerful damage dealers.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'druid'">
      <legend class="legend-label">🌿 About the Druid</legend>
      <div class="text-slate-300 space-y-2">
        <p>Druids wield the magic of nature, commanding the elements, transforming into beasts, and restoring life to allies.</p>
        <p>They are deeply connected to the natural world and draw from its forces to protect and preserve.</p>
        <p>Their spell list includes healing, control, and terrain manipulation, and their Wild Shape adds unique tactical options.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'fighter'">
      <legend class="legend-label">⚔️ About the Fighter</legend>
      <div class="text-slate-300 space-y-2">
        <p>Fighters are weapon masters and tactical experts with the most attacks per round in 5e.</p>
        <p>They can specialize through Martial Archetypes like the Battle Master, Champion, or Eldritch Knight.</p>
        <p>Great for both new and veteran players, they are simple to learn and endlessly customizable.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'monk'">
      <legend class="legend-label">🥋 About the Monk</legend>
      <div class="text-slate-300 space-y-2">
        <p>Monks harness the power of ki to deliver precise strikes, deflect attacks, and move with supernatural speed.</p>
        <p>They favor unarmored combat and excel at mobility, battlefield control, and creative problem-solving.</p>
        <p>Perfect for players who enjoy finesse, martial arts, and versatility over brute force.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'paladin'">
      <legend class="legend-label">🛡️ About the Paladin</legend>
      <div class="text-slate-300 space-y-2">
        <p>Paladins are holy warriors bound by sacred oaths to uphold justice, protect the innocent, and smite evil.</p>
        <p>They combine divine spellcasting, melee prowess, and powerful support abilities like Lay on Hands and auras.</p>
        <p>Whether noble or vengeful, their oath defines their powers and role in the world.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'ranger'">
      <legend class="legend-label">🏹 About the Ranger</legend>
      <div class="text-slate-300 space-y-2">
        <p>Rangers are skilled hunters and trackers who blend martial and magical abilities to survive in any terrain.</p>
        <p>They are specialists in ambush tactics, nature magic, and favored enemies.</p>
        <p>Great for players who enjoy tactical combat, exploration, and themes of wilderness survival.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'rogue'">
      <legend class="legend-label">🗡️ About the Rogue</legend>
      <div class="text-slate-300 space-y-2">
        <p>Rogues are cunning, agile, and precise, relying on stealth and skill rather than brute strength.</p>
        <p>They deal devastating damage with Sneak Attack and excel at skills, traps, and infiltration.</p>
        <p>Perfect for creative, strategic players who like clever tactics and trickery.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'sorcerer'">
      <legend class="legend-label">🔮 About the Sorcerer</legend>
      <div class="text-slate-300 space-y-2">
        <p>Sorcerers are innate spellcasters whose magic comes from a powerful bloodline or otherworldly influence.</p>
        <p>They can manipulate spells in dynamic ways using their unique Sorcery Points and Metamagic.</p>
        <p>Focused on raw magical force and creative spell usage, they are explosive and unpredictable.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'warlock'">
      <legend class="legend-label">🕯️ About the Warlock</legend>
      <div class="text-slate-300 space-y-2">
        <p>Warlocks draw their magic from pacts with mysterious entities like fiends, fey, or eldritch beings.</p>
        <p>They combine limited spellcasting with powerful invocations and customizable Eldritch Blast builds.</p>
        <p>Warlocks are mysterious and dark, offering flexible builds and potent roleplaying potential.</p>
      </div>
    </fieldset>

    <fieldset class="card-block" v-if="classData.index === 'wizard'">
      <legend class="legend-label">📚 About the Wizard</legend>
      <div class="text-slate-300 space-y-2">
        <p>Wizards are scholarly masters of arcane knowledge with the widest spell selection in D&D 5e.</p>
        <p>They can specialize in schools of magic, and prepare spells daily for any occasion.</p>
        <p>Although fragile, their power scales immensely with level, making them the ultimate magical tacticians.</p>
      </div>
    </fieldset>

    <!-- Description -->
    <fieldset class="card-block" v-if="classData.desc?.length">
      <legend class="legend-label">📜 Description</legend>
      <div class="grid grid-cols-1 gap-2 text-slate-300 leading-relaxed">
        <p v-for="(line, i) in classData.desc" :key="i">{{ line }}</p>
      </div>
    </fieldset>

    <!-- Proficiencies -->
    <fieldset class="card-block" v-if="classData.proficiencies?.length">
      <legend class="legend-label">🎯 Proficiencies</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="prof in classData.proficiencies" :key="prof.index" class="chip">
          {{ prof.name }}
        </span>
      </div>
    </fieldset>

    <!-- Saving Throws -->
    <fieldset class="card-block" v-if="classData.saving_throws?.length">
      <legend class="legend-label">🛡️ Saving Throws</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="save in classData.saving_throws" :key="save.index" class="chip">
          {{ save.name }}
        </span>
      </div>
    </fieldset>

    <!-- Subclasses -->
    <fieldset class="card-block" v-if="classData.subclasses?.length">
      <legend class="legend-label">🧬 Subclasses</legend>
      <div class="flex flex-wrap gap-2">
        <span v-for="sub in classData.subclasses" :key="sub.index" class="chip">
          {{ sub.name }}
        </span>
      </div>
    </fieldset>

    <!-- Proficiency Choices -->
    <fieldset class="card-block" v-if="classData.proficiency_choices?.length">
      <legend class="legend-label">🧩 Proficiency Choices</legend>
      <div class="space-y-4 text-slate-300">
        <div v-for="(choice, i) in classData.proficiency_choices" :key="i">
          <p class="font-medium mb-1">
            {{ choice.desc || ('Choose ' + choice.choose + ' from:') }}
          </p>
          <div class="flex flex-wrap gap-2">
            <span
                v-for="(option, j) in (choice.from?.options || [])"
                :key="j"
                class="chip"
            >
              {{ option.item.name }}
            </span>
          </div>
        </div>
      </div>
    </fieldset>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { DnDService } from '@/services/dndApiService.js'

const classData = ref({})
const route = useRoute()

onMounted(async () => {
  try {
    const classIndex = route.params.index || 'bard'
    const data = await DnDService.getClass(classIndex)
    classData.value = data
  } catch (err) {
    console.error('Failed to fetch class data:', err)
    classData.value = { error: 'Failed to fetch class data' }
  }
})
</script>

<style scoped>
.card-block {
  background-color: #1e293b;
  border: 1px solid #334155;
  border-radius: 0.75rem;
  padding: 1.5rem;
}
.legend-label {
  color: #e0f2fe;
  font-weight: 600;
  font-size: 1.1rem;
  padding: 0 0.5rem;
}
.chip {
  background-color: #6366f1;
  color: white;
  font-size: 0.875rem;
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
}
</style>
