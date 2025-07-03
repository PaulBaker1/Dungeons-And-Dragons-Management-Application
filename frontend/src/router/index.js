// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { defineStore } from 'pinia'
import axios from 'axios'

// Campaigns
import CampaignList from '../components/campaigns/CampaignList.vue'
import CampaignDetails from '@/components/campaigns/CampaignDetails.vue'
import CampaignCreate from '@/components/campaigns/CampaignCreate.vue'
import CampaignNotesEdit from "@/components/campaigns/CampaignNotesEdit.vue"

// Sessions
import SessionList from '@/components/sessions/SessionList.vue'
import SessionCreate from '@/views/SessionCreate.vue'
import SessionEdit from '@/views/SessionEdit.vue'
import SessionView from '@/views/SessionView.vue'

// Player Logs
import PlayerLogList from '@/views/PlayerLogList.vue'
import PlayerLogCreate from '@/views/PlayerLogCreate.vue'
import PlayerLogEdit from '@/views/PlayerLogEdit.vue'

// Characters
import CharacterList from '@/components/characters/CharacterList.vue'
import CharacterCreate from '@/components/characters/CharacterCreate.vue'
import CharacterEdit from '@/components/characters/CharacterEdit.vue'
import CharacterView from '@/views/CharacterView.vue'

// Quest
// import QuestView from "@/views/QuestView.vue"
// import QuestList from '@/components/quest/QuestList.vue'
// import QuestEdit from '@/components/quest/QuestEdit.vue'

import QuestList from '@/components/quest/QuestList.vue';
import QuestForm from '@/components/quest/QuestForm.vue';

// Dice
import DiceRollerPage from '@/pages/DiceRollerPage.vue'
import PrepItemManager from "@/views/PrepItemManager.vue"
import DebugClass from "@/pages/dndapi/DebugClass.vue";
import SimpleClassDetail from "@/components/dndapi/class/SimpleClassDetail.vue";
import ClassDetailPage from "@/components/dndapi/class/ClassDetailPage.vue";
import BoardPage from "@/pages/boards/BoardPage.vue";
import MapsGallery from "@/components/board/MapsGallery.vue";
import MapsPaints from "@/components/board/MapsPaints.vue";


import PDFView from '@/components/pdfview/PdfViewer.vue'; // Markdown reader component
import PDFUpload from '@/components/pdfview/MdStorage.vue'; // File upload component
import PDFList from '@/components/pdfview/FileList.vue';
import Login from "@/components/auth/Login.vue";
import Home from "@/views/Home.vue";
import ErrorPage from "@/components/ErrorPage.vue";
import MainPage from "@/components/MainPage.vue"; // File list component

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,    // { id, username, role, characterId, … }
        token: '',     // basic creds or JWT
    }),
    actions: {
        async login(username, password) {
            // 1) send basic creds to backend
            const creds = btoa(`${username}:${password}`)
            axios.defaults.headers.common.Authorization = `Basic ${creds}`

            // 2) hit your “/api/me” endpoint to get user + role
            const { data } = await axios.get('/api/me')
            //    data = { id, username, role: "dm"|"user", characterId: 42, … }

            // 3) store everything
            this.token = creds
            this.user  = data
            localStorage.setItem('basicCreds', creds)
        },

        logout() {
            this.user = null
            this.token = ''
            delete axios.defaults.headers.common.Authorization
            localStorage.removeItem('basicCreds')
        },

        initializeFromStorage() {
            const creds = localStorage.getItem('basicCreds')
            if (!creds) return
            axios.defaults.headers.common.Authorization = `Basic ${creds}`
            // optional: re‑fetch /api/me to restore user state
        }
    }
})

const routes = [
    { path: '/', redirect: '/mainpage', component: MainPage },
    { path: '/mainpage', component: MainPage },

    // Campaigns
    { path: '/campaigns', component: CampaignList },
    { path: '/campaigns/create', component: CampaignCreate, meta: { roles: ['dm'] } },
    { path: '/campaigns/:id', name: 'CampaignDetails', component: CampaignDetails },

    // Sessions
    { path: '/campaigns/:campaignId/sessions/create', name: 'SessionCreate', component: SessionCreate, meta: { roles: ['dm'] } },
    { path: '/campaigns/:campaignId/sessions/:id/edit', name: 'SessionEdit', component: SessionEdit, meta: { roles: ['dm'] } },
    { path: '/campaigns/:campaignId/sessions/:id/view', name: 'SessionView', component: SessionView },

    // Player Logs
    { path: '/campaigns/:campaignId/playerlogs', name: 'PlayerLogsByCampaign', component: PlayerLogList },
    { path: '/sessions/:sessionId/logs', name: 'PlayerLogsBySession', component: PlayerLogList },
    { path: '/sessions/:sessionId/logs/create', name: 'PlayerLogCreate', component: PlayerLogCreate },
    { path: '/playerlogs/:id/edit', name: 'PlayerLogEdit', component: PlayerLogEdit },

    // Characters
    { path: '/characters', name: 'CharacterList', component: CharacterList },
    { path: '/characters/create', name: 'CharacterCreate', component: CharacterCreate, meta: { roles: ['dm'] } },
    { path: '/characters/:id/view', name: 'CharacterView', component: CharacterView, props: true },
    { path: '/characters/:id', name: 'CharacterEdit', component: CharacterEdit, meta: { roles: ['dm'] }, props: true },

    // Redirect legacy path
    { path: '/content/characters', redirect: '/characters' },
    { path: '/character-test/:id', component: () => import('@/views/CharacterTest.vue') },

    // Dice roller
    { path: '/dice', name: 'DiceRoller', component: DiceRollerPage },

    // PrepTime
    { path: '/prepitems', name: 'PrepItems', component: PrepItemManager },

    // Quests
    { path: '/campaigns/:campaignId/quests', component: QuestList },
    { path: '/campaigns/:campaignId/quests/create', component: QuestForm, meta: { roles: ['dm'] } },  // Create a new quest
    { path: '/campaigns/:campaignId/quests/:id/edit', component: QuestForm, meta: { roles: ['dm'] } },  // Edit a specific quest

    // { path: '/quest-tracker', name: 'QuestTracker', component: QuestTracker },
    {
        path: '/classes',
        name: 'ClassesPage',
        component: () => import('@/pages/dndapi/ClassesPage.vue')
    },
    {
        path: '/classes/:index',
        name: 'ClassDetailPage',
        component: () => import('@/pages/dndapi/ClassDetailPage.vue')
    },
    { path: '/debug', name: 'DebugClass', component: DebugClass },
    {
        path: '/simple-class-detail',
        name: 'SimpleClassDetail',
        component: SimpleClassDetail
    },
    { path: '/classes/:index', name: 'ClassDetailPage', component: ClassDetailPage },
    // router/index.js or router.ts
    {
        path: '/spells',
        name: 'AllSpells',
        component: () => import('@/components/dndapi/spell/AllSpellsPage.vue')
    },
    {
        path: '/spells/class/:index',
        name: 'SpellsByClass',
        component: () => import('@/components/dndapi/spell/SpellsByClassPage.vue')
    },
    {
        path: '/spells/:index',
        name: 'SpellDetail',
        component: () => import('@/components/dndapi/spell/SpellDetail.vue')
    },
    {
        path: '/races/:index',
        name: 'RaceDetail',
        component: () => import('@/components/dndapi/race/RaceDetail.vue')
    },
    {
        path: '/races',
        name: 'RaceList',
        component: () => import('@/components/dndapi/race/RaceList.vue')
    },
    {
        path: '/monsters/:index',
        name: 'MonsterDetail',
        component: () => import('@/components/dndapi/monsters/MonsterDetail.vue')
    },
    {
        path: '/monsters',
        name: 'MonsterList',
        component: () => import('@/components/dndapi/monsters/MonsterList.vue')
    },
    {
        path: '/equipment/:index',
        name: 'EquipmentDetail',
        component: () => import('@/components/dndapi/equipment/EquipmentDetail.vue')
    },
    {
        path: '/equipment',
        name: 'EquipmentList',
        component: () => import('@/components/dndapi/equipment/EquipmentList.vue')
    },
    {
        path: '/board-paint', // The route for the board page
        name: 'BoardPage',
        component: BoardPage
    },
    {
        path: '/maps-gallery',
        name: 'MapsGallery',
        component: MapsGallery
    },
    {
        path: '/map-paint',
        name: 'MapsPaint',
        component: MapsPaints

    },
    {
      path: '/remainders',
      name: 'Remainders',
        component: () => import('@/components/reminders/ReminderPage.vue')
    },
    {
        path: '/pdf-view', // Route for viewing specific file
        name: 'PDFView',
        component: PDFView,
        props: true, // Pass `filename` as a prop
    },
    {
        path: '/pdf-upload', // Route for uploading files
        name: 'PDFUpload',
        component: PDFUpload, meta: { roles: ['dm'] },
    },
    {
        path: '/pdf-list', // Route for listing files
        name: 'PDFList',
        component: PDFList,
    },
    {
        path: '/image-gallery',
        name: 'ImageGallery',
        component: () => import('@/components/images/Gallery.vue')
    },
    {
        path: '/midi-player',
        name: 'Midi-Player',
        component: () => import('@/components/midi/Mp3Player.vue')
    },
    { path: '/login',     name: 'Login',     component: Login },
    {
        path: '/campaigns/:id/notes/:type(private|public)',
        component: CampaignNotesEdit
    },
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/:pathMatch(.*)*', name: 'NotFound', component: ErrorPage
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router