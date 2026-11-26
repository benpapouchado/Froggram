
import { SafeAreaView } from "react-native-safe-area-context";
import { View, Text, Button, StyleSheet, Image } from "react-native";
export default function Profile() {
    return (
        <SafeAreaView style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
    <Image
      source={{
        uri: 'https://frogggram-demo.s3.eu-west-1.amazonaws.com/welcome-frog.JPG',
      }}
      style={{
        width: 50,
        height: 50,
      }}
      resizeMode="cover"
    />
        </SafeAreaView>
    )
}


const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    tinyLogo: {
        width: 50,
        height: 50,
    },
    logo: {
        width: 66,
        height: 58,
    },
});