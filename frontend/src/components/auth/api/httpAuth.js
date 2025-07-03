// src/api/httpAuth.js
export async function authFetch(url, options = {}) {
    const creds = localStorage.getItem('basicCreds');
    if (!creds) throw new Error('Not authenticated');

    const headers = {
        ...options.headers,
        'Authorization': `Basic ${creds}`
    };

    const response = await fetch(url, { ...options, headers });
    if (!response.ok) {
        // Optional: handle 401 by clearing creds or redirecting back to login
        throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    // Assume JSON response for data endpoints
    return response.json();
}