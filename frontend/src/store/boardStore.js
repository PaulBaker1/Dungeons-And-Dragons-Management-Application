// src/stores/boardStore.js
import { defineStore } from 'pinia';

export const useBoardStore = defineStore('board', {
    state: () => ({
        mapImage: '', // To store the map image
        tokens: [
            { x: 50, y: 100, image: 'path/to/token1.png', name: 'Token 1' },
            { x: 150, y: 200, image: 'path/to/token2.png', name: 'Token 2' },
        ],
    }),
    actions: {
        setMapImage(image) {
            this.mapImage = image;
        },
        moveToken(token, x, y) {
            token.x = x;
            token.y = y;
        },
        addToken(token) {
            this.tokens.push(token);
        },
    },
});
