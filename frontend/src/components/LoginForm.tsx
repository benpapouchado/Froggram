import { Button, Column, Host, TextInput, useNativeState } from '@expo/ui';
import type { Dispatch, SetStateAction } from 'react';

type LoginFormProps = {
    username: string,
    setUsername: Dispatch<SetStateAction<string>>,
    password: string,
    setPassword: Dispatch<SetStateAction<string>>,
}

export default function LoginForm() {

    const API_URL = "http://172.20.10.2:8080";
    const username = useNativeState("");
    const password = useNativeState("");

    const login = async () => {
        console.log(username.value, password.value);
        try {
            const response = await fetch(`${API_URL}/api/user/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username.value,
                    password_hash: password.value,
                }),
            });

            

            if (!response.ok) {
                if (response.status === 401) {
                    console.log('Invalid username or password');
                    return;
                }

                throw new Error(`Login failed: ${response.status}`);
            }

            const data = await response.json();

            console.log('Login successful:', data);

            // data.userId
            // data.username
            // data.token

        } catch (error) {
            console.error('Login error:', error);
            console.log(username.value, password.value);

        }
    };

    return (
        <Host matchContents={{ vertical: true }} style={{ width: '100%' }}>
            <Column spacing={12}>
                <TextInput
                    placeholder="Username"
                    value={username}
                    accessibilityLabel="Username"
                    autoCapitalize="none"
                    autoCorrect={false}
                    autoComplete="username"
                />
                <TextInput
                    placeholder="Password"
                    value={password}
                    accessibilityLabel="Password"
                    // secureTextEntry
                    autoComplete="password"
                />
                <Button
                    label={"Submit"}
                    onPress={login} />
            </Column>
        </Host>
    );
}