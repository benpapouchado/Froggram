const API_URL = process.env.EXPO_PUBLIC_API_URL;

export async function fetchPreSignedUrl(username: string) {

    const response = await fetch(`${API_URL}/photo/generate-pre-signed-url`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            username: username
        }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "fetch failed");
    }

    console.log("Plop", data);
    return data;
}

// export async function postPhotoToS3(url: string){

// }