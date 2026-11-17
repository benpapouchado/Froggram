import * as Device from 'expo-device';
import { Platform, StyleSheet, TextInput, Button } from 'react-native';
import { useEffect, useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';

import { AnimatedIcon } from '@/components/animated-icon';
import { HintRow } from '@/components/hint-row';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { BottomTabInset, MaxContentWidth, Spacing } from '@/constants/theme';
import LoginForm from '@/components/LoginForm';

export default function HomeScreen() {

  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const API_URL = "http://172.20.10.2:8080";


  const testConnection = async () => {
    try {
      const response = await fetch(
        `${API_URL}/api/user/hello`
      );

      const text = await response.text();

      console.log("Status:", response.status);
      console.log("Response:", text);

    } catch (error) {
      console.error("Connection failed:", error);
    }
  };

  return (
    <ThemedView style={styles.container}>
      <SafeAreaView style={styles.safeArea}>

        <Button
          title={"Submit"}
          onPress={testConnection} />


        <ThemedView style={styles.heroSection}>
          
          {/* <AnimatedIcon /> */}
          <ThemedText type="title" style={styles.title}>
            Froggram
          </ThemedText>
        </ThemedView>

        <ThemedText type="code" style={styles.code}>
          Get started
        </ThemedText>

        <LoginForm />
        {/* 
        <ThemedView type="backgroundElement" style={styles.stepContainer}>
          <HintRow
            title="Try editing"
            hint={<ThemedText type="code">src/app/index.tsx</ThemedText>}
          />
          <HintRow
            title="Fresh start"
            hint={<ThemedText type="code">npm run reset-project</ThemedText>}
          />
        </ThemedView> */}

      </SafeAreaView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    flexDirection: 'row',
  },
  safeArea: {
    flex: 1,
    paddingHorizontal: Spacing.four,
    alignItems: 'center',
    gap: Spacing.three,
    paddingBottom: BottomTabInset + Spacing.three,
    maxWidth: MaxContentWidth,
  },
  heroSection: {
    alignItems: 'center',
    justifyContent: 'center',
    flex: 1,
    paddingHorizontal: Spacing.four,
    gap: Spacing.four,
  },
  title: {
    textAlign: 'center',
  },
  code: {
    textTransform: 'uppercase',
  },
  stepContainer: {
    gap: Spacing.three,
    alignSelf: 'stretch',
    paddingHorizontal: Spacing.three,
    paddingVertical: Spacing.four,
    borderRadius: Spacing.four,
  },
});
