import { useAuth } from '../context/AuthContext';
import { Stack, useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Animated, Button, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
export function Profile() {
    const { user } = useAuth();

    return (
        <Stack.Screen>
            <SafeAreaView>
                <View>
                    {user?.username}
                </View>
            </SafeAreaView>
        </Stack.Screen>
    )

}