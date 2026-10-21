import {API_BASE_URL} from "../config/api.ts";

export async function apiFetch(url: string, options : RequestInit = {}): Promise<Response> {

    const headers = new Headers(options.headers || {});

    const token = await getCsrfToken()

    if (token) {
        headers.set("X-XSRF-TOKEN", token);
    }

        return fetch(url, {
            ...options,
            credentials: "include",
            headers
        });
}

async function getCsrfToken() : Promise<string | null> {
    
    const response = await fetch(`${API_BASE_URL}/auth/csrf`, {
        credentials: "include",
    })

    if (!response.ok) {
        throw new Error("Could not find Csrftoken token");
    }

    const data = await response.json();
    return data.token;



}