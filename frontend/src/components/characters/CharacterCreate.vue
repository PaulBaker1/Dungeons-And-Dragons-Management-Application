<template>
  <div class="max-w-6xl mx-auto px-6 py-10 text-white space-y-8">
    <h1 class="text-3xl font-bold mb-6">🦉 Edit Player</h1>

    <!-- Basic Info -->
    <fieldset class="card">
      <legend class="section">🙍 Basic Info</legend>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="label">Player name *</label>
          <input v-model="character.playerName" class="input" required />
        </div>
        <div>
          <label class="label">Character name *</label>
          <input v-model="character.characterName" class="input" required />
        </div>
        <div class="md:col-span-2">
          <label class="label">Give control to player</label>
          <input v-model="character.controlPlayer" class="input" placeholder="Search username or email" />
        </div>
      </div>
    </fieldset>

    <!-- Avatar & class -->
    <fieldset class="card">
      <legend class="section">🎨 Avatar & Class</legend>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="label">🧍 Avatar</label>
          <div class="w-32 h-32 border border-slate-600 bg-slate-800 rounded overflow-hidden">
            <img :src="character.avatar || placeholderAvatar" class="object-cover w-full h-full" />
          </div>
          <input type="file" accept="image/*" @change="handleAvatarUpload" class="mt-2 text-sm text-slate-300" />
        </div>
        <div>
          <label class="label">🎓 Character Class *</label>
          <select v-model="character.class" class="input" required>
            <option disabled value="">Select a class</option>
            <option v-for="cls in dndClasses" :key="cls" :value="cls">{{ cls }}</option>
          </select>
        </div>
        <div>
          <label class="label">🎯 Campaign</label>
          <select v-model="character.campaignId" class="input" required>
            <option disabled value="">Choose campaign</option>
            <option
                v-for="c in campaigns"
                :key="c.id"
                :value="c.id"
            >
              {{ c.title }}
            </option>
          </select>
        </div>
      </div>
    </fieldset>

    <!-- Level & Base Stats -->
    <fieldset class="card">
      <legend class="section">📊 Level & Base Stats</legend>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
        <div><label class="label">📈 Experience</label><input v-model.number="character.experience" class="input" /></div>
        <div><label class="label">🔢 Level</label><input v-model.number="character.level" class="input" /><p class="text-xs text-slate-500 mt-1">Level override</p></div>
        <div><label class="label">❤️ Hit points *</label><input v-model.number="character.hitPoints" class="input" required /></div>
        <div><label class="label">🛡️ Armor Class *</label><input v-model.number="character.armorClass" class="input" required /></div>
        <div><label class="label">✨ Spell save DC</label><input v-model.number="character.spellSaveDC" class="input" /></div>
        <div><label class="label">👟 Speed</label><input v-model.number="character.speed" class="input" /></div>
        <div><label class="label">⚡ Initiative</label><input v-model.number="character.initiative" class="input" /></div>
      </div>
    </fieldset>

    <!-- Ability Scores -->
    <fieldset class="card">
      <legend class="section">🧠 Ability Scores</legend>
      <div class="grid grid-cols-2 md:grid-cols-6 gap-4">
        <div v-for="(label, key) in abilityLabels" :key="key">
          <label class="label">{{ label }}</label>
          <input v-model.number="character[key]" class="input" />
        </div>
      </div>
    </fieldset>

    <!-- Senses -->
    <fieldset class="card">
      <legend class="section">👁️‍🗨️ Senses</legend>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div><label class="label">👀 Passive perception</label><input v-model="character.passivePerception" class="input" /></div>
        <div><label class="label">🕵️ Passive investigation</label><input v-model="character.passiveInvestigation" class="input" /></div>
        <div><label class="label">🧘 Passive insight</label><input v-model="character.passiveInsight" class="input" /></div>
      </div>
    </fieldset>

    <!-- Resistances & Vulnerabilities -->
    <fieldset class="card">
      <legend class="section">🛡️ Resistances & Vulnerabilities</legend>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="label">🔥 Damage Vulnerabilities</label>
          <textarea v-model="character.vulnerabilities" class="input" placeholder="e.g. Fire, Cold, Acid" rows="3" />
        </div>
        <div>
          <label class="label">🧱 Damage Resistances</label>
          <textarea v-model="character.resistances" class="input" placeholder="e.g. Bludgeoning, Lightning" rows="3" />
        </div>
        <div>
          <label class="label">🧬 Damage Immunities</label>
          <textarea v-model="character.immunities" class="input" placeholder="e.g. Poison, Thunder" rows="3" />
        </div>
        <div>
          <label class="label">🧠 Condition Immunities</label>
          <textarea v-model="character.conditionImmunities" class="input" placeholder="e.g. Charmed, Deafened" rows="3" />
        </div>
      </div>
    </fieldset>

    <!-- Skills -->
    <fieldset class="card">
      <legend class="section">🎯 Skills</legend>
      <p class="text-sm mb-4">Proficiency Bonus: +2</p>
      <label class="text-xs flex items-center gap-2 mb-4">
        <input type="checkbox" v-model="character.jackOfAllTrades" />
        Jack of all Trades
      </label>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div v-for="skill in character.skills" :key="skill.name" class="bg-slate-800 p-3 rounded">
          <div class="flex justify-between items-center mb-1">
            <span class="text-sm font-medium">{{ skill.name }}</span>
            <span class="text-sm text-slate-300 font-mono">+{{ skillBonus(skill) }}</span>
          </div>
          <div class="flex gap-4 text-sm text-slate-300">
            <label class="checkbox-item">
              <input type="checkbox" v-model="skill.proficient" />
              Proficiency
            </label>
            <label class="checkbox-item">
              <input type="checkbox" v-model="skill.expertise" :disabled="!skill.proficient" />
              Expertise
            </label>
          </div>
        </div>
      </div>
    </fieldset>

    <!-- Companions -->
    <fieldset class="card">
      <legend class="section">🧍 Companions</legend>
      <p class="text-sm text-slate-300 mb-4">You currently have no custom NPCs created</p>
      <button type="button" class="btn-outline">+ Add an NPC</button>
    </fieldset>

    <!-- Actions -->
    <div class="pt-6 flex justify-between">
      <router-link :to="`/content/characters`" class="btn-outline">← Cancel</router-link>
      <button @click="saveCharacter" class="btn-primary" :disabled="!isValid">💾 Save</button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const touched = ref(false)
const campaigns = ref([])

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

const character = reactive({
  id: null,
  campaignId: null,
  playerName: '',
  characterName: '',
  controlPlayer: '',
  avatar: '',
  class: '',
  experience: 0,
  level: 1,
  hitPoints: 10,
  armorClass: 10,
  spellSaveDC: 10,
  speed: '',
  initiative: '',
  strength: 10,
  dexterity: 10,
  constitution: 10,
  intelligence: 10,
  wisdom: 10,
  charisma: 10,
  passivePerception: false,
  passiveInvestigation: false,
  passiveInsight: false,
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

const isValid = computed(() =>
    character.playerName.trim() &&
    character.characterName.trim() &&
    character.hitPoints > 0 &&
    character.armorClass > 0 &&
    character.class
)

function skillBonus(skill) {
  if (skill.expertise) return 4
  if (skill.proficient) return 2
  return 0
}

function handleAvatarUpload(e) {
  const file = e.target.files[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = e => {
    character.avatar = e.target.result
  }
  reader.readAsDataURL(file)
}

async function saveCharacter() {
  touched.value = true;
  if (!isValid.value) return;

  const payload = {
    playerName: character.playerName,
    characterName: character.characterName,
    classAndLevel: `${character.class} ${character.level}`,
    experience: character.experience,
    level: character.level,
    levelOverride: true,
    hitPoints: character.hitPoints,
    armorClass: character.armorClass,
    spellDC: character.spellSaveDC || 0,
    speed: parseInt(character.speed) || 0,
    initiative: parseInt(character.initiative) || 0,
    strength: character.strength,
    dexterity: character.dexterity,
    constitution: character.constitution,
    intelligence: character.intelligence,
    wisdom: character.wisdom,
    charisma: character.charisma,
    passivePerception: !!character.passivePerception,
    passiveInvestigation: !!character.passiveInvestigation,
    passiveInsight: !!character.passiveInsight,
    jackOfAllTrades: character.jackOfAllTrades,
    playerControlled: !!character.controlPlayer,
    damageResistances: character.resistances.split(",").map(s => s.trim()).filter(Boolean),
    damageVulnerabilities: character.vulnerabilities.split(",").map(s => s.trim()).filter(Boolean),
    damageImmunities: character.immunities.split(",").map(s => s.trim()).filter(Boolean),
    conditionImmunities: character.conditionImmunities.split(",").map(s => s.trim()).filter(Boolean),
    skillProficiencies: character.skills.filter(s => s.proficient).map(s => s.name),
    campaignId: character.campaignId || 1,
    notes: ""
  };

  try {
    const isNew = !character.id;
    const method = isNew ? "POST" : "PUT";
    const url = isNew ? "/api/characters" : `/api/characters/${character.id}`;

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error("Failed to save");

    router.push("/content/characters");
  } catch (e) {
    console.error(e);
    alert("Save failed");
  }
}


async function deleteCharacter() {
  if (!confirm(`Delete ${character.characterName}?`)) return

  try {
    const res = await fetch(`/api/characters/${character.id}`, { method: 'DELETE' })
    if (!res.ok) throw new Error('Delete failed')
    router.push('/content/characters')
  } catch (e) {
    console.error(e)
    alert('Delete failed')
  }
}

async function loadCharacter() {
  const id = route.params.id;  // Ensure the `id` is passed correctly from the URL
  if (!id) return;

  try {
    const res = await fetch(`/api/characters/${id}`);
    if (!res.ok) throw new Error("Character not found");
    const data = await res.json();

    Object.assign(character, {
      ...data,
      id: data.id,
      class: data.classAndLevel?.split(" ")[0] || "",
      level: parseInt(data.classAndLevel?.split(" ")[1]) || 1,
      resistances: data.damageResistances?.join(", ") || "",
      vulnerabilities: data.damageVulnerabilities?.join(", ") || "",
      immunities: data.damageImmunities?.join(", ") || "",
      conditionImmunities: data.conditionImmunities?.join(", ") || "",
      skills: character.skills.map(skill => ({
        ...skill,
        proficient: data.skillProficiencies?.includes(skill.name) || false,
        expertise: false
      }))
    });
  } catch (err) {
    console.error("Failed to load character", err);
  }
}


async function loadCampaigns() {
  try {
    const res = await fetch('/api/campaigns')
    campaigns.value = await res.json()
  } catch (err) {
    console.error('Could not load campaigns:', err)
  }
}

onMounted(async () => {
  await Promise.all([loadCampaigns(), loadCharacter()])
})
</script>



<style scoped>
.input {
  @apply w-full bg-slate-800 p-2 border border-slate-600 rounded text-white;
}
.label {
  @apply block text-sm mb-1 text-slate-400;
}
.section {
  @apply text-xl font-bold mb-2 text-slate-100;
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
.checkbox-item {
  @apply flex items-center gap-2 text-sm text-slate-300;
}
.input option {
  color: white;
  background-color: #1e293b; /* slate-800 */
}
</style>
