<template>
  <div class="dice-roller-page">
    <h1>🎲 Dice Roller</h1>

    <div class="dice-controls">
      <div
          v-for="(entry, idx) in diceInputs"
          :key="idx"
          class="dice-row"
      >
        <input
            type="number"
            min="1"
            v-model.number="entry.count"
            placeholder="#"
        />
        <select v-model="entry.type">
          <option disabled value="">Select Die</option>
          <option v-for="d in diceTypes" :key="d" :value="d">d{{ d }}</option>
        </select>
        <input
            type="number"
            v-model.number="entry.modifier"
            placeholder="Modifier"
        />
        <button @click="removeDice(idx)">❌</button>
      </div>

      <div class="action-buttons">
        <button @click="addDice">➕ Add Dice</button>
        <button @click="rollAllDice">🎯 Roll Dice</button>
        <button @click="clearHistory" class="clear">🧹 Clear Log</button>
      </div>
    </div>

    <!-- Dice Log -->
    <div class="dice-log" v-if="rollHistory.length">
      <h2>🧾 Dice Roll Log</h2>
      <ul>
        <li v-for="(roll, index) in rollHistory" :key="index">
          <div class="log-row">
            <span>{{ roll.expression }} = {{ roll.output }} ({{ roll.total }})</span>
            <div class="log-actions">
              <small class="timestamp">{{ roll.timestamp }}</small>
              <button @click="copyResult(roll.total)">📋</button>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>

  <!-- Explanation Section Split in Two -->
  <div class="explanation-split">
    <div class="explanation-box">
      <h3>Dice Types</h3>
      <p>Common RPG dice and their uses:</p>
      <ul>
        <li><code>d4</code> – 4-sided die</li>
        <li><code>d6</code> – 6-sided die (standard cube)</li>
        <li><code>d8</code> – 8-sided die</li>
        <li><code>d10</code> – 10-sided die</li>
        <li><code>d12</code> – 12-sided die</li>
        <li><code>d20</code> – 20-sided die (most common in D&D)</li>
        <li><code>d100</code> – 100-sided die (percentile rolls)</li>
      </ul>
    </div>

    <div class="explanation-box">
      <h3>Dice Notation</h3>
      <p>Dice expressions follow this format: <code>XdY + Z</code></p>
      <ul>
        <li><strong>X</strong> = number of dice</li>
        <li><strong>dY</strong> = die type</li>
        <li><strong>Z</strong> = modifier (can be positive or negative)</li>
      </ul>

      <h3>Examples</h3>
      <ul>
        <li><code>2d6</code> – roll two 6-sided dice</li>
        <li><code>1d20 + 5</code> – roll one d20 and add 5</li>
        <li><code>4d8 - 2</code> – roll four d8s and subtract 2</li>
      </ul>

      <h3>Tips</h3>
      <p>
        Combine different dice and modifiers like <code>2d6 + 1d4 - 1</code> to represent attack or damage rolls.
      </p>
    </div>
  </div>

</template>

<script setup>
import { ref } from 'vue'
import { DiceRoller } from '@dice-roller/rpg-dice-roller'

const diceTypes = [4, 6, 8, 10, 12, 20, 100]

const diceInputs = ref([
  { count: 1, type: 20, modifier: 0 }
])

const rollHistory = ref([])

function addDice() {
  diceInputs.value.push({ count: 1, type: '', modifier: 0 })
}

function removeDice(index) {
  diceInputs.value.splice(index, 1)
}

function rollAllDice() {
  const roller = new DiceRoller()

  for (const input of diceInputs.value) {
    if (!input.type || input.count < 1) continue

    const baseExpr = `${input.count}d${input.type}`
    const modifier = input.modifier ? (input.modifier > 0 ? `+${input.modifier}` : input.modifier) : ''
    const fullExpr = `${baseExpr}${modifier}`

    try {
      const roll = roller.roll(fullExpr)
      const timestamp = new Date().toLocaleTimeString()

      rollHistory.value.unshift({
        expression: fullExpr,
        output: roll.output,
        total: roll.total,
        timestamp
      })
    } catch (err) {
      console.error(`Invalid expression: ${fullExpr}`, err)
      alert(`Invalid roll: ${fullExpr}`)
    }
  }
}

function clearHistory() {
  if (confirm('Clear the dice log?')) {
    rollHistory.value = []
  }
}

function copyResult(value) {
  navigator.clipboard.writeText(String(value)).then(() => {
    alert(`Copied: ${value}`)
  }).catch(() => {
    alert('Failed to copy! 😢')
  })
}
</script>

<style scoped>
.dice-roller-page {
  max-width: 700px;
  margin: 2rem auto;
  padding: 2rem;
  background: #1e1e2f;
  border-radius: 10px;
  color: #f8fafc;
  font-family: 'Segoe UI', sans-serif;
  box-shadow: 0 0 20px rgba(0,0,0,0.4);
}

h1 {
  text-align: center;
  font-size: 2rem;
  color: #f87171;
  margin-bottom: 1.5rem;
}

h2 {
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
}

.dice-controls {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.dice-row {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.dice-row input,
.dice-row select {
  padding: 0.4rem;
  font-size: 1rem;
  border-radius: 4px;
  border: 1px solid #475569;
  background: #334155;
  color: white;
}

.dice-row input[type="number"] {
  width: 60px;
}

.dice-row button {
  background: transparent;
  border: none;
  font-size: 1.1rem;
  color: #f87171;
  cursor: pointer;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
}

.action-buttons button {
  flex: 1;
  padding: 0.6rem;
  font-size: 1rem;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  background-color: #10b981;
  color: white;
  transition: background 0.2s;
}

.action-buttons button:hover {
  background-color: #059669;
}

.action-buttons .clear {
  background-color: #f87171;
}
.action-buttons .clear:hover {
  background-color: #ef4444;
}

.dice-log {
  margin-top: 2rem;
  padding: 1rem;
  background: #0f172a;
  border-radius: 8px;
  box-shadow: 0 0 0 1px #334155;
}

.dice-log ul {
  list-style: none;
  padding: 0;
}

.log-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid #334155;
}

.log-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.timestamp {
  font-size: 0.75rem;
  color: #94a3b8;
}

.copy-btn {
  background: none;
  border: none;
  color: #38bdf8;
  cursor: pointer;
  font-size: 1rem;
}

.explanation {
  margin-top: 3rem;
  padding: 2rem;
  background-color: #0f172a;
  border-radius: 8px;
  box-shadow: 0 0 0 1px #334155;
}

.explanation-split {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  margin-top: 3rem;
  padding: 1.5rem 0;
}

.explanation-box {
  flex: 1 1 300px;
  background-color: #0f172a;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 0 0 1px #334155;
}

.explanation-box h3 {
  margin-bottom: 0.5rem;
}

.explanation-box p {
  margin-bottom: 0.5rem;
  color: #cbd5e1;
}

.explanation-box ul {
  list-style: disc;
  padding-left: 1.5rem;
  margin-bottom: 1rem;
  color: #cbd5e1;
}

.explanation-box code {
  background-color: #1e293b;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  font-family: monospace;
  color: #93c5fd;
}


</style>
