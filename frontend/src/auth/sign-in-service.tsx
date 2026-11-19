const API_URL = process.env.EXPO_PUBLIC_API_URL;

export async function authenticate(username: string, password: string) {
    const response = await fetch(`${API_URL}/api/user/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ 
            username: username, 
            password_hash: password }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Login failed");
    }

    return data;
}

export async function revokeRefreshToken(username: string) {
    const response = await fetch(`${API_URL}/api/user/logout`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ 
            username: username}),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Logout failed");
    }

    return data;
}

// {"user":{"userId":"892e701c-dd1f-46ac-9302-31582771666c","username":"hamster"},
// "accessToken":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4OTJlNzAxYy1kZDFmLTQ2YWMtOTMwMi0zMTU4Mjc3MTY2NmMiLCJpYXQiOjE3OTUxMzEyNjksImV4cCI6MTc5NTEzNDg2OSwiaXNzIjoiZnJvZ2dyYW0ifQ.jfQvVaEbnxqbccyyglJ8V8C-2qLhG8gEEaMRJarqbYo",
// "refreshToken":"8kzkFTESP1I49SIr1zqHOQpLN0xcHvELqbiT0EHcBGSm0R4Kj44tWPHgtXtlb1ECOKpkiXLvCFHx3exeh94Cmg"}