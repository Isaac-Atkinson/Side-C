import {useEffect, useState} from "react";
import {Alert, Box, CircularProgress, Dialog, DialogContent, DialogTitle} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumSearchResult} from "../types/AlbumSearchResult.ts";
import AlbumSearchDisplayCard from "../components/cards/AlbumSearchDisplayCard.tsx";
import AddAlbumForm from "../components/forms/AddAlbumForm.tsx";
import type {ArtistSearchResult} from "../types/ArtistSearchResult.ts";
import ArtistSearchDisplayCard from "../components/cards/ArtistSearchDisplayCard.tsx";
import AddArtistForm from "../components/forms/AddArtistForm.tsx";
import {useSearchParams} from "react-router-dom";


function SearchPage(){
    const[searchParams] = useSearchParams();
    const[albumResults, setAlbumResults] = useState<AlbumSearchResult[]>([]);
    const[artistResults, setArtistResults] = useState<ArtistSearchResult[]>([]);

    const[loading, setLoading] = useState<boolean>(true);
    const[error, setError] = useState<string | null>(null);

    const[selectedAlbum, setSelectedAlbum] = useState<AlbumSearchResult | null>(null);
    const[selectedArtist, setSelectedArtist] = useState<ArtistSearchResult | null>(null);

    const search = searchParams.get("query") ?? "";


    useEffect(() => {
        const loadResults = async () => {
            try{
                const response = await fetch(
                    `${API_BASE_URL}/search?query=${encodeURIComponent(search)}`,{
                        credentials: "include",
                    }
                )

                if(!response.ok){
                    throw new Error("Could not load results.");
                }

                const data = await response.json();

                setAlbumResults(data.albums);

                setArtistResults(data.artists);
            } catch (err) {
                console.error(err);
                setError("Could not load search results");
            } finally {
                setLoading(false);
            }
        }

        loadResults();
    }, [search]);

    if(loading) {
        return (
            <div className="loading-container">
                <CircularProgress/>
            </div>

        )
    }

    if(error){
        return (
            <Alert severity="error">
                {error}
            </Alert>
        )
    }

    return(
        <div>

            <h1>Albums</h1>

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
                    gap: 2,
                    mt: 2
                }}>


                {albumResults.map(album => (
                    <div key={album.musicBrainzId|| `${album.artist}-${album.title}`}
                         onClick={() => setSelectedAlbum(album)}
                         style={{ cursor: "pointer" }}>
                        <AlbumSearchDisplayCard searchResult={album}/>
                    </div>
                ))}

                <Dialog
                    open={selectedAlbum !== null}
                    onClose={() => setSelectedAlbum(null)}
                >
                    <DialogTitle>Add Review</DialogTitle>

                    <DialogContent>
                        {selectedAlbum && (
                            <AddAlbumForm
                                album={selectedAlbum}
                                onSuccess={() => setSelectedAlbum(null)}
                                onCancel={() => setSelectedAlbum(null)}
                            />
                        )}
                    </DialogContent>
                </Dialog>
            </Box>

            <h1>Artists</h1>
            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
                    gap: 2,
                    mt: 2
                }}>
                {artistResults.map(artist => (
                    <div key={artist.musicBrainzId || `${artist.name}`}
                         onClick={() => setSelectedArtist(artist)}
                         style={{ cursor: "pointer" }}>
                        <ArtistSearchDisplayCard searchResult={artist}/>
                    </div>
                ))}
            </Box>

            <Dialog
                open={selectedArtist !== null}
                onClose={() => setSelectedArtist(null)}
            >
                <DialogTitle>Add Review</DialogTitle>

                <DialogContent>
                    {selectedArtist && (
                        <AddArtistForm
                            artist={selectedArtist}
                            onSuccess={() => setSelectedArtist(null)}
                            onCancel={() => setSelectedArtist(null)}
                        />
                    )}
                </DialogContent>
            </Dialog>


        </div>
    )
}

export default SearchPage;