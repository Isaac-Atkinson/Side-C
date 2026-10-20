export async function apiFetch(url: string, options : RequestInit = {}): Promise<Response> {

    const headers = new Headers(options.headers || {});

    const token = getCsrfToken()

    if (token) {
        headers.set("X-XSRF-TOKEN", token);
    }

        return fetch(url, {
            ...options,
            credentials: "include",
            headers
        });
}

function getCsrfToken() : string | null {
    const cookie = document.cookie
        .split("; ")
        .find(row => row.startsWith("XSRF-TOKEN="));

    return cookie
        ? decodeURIComponent(cookie.split("=")[1])
        : null;
}