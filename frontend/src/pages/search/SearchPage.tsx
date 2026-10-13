import {useEffect, useState} from "react";
import {Alert, CircularProgress, Dialog, DialogContent, DialogTitle, Typography} from "@mui/material";
import {API_BASE_URL} from "../../config/api.ts";
import type {AlbumSearchResult} from "../../types/AlbumSearchResult.ts";
import AddAlbumForm from "../../components/forms/AddAlbumForm.tsx";
import type {ArtistSearchResult} from "../../types/ArtistSearchResult.ts";
import AddArtistForm from "../../components/forms/AddArtistForm.tsx";
import {useSearchParams} from "react-router-dom";
import CardGrid from "../../components/CardGrid.tsx";
import { AlbumSearchDisplayCard, ArtistSearchDisplayCard } from "../../components/cards/SearchCards.tsx";


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

            <Typography variant="h4" component="h2" gutterBottom>Albums</Typography>

            <CardGrid>
                {albumResults.map(album => (
                    <AlbumSearchDisplayCard
                        key={album.musicBrainzId|| `${album.artist}-${album.title}`}
                        searchResult={album}
                        onClick={() => setSelectedAlbum(album)}
                    />
                ))}
            </CardGrid>


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


            <Typography variant="h4" component="h2" gutterBottom>Artists</Typography>


                <CardGrid>
                    {artistResults.map(artist => (
                        <ArtistSearchDisplayCard
                            key={artist.musicBrainzId || artist.name}
                            searchResult={artist}
                            onClick={() => setSelectedArtist(artist)}/>
                    ))}
                </CardGrid>


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