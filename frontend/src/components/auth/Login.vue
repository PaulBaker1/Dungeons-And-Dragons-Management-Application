<template>
  <div class="login-demo">
    <h2>Login</h2>
    <input v-model="username" placeholder="Username" />
    <input v-model="password" type="password" placeholder="Password" />
    <button @click="login">Login</button>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  data: () => ({ username: '', password: '', error: '' }),
  methods: {
    async login() {
      const creds = btoa(`${this.username}:${this.password}`)
      const res = await fetch('/api/hello', { headers: { Authorization: `Basic ${creds}` } })
      if (res.ok) {
        localStorage.setItem('basicCreds', creds)
        axios.defaults.headers.common.Authorization = `Basic ${creds}`
        this.$router.push('/')
      } else {
        this.error = 'Invalid credentials'
      }
    }
  }
}
</script>

<style scoped>
.login-demo {
  max-width: 320px;
  margin: 2em auto;
  padding: 1em;
  border: 1px solid #ccc;
  border-radius: 4px;
}
input, button {
  width: 100%;
  padding: 0.5em;
  margin: 0.5em 0;
}
.error {
  color: red;
  font-size: 0.9em;
}
</style>