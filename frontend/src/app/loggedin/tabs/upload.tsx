import { CameraMode, CameraType, CameraView, useCameraPermissions, } from "expo-camera";
import * as ImagePicker from 'expo-image-picker';
import { useRef, useState } from "react";
import { Button, Pressable, StyleSheet, Text, View, Alert } from "react-native";
import { Image } from "expo-image";
import * as ImageManipulator from 'expo-image-manipulator';
import AntDesign from "@expo/vector-icons/AntDesign";
import Feather from "@expo/vector-icons/Feather";
import FontAwesome6 from "@expo/vector-icons/FontAwesome6";
import { fetchPreSignedUrl } from '../post/post';
import { useAuth } from "../../../context/AuthContext";

const API_URL = process.env.EXPO_PUBLIC_API_URL;


export default function ImageSelect() {
    const [image, setImage] = useState<string | null>(null);

    const[openList, setOpenList] = useState<boolean>(false);

    const pickImage = async () => {

        const permissionResult = await ImagePicker.requestMediaLibraryPermissionsAsync();

        if (!permissionResult.granted) {
            Alert.alert('Permission required', 'Permission to access the media library is required.');
            return;
        }

        let result = await ImagePicker.launchImageLibraryAsync({
            mediaTypes: ['images', 'videos'],
            allowsEditing: true,
            aspect: [16, 9],
            quality: 1,
        });

        console.log(result);

        if (!result.canceled) {
            setImage(result.assets[0].uri);
        }
    };

    const takePhoto = async () => {
        const permissionResult = await ImagePicker.requestCameraPermissionsAsync();

        if (!permissionResult.granted) {
            Alert.alert('Permission required', 'Permission to access the camera is required.');
            return;
        }

        let result = await ImagePicker.launchCameraAsync({
            allowsEditing: true,
            aspect: [4, 3],
            quality: 1,
        });

        console.log(result);

        if (!result.canceled) {
            setImage(result.assets[0].uri);
        }
    };

        const [url, setUrl] = useState<string | null>(null);
        const [error, setError] = useState("");
        const { user } = useAuth();

        const upload = async () => {
            try {
                setError("");

                const username = user?.username;
                if (!username) {
                    throw new Error("Username is unavailable");
                }
                const uri = await fetchPreSignedUrl(username);
                setUrl(uri);
                console.log(uri);
            } catch (err: any) {
                setError(err.message || "fetch url failed");
            } finally {
                console.log("beep",user?.username, url);
            }
        };

    return (
        <View style={imageSelect.container}>
            <Button title="Photo Library" onPress={pickImage} />
            <Button title="Take a photo" onPress={takePhoto} />
            <Button title="Upload" onPress={upload} />
            {image && <Image source={{ uri: image }} style={imageSelect.image} />}
        </View>
    );
}

const imageSelect = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    image: {
        width: 400,
        height: 400,
    },
});