const API_URL = process.env.EXPO_PUBLIC_API_URL;

export async function authenticate(username: string, password: string) {
    const response = await fetch(`${API_URL}/api/user/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Login failed");
    }

    return data;
}