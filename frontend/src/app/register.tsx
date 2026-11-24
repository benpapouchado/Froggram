
import { useState } from "react";
import {
    View,
    TextInput,
    Button,
    Text,
    StyleSheet,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Stack, useRouter } from 'expo-router';
import { register } from "../auth/sign-in-service";
import CustomDatePicker from './Date/CustomDatePicker';


export default function Register() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [bio, setBio] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState<Date>(new Date());
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const router = useRouter();


    const handleRegister = async () => {
        try {
            setError("");

            await register(username, password, bio, dateOfBirth);
        } catch (err: any) {
            setError(err.message || "Login failed");
        } finally {
            setLoading(false);
            // router.navigate('../loggedin/profile');
        }
    };

    return (
        <SafeAreaView style={styles.container}>
            <View style={styles.form}>
                <Text style={styles.title}>Register</Text>

                <TextInput
                    style={styles.input}
                    placeholder="Username"
                    value={username}
                    onChangeText={setUsername}
                    autoCapitalize="none"
                    autoCorrect={false}
                />

                <TextInput
                    style={styles.input}
                    placeholder="Password"
                    value={password}
                    onChangeText={setPassword}
                    secureTextEntry
                />

                <TextInput
                    style={styles.input}
                    placeholder="Bio. Can be changed"
                    value={bio}
                    onChangeText={setBio}
                />

                <CustomDatePicker
                    date={dateOfBirth}
                    onDateChange={(selectedDate) => {
                        setDateOfBirth(selectedDate);
                    }}
                />
                {error ? (
                    <Text style={styles.error}>{error}</Text>
                ) : null}

                <Button
                    title="Create Account"
                    onPress={handleRegister}
                    disabled={loading}
                />
            </View>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: "center",
        paddingHorizontal: 20,
    },
    form: {
        gap: 15,
    },
    title: {
        fontSize: 28,
        fontWeight: "bold",
        textAlign: "center",
    },
    input: {
        borderWidth: 1,
        borderColor: "#ccc",
        borderRadius: 10,
        padding: 14,
        fontSize: 16,
    },
    error: {
        color: "red",
    },
});
