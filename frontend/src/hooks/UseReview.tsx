import {useEffect, useState} from "react";
import {apiFetch} from "../api/ApiFetch.ts";

function useReview<T>(url : string, errMessage : string){
    const [review, setReview] = useState<T | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const loadReview = async () => {
            try{
                const response = await apiFetch(url);

                if(!response.ok){
                    throw new Error(errMessage);
                }

                const data : T = await response.json();
                setReview(data);
            } catch (err) {
                console.error(err);
                setError(errMessage);
            } finally {
                setLoading(false);
            }

        };

        loadReview();
    }, [url, errMessage]);

    const updateReview = (updatedReview: T) => {
        setReview(updatedReview);
    };

    return {review, updateReview, loading, error}
}

export default useReview;