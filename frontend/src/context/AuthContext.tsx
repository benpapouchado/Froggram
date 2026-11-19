
import React, {
    createContext,
    useContext,
    useEffect,
    useState,
    ReactNode,
} from "react";

import * as SecureStore from "expo-secure-store";

import { authenticate } from "@/auth/sign-in-service";

export interface User {
    userId: string;
    username: string;
}

interface LoginResponse {
    user: User;
    accessToken: string;
    refreshToken: string;
}

interface AuthContextType {
    user: User | null;
    accessToken: string | null;
    refreshToken: string | null;
    loading: boolean;
    isAuthenticated: boolean;
    login: (email: string, password: string) => Promise<void>;
    logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(
    undefined
);

interface AuthProviderProps { children: ReactNode; }

export const AuthProvider = ({ children, }: AuthProviderProps) => {
    const [user, setUser] = useState<User | null>(null);
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [refreshToken, setRefreshToken] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const restoreSession = async () => {
            try {
                const savedAccessToken = await SecureStore.getItemAsync("accessToken");
                const savedRefreshToken = await SecureStore.getItemAsync("refreshToken");
                const savedUser = await SecureStore.getItemAsync("user");

                if (savedAccessToken && savedRefreshToken && savedUser) {
                    setAccessToken(savedAccessToken);
                    setRefreshToken(savedRefreshToken);
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

        await SecureStore.setItemAsync("refreshToken", data.refreshToken);
        await SecureStore.setItemAsync("accessToken", data.accessToken);
        await SecureStore.setItemAsync(
            "user",
            JSON.stringify(data.user)
        );

        setAccessToken(data.accessToken);
        setRefreshToken(data.refreshToken);
        setUser(data.user);
    };

    const logout = async (): Promise<void> => {
        await SecureStore.deleteItemAsync("accessToken");
        await SecureStore.deleteItemAsync("refreshToken");        
        await SecureStore.deleteItemAsync("user");

        setAccessToken(null);
        setRefreshToken(null);
        setUser(null);
    };

    const isAuthenticated = !!accessToken && !!refreshToken && !!user;

    return (
        <AuthContext.Provider
            value={{
                user,
                accessToken,
                refreshToken,
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

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error(
            "useAuth must be used within AuthProvider"
        );
    }

    return context;
};
