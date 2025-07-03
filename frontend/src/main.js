// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'


// Quasar
import { Quasar } from 'quasar'
import 'quasar/dist/quasar.css'
import '@quasar/extras/material-icons/material-icons.css'

// PrimeVue
import PrimeVue from 'primevue/config'
import 'primevue/resources/themes/arya-blue/theme.css'; // 🎯 Best fit
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';


// Components (only those used)
import Button from 'primevue/button'
import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Textarea from 'primevue/textarea'
import Dropdown from 'primevue/dropdown'

import 'primevue/resources/themes/aura-dark-blue/theme.css'
import 'primeicons/primeicons.css'

import Vue3BeautifulChat from 'vue3-beautiful-chat'    // ← the plugin

// App styles
import './assets/style.css'
import './assets/tailwind.css'
import Sidebar from "primevue/sidebar";
import axios from "axios";


// Basic Auth interceptor
axios.interceptors.request.use(cfg => {
    const creds = localStorage.getItem('basicCreds')
    if (creds) cfg.headers.Authorization = `Basic ${creds}`
    return cfg
})

const app = createApp(App)

app.use(createPinia())
app.use(router)

// ✅ PrimeVue must be registered before usage
app.use(PrimeVue, {
    ripple: true,
    inputStyle: 'outlined'
})

// Register
app.component('Button', Button)
app.component('Card', Card)
app.component('InputText', InputText)
app.component('Textarea', Textarea)
app.component('Dropdown', Dropdown)
app.component('Sidebar', Sidebar)


// ✅ Then apply Quasar
app.use(Quasar, {
    config: {},
    plugins: {},
    iconSet: 'material-icons'
})

app.use(Vue3BeautifulChat) // ✅ REGISTER PLUGIN HERE

app.mount('#app')
