import {createContext, type ReactNode, useContext, useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import {apiFetch} from "../api/ApiFetch.ts";

type UserContextType = {
    username : string | null;
    loading: boolean;
    getUser: () => Promise<void>;
    logout: () => Promise<void>;
}

const UserContext = createContext<UserContextType | null>(null);

export function UserProvider({ children }: { children: ReactNode }) {
   const[username, setUsername] = useState<string | null>(null)
    const [loading, setLoading] = useState(true)

   const getUser = async () => {
       try{
           const response = await apiFetch(`${API_BASE_URL}/auth/user`);

           if(response.ok){
               const data = await response.json();
               setUsername(data.username);
           } else {
               setUsername(null);
           }
       } catch {
           setUsername(null);
       } finally {
           setLoading(false);
       }
   }

    const logout = async () => {
        await apiFetch(`${API_BASE_URL}/auth/logout`, {
            method: "POST",
        });
        setUsername(null);
    };

   useEffect(() => {
       const initialiseSession = async () => {
           await getUser();
           await apiFetch(`${API_BASE_URL}/auth/csrf`);
       }

       initialiseSession();
   }, [])


   return (
       <UserContext.Provider value = {{username, loading, getUser, logout}}>
           {children}
       </UserContext.Provider>
   )



}

export function useUserContext(): UserContextType {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUserContext must be used inside UserProvider");
    }
    return context;
}