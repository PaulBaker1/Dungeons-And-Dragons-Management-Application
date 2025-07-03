<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <div class="flex items-center justify-between">
      <h1 class="text-3xl font-bold mb-6">
        {{ isEditing ? '✏️ Edit Character' : '🦉 Character Info' }}
      </h1>
      <div class="flex items-center gap-4">
        <!-- Back button shows different behavior based on mode -->
        <button
            v-if="!isEditing"
            @click="goBackToList"
            class="btn-outline"
        >
          ← Back to Characters
        </button>
        <button
            v-else
            @click="cancelEdit"
            class="btn-outline"
        >
          ← Cancel
        </button>

        <button
            v-if="!isEditing"
            @click="isEditing = true"
            class="btn-primary"
        >
          ✏️ Edit
        </button>
        <button
            v-else
            @click="saveCharacter"
            class="btn-primary"
        >
          💾 Save
        </button>
      </div>
    </div>

    <div v-if="character && character.id">
      <fieldset class="card space-y-6">
        <!-- Basic Info -->
        <legend class="section">🙍 Basic Info</legend>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="label">Player Name *</label>
            <input v-model="character.playerName" class="input" required :disabled="!isEditing" />
          </div>
          <div>
            <label class="label">Character Name *</label>
            <input v-model="character.characterName" class="input" required :disabled="!isEditing" />
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="label">Class *</label>
            <input :value="character.class" class="input" disabled />
          </div>
          <div>
            <label class="label">Level</label>
            <input type="number" v-model="character.level" class="input" :disabled="!isEditing" />
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="label">Avatar</label>
            <div class="mt-2 w-32 h-32 border border-slate-700 bg-slate-800 rounded overflow-hidden">
              <img :src="character.avatar || placeholderAvatar" class="object-cover w-full h-full" alt="" />
            </div>
          </div>
          <div>
            <label class="label">Control Player</label>
            <input v-model="character.controlPlayer" class="input" :disabled="!isEditing" />
          </div>
        </div>

        <!-- Ability Scores & Stats -->
<!--        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">-->
<!--          <div v-for="(label, key) in abilityLabels" :key="key">-->
<!--            <label class="label">{{ label }}</label>-->
<!--            <input type="number" v-model="character[key]" class="input" :disabled="!isEditing" />-->
<!--          </div>-->
<!--        </div>-->

        <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
          <div>
            <label class="label">Speed</label>
            <input type="number" v-model="character.speed" class="input" :disabled="!isEditing" />
          </div>
          <div>
            <label class="label">Initiative</label>
            <input type="number" v-model="character.initiative" class="input" :disabled="!isEditing" />
          </div>
          <div>
            <label class="label">Hit Points</label>
            <input type="number" v-model="character.hitPoints" class="input" :disabled="!isEditing" />
          </div>
        </div>

        <!-- Ability Scores -->
        <fieldset class="card">
          <legend class="section">🧠 Ability Scores</legend>
          <div class="grid grid-cols-2 md:grid-cols-6 gap-4">
            <div v-for="(label, key) in abilityLabels" :key="key">
              <label class="label">{{ label }}</label>
              <input v-model.number="character[key]" class="input" :disabled="!isEditing" />
            </div>
          </div>
        </fieldset>

        <!-- Senses -->
        <fieldset class="card">
          <legend class="section">👁️‍🗨️ Senses</legend>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div><label class="label">👀 Passive perception</label><input v-model="character.passivePerception" class="input" :disabled="!isEditing" /></div>
            <div><label class="label">🕵️ Passive investigation</label><input v-model="character.passiveInvestigation" class="input" :disabled="!isEditing" /></div>
            <div><label class="label">🧘 Passive insight</label><input v-model="character.passiveInsight" class="input" :disabled="!isEditing" /></div>
          </div>
        </fieldset>

        <!-- Resistances & Vulnerabilities -->
        <fieldset class="card">
          <legend class="section">🛡️ Resistances & Vulnerabilities</legend>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="label">🔥 Damage Vulnerabilities</label>
              <textarea v-model="character.vulnerabilities" class="input" placeholder="e.g. Fire, Cold, Acid" rows="3" :disabled="!isEditing" />
            </div>
            <div>
              <label class="label">🧱 Damage Resistances</label>
              <textarea v-model="character.resistances" class="input" placeholder="e.g. Bludgeoning, Lightning" rows="3" :disabled="!isEditing" />
            </div>
            <div>
              <label class="label">🧬 Damage Immunities</label>
              <textarea v-model="character.immunities" class="input" placeholder="e.g. Poison, Thunder" rows="3" :disabled="!isEditing" />
            </div>
            <div>
              <label class="label">🧠 Condition Immunities</label>
              <textarea v-model="character.conditionImmunities" class="input" placeholder="e.g. Charmed, Deafened" rows="3" :disabled="!isEditing" />
            </div>
          </div>
        </fieldset>

        <!-- Skills (Disabled Editing for Skills) -->
        <div>
          <label class="label">Skills</label>
          <div class="grid grid-cols-2 md:grid-cols-3 gap-2">
            <label v-for="skill in character.skills" :key="skill.name" class="flex items-center gap-2 text-sm">
              <input type="checkbox" v-model="skill.proficient" :disabled="true" />
              {{ skill.name }}
            </label>
          </div>
        </div>
      </fieldset>
    </div>

    <div v-else class="text-slate-400 text-sm italic text-center pt-10">
      Loading character...
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const placeholderAvatar = 'https://placehold.co/150x150?text=Avatar'
const dndClasses = ['Barbarian', 'Bard', 'Cleric', 'Druid', 'Fighter', 'Monk', 'Paladin', 'Ranger', 'Rogue', 'Sorcerer', 'Warlock', 'Wizard']
const abilityLabels = {
  strength: '💪 Strength',
  dexterity: '🤸 Dexterity',
  constitution: '🧍 Constitution',
  intelligence: '🧠 Intelligence',
  wisdom: '🦉 Wisdom',
  charisma: '🗣 Charisma'
}

const isEditing = ref(false)

const character = reactive({
  id: null,
  campaignId: null,
  playerName: '',
  characterName: '',
  controlPlayer: 'No',
  avatar: '',
  class: '',
  experience: 0,
  level: 1,
  hitPoints: 45,
  armorClass: 13,
  spellSaveDC: 17,
  speed: 30,
  initiative: 2,
  strength: 8,
  dexterity: 14,
  constitution: 12,
  intelligence: 18,
  wisdom: 12,
  charisma: 10,
  passivePerception: true,
  passiveInvestigation: true,
  passiveInsight: true,
  vulnerabilities: '',
  resistances: '',
  immunities: '',
  conditionImmunities: '',
  jackOfAllTrades: false,
  skills: [
    'Acrobatics', 'Animal Handling', 'Arcana', 'Athletics', 'Deception', 'History',
    'Insight', 'Intimidation', 'Investigation', 'Medicine', 'Nature', 'Perception',
    'Performance', 'Persuasion', 'Religion', 'Sleight Of Hand', 'Stealth', 'Survival'
  ].map(name => ({ name, proficient: false, expertise: false }))
})

async function saveCharacter() {
  const payload = {
    playerName: character.playerName,
    characterName: character.characterName,
    classAndLevel: `${character.class} ${character.level}`,
    experience: character.experience,
    level: character.level,
    levelOverride: true,
    hitPoints: character.hitPoints,
    armorClass: character.armorClass,
    spellDC: character.spellSaveDC,
    speed: character.speed,
    initiative: character.initiative,
    strength: character.strength,
    dexterity: character.dexterity,
    constitution: character.constitution,
    intelligence: character.intelligence,
    wisdom: character.wisdom,
    charisma: character.charisma,
    passivePerception: character.passivePerception,
    passiveInvestigation: character.passiveInvestigation,
    passiveInsight: character.passiveInsight,
    jackOfAllTrades: character.jackOfAllTrades,
    playerControlled: character.controlPlayer === 'Yes',
    damageResistances: character.resistances.split(',').map(s => s.trim()),
    damageVulnerabilities: character.vulnerabilities.split(',').map(s => s.trim()),
    damageImmunities: character.immunities.split(',').map(s => s.trim()),
    conditionImmunities: character.conditionImmunities.split(',').map(s => s.trim()),
    skillProficiencies: character.skills.filter(s => s.proficient).map(s => s.name),
    campaignId: character.campaignId,
    notes: "Updated arcane trickster."
  }

  try {
    const method = character.id ? 'PUT' : 'POST'
    const url = character.id ? `/api/characters/${character.id}` : '/api/characters'
    const res = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })

    if (!res.ok) throw new Error("Failed to save")

    // Stay on the same page after saving
    isEditing.value = false
    await loadCharacter() // Refresh data

  } catch (e) {
    console.error("Save error:", e)
    alert("Save failed. Check console for details.")
  }
}


async function loadCharacter() {
  const id = route.params.id
  if (!id) return

  try {
    const res = await fetch(`/api/characters/${id}`)
    const data = await res.json()
    const [characterClass, level] = data.classAndLevel?.split(' ') || []

    Object.assign(character, {
      ...data,
      class: characterClass || '',
      level: parseInt(level) || 1,
      resistances: data.damageResistances?.join(', ') || '',
      vulnerabilities: data.damageVulnerabilities?.join(', ') || '',
      immunities: data.damageImmunities?.join(', ') || '',
      conditionImmunities: data.conditionImmunities?.join(', ') || '',
      controlPlayer: data.playerControlled ? 'Yes' : 'No',
      skills: character.skills.map(skill => ({
        ...skill,
        proficient: data.skillProficiencies?.includes(skill.name) || false
      }))
    })
  } catch (err) {
    console.error("Loading failed:", err)
  }
}

function goBackToList() {
  router.push('/characters') // Adjust to your character list route
}

function cancelEdit() {
  isEditing.value = false
  loadCharacter() // Reload original data
}


onMounted(loadCharacter)
</script>

<style scoped>
.input {
  @apply w-full bg-slate-800 p-2 border border-slate-600 rounded text-white;
}
.label {
  @apply block text-sm mb-1 text-slate-400;
}
.card {
  @apply border border-slate-700 rounded-lg p-4 bg-slate-900;
}
.btn-primary {
  @apply bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold;
}
.btn-outline {
  @apply text-blue-400 hover:underline text-sm;
}
</style>
