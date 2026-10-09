import {useEffect, useState} from "react";


function useReviews<T>(url : string, errMessage : string){
    const [reviews, setReviews] = useState<T[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const loadReviews = async () => {
            try{
                const response = await fetch(url,{
                    credentials: "include",
                });

                if(!response.ok){
                    throw new Error(errMessage);
                }

                const data : T[] = await response.json();
                setReviews(data);
            } catch (err) {
                console.error(err);
                setError(errMessage);
            } finally {
                setLoading(false);
            }

        };

        loadReviews();
    }, [url, errMessage]);

    return {reviews, loading, error}
}

export default useReviews;