// src/store/sidebarStore.js
import { defineStore } from 'pinia'

export const useSidebarStore = defineStore('sidebar', {
  state: () => ({
    sideCollapsed: false,
    smallScreen: false,
    user: { admin: true, contribute: ['character-builder'] }, // temp mock
    userInfo: {
      contribute: ['character-builder'],
      admin: true
    }
  }),
  actions: {
    toggleSideCollapsed() {
      this.sideCollapsed = !this.sideCollapsed
    },
    setSideCollapsed(value) {
      this.sideCollapsed = value
    },
    setSideSmallScreen(value) {
      this.smallScreen = value
    }
  },
  getters: {
    isCollapsed: (state) => state.sideCollapsed
  }
})
