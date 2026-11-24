const API_URL = process.env.EXPO_PUBLIC_API_URL;

export async function authenticate(username: string, password: string) {
    const response = await fetch(`${API_URL}/api/user/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            username: username,
            password_hash: password
        }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Login failed");
    }

    return data;
}

export async function revokeRefreshToken(refreshToken: string) {
    const response = await fetch(`${API_URL}/api/user/logout`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            refreshToken: refreshToken
        }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Logout failed");
    }

    return data;
}

export async function rotateToken(refreshToken: string) {
    const response = await fetch(`${API_URL}/token/refresh`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            refreshToken: refreshToken
        }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Rotation failed");
    }

    return data;
}

export async function register(
    username: string,
    password: string,
    bio: string,
    dateOfBirth: Date
) {
    const formattedDate =
        `${dateOfBirth.getFullYear()}-${String(dateOfBirth.getMonth() + 1).padStart(2, '0')}-${String(dateOfBirth.getDate()).padStart(2, '0')}`;

        console.log(password);

    const response = await fetch(`${API_URL}/api/user/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            username: username,
            password_hash: password,
            bio: bio,
            dateOfBirth: formattedDate,
        }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Account creation failed");
    }

    return data;
}