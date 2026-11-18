
import React, {
    createContext,
    useContext,
    useEffect,
    useState,
    ReactNode,
} from "react";

import * as SecureStore from "expo-secure-store";

import { authenticate } from "@/auth/sign-in-service";

// User interface
export interface User {
    id: string;
    username: string;
}

// Login response interface
interface LoginResponse {
    token: string;
    user: User;
}

// Auth context interface
interface AuthContextType {
    user: User | null;
    token: string | null;
    loading: boolean;
    isAuthenticated: boolean;
    login: (email: string, password: string) => Promise<void>;
    logout: () => Promise<void>;
}

// Context
const AuthContext = createContext<AuthContextType | undefined>(
    undefined
);

// Provider
interface AuthProviderProps { children: ReactNode; }

const API_URL = process.env.EXPO_PUBLIC_API_URL;

export const AuthProvider = ({ children, }: AuthProviderProps) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    // Restore session
    useEffect(() => {
        const restoreSession = async () => {
            try {
                const savedToken = await SecureStore.getItemAsync("token");
                const savedUser = await SecureStore.getItemAsync("user");

                if (savedToken && savedUser) {
                    setToken(savedToken);
                    setUser(JSON.parse(savedUser) as User);
                }
            } catch (error) {
                console.error("Restore session error:", error);
            } finally {
                setLoading(false);
            }
        };

        restoreSession();
    }, []);

    const login = async (username: string, password: string) => {
        const data = await authenticate(username, password);

        await SecureStore.setItemAsync("token", data.token);
        await SecureStore.setItemAsync(
            "user",
            JSON.stringify(data.user)
        );

        setToken(data.token);
        setUser(data.user);
    };

    // Logout
    const logout = async (): Promise<void> => {
        await SecureStore.deleteItemAsync("token");
        await SecureStore.deleteItemAsync("user");

        setToken(null);
        setUser(null);
    };

    const isAuthenticated = !!token && !!user;

    return (
        <AuthContext.Provider
            value={{
                user,
                token,
                loading,
                isAuthenticated,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

// Custom hook
export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error(
            "useAuth must be used within AuthProvider"
        );
    }

    return context;
};
