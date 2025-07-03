import { createStore } from 'vuex';  // Import Vuex
import boardStore from './boardStore'; // Import the boardStore.js as a module

export default createStore({
    modules: {
        board: boardStore  // Register boardStore as a module with the key 'board'
    }
});
