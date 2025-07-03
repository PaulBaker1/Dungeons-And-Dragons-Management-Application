import axios from 'axios';

// If you set VITE_DND_API in .env this will use it;
// otherwise it falls back to the official public endpoint.
const BASE_URL = import.meta.env.VITE_DND_API || 'https://www.dnd5eapi.co/api'

/** Fetch the list of classes */
export async function fetchAllClasses() {
    const res = await fetch(`${BASE_URL}/classes`)
    if (!res.ok) throw new Error(`Failed to load classes: ${res.statusText}`)
    const data = await res.json()
    return data.results   // array of { index, name, ... }
}

/** Fetch the full class detail by its index */
export async function fetchClass(index) {
    const res = await fetch(`${BASE_URL}/classes/${index}`)
    if (!res.ok) throw new Error(`Failed to load class ${index}: ${res.statusText}`)
    return await res.json()
}

/** Fetch the list of all equipment */
export async function fetchAllEquipment() {
    const res = await fetch(`${BASE_URL}/equipment`)
    if (!res.ok) {
        throw new Error(`Failed to load equipment: ${res.statusText}`)
    }
    const data = await res.json()
    // The API returns { count, results: [ { index, name, ...}, ... ] }
    return data
}
