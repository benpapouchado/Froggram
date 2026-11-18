
import { Column, Host } from "@expo/ui";
import { useState } from "react";
import { TextInput, Button, Text, StyleSheet } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { useAuth } from "@/context/AuthContext";

function LoginForm() {
    const { login } = useAuth();

    const API_URL = process.env.EXPO_PUBLIC_API_URL;

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleLogin = async () => {
        try {
            setLoading(true);
            setError("");

            await login(username, password);

        } catch (error: any) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (<SafeAreaView>
        <Host matchContents={{ vertical: true }} style={{ width: "100%" }}>
            <Column spacing={12}>
                <TextInput
                    placeholder="Username"
                    value={username}
                    onChangeText={setUsername}
                    autoCapitalize="none"
                    autoCorrect={false}
                    autoComplete="username"
                    accessibilityLabel="Username"
                />

                <TextInput
                    placeholder="Password"
                    value={password}
                    onChangeText={setPassword}
                    secureTextEntry
                    autoComplete="current-password"
                    accessibilityLabel="Password"
                />

                {/* {error ? <Text style={styles.error}>{error}</Text> : null} */}

                <Button
                    title={loading ? "Logging in..." : "Login"}
                    onPress={handleLogin}
                    disabled={loading}
                />
            </Column>
        </Host>
    </SafeAreaView>
    );
}
